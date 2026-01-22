package app.servlet.owner;

import app.dao.UserDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/owner/people/delete")
public class OwnerPeopleDeleteServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        String userID = req.getParameter("userID");

        try {
            UserDao dao = new UserDao();
            boolean ok = dao.deleteStaff(userID);

            req.setAttribute("deleted", ok);
            req.setAttribute("userID", userID);
            req.setAttribute("activeTab", "people");
            req.getRequestDispatcher("/WEB-INF/jsp/owner/people_delete_done.jsp").forward(req, resp);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
