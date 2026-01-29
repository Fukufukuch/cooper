package controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import app.domain.*;
import app.service.ShiftService;

public class ShiftController extends HttpServlet {

    private final ShiftService shiftService = new ShiftService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        System.out.println("ShiftController 呼ばれました");

        Map<LocalDate, Map<TimeSlot, Map<Position, List<String>>>> shifts = shiftService.generateShift();
        List<TimeSlot> timeSlots = shiftService.getTimeSlots();
        List<Position> positions = shiftService.getPositions();

        // JSP に渡す
        req.setAttribute("shifts", shifts);
        req.setAttribute("timeSlots", timeSlots);
        req.setAttribute("positions", positions);

        try {
            req.getRequestDispatcher("/WEB-INF/jsp/shiftGenerate/result.jsp")
               .forward(req, resp);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }
}
