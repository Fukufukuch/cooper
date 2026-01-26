package app.servlet.owner;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/owner/setting/menu")
public class OwnerSettingMenuServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        // セッションチェック
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userID") == null) {
            resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            resp.setHeader("Pragma", "no-cache");
            resp.setHeader("Expires", "0");
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        req.setAttribute("activeTab", "setting");
        req.getRequestDispatcher("/WEB-INF/jsp/owner/setting_menu.jsp")
           .forward(req, resp);
    }
}
