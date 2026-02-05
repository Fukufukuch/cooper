package jp.ac.kochi.tech.soft.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import jp.ac.kochi.tech.soft.dao.ShiftDAO;
import jp.ac.kochi.tech.soft.model.Shift;
import jp.ac.kochi.tech.Login;
import jp.ac.kochi.tech.Admin;
import jp.ac.kochi.tech.DBconfig;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
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
            response.sendRedirect(request.getContextPath() + "/LoginServlet");
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

                List<Map<String, Object>> approvedHelps = new ArrayList<>();
                String sqlApprovedHelp = """
                        SELECT h.helpID,
                                     h.help_want_userID, uw.username AS want_username,
                                     h.helper_userID, uh.username AS helper_username,
                                     h.help_want_day, h.help_want_time_start, h.help_want_time_end,
                                     sw.date AS want_shift_date, sw.start_minute AS want_start_min, sw.end_minute AS want_end_min,
                                     sh.date AS helper_shift_date, sh.start_minute AS helper_start_min, sh.end_minute AS helper_end_min
                        FROM help h
                        LEFT JOIN users uw ON uw.userID = h.help_want_userID
                        LEFT JOIN shift sw ON sw.workerID = h.help_want_userID AND sw.date = h.help_want_day
                        LEFT JOIN users uh ON uh.userID = h.helper_userID
                        LEFT JOIN shift sh ON sh.workerID = h.helper_userID AND sh.date = h.help_want_day
                        WHERE h.apply = 2
                            AND (
                                     (h.help_want_userID = ? AND (h.want_user_acknowledged IS NULL OR h.want_user_acknowledged = 0))
                                OR (h.helper_userID = ? AND (h.helper_user_acknowledged IS NULL OR h.helper_user_acknowledged = 0))
                            )
                        ORDER BY h.help_want_day DESC, h.help_want_time_start DESC
                """;

        try (Connection con = DBconfig.getConnection();
             PreparedStatement ps = con.prepareStatement(sqlApprovedHelp)) {

            ps.setString(1, userID);
            ps.setString(2, userID);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> help = new HashMap<>();
                    help.put("helpID", rs.getInt("helpID"));
                    help.put("wantUserID", rs.getString("help_want_userID"));
                    help.put("wantUsername", rs.getString("want_username"));
                    help.put("helperUserID", rs.getString("helper_userID"));
                    help.put("helperUsername", rs.getString("helper_username"));
                    help.put("day", rs.getDate("help_want_day"));
                    help.put("startTime", rs.getTime("help_want_time_start"));
                    help.put("endTime", rs.getTime("help_want_time_end"));
                    help.put("wantShiftDate", rs.getDate("want_shift_date"));
                    help.put("wantStartMin", rs.getInt("want_start_min"));
                    help.put("wantEndMin", rs.getInt("want_end_min"));
                    help.put("helperShiftDate", rs.getDate("helper_shift_date"));
                    help.put("helperStartMin", rs.getInt("helper_start_min"));
                    help.put("helperEndMin", rs.getInt("helper_end_min"));
                    approvedHelps.add(help);
                }
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }

        request.setAttribute("year", year);
        request.setAttribute("month", month);
        request.setAttribute("daysInMonth", daysInMonth);
        request.setAttribute("startDayOfWeek", startDayOfWeek);
        request.setAttribute("shiftMap", shiftMap);
        request.setAttribute("userName", userName);
        request.setAttribute("activeTab", "calendar");
        request.setAttribute("approvedHelps", approvedHelps);

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
