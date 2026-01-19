package jp.ac.kochi.tech.soft.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/calendar")
public class CalendarServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int year;
        int month;

        String yearParam = request.getParameter("year");
        String monthParam = request.getParameter("month");

        LocalDate today = LocalDate.now();

        if (yearParam == null || monthParam == null) {
            year = today.getYear();
            month = today.getMonthValue();
        } else {
            year = Integer.parseInt(yearParam);
            month = Integer.parseInt(monthParam);
        }

        YearMonth yearMonth = YearMonth.of(year, month);

        LocalDate firstDay = yearMonth.atDay(1);
        int daysInMonth = yearMonth.lengthOfMonth();
        int startDayOfWeek = firstDay.getDayOfWeek().getValue(); // 月=1 ... 日=7

        List<Integer> days = new ArrayList<>();
        for (int i = 1; i <= daysInMonth; i++) {
            days.add(i);
        }

        request.setAttribute("year", year);
        request.setAttribute("month", month);
        request.setAttribute("days", days);
        request.setAttribute("startDayOfWeek", startDayOfWeek);

        request.getRequestDispatcher("/WEB-INF/jsp/calendar.jsp")
               .forward(request, response);
    }
}
