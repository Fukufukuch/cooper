package app.servlet.owner;

import app.dao.RequestDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/owner/help")
public class OwnerHelpServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            RequestDao dao = new RequestDao();
            req.setAttribute("requestList", dao.listAll());
            req.getRequestDispatcher("/WEB-INF/jsp/owner/help_list.jsp").forward(req, resp);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
