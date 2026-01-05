package jp.ac.kochi.tech;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.*;

// クラス名はファイル名と一致させる必要があります
@WebServlet("/authLogin") 
public class AuthLoginModule extends HttpServlet {

    private static Map<String, String> userDb = new HashMap<>();

    static {
        // テスト用データ
        userDb.put("taro", "1234");
        userDb.put("hanako", "pass");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        // 🟢 修正点1: JSPの input name="" に合わせる
        String userID = req.getParameter("userID");   // JSP側の name="userID"
        String password = req.getParameter("password"); // JSP側の name="password"

        // ログイン判定
        // userDbのキーとパスワードをチェック
        if (userDb.containsKey(userID) && userDb.get(userID).equals(password)) {
            HttpSession session = req.getSession();
            session.setAttribute("loginUser", userID);

            // ログイン成功時の移動先（adminShift.jspなど、実在するファイルへ）
            req.getRequestDispatcher("/adminShift.jsp").forward(req, resp);
        } else {
            // 失敗時
            req.setAttribute("errorMessage", "IDまたはパスワードが違います");
            req.getRequestDispatcher("/UserLogin.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/UserLogin.jsp").forward(req, resp);
    }
}