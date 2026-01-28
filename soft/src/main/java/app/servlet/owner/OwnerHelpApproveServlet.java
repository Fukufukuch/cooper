package app.servlet.owner;

import app.dao.RequestDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/owner/help/approve")
public class OwnerHelpApproveServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        jakarta.servlet.http.HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userID") == null) {
            resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            resp.setHeader("Pragma", "no-cache");
            resp.setHeader("Expires", "0");
            resp.sendRedirect(req.getContextPath() + "/LoginServlet");
            return;
        }

        req.setCharacterEncoding("UTF-8");

        int requestID = Integer.parseInt(req.getParameter("requestID"));

        try {
            RequestDao dao = new RequestDao();
            dao.approveToShift(requestID);

            resp.sendRedirect(req.getContextPath() + "/owner/help");

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
