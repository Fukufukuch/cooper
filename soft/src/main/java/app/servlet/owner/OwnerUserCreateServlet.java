package app.servlet.owner;

import app.dao.UserDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Date;

@WebServlet("/owner/account/create")
public class OwnerUserCreateServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setAttribute("activeTab", "setting");
        req.getRequestDispatcher("/WEB-INF/jsp/owner/account_create.jsp")
           .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String username = req.getParameter("username");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");
        String dob = req.getParameter("dob");
        String password = req.getParameter("password");
        String passwordConfirm = req.getParameter("passwordConfirm");

        if (password == null || !password.equals(passwordConfirm)) {
            req.setAttribute("activeTab", "setting");
            req.setAttribute("error", "パスワードが一致しません");
            req.getRequestDispatcher("/WEB-INF/jsp/owner/account_create.jsp")
               .forward(req, resp);
            return;
        }

        try {
            UserDao dao = new UserDao();
            String createdId = dao.createStaff(username, email, phone, Date.valueOf(dob), password);
            req.setAttribute("createdId", createdId);
        } catch (Exception e) {
            throw new ServletException(e);
        }

        req.setAttribute("activeTab", "setting");
        req.getRequestDispatcher("/WEB-INF/jsp/owner/account_create_done.jsp")
           .forward(req, resp);
    }
}
