package app.servlet.user;

import java.io.BufferedReader;
import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/user/shift/submit/api")
public class ShiftSubmitServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 文字コード設定
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");

        // ===== リクエストボディ(JSON)を読む =====
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }

        String json = sb.toString();

        // デバッグ用（Tomcatログに出る）
        System.out.println("受信JSON: " + json);

        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write("{\"status\":\"ok\"}");
    }
}
