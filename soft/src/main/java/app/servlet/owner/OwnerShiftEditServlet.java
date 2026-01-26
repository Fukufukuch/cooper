package app.servlet.owner;

import app.dao.ShiftDao;
import app.dao.TimeslotDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/owner/shift/edit")
public class OwnerShiftEditServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        // セッションチェック
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userID") == null) {
            resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            resp.setHeader("Pragma", "no-cache");
            resp.setHeader("Expires", "0");
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        try {
            LocalDate today = LocalDate.now();
            LocalDate from = today.minusDays(7);
            LocalDate to = today.plusDays(14);

            ShiftDao shiftDao = new ShiftDao();
            TimeslotDao timeslotDao = new TimeslotDao();

            req.setAttribute("shiftList", shiftDao.findByDateRange(from, to));
            req.setAttribute("timeslotList", timeslotDao.listAll());

            req.getRequestDispatcher("/WEB-INF/jsp/shift_edit.jsp").forward(req, resp);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
