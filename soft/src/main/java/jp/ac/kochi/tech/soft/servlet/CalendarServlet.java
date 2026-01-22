package jp.ac.kochi.tech.soft.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import jp.ac.kochi.tech.soft.dao.ShiftDAO;
import jp.ac.kochi.tech.soft.model.Shift;
import jp.ac.kochi.tech.Login;
import jp.ac.kochi.tech.Admin;

import java.io.IOException;
import java.time.*;
import java.util.*;

@WebServlet("/calendar")
public class CalendarServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("★★ CalendarServlet: 処理開始 ★★");

        HttpSession session = request.getSession();
        String userID = (String) session.getAttribute("userID");
        String userName = (String) session.getAttribute("userName");
        Integer usertype = (Integer) session.getAttribute("usertype");

        System.out.println("セッションから取得したuserID: [" + userID + "]");
        System.out.println("セッションから取得したuserName: [" + userName + "]");

        if (userID == null) {
            System.out.println("★★ CalendarServlet: ログイン情報がありません ★★");
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // userName がセッションにない場合は、ダミーパスワード（空）でデータベースから取得
        if (userName == null) {
            Login login = new Login();
            Admin admin = login.check(userID, "");
            if (admin != null && admin.getName() != null) {
                userName = admin.getName();
                session.setAttribute("userName", userName);
                System.out.println("★★ CalendarServlet: DBからユーザー名を取得: [" + userName + "] ★★");
            } else {
                userName = userID; // デフォルトでuserIDを使用
            }
        }

        int year, month;
        LocalDate today = LocalDate.now();

        try {
            year = Integer.parseInt(request.getParameter("year"));
            month = Integer.parseInt(request.getParameter("month"));
        } catch (Exception e) {
            year = today.getYear();
            month = today.getMonthValue();
        }

        System.out.println("表示対象年月: " + year + "年 " + month + "月");

        YearMonth ym = YearMonth.of(year, month);
        LocalDate firstDay = ym.atDay(1);

        int daysInMonth = ym.lengthOfMonth();
        int startDayOfWeek = firstDay.getDayOfWeek().getValue();

        ShiftDAO dao = new ShiftDAO();
        Map<Integer, List<Shift>> shiftMap =
                dao.getMonthlyShifts(userID, year, month);

        System.out.println("★★ CalendarServlet: シフト取得完了 ★★");
        System.out.println("取得したシフト件数: " + shiftMap.size());

        request.setAttribute("year", year);
        request.setAttribute("month", month);
        request.setAttribute("daysInMonth", daysInMonth);
        request.setAttribute("startDayOfWeek", startDayOfWeek);
        request.setAttribute("shiftMap", shiftMap);
        request.setAttribute("userName", userName);
        request.setAttribute("activeTab", "calendar");

        // usertype に基づいて異なるJSPにフォワード
        String jspPath;
        if (usertype != null && usertype == 0x01) {
            jspPath = "/WEB-INF/jsp/user/userCalendar.jsp";
        } else {
            jspPath = "/WEB-INF/jsp/owner/ownerCalendar.jsp";
        }

        System.out.println("★★ CalendarServlet: フォワード先 = " + jspPath + " ★★");
        request.getRequestDispatcher(jspPath)
               .forward(request, response);
    }
}
