package app.servlet.owner;

import app.dao.ShiftDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;

@WebServlet("/owner/shift/edit")
public class OwnerShiftEditServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            // クエリで year/month 来たらそれ優先（無ければ今月）
            int year, month;
            String y = req.getParameter("year");
            String m = req.getParameter("month");

            if (y != null && m != null) {
                year = Integer.parseInt(y);
                month = Integer.parseInt(m);
            } else {
                LocalDate now = LocalDate.now();
                year = now.getYear();
                month = now.getMonthValue();
            }

            YearMonth ym = YearMonth.of(year, month);
            LocalDate from = ym.atDay(1);
            LocalDate to = ym.atEndOfMonth();

            ShiftDao dao = new ShiftDao();
            var rows = dao.findByDateRange(from, to);

            req.setAttribute("activeTab", "shift");
            req.setAttribute("year", year);
            req.setAttribute("month", month);
            req.setAttribute("rows", rows);

            req.getRequestDispatcher("/WEB-INF/jsp/owner/shift_edit.jsp").forward(req, resp);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
