package app.servlet.owner;

import app.dao.ShiftDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/owner/shift/add")
public class OwnerShiftAddServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            String dateStr = req.getParameter("date");
            String userID = req.getParameter("userID");
            int positionID = Integer.parseInt(req.getParameter("positionID"));
            int timeslotID = Integer.parseInt(req.getParameter("timeslotID"));

            LocalDate day = LocalDate.parse(dateStr);

            ShiftDao dao = new ShiftDao();
            dao.insert(day, userID, positionID, timeslotID);

            resp.sendRedirect(req.getContextPath() + "/owner/shift/edit");

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
