package app.servlet.owner;

import app.entity.PositionEntity;
import app.repository.PositionRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/admin/position"})
public class PositionAdminServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PositionRepository repo = new PositionRepository();
        List<PositionEntity> list = repo.findAll();
        req.setAttribute("positions", list);
        String err = req.getParameter("error");
        if (err != null && !err.isEmpty()) req.setAttribute("error", err);
        req.getRequestDispatcher("/WEB-INF/jsp/admin/position_list.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        PositionRepository repo = new PositionRepository();

        try {
            if ("create".equals(action)) {
                String name = req.getParameter("name");
                int minWorkers = Integer.parseInt(req.getParameter("minWorkers"));
                int maxWorkers = Integer.parseInt(req.getParameter("maxWorkers"));
                int requireAuth = Integer.parseInt(req.getParameter("requireAuthorityWorkers"));
                repo.insert(name, minWorkers, maxWorkers, requireAuth);
            } else if ("update".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                String name = req.getParameter("name");
                int minWorkers = Integer.parseInt(req.getParameter("minWorkers"));
                int maxWorkers = Integer.parseInt(req.getParameter("maxWorkers"));
                int requireAuth = Integer.parseInt(req.getParameter("requireAuthorityWorkers"));
                repo.update(id, name, minWorkers, maxWorkers, requireAuth);
            } else if ("delete".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                repo.delete(id);
            } else if ("reactivate".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                repo.setActive(id, true);
            }
        } catch (Exception e) {
            String msg = e.getMessage();
            if (msg == null) msg = "不明なエラーが発生しました";
            resp.sendRedirect(req.getContextPath() + "/admin/position?error=" + java.net.URLEncoder.encode(msg, "UTF-8"));
            return;
        }

        resp.sendRedirect(req.getContextPath() + "/admin/position");
    }
}
