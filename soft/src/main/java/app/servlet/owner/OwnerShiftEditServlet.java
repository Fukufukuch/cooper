package app.servlet.owner;

import app.dao.ShiftDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/owner/shift/edit")
public class OwnerShiftEditServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            LocalDate now = LocalDate.now();

            String yearParam = req.getParameter("year");
            String monthParam = req.getParameter("month");

            int year = (yearParam == null || yearParam.isBlank())
                    ? now.getYear()
                    : Integer.parseInt(yearParam);

            int month = (monthParam == null || monthParam.isBlank())
                    ? now.getMonthValue()
                    : Integer.parseInt(monthParam);

            LocalDate from = LocalDate.of(year, month, 1);
            LocalDate to = from.withDayOfMonth(from.lengthOfMonth());

            ShiftDao dao = new ShiftDao();
            req.setAttribute("rows", dao.findByDateRange(from, to)); // ← JSP側が rows を見てるなら rows
            req.setAttribute("year", year);
            req.setAttribute("month", month);

            req.getRequestDispatcher("/WEB-INF/jsp/owner/shift_edit.jsp").forward(req, resp);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
