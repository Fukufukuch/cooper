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
        java.util.List<String> warnings = shiftService.getWarnings();
        java.util.List<app.generate.shortageSlot> shortageSlots = shiftService.getShortageSlots();
        java.util.List<app.generate.WarningSlot> warningSlots = shiftService.getWarningSlots();
        java.util.List<java.util.Map<String, Object>> shortageSummary = shiftService.getShortageSummary();

        // Build map userID -> username for display
        java.util.Set<String> ids = new java.util.HashSet<>();
        for (var dateEntry : shifts.entrySet()) {
            for (var slotEntry : dateEntry.getValue().entrySet()) {
                for (var posEntry : slotEntry.getValue().entrySet()) {
                    ids.addAll(posEntry.getValue());
                }
            }
        }
        app.repository.UserRepository userRepo = new app.repository.UserRepository();
        java.util.Map<String,String> usernames = userRepo.findUsernamesByIds(ids);

        // JSP に渡す
        req.setAttribute("shifts", shifts);
        req.setAttribute("timeSlots", timeSlots);
        req.setAttribute("positions", positions);
        req.setAttribute("usernames", usernames);
        req.setAttribute("warnings", warnings);
        req.setAttribute("shortageSlots", shortageSlots);
        req.setAttribute("warningSlots", warningSlots);
        req.setAttribute("shortageSummary", shortageSummary);

        try {
            req.getRequestDispatcher("/WEB-INF/jsp/shiftGenerate/result.jsp")
               .forward(req, resp);
        } catch (Exception e) {
            throw new IOException(e);
        }
    }
}
