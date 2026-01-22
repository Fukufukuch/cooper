package app.servlet.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/user/setting/menu")
public class UserMenuServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // タブ制御用（必要なら）
        req.setAttribute("activeTab", "setting");

        // userMenu.jsp へフォワード
        req.getRequestDispatcher("/WEB-INF/jsp/user/userMenu.jsp")
           .forward(req, resp);
    }
}
