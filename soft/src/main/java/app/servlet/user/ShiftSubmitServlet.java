package app.servlet.user;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.*;

/*import com.google.gson.Gson;*/

import jp.ac.kochi.tech.ShiftRequest;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import jp.ac.kochi.tech.ShiftRequest;

@WebServlet("/user/shift/submit/api")
public class ShiftSubmitServlet extends HttpServlet {

    private static final String URL =
        "jdbc:mysql://localhost:3306/shift_db?useSSL=false&serverTimezone=Asia/Tokyo";
    private static final String USER = "cooper";
    private static final String PASSWORD = "CooperG10!";

    private ShiftRequest parseJsonToShiftRequest(String json) {
        // 例: {"timeSlotId":1,"helpDay":"2023-10-01","reason":"理由"}
        json = json.trim().replaceAll("^\\{|\\}$", ""); // 外側{}を除去
        String[] pairs = json.split(",");
        int timeSlotId = 0;
        String helpDay = "";
        String reason = "";
        for (String pair : pairs) {
            String[] keyValue = pair.split(":", 2);
            if (keyValue.length == 2) {
                String key = keyValue[0].trim().replaceAll("^\"|\"$", "");
                String value = keyValue[1].trim().replaceAll("^\"|\"$", "");
                switch (key) {
                    case "timeSlotId":
                        timeSlotId = Integer.parseInt(value);
                        break;
                    case "helpDay":
                        helpDay = value;
                        break;
                    case "reason":
                        reason = value;
                        break;
                }
            }
        }
        return new ShiftRequest(timeSlotId, helpDay, reason);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 文字コード設定
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");

        // セッション確認
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"status\":\"unauthorized\"}");
            return;
        }
        String userId = (String) session.getAttribute("userId");

        
        StringBuilder sb = new StringBuilder();// ===== リクエストボディ(JSON)を読む =====;
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }

        String json = sb.toString();

        // デバッグ用（Tomcatログに出る）
        System.out.println("受信JSON: " + json);

        // JSON → Java(Gsonの代わりに手動パース)
        ShiftRequest req = parseJsonToShiftRequest(json);

        // DB保存
        String selectSlotSql =
            "SELECT start_minute, end_minute FROM timeslot WHERE id = ?";

        String insertHelpSql =
            "INSERT INTO help (" +
            "help_want_userID, help_want_day, help_want_time_start, " +
            "help_want_time_end, apply, help_reason" +
            ") VALUES (?, ?, ?, ?, 0, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)){
            // ① TimeSlot から時間を取得
            int startMinute;
            int endMinute;
        try (PreparedStatement ps = conn.prepareStatement(selectSlotSql)) {
                ps.setInt(1, req.getTimeSlotId());
                ResultSet rs = ps.executeQuery();

                if (!rs.next()) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("{\"status\":\"invalid_timeslot\"}");
                    return;
                }

                startMinute = rs.getInt("start_minute");
                endMinute = rs.getInt("end_minute");
            }

            // 分 → Time に変換
            Time startTime = new Time(startMinute * 60L * 1000);
            Time endTime = new Time(endMinute * 60L * 1000);

            // ② help テーブルに INSERT
            try (PreparedStatement ps = conn.prepareStatement(insertHelpSql)) {
                ps.setString(1, userId);
                ps.setDate(2, Date.valueOf(req.getHelpDay()));
                ps.setTime(3, startTime);
                ps.setTime(4, endTime);
                ps.setString(5, req.getReason());

                ps.executeUpdate();
            }
            response.getWriter().write("{\"status\":\"success\"}");

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"status\":\"error\"}");
        }
    }
}
