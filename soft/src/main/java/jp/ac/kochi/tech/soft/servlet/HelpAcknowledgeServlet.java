package jp.ac.kochi.tech.soft.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import jp.ac.kochi.tech.DBconfig;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

@WebServlet("/user/help/ack")
public class HelpAcknowledgeServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userID") == null) {
            response.sendRedirect(request.getContextPath() + "/LoginServlet");
            return;
        }

        request.setCharacterEncoding("UTF-8");
        String userID = (String) session.getAttribute("userID");
        String helpIdParam = request.getParameter("helpID");
        String role = request.getParameter("role");

        if (helpIdParam == null || role == null) {
            response.sendRedirect(request.getContextPath() + "/calendar");
            return;
        }

        int helpID = Integer.parseInt(helpIdParam);

        String sql;
        if ("want".equals(role)) {
            sql = "UPDATE help SET want_user_acknowledged = 1 WHERE helpID = ? AND help_want_userID = ?";
        } else if ("helper".equals(role)) {
            sql = "UPDATE help SET helper_user_acknowledged = 1 WHERE helpID = ? AND helper_userID = ?";
        } else {
            response.sendRedirect(request.getContextPath() + "/calendar");
            return;
        }

        try (Connection con = DBconfig.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, helpID);
            ps.setString(2, userID);
            ps.executeUpdate();

        } catch (Exception e) {
            throw new ServletException(e);
        }

        response.sendRedirect(request.getContextPath() + "/calendar");
    }
}
