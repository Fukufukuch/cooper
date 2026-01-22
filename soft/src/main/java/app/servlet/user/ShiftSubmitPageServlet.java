package app.servlet.user;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/user/shift/submit")
public class ShiftSubmitPageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setAttribute("activeTab", "submit");
        req.getRequestDispatcher("/WEB-INF/jsp/user/shiftSubmit.jsp")
           .forward(req, resp);
    }
}

