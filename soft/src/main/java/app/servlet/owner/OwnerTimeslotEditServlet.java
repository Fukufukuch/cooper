package app.servlet.owner;

import app.dao.TimeslotDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalTime;

@WebServlet("/owner/timeslot/edit")
public class OwnerTimeslotEditServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            TimeslotDao dao = new TimeslotDao();
            req.setAttribute("timeslotList", dao.listAll());
            req.getRequestDispatcher("/WEB-INF/jsp/timeslot_edit.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            int id = Integer.parseInt(req.getParameter("timeslotID"));
            String name = req.getParameter("name");
            LocalTime start = LocalTime.parse(req.getParameter("start"));
            LocalTime end = LocalTime.parse(req.getParameter("end"));

            TimeslotDao dao = new TimeslotDao();
            dao.update(id, name, start, end);

            resp.sendRedirect(req.getContextPath() + "/owner/timeslot/edit");

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
