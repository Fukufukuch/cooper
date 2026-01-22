package app.servlet.owner;

import app.dao.UserDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/owner/password")
public class OwnerPasswordChangeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setAttribute("activeTab", "setting");
        req.getRequestDispatcher("/WEB-INF/jsp/owner/password_change.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String userID = req.getParameter("userID");
        String oldPassword = req.getParameter("oldPassword");
        String newPassword = req.getParameter("newPassword");

        try {
            UserDao dao = new UserDao();
            boolean ok = dao.changePassword(userID, oldPassword, newPassword);

            if (!ok) {
                req.setAttribute("error", "ユーザーIDか現在のパスワードが違います。");
                req.setAttribute("activeTab", "setting");
                req.getRequestDispatcher("/WEB-INF/jsp/owner/password_change.jsp").forward(req, resp);
                return;
            }

            // ✅ 完了画面へ
            req.setAttribute("userID", userID);
            req.setAttribute("activeTab", "setting");
            req.getRequestDispatcher("/WEB-INF/jsp/owner/password_change_done.jsp")
               .forward(req, resp);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
