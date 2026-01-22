package app.servlet.owner;

import app.dao.ShiftDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/owner/shift/delete")
public class OwnerShiftDeleteServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        int shiftID = Integer.parseInt(req.getParameter("shiftID"));
        String year = req.getParameter("year");
        String month = req.getParameter("month");

        try {
            ShiftDao dao = new ShiftDao();
            dao.deleteByShiftId(shiftID);

            String redirect = req.getContextPath() + "/owner/shift/edit";
            if (year != null && month != null && !year.isBlank() && !month.isBlank()) {
                redirect += "?year=" + year + "&month=" + month;
            }
            resp.sendRedirect(redirect);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
