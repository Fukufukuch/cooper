package app.servlet.user;

import app.dao.UserDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/user/password")
public class UserPasswordChangeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        var session = req.getSession(false);
        if (session == null || session.getAttribute("userID") == null) {
            resp.sendRedirect(req.getContextPath() + "/LoginServlet");
            return;
        }

        req.setAttribute("activeTab", "setting");
        req.getRequestDispatcher("/WEB-INF/jsp/user/passwordChange.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        var session = req.getSession(false);
        if (session == null || session.getAttribute("userID") == null) {
            resp.sendRedirect(req.getContextPath() + "/LoginServlet");
            return;
        }

        String userID = req.getParameter("userID");
        String oldPassword = req.getParameter("oldPassword");
        String newPassword = req.getParameter("newPassword");

        try {
            UserDao dao = new UserDao();
            boolean ok = dao.changePassword(userID, oldPassword, newPassword);

            if (!ok) {
                req.setAttribute("error", "ユーザーIDか現在のパスワードが違います。");
                req.setAttribute("activeTab", "setting");
                req.getRequestDispatcher("/WEB-INF/jsp/user/passwordChange.jsp").forward(req, resp);
                return;
            }

            // ✅ 完了画面へ
            req.setAttribute("userID", userID);
            req.setAttribute("activeTab", "setting");
            req.getRequestDispatcher("/WEB-INF/jsp/user/passwordChangeDone.jsp")
               .forward(req, resp);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}

