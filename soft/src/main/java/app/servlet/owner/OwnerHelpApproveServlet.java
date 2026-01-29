package app.servlet.owner;

import app.db.Db;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.*;

@WebServlet("/owner/help/approve")
public class OwnerHelpApproveServlet extends HttpServlet {

    private int toMinute(Time t) {
        return t.toLocalTime().getHour() * 60 + t.toLocalTime().getMinute();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userID") == null) {
            resp.sendRedirect(req.getContextPath() + "/LoginServlet");
            return;
        }

        req.setCharacterEncoding("UTF-8");
        int helpID = Integer.parseInt(req.getParameter("helpID"));

        try (Connection con = Db.getConnection()) {
            con.setAutoCommit(false);

            try {
                // 1) help取得（ロック）
                String wantUserID;
                String helperUserID;
                Date day;
                Time start;
                Time end;

                String sqlHelp =
                        "SELECT help_want_userID, helper_userID, help_want_day, help_want_time_start, help_want_time_end " +
                        "FROM help WHERE helpID = ? FOR UPDATE";

                try (PreparedStatement ps = con.prepareStatement(sqlHelp)) {
                    ps.setInt(1, helpID);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            con.rollback();
                            session.setAttribute("flash", "対象helpが見つかりません。");
                            resp.sendRedirect(req.getContextPath() + "/owner/help");
                            return;
                        }
                        wantUserID = rs.getString("help_want_userID");
                        helperUserID = rs.getString("helper_userID");
                        day = rs.getDate("help_want_day");
                        start = rs.getTime("help_want_time_start");
                        end = rs.getTime("help_want_time_end");
                    }
                }

                if (helperUserID == null || helperUserID.isBlank()) {
                    con.rollback();
                    session.setAttribute("flash", "応募者が未設定のため承認できません。");
                    resp.sendRedirect(req.getContextPath() + "/owner/help");
                    return;
                }

                int startMin = toMinute(start);
                int endMin = toMinute(end);

                // 2) shift特定（運用上1件の想定）
                Integer shiftId = null;

                String sqlShift =
                        "SELECT id FROM shift " +
                        "WHERE workerID = ? AND date = ? AND start_minute = ? AND end_minute = ?";

                try (PreparedStatement ps = con.prepareStatement(sqlShift)) {
                    ps.setString(1, wantUserID);
                    ps.setDate(2, day);
                    ps.setInt(3, startMin);
                    ps.setInt(4, endMin);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            shiftId = rs.getInt("id");
                        }
                    }
                }

                if (shiftId == null) {
                    con.rollback();
                    session.setAttribute("flash", "該当するshiftが見つかりません（依頼者/日付/時間が一致しない）。");
                    resp.sendRedirect(req.getContextPath() + "/owner/help");
                    return;
                }

                // 3) shift更新（交代確定）
                String sqlUpdateShift = "UPDATE shift SET workerID = ? WHERE id = ?";
                try (PreparedStatement ps = con.prepareStatement(sqlUpdateShift)) {
                    ps.setString(1, helperUserID);
                    ps.setInt(2, shiftId);
                    ps.executeUpdate();
                }

                // 4) help承認済（apply=2）
                String sqlUpdateHelp = "UPDATE help SET apply = 2 WHERE helpID = ?";
                try (PreparedStatement ps = con.prepareStatement(sqlUpdateHelp)) {
                    ps.setInt(1, helpID);
                    ps.executeUpdate();
                }

                con.commit();
                session.setAttribute("flash", "承認しました（shift交代確定）。");
                resp.sendRedirect(req.getContextPath() + "/owner/help");

            } catch (Exception e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
