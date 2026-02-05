package app.servlet.owner;

import app.entity.TimeSlotEntity;
import app.repository.TimeSlotRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/admin/timeslot"})
public class TimeSlotAdminServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TimeSlotRepository repo = new TimeSlotRepository();
        List<TimeSlotEntity> list = repo.findAll();
        req.setAttribute("timeslots", list);
        String err = req.getParameter("error");
        if (err != null && !err.isEmpty()) req.setAttribute("error", err);
        req.getRequestDispatcher("/WEB-INF/jsp/admin/timeslot_list.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        TimeSlotRepository repo = new TimeSlotRepository();

        try {
            if ("create".equals(action)) {
                String name = req.getParameter("name");
                if (req.getParameter("startMinute") == null || req.getParameter("endMinute") == null) {
                    throw new IllegalArgumentException("時刻が入力されていません");
                }
                int startMinute = Integer.parseInt(req.getParameter("startMinute"));
                int endMinute = Integer.parseInt(req.getParameter("endMinute"));
                if (startMinute < 0 || startMinute >= 24*60 || endMinute < 0 || endMinute >= 24*60) {
                    throw new IllegalArgumentException("時刻が不正です（0:00〜23:59の範囲）");
                }
                int minExtra = Integer.parseInt(req.getParameter("minExtraWorkers"));
                int maxExtra = Integer.parseInt(req.getParameter("maxExtraWorkers"));
                int requireAuth = Integer.parseInt(req.getParameter("requireAuthorityWorkers"));
                repo.insert(name, startMinute, endMinute, minExtra, maxExtra, requireAuth);
            } else if ("update".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                String name = req.getParameter("name");
                if (req.getParameter("startMinute") == null || req.getParameter("endMinute") == null) {
                    throw new IllegalArgumentException("時刻が入力されていません");
                }
                int startMinute = Integer.parseInt(req.getParameter("startMinute"));
                int endMinute = Integer.parseInt(req.getParameter("endMinute"));
                if (startMinute < 0 || startMinute >= 24*60 || endMinute < 0 || endMinute >= 24*60) {
                    throw new IllegalArgumentException("時刻が不正です（0:00〜23:59の範囲）");
                }
                int minExtra = Integer.parseInt(req.getParameter("minExtraWorkers"));
                int maxExtra = Integer.parseInt(req.getParameter("maxExtraWorkers"));
                int requireAuth = Integer.parseInt(req.getParameter("requireAuthorityWorkers"));
                repo.update(id, name, startMinute, endMinute, minExtra, maxExtra, requireAuth);
            } else if ("delete".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                repo.delete(id);
            } else if ("reactivate".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                repo.setActive(id, true);
            }
        } catch (Exception e) {
            // redirect with error message so it can be displayed after redirect
            String msg = e.getMessage();
            if (msg == null) msg = "不明なエラーが発生しました";
            resp.sendRedirect(req.getContextPath() + "/admin/timeslot?error=" + java.net.URLEncoder.encode(msg, "UTF-8"));
            return;
        }

        resp.sendRedirect(req.getContextPath() + "/admin/timeslot");
    }
}
