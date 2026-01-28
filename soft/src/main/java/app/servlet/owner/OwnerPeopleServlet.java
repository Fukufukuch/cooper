package app.servlet.owner;

import app.dao.UserDao;
import app.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/owner/people")
public class OwnerPeopleServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        // セッションチェック
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userID") == null) {
            resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            resp.setHeader("Pragma", "no-cache");
            resp.setHeader("Expires", "0");
            resp.sendRedirect(req.getContextPath() + "/LoginServlet");
            return;
        }

        try {
            UserDao dao = new UserDao();

            // ✅ 管理者 + スタッフ を表示
            List<User> users = dao.listAllUsers();

            // JSPが staffList を見てるので名前は合わせる
            req.setAttribute("staffList", users);
            req.setAttribute("activeTab", "people");

            req.getRequestDispatcher("/WEB-INF/jsp/owner/people_list.jsp").forward(req, resp);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
