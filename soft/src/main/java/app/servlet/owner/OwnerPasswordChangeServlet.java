package app.servlet.owner;

import app.dao.UserDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/owner/password")
public class OwnerPasswordChangeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userID") == null) {
            resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            resp.setHeader("Pragma", "no-cache");
            resp.setHeader("Expires", "0");
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        req.setAttribute("activeTab", "setting");
        req.getRequestDispatcher("/WEB-INF/jsp/owner/password_change.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userID") == null) {
            resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            resp.setHeader("Pragma", "no-cache");
            resp.setHeader("Expires", "0");
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

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
