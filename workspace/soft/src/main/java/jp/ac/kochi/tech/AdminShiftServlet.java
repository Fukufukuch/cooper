package jp.ac.kochi.tech;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.*;

@WebServlet("/adminShift")
public class AdminShiftServlet extends HttpServlet {

    // 🔴 一時保存用（サーバーが動いてる間だけ残る）
    private static List<Map<String, String>> shiftList = new ArrayList<>();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String name = req.getParameter("name");
        String date = req.getParameter("date");
        String time = req.getParameter("time");

        // 1件分のシフト
        Map<String, String> shift = new HashMap<>();
        shift.put("name", name);
        shift.put("date", date);
        shift.put("time", time);

        // 保存
        shiftList.add(shift);

        // JSPへ渡す
        req.setAttribute("shiftList", shiftList);
        req.getRequestDispatcher("/adminShift.jsp").forward(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setAttribute("shiftList", shiftList);
        req.getRequestDispatcher("/adminShift.jsp").forward(req, resp);
    }
}
