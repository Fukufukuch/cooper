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
;

/**
 * 労働者がヘルプ募集（シフト代行依頼）を投稿し、一時保存するサーブレット
 */
@WebServlet("/HelpRequestServlet")
public class HelpRequestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        String userId = (String) session.getAttribute("userID");
        System.out.println("userID: " + userId); // デバッグ

        try (Connection con = DBconfig.getConnection()) {

            // 自分のシフト一覧
            String sqlShifts = "SELECT id AS shift_id, date, shift_timetable FROM shift WHERE workerID = ? ORDER BY date, shift_timetable";
            PreparedStatement psShifts = con.prepareStatement(sqlShifts);
            psShifts.setString(1, userId);
            ResultSet rsShifts = psShifts.executeQuery();
            List<Map<String, Object>> confirmedShifts = new ArrayList<>();
            while (rsShifts.next()) {
                Map<String, Object> shift = new HashMap<>();
                shift.put("shift_id", rsShifts.getInt("shift_id"));
                shift.put("date", rsShifts.getString("date"));
                shift.put("shift_timetable", rsShifts.getString("shift_timetable"));
                confirmedShifts.add(shift);
            }
            System.out.println("confirmedShifts size: " + confirmedShifts.size()); // デバッグ

            // 自分の募集履歴
            String sqlHelp = """
                SELECT h.apply, h.help_reason, h.help_want_day, h.help_want_time_start, h.help_want_time_end, h.helper_userID AS helper_id
                FROM help h
                WHERE h.help_want_userID = ?
                ORDER BY h.helpID DESC
            """;
            PreparedStatement psHelp = con.prepareStatement(sqlHelp);
            psHelp.setString(1, userId);
            ResultSet rsHelp = psHelp.executeQuery();

            List<Map<String, String>> helpList = new ArrayList<>();
            while (rsHelp.next()) {
                Map<String, String> h = new HashMap<>();
                h.put("date", rsHelp.getString("help_want_day"));
                java.sql.Time startTime = rsHelp.getTime("help_want_time_start");
                java.sql.Time endTime = rsHelp.getTime("help_want_time_end");
                String timeStr = String.format("%02d:%02d-%02d:%02d",
                    startTime.getHours(), startTime.getMinutes(),
                    endTime.getHours(), endTime.getMinutes());
                h.put("time", timeStr);
                h.put("reason", rsHelp.getString("help_reason"));
                h.put("status", rsHelp.getString("apply"));
                h.put("helper_id", rsHelp.getString("helper_id"));
                helpList.add(h);
            }

            request.setAttribute("confirmedShifts", confirmedShifts);
            request.setAttribute("helpList", helpList);

            // 自分のシフトで使用されているtimeslotのname一覧（重複除去）
            String sqlTypes = """
                SELECT DISTINCT t.name
                FROM shift s
                JOIN timeslot t ON s.start_minute = t.start_minute AND s.end_minute = t.end_minute
                WHERE s.workerID = ?
                ORDER BY t.start_minute
            """;
            PreparedStatement psTypes = con.prepareStatement(sqlTypes);
            psTypes.setString(1, userId);
            ResultSet rsTypes = psTypes.executeQuery();
            List<String> shiftTypes = new ArrayList<>();
            while (rsTypes.next()) {
                shiftTypes.add(rsTypes.getString("name"));
            }
            request.setAttribute("shiftTypes", shiftTypes);

        } catch (Exception e) {
            throw new ServletException(e);
        }

        RequestDispatcher dispatcher =
                request.getRequestDispatcher("/WEB-INF/jsp/user/helpRequest.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        String userId = (String) session.getAttribute("userId");

        int shiftId = Integer.parseInt(request.getParameter("shift_id"));
        String reason = request.getParameter("help_reason");

        try (Connection con = DBconfig.getConnection()) {

            // shift_idから日付と時間を取得
            String sqlGetShift = "SELECT date, start_minute, end_minute FROM shift WHERE id = ?";
            PreparedStatement psGet = con.prepareStatement(sqlGetShift);
            psGet.setInt(1, shiftId);
            ResultSet rs = psGet.executeQuery();
            if (!rs.next()) {
                throw new ServletException("シフトが見つかりません");
            }
            String helpDay = rs.getString("date");
            int startMinute = rs.getInt("start_minute");
            int endMinute = rs.getInt("end_minute");

            // ヘルプ募集を一時保存する
            String sqlInsert = "INSERT INTO help (help_want_userID, help_reason, help_want_day, help_want_time_start, help_want_time_end, apply) "
                    + "VALUES (?, ?, ?, ?, ?, 0)";
            PreparedStatement psInsert = con.prepareStatement(sqlInsert);

            psInsert.setString(1, userId);
            psInsert.setString(2, reason);
            psInsert.setString(3, helpDay);
            
            // 分をTimeに変換
            java.sql.Time helpTimeStart = java.sql.Time.valueOf(String.format("%02d:%02d:00", startMinute / 60, startMinute % 60));
            java.sql.Time helpTimeEnd = java.sql.Time.valueOf(String.format("%02d:%02d:00", endMinute / 60, endMinute % 60));
            
            psInsert.setTime(4, helpTimeStart);
            psInsert.setTime(5, helpTimeEnd);

            psInsert.executeUpdate();

        } catch (Exception e) {
            throw new ServletException(e);
        }

        // 二重送信防止（PRGパターン）
        response.sendRedirect(request.getContextPath() + "/HelpRequestServlet");
    }

}

/**処理の大まかな流れ
 * 1 helpRequest.jsp を表示
 * 
 * 2 募集を投稿する (doPost)
 * 　1 ユーザーが日付や理由を入力して「投稿」ボタンを押す
 * 　2 現在ログインしているのが誰か (userId) を特定
 * 　3 画面から入力された「日付」「時間」「理由」を読み取る
 * 　4 この募集を特定するためのID生成
 * 　5 「誰が・いつ・何の理由で」という情報をセットにし、
 * 　　　状態を 0 (募集中) として共有リストに追加
 * 　6 再び募集画面を表示
 */