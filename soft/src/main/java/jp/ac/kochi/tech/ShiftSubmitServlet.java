package jp.ac.kochi.tech;

import java.io.BufferedReader;
import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/submitShift")
public class ShiftSubmitServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");

        // ===== JSON文字列をそのまま受け取る =====
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;

        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        String json = sb.toString();

        System.out.println("受信JSON: " + json);

        // ここで json をDBに保存したり、後でパースする

        response.getWriter().write("{\"status\":\"success\"}");
    }
}
