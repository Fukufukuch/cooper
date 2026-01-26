package jp.ac.kochi.tech;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

@WebServlet("/HelpResponseServlet")
public class HelpResponseServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * 募集中のヘルプ一覧を表示
     */


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String userId = "8792312115"; // 本来はセッション

        try (Connection con = DBconfig.getConnection()) {

            // ① 自分のシフト日一覧（重複除去） → すべてのシフトを表示（シフト者と時間区分）
            String sqlShift = """
                SELECT
                  s.id AS shift_id,
                  s.date,
                  s.workerID AS worker_id,
                  t.name AS timeslot_name
                FROM shift s
                JOIN timeslot t
                  ON s.start_minute = t.start_minute
                 AND s.end_minute   = t.end_minute
                ORDER BY s.date, t.start_minute
            """;
            PreparedStatement psShift = con.prepareStatement(sqlShift);
            ResultSet rsShift = psShift.executeQuery();

            List<Map<String, Object>> confirmedShifts = new ArrayList<>();
            while (rsShift.next()) {
                Map<String, Object> row = new HashMap<>();
                row.put("shift_id", rsShift.getInt("shift_id"));
                row.put("date", rsShift.getString("date"));
                row.put("worker_id", rsShift.getString("worker_id"));
                row.put("timeslot", rsShift.getString("timeslot_name"));
                confirmedShifts.add(row);
            }

            // 自分の募集履歴（変更なし）
            String sqlHelp = """
                SELECT
                    h.apply,
                    h.help_reason,
                    h.help_want_day,
                    t.name AS timeslot_name
                    FROM help h
                    JOIN shift s
                    ON s.workerID = h.help_want_userID
                    AND s.date = h.help_want_day
                    AND s.start_minute = (HOUR(h.help_want_time_start) * 60 + MINUTE(h.help_want_time_start))
                    AND s.end_minute   = (HOUR(h.help_want_time_end)   * 60 + MINUTE(h.help_want_time_end))
                    JOIN timeslot t
                    ON s.start_minute = t.start_minute
                    AND s.end_minute   = t.end_minute
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
                h.put("time", rsHelp.getString("timeslot_name"));
                h.put("reason", rsHelp.getString("help_reason"));
                h.put("status", rsHelp.getString("apply"));
                helpList.add(h);
            }

            request.setAttribute("confirmedShifts", confirmedShifts);
            request.setAttribute("helpList", helpList);

        } catch (Exception e) {
            throw new ServletException(e);
        }

        RequestDispatcher dispatcher =
                request.getRequestDispatcher("/WEB-INF/jsp/user/helpRequest.jsp");
        dispatcher.forward(request, response);
    }


    /**
     * ヘルプに応募する処理
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String shiftId = request.getParameter("shift_id");
        String reason = request.getParameter("help_reason");
        String userId = "8792312115"; // ← 本来は session

        try (Connection con = DBconfig.getConnection()) {

            // シフトから日付・時間を取得
            String sqlShift = """
                SELECT date, start_minute, end_minute
                FROM shift
                WHERE id = ?
            """;

            PreparedStatement psShift = con.prepareStatement(sqlShift);
            psShift.setString(1, shiftId);
            ResultSet rs = psShift.executeQuery();

			if (!rs.next()) {
                throw new ServletException("shift not found");
            }

            String date = rs.getString("date");
            int startMin = rs.getInt("start_minute");
            int endMin   = rs.getInt("end_minute");

            String startTime =
                String.format("%02d:%02d", startMin / 60, startMin % 60);
            String endTime =
                String.format("%02d:%02d", endMin / 60, endMin % 60);

            // help 登録
            String sqlInsert = """
                INSERT INTO help
                (help_want_userID, help_want_day,
                 help_want_time_start, help_want_time_end,
                 help_reason, apply)
                VALUES (?, ?, ?, ?, ?, 0)
            """;

            PreparedStatement ps = con.prepareStatement(sqlInsert);
            ps.setString(1, userId);
            ps.setString(2, date);
            ps.setString(3, startTime);
            ps.setString(4, endTime);
            ps.setString(5, reason);
            ps.executeUpdate();


        } catch (Exception e) {
            throw new ServletException(e);
        }

        response.sendRedirect(request.getContextPath() + "/HelpResponseServlet");
    }
}


/**処理の大まかな流れ
 * 1 helpResponse.jsp を表示
 * 
 * 2 ヘルプに応募する (doPost)
 * 　1 ユーザーが特定の募集の「応募する」ボタンを押す
 * 　2 今ボタンを押したのが誰か (helperId) をセッションから取得
 * 　3 どの募集に応募したのかを「固有のID (help_id)」で判断
 * 　4 保存されているリストの中から、その固有のIDを持つデータを探し出す
 * 　5 見つけたデータの status を 0 (募集) から 1 (承認待ち) に書き換え、
 * 　　helperId に自分の名前を書き込み
 * 　6 一覧画面を再表示
 */