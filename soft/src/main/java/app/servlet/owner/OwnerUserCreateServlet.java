package app.servlet.owner;

import app.dao.PositionDao;
import app.dao.UserDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

@WebServlet("/owner/account/create")
public class OwnerUserCreateServlet extends HttpServlet {

    private void loadPositions(HttpServletRequest req) throws Exception {
        PositionDao pdao = new PositionDao();
        List<PositionDao.PositionItem> positions = pdao.list();
        req.setAttribute("positions", positions);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        jakarta.servlet.http.HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userID") == null) {
            resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            resp.setHeader("Pragma", "no-cache");
            resp.setHeader("Expires", "0");
            resp.sendRedirect(req.getContextPath() + "/LoginServlet");
            return;
        }

        try {
            req.setAttribute("activeTab", "setting");
            loadPositions(req);
            req.getRequestDispatcher("/WEB-INF/jsp/owner/account_create.jsp")
               .forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

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

        String username = req.getParameter("username");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");
        String dob = req.getParameter("dob");
        String workPlace = req.getParameter("work_place");
        String tagStr = req.getParameter("tag");
        String positionStr = req.getParameter("positionID");

        // 0=管理者 / 1=労働者(スタッフ)
        String usertypeStr = req.getParameter("usertype"); // "0" or "1"

        String password = req.getParameter("password");
        String passwordConfirm = req.getParameter("passwordConfirm");

        // ---- validation（落ちない最低限）----
        if (username == null || username.isBlank()
                || email == null || email.isBlank()
                || phone == null || phone.isBlank()
                || dob == null || dob.isBlank()
                || workPlace == null || workPlace.isBlank()
                || usertypeStr == null || usertypeStr.isBlank()) {

            try {
                req.setAttribute("activeTab", "setting");
                req.setAttribute("error", "未入力の項目があります。");
                loadPositions(req);
                req.getRequestDispatcher("/WEB-INF/jsp/owner/account_create.jsp")
                   .forward(req, resp);
                return;
            } catch (Exception e) {
                throw new ServletException(e);
            }
        }

        if (password == null || !password.equals(passwordConfirm)) {
            try {
                req.setAttribute("activeTab", "setting");
                req.setAttribute("error", "パスワードが一致しません");
                loadPositions(req);
                req.getRequestDispatcher("/WEB-INF/jsp/owner/account_create.jsp")
                   .forward(req, resp);
                return;
            } catch (Exception e) {
                throw new ServletException(e);
            }
        }

        int tag = 0;
        int positionID = 1; // 未設定(1)に倒す
        int usertype = 1;

        try { tag = Integer.parseInt(tagStr); } catch (Exception ignore) {}
        try { positionID = Integer.parseInt(positionStr); } catch (Exception ignore) {}
        try { usertype = Integer.parseInt(usertypeStr); } catch (Exception ignore) {}

        try {
            UserDao dao = new UserDao();

            String createdId = dao.createUser(
                    username,
                    email,
                    phone,
                    Date.valueOf(dob),
                    password,
                    usertype,   // 0 or 1
                    tag,
                    positionID,
                    workPlace
            );

            req.setAttribute("createdId", createdId);
            req.setAttribute("createdType", (usertype == 0 ? "管理者" : "スタッフ"));

        } catch (Exception e) {
            throw new ServletException(e);
        }

        req.setAttribute("activeTab", "setting");
        req.getRequestDispatcher("/WEB-INF/jsp/owner/account_create_done.jsp")
           .forward(req, resp);
    }
}
