package app.servlet.owner;

import app.dao.ShiftDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/owner/shift/delete")
public class OwnerShiftDeleteServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            int shiftID = Integer.parseInt(req.getParameter("shiftID"));

            ShiftDao dao = new ShiftDao();
            dao.deleteByShiftId(shiftID);

            // 画面の月に戻す（year/monthが来てたら維持）
            String year = req.getParameter("year");
            String month = req.getParameter("month");

            String redirect = req.getContextPath() + "/owner/shift/edit";
            if (year != null && month != null) {
                redirect += "?year=" + year + "&month=" + month;
            }
            resp.sendRedirect(redirect);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
