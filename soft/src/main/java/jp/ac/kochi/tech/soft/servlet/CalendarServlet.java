package jp.ac.kochi.tech.soft.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import jp.ac.kochi.tech.soft.dao.ShiftDAO;
import jp.ac.kochi.tech.soft.model.Shift;

import java.io.IOException;
import java.time.*;
import java.util.*;

@WebServlet("/calendar")
public class CalendarServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String userID = (String) session.getAttribute("userID");

        if (userID == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int year, month;
        LocalDate today = LocalDate.now();

        try {
            year = Integer.parseInt(request.getParameter("year"));
            month = Integer.parseInt(request.getParameter("month"));
        } catch (Exception e) {
            year = today.getYear();
            month = today.getMonthValue();
        }

        YearMonth ym = YearMonth.of(year, month);
        LocalDate firstDay = ym.atDay(1);

        int daysInMonth = ym.lengthOfMonth();
        int startDayOfWeek = firstDay.getDayOfWeek().getValue();

        ShiftDAO dao = new ShiftDAO();
        Map<Integer, List<Shift>> shiftMap =
                dao.getMonthlyShifts(userID, year, month);

        request.setAttribute("year", year);
        request.setAttribute("month", month);
        request.setAttribute("daysInMonth", daysInMonth);
        request.setAttribute("startDayOfWeek", startDayOfWeek);
        request.setAttribute("shiftMap", shiftMap);

        request.getRequestDispatcher("/WEB-INF/jsp/calendar.jsp")
               .forward(request, response);
    }
}
