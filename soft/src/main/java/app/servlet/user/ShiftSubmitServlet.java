package app.servlet.user;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/*import com.google.gson.Gson;*/

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import jp.ac.kochi.tech.DBconfig;

import jp.ac.kochi.tech.ShiftRequest;

@WebServlet("/user/shift/submit/api")
public class ShiftSubmitServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // JSON配列 → List<ShiftRequest>
    private List<ShiftRequest> parseJsonArray(String json) {
        List<ShiftRequest> list = new ArrayList<>();

        json = json.trim();
        json = json.substring(1, json.length() - 1); // [ ]

        String[] objects = json.split("\\},\\{");

        for (String obj : objects) {
            obj = obj.replace("{", "").replace("}", "");
            String[] pairs = obj.split(",");

            int timeSlotId = 0;
            String helpDay = "";

            for (String pair : pairs) {
                String[] kv = pair.split(":", 2);
                String key = kv[0].replace("\"", "").trim();
                String value = kv[1].replace("\"", "").trim();

                if ("timeSlotId".equals(key)) {
                    timeSlotId = Integer.parseInt(value);
                } else if ("helpDay".equals(key)) {
                    helpDay = value;
                }
            }
            list.add(new ShiftRequest(timeSlotId, helpDay));
        }
        return list;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 文字コード設定
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");

        // セッション確認
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userID") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"status\":\"unauthorized\"}");
            return;
        }
        String userId = (String) session.getAttribute("userID");

        
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }

        //String json = sb.toString();

        // デバッグ用（Tomcatログに出る）
        System.out.println("受信JSON: " + sb.toString());

        // JSON → Java
        List<ShiftRequest> reqList = parseJsonArray(sb.toString());

        // DB保存
        //String selectSlotSql =
        //    "SELECT start_minute, end_minute FROM timeslot WHERE id = ?";

        String insertRequestSql =
            "INSERT INTO worker_shift_request (" +
            "workerID, date, timeslotID " +
            ") VALUES (?, ?, ?)";

        try (Connection conn = DBconfig.getConnection()) {
            // ① TimeSlot から時間を取得
            for (ShiftRequest req : reqList) {

                try (PreparedStatement ps = conn.prepareStatement(insertRequestSql)) {
                    ps.setString(1, userId);
                    ps.setDate(2, Date.valueOf(req.getHelpDay()));
                    ps.setInt(3, req.getTimeSlotId());
                    ps.executeUpdate();
                }
            }

            response.getWriter().write("{\"status\":\"success\"}");


        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"status\":\"error\"}");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("doGet called"); // デバッグ

        response.setContentType("application/json; charset=UTF-8");

        try (Connection conn = DBconfig.getConnection()) {
            String sql = "SELECT id, name FROM timeslot ORDER BY start_minute";
            System.out.println("DB CONNECT OK");
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                StringBuilder json = new StringBuilder("[");
                boolean first = true;
                while (rs.next()) {
                    if (!first) json.append(",");
                    json.append("{\"id\":").append(rs.getInt("id")).append(",\"name\":\"").append(rs.getString("name")).append("\"}");
                    first = false;
                }
                json.append("]");

                response.getWriter().write(json.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json; charset=UTF-8");
            response.getWriter().write("{\"error\":\"timeslot_fetch_failed\"}");
        }
    }

}
