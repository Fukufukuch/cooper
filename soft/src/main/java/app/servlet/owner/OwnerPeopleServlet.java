package app.servlet.owner;

import app.dao.UserDao;
import app.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/owner/people")
public class OwnerPeopleServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

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
