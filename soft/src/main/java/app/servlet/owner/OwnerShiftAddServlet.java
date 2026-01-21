package app.servlet.owner;

import app.dao.ShiftDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@WebServlet("/owner/shift/add")
public class OwnerShiftAddServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String userID = req.getParameter("userID");
        String dayStr = req.getParameter("day");
        String timetable = req.getParameter("timetable");
        String timetableNumberStr = req.getParameter("timetableNumber");

        String year = req.getParameter("year");
        String month = req.getParameter("month");

        try {
            LocalDate day = LocalDate.parse(dayStr);

            Integer timetableNumber = null;
            if (timetableNumberStr != null && !timetableNumberStr.isBlank()) {
                timetableNumber = Integer.parseInt(timetableNumberStr);
            }

            ShiftDao dao = new ShiftDao();
            dao.insert(userID, day, timetable, timetableNumber);

            String redirect = req.getContextPath() + "/owner/shift/edit";
            if (year != null && month != null && !year.isBlank() && !month.isBlank()) {
                redirect += "?year=" + year + "&month=" + month;
            }
            resp.sendRedirect(redirect);

        } catch (DateTimeParseException e) {
            throw new ServletException("日付形式が不正です。例: 2026-01-09 の形式で入力してください。", e);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
