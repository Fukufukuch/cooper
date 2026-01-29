package app.servlet.owner;

import app.db.Db;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

@WebServlet("/owner/help/reject")
public class OwnerHelpRejectServlet extends HttpServlet {

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

        try (Connection con = Db.getConnection();
             PreparedStatement ps =
                     con.prepareStatement("UPDATE help SET apply = 0, helper_userID = NULL WHERE helpID = ?")) {

            ps.setInt(1, helpID);
            ps.executeUpdate();

        } catch (Exception e) {
            throw new ServletException(e);
        }

        session.setAttribute("flash", "応募を却下しました（募集中に戻しました）。");
        resp.sendRedirect(req.getContextPath() + "/owner/help");
    }
}
