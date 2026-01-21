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

        try {
            req.setCharacterEncoding("UTF-8");

            String userID = req.getParameter("userID");

            UserDao dao = new UserDao();
            dao.deleteStaff(userID); // 安全策：usertype=1 だけ消える

            resp.sendRedirect(req.getContextPath() + "/owner/people");

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
