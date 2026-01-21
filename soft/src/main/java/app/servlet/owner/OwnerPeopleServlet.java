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
            // タブのアクティブ
            req.setAttribute("activeTab", "people");

            UserDao dao = new UserDao();
            List<User> list = dao.listStaff(); // usertype=1のみ

            req.setAttribute("list", list);

            req.getRequestDispatcher("/WEB-INF/jsp/owner/people_list.jsp")
               .forward(req, resp);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
