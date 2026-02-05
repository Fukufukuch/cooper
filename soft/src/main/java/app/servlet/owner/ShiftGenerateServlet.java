package app.servlet.owner;

import jakarta.servlet.ServletException;
import app.repository.OptionRepository;
import app.entity.OptionEntity;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/shiftGenerate/index.jsp", "/shiftGenerate/setting.jsp", "/shiftGenerate/confirm.jsp"})
public class ShiftGenerateServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String servletPath = req.getServletPath();
        OptionRepository optRepo = new OptionRepository();
        OptionEntity opt = null;
        try {
            opt = optRepo.find();
        } catch (Exception ignored) {
        }

        if (servletPath.endsWith("confirm.jsp")) {
            // confirm: prefer request parameters, otherwise use DB option
            if (req.getParameter("generateDays") != null) {
                req.setAttribute("days", Integer.parseInt(req.getParameter("generateDays")));
            } else if (opt != null) {
                req.setAttribute("days", opt.getGenerateDays());
            }

            if (req.getParameter("maxWorkMonth") != null) {
                req.setAttribute("maxMonth", Integer.parseInt(req.getParameter("maxWorkMonth")));
            } else if (opt != null) {
                req.setAttribute("maxMonth", opt.getMaxWorktimeofMonth());
            }

            if (req.getParameter("maxWorkDay") != null) {
                req.setAttribute("maxDay", Integer.parseInt(req.getParameter("maxWorkDay")));
            } else if (opt != null) {
                req.setAttribute("maxDay", opt.getMaxWorktimeofDay());
            }

            if (req.getParameter("newcomerMinutes") != null) {
                req.setAttribute("newcomerMinutes", Integer.parseInt(req.getParameter("newcomerMinutes")));
            } else if (opt != null) {
                req.setAttribute("newcomerMinutes", opt.getNewcomerThresholdMinutes());
            }

            if (req.getParameter("seniorRequired") != null) {
                try {
                    req.setAttribute("seniorRequired", Integer.parseInt(req.getParameter("seniorRequired")));
                } catch (NumberFormatException ignored) {
                }
            } else if (opt != null) {
                req.setAttribute("seniorRequired", opt.getRequiredSeniorWorkers());
            }

            if (req.getParameter("firstDate") != null) {
                req.setAttribute("firstDate", req.getParameter("firstDate"));
            } else if (opt != null) {
                req.setAttribute("firstDate", opt.getFirstdate());
            }

            req.getRequestDispatcher("/WEB-INF/jsp/shiftGenerate/confirm.jsp").forward(req, resp);
        } else if (servletPath.endsWith("setting.jsp")) {
            // setting: populate input initial values from DB when available
            if (opt != null) {
                req.setAttribute("firstDate", opt.getFirstdate());
                req.setAttribute("generateDays", opt.getGenerateDays());
                req.setAttribute("maxWorkMonth", opt.getMaxWorktimeofMonth());
                req.setAttribute("maxWorkDay", opt.getMaxWorktimeofDay());
                req.setAttribute("newcomerMinutes", opt.getNewcomerThresholdMinutes());
                req.setAttribute("seniorRequired", opt.getRequiredSeniorWorkers());
            }
            req.getRequestDispatcher("/WEB-INF/jsp/shiftGenerate/setting.jsp").forward(req, resp);
        } else if (servletPath.endsWith("index.jsp")) {
            req.getRequestDispatcher("/WEB-INF/jsp/shiftGenerate/index.jsp").forward(req, resp);
        } else {
            req.getRequestDispatcher("/WEB-INF/jsp/shiftGenerate/result.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
