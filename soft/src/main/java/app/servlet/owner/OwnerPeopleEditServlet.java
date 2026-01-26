package app.servlet.owner;

import app.dao.UserDao;
import app.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Date;

@WebServlet("/owner/people/edit")
public class OwnerPeopleEditServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        jakarta.servlet.http.HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userID") == null) {
            resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            resp.setHeader("Pragma", "no-cache");
            resp.setHeader("Expires", "0");
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        String userID = req.getParameter("userID");

        if (userID == null || userID.isBlank()) {
            resp.sendRedirect(req.getContextPath() + "/owner/people");
            return;
        }

        try {
            UserDao dao = new UserDao();

            // ✅ 管理者・スタッフどちらも取得
            User u = dao.findUserById(userID);

            if (u == null) {
                req.setAttribute("error", "対象ユーザーが見つかりません。");
                req.setAttribute("activeTab", "people");
                req.getRequestDispatcher("/WEB-INF/jsp/owner/people_edit.jsp")
                   .forward(req, resp);
                return;
            }

            req.setAttribute("user", u);
            req.setAttribute("activeTab", "people");
            req.getRequestDispatcher("/WEB-INF/jsp/owner/people_edit.jsp")
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
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        req.setCharacterEncoding("UTF-8");

        String userID = req.getParameter("userID");
        String username = req.getParameter("username");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone_number");
        String dobStr = req.getParameter("date_of_birth");
        String tagStr = req.getParameter("tag");
        String posStr = req.getParameter("position");
        String workPlace = req.getParameter("work_place");

        // --------------------
        // 入力チェック
        // --------------------
        if (userID == null || userID.isBlank()
                || username == null || username.isBlank()
                || email == null || email.isBlank()
                || phone == null || phone.isBlank()
                || dobStr == null || dobStr.isBlank()
                || workPlace == null || workPlace.isBlank()) {

            req.setAttribute("error", "未入力の項目があります。");

            User back = new User();
            back.setUserID(userID);
            back.setUsername(username);
            back.setEmail(email);
            back.setPhoneNumber(phone);
            try { back.setDateOfBirth(Date.valueOf(dobStr)); } catch (Exception ignore) {}
            try { back.setTag(Integer.parseInt(tagStr)); } catch (Exception ignore) {}
            try { back.setPosition(Integer.parseInt(posStr)); } catch (Exception ignore) {}
            back.setWorkPlace(workPlace);

            req.setAttribute("user", back);
            req.setAttribute("activeTab", "people");
            req.getRequestDispatcher("/WEB-INF/jsp/owner/people_edit.jsp")
               .forward(req, resp);
            return;
        }

        int tag = 0;
        int position = 1;
        Date dob;

        try {
            dob = Date.valueOf(dobStr);
        } catch (Exception e) {
            req.setAttribute("error", "生年月日の形式が不正です。");
            req.setAttribute("activeTab", "people");
            req.getRequestDispatcher("/WEB-INF/jsp/owner/people_edit.jsp")
               .forward(req, resp);
            return;
        }

        try { tag = Integer.parseInt(tagStr); } catch (Exception ignore) {}
        try { position = Integer.parseInt(posStr); } catch (Exception ignore) {}

        try {
            UserDao dao = new UserDao();

            // ✅ 管理者・スタッフ共通更新
            boolean ok = dao.updateUser(
                    userID,
                    username,
                    email,
                    phone,
                    dob,
                    tag,
                    position,
                    workPlace
            );

            if (!ok) {
                req.setAttribute("error", "更新に失敗しました。");
                User u = dao.findUserById(userID);
                req.setAttribute("user", u);
                req.getRequestDispatcher("/WEB-INF/jsp/owner/people_edit.jsp")
                   .forward(req, resp);
                return;
            }

            req.setAttribute("userID", userID);
            req.getRequestDispatcher("/WEB-INF/jsp/owner/people_edit_done.jsp")
               .forward(req, resp);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
