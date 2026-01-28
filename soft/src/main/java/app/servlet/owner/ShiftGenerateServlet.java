package app.servlet.owner;

import jakarta.servlet.ServletException;
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
        if (servletPath.endsWith("confirm.jsp")) {
            req.getRequestDispatcher("/WEB-INF/jsp/shiftGenerate/confirm.jsp").forward(req, resp);
        } else if (servletPath.endsWith("setting.jsp")) {
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
