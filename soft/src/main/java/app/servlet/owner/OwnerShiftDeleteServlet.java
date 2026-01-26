package app.servlet.owner;

import app.dao.ShiftDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/owner/shift/delete")
public class OwnerShiftDeleteServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        jakarta.servlet.http.HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userID") == null) {
            resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            resp.setHeader("Pragma", "no-cache");
            resp.setHeader("Expires", "0");
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        try {
            int id = Integer.parseInt(req.getParameter("id"));
            ShiftDao dao = new ShiftDao();
            dao.deleteByShiftId(id);

            resp.sendRedirect(req.getContextPath() + "/owner/shift/edit");

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
