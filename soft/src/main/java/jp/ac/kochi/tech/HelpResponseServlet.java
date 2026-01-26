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

        List<Map<String, String>> helpList = new ArrayList<>();

        try (Connection con = DBconfig.getConnection()) {

            String sql = """
                SELECT helpID, help_want_userID,
                       help_want_day,
                       help_want_time_start, help_want_time_end,
                       help_reason, apply
                FROM help
                WHERE apply = 0
                ORDER BY helpID DESC
            """;

            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Map<String, String> h = new HashMap<>();
                h.put("id", rs.getString("helpID"));
                h.put("userId", rs.getString("help_want_userID"));
                h.put("date", rs.getString("help_want_day"));
                h.put("time",
                        rs.getString("help_want_time_start")
                        + "〜"
                        + rs.getString("help_want_time_end"));
                h.put("reason", rs.getString("help_reason"));
                h.put("status", rs.getString("apply"));
                helpList.add(h);
            }

        } catch (Exception e) {
            throw new ServletException(e);
        }

        request.setAttribute("helpList", helpList);
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

        int helpId = Integer.parseInt(request.getParameter("help_id"));
        String helperId = "USER_HELPER"; // 本来は session

        try (Connection con = DBconfig.getConnection()) {

            String sql = """
                UPDATE help
                SET helper_userID = ?, apply = 1
                WHERE helpID = ? AND apply = 0
            """;

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, helperId);
            ps.setInt(2, helpId);
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