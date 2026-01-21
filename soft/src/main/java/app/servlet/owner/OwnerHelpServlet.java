package app.servlet.owner;

import app.dao.RequestDao;
import app.dao.RequestDao.RequestRow;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;

@WebServlet("/owner/help")
public class OwnerHelpServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        req.setAttribute("activeTab", "help");

        try {
            RequestDao dao = new RequestDao();
            List<RequestRow> rows = dao.findAll();
            req.setAttribute("rows", rows);

            req.getRequestDispatcher("/WEB-INF/jsp/owner/help_list.jsp").forward(req, resp);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
