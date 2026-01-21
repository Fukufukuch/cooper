package app.servlet.owner;

import app.dao.RequestDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/owner/help/approve")
public class OwnerHelpApproveServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            req.setCharacterEncoding("UTF-8");
            int requestID = Integer.parseInt(req.getParameter("requestID"));

            RequestDao dao = new RequestDao();
            dao.approveToShift(requestID); // shiftへ反映 + request削除

            resp.sendRedirect(req.getContextPath() + "/owner/help");

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
