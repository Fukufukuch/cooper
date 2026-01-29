package jp.ac.kochi.tech;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

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
        
        HttpSession session = request.getSession(false);
        String userId = (String) session.getAttribute("userID");
        System.out.println("userID in HelpResponse: " + userId); // デバッグ

        try (Connection con = DBconfig.getConnection()) {

            // 募集中のヘルプ一覧（自分のもの以外）
            String sqlHelp;
            PreparedStatement psHelp;
            if (userId == null) {
                sqlHelp = """
                    SELECT h.helpID, h.help_want_day, h.help_want_time_start, h.help_want_time_end, h.help_reason
                    FROM help h
                    WHERE h.apply = 0 AND h.helper_userID IS NULL
                    ORDER BY h.helpID DESC
                """;
                psHelp = con.prepareStatement(sqlHelp);
            } else {
                sqlHelp = """
                    SELECT h.helpID, h.help_want_day, h.help_want_time_start, h.help_want_time_end, h.help_reason
                    FROM help h
                    WHERE h.apply = 0 AND h.helper_userID IS NULL AND h.help_want_userID != ?
                    ORDER BY h.helpID DESC
                """;
                psHelp = con.prepareStatement(sqlHelp);
                psHelp.setString(1, userId);
            }
            ResultSet rsHelp = psHelp.executeQuery();

            List<Map<String, String>> availableHelps = new ArrayList<>();
            while (rsHelp.next()) {
                Map<String, String> h = new HashMap<>();
                h.put("helpID", rsHelp.getString("helpID"));
                h.put("date", rsHelp.getString("help_want_day"));
                java.sql.Time startTime = rsHelp.getTime("help_want_time_start");
                java.sql.Time endTime = rsHelp.getTime("help_want_time_end");
                String timeStr = String.format("%02d:%02d-%02d:%02d",
                    startTime.getHours(), startTime.getMinutes(),
                    endTime.getHours(), endTime.getMinutes());
                h.put("time", timeStr);
                h.put("reason", rsHelp.getString("help_reason"));
                availableHelps.add(h);
            }
            System.out.println("availableHelps size: " + availableHelps.size()); // デバッグ

            request.setAttribute("availableHelps", availableHelps);

        } catch (Exception e) {
            throw new ServletException(e);
        }

        RequestDispatcher dispatcher =
                request.getRequestDispatcher("/WEB-INF/jsp/user/helpResponse.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * ヘルプに応募する処理
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userID") == null) {
            response.sendRedirect(request.getContextPath() + "/LoginServlet");
            return;
        }
        String userId = (String) session.getAttribute("userID");

        String helpId = request.getParameter("help_id");

        try (Connection con = DBconfig.getConnection()) {

            // ヘルプに応募（helper_userIDを更新）
            String sqlUpdate = "UPDATE help SET helper_userID = ?, apply = 1 WHERE helpID = ?";
            PreparedStatement ps = con.prepareStatement(sqlUpdate);
            ps.setString(1, userId);
            ps.setString(2, helpId);
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