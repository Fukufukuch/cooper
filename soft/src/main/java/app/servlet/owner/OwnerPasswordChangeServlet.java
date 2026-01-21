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
        req.getRequestDispatcher("/WEB-INF/jsp/owner/password_change.jsp")
           .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String oldPass = req.getParameter("oldPassword");
        String newPass = req.getParameter("newPassword");
        String confirm = req.getParameter("confirmPassword");

        req.setAttribute("activeTab", "setting");

        if (newPass == null || !newPass.equals(confirm)) {
            req.setAttribute("error", "新しいパスワードが一致しません");
            req.getRequestDispatcher("/WEB-INF/jsp/owner/password_change.jsp")
               .forward(req, resp);
            return;
        }

        try {
            UserDao dao = new UserDao();
            String adminId = dao.findAdminUserId();
            if (adminId == null) {
                req.setAttribute("error", "管理者アカウントがDBに存在しません（users.usertype=0 を確認）");
                req.getRequestDispatcher("/WEB-INF/jsp/owner/password_change.jsp")
                   .forward(req, resp);
                return;
            }

            boolean ok = dao.changePassword(adminId, oldPass, newPass);
            if (!ok) {
                req.setAttribute("error", "現在のパスワードが違います");
            } else {
                req.setAttribute("success", "パスワードを変更しました");
            }

        } catch (Exception e) {
            throw new ServletException(e);
        }

        req.getRequestDispatcher("/WEB-INF/jsp/owner/password_change.jsp")
           .forward(req, resp);
    }
}
