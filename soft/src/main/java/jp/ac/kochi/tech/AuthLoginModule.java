package jp.ac.kochi.tech;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.*;

// クラス名はファイル名と一致させる必要があります
@WebServlet("/authLogin") 
public class AuthLoginModule extends HttpServlet {

    // 🔴 簡易ユーザーデータベース（サーバーが動いている間だけ有効なモック）
    // 本来はデータベースを使いますが、AdminShiftServletのリストと同じようにメモリ上で処理します
    private static Map<String, String> userDb = new HashMap<>();

    static {
        userDb.put("taro", "1234");
        userDb.put("hanako", "pass");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        // フォームから送信されたIDとパスワードを取得
        String name = req.getParameter("name"); // または "id"
        String pass = req.getParameter("pass"); // または "password"

        // ログイン判定（userDbに含まれているか、パスワードが合うか）
        if (userDb.containsKey(name) && userDb.get(name).equals(pass)) {
            // ■ 成功時: セッションにユーザー情報を保存
            HttpSession session = req.getSession();
            session.setAttribute("loginUser", name);

            // ログイン後の画面（例: adminShiftなど）へ移動
            // redirectの方がリロード時の再送信を防げますが、ここではforwardの形式に合わせています
            req.getRequestDispatcher("/adminShift.jsp").forward(req, resp);
        } else {
            // ■ 失敗時: エラーメッセージを入れてログイン画面に戻す
            req.setAttribute("errorMessage", "IDまたはパスワードが違います");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        // ログイン画面を表示するだけ
        // (もしログアウト処理を入れるならここに書くこともあります)
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }
}