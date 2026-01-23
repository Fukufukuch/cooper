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
