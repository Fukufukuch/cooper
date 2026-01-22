package jp.ac.kochi.tech;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jp.ac.kochi.tech.DBconfig;

@WebServlet("/HelpRequestServlet")
public class HelpRequestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		String userId = "USER_KOSHIRO"; // 本来はセッション

		try (Connection con = DBconfig.getConnection()) {

            // ① 自分のシフト日一覧（重複除去）
            String sqlDate = """
                SELECT DISTINCT date
                FROM shift
                WHERE workerID = ?
                ORDER BY date
            """;
            PreparedStatement psDate = con.prepareStatement(sqlDate);
            psDate.setString(1, userId);
            ResultSet rsDate = psDate.executeQuery();

            List<String> shiftDates = new ArrayList<>();
            while (rsDate.next()) {
                shiftDates.add(rsDate.getString("date"));
            }
			
			/* ② TimeSlot 一覧 */
            String sqlSlot = "SELECT * FROM timeslot ORDER BY start_minute";
            PreparedStatement psSlot = con.prepareStatement(sqlSlot);
            ResultSet rsSlot = psSlot.executeQuery();

            List<Map<String, Object>> timeSlots = new ArrayList<>();
            while (rsSlot.next()) {
                Map<String, Object> slot = new HashMap<>();
                slot.put("id", rsSlot.getInt("id"));
                slot.put("name", rsSlot.getString("name"));
                slot.put("start", rsSlot.getInt("start_minute"));
                slot.put("end", rsSlot.getInt("end_minute"));
                timeSlots.add(slot);
            }

            // ② 自分の募集履歴
            String sqlHelp = """
                SELECT help_want_day, help_want_time_start, help_want_time_end,
                       help_reason, apply
                FROM help
                WHERE help_want_userID = ?
                ORDER BY helpID DESC
            """;
            PreparedStatement psHelp = con.prepareStatement(sqlHelp);
            psHelp.setString(1, userId);
            ResultSet rsHelp = psHelp.executeQuery();

            List<Map<String, String>> helpList = new ArrayList<>();
            while (rsHelp.next()) {
                Map<String, String> h = new HashMap<>();
                h.put("date", rsHelp.getString("help_want_day"));
                h.put("time",
                        rsHelp.getString("help_want_time_start")
                        + "〜"
                        + rsHelp.getString("help_want_time_end"));
                h.put("reason", rsHelp.getString("help_reason"));
                h.put("status", rsHelp.getString("apply"));
                helpList.add(h);
            }

            request.setAttribute("shiftDates", shiftDates);
			request.setAttribute("timeSlots", timeSlots);
            request.setAttribute("helpList", helpList);

        } catch (Exception e) {
            throw new ServletException(e);
        }

        RequestDispatcher dispatcher =
                request.getRequestDispatcher("/WEB-INF/jsp/user/helpRequest.jsp");
        dispatcher.forward(request, response);
    }

    // =========================
    // POST：募集登録
    // =========================
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String helpDate = request.getParameter("help_date");
        int shiftId = Integer.parseInt(request.getParameter("shift_id"));
        String reason = request.getParameter("help_reason");

        String userId = "USER_KOSHIRO"; // session想定

        try (Connection con = DBconfig.getConnection()) {

            /* TimeSlot → 時刻変換 */
            String sqlSlot = "SELECT startminute, endminute FROM timeslot WHERE id = ?";
            PreparedStatement psSlot = con.prepareStatement(sqlSlot);
            psSlot.setInt(1, shiftId);
            ResultSet rs = psSlot.executeQuery();

            if (!rs.next()) {
                throw new ServletException("TimeSlot not found");
            }

            int startMin = rs.getInt("start_minute");
            int endMin = rs.getInt("end_minute");

            String startTime = String.format("%02d:%02d", startMin / 60, startMin % 60);
            String endTime = String.format("%02d:%02d", endMin / 60, endMin % 60);

            /* help テーブルへ保存 */
            String sql = """
                INSERT INTO help
                (help_want_userID, help_want_day,
                 help_want_time_start, help_want_time_end,
                 help_reason, apply)
                VALUES (?, ?, ?, ?, ?, 0)
            """;

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, userId);
            ps.setString(2, helpDate);
            ps.setString(3, startTime);
            ps.setString(4, endTime);
            ps.setString(5, reason);
            ps.executeUpdate();

        } catch (Exception e) {
            throw new ServletException(e);
        }

        // 二重送信防止（PRGパターン）
        response.sendRedirect(request.getContextPath() + "/HelpRequestServlet");
    }
}