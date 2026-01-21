package app.servlet.owner;

import app.dao.ShiftDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@WebServlet("/owner/shift/add")
public class OwnerShiftAddServlet extends HttpServlet {

    // 入力ゆれ対応：2026-1-9 / 2026/01/09 とかも通す
    private static LocalDate parseFlexibleDate(String raw) {
        if (raw == null) return null;
        String s = raw.trim();
        if (s.isEmpty()) return null;

        // "/" を "-" に寄せる
        s = s.replace('/', '-');

        // "YYYY-M-DD" や "YYYY-MM-D" を "YYYY-MM-DD" に揃える
        // 例: 2026-1-09 -> 2026-01-09, 2026-01-9 -> 2026-01-09
        String[] parts = s.split("-");
        if (parts.length == 3) {
            String y = parts[0];
            String m = parts[1];
            String d = parts[2];

            // 月日が1桁ならゼロ埋め
            if (m.length() == 1) m = "0" + m;
            if (d.length() == 1) d = "0" + d;

            s = y + "-" + m + "-" + d;
        }

        // 最終的に ISO(yyyy-MM-dd) でパース
        return LocalDate.parse(s, DateTimeFormatter.ISO_LOCAL_DATE);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        try {
            String userID = req.getParameter("userID");
            String dayStr = req.getParameter("day");           // 想定: 2026-01-20
            String timetable = req.getParameter("timetable");  // 例: 早番
            String numStr = req.getParameter("timetableNumber");

            if (userID == null || userID.isBlank()) {
                throw new ServletException("userID が空です");
            }

            LocalDate day = parseFlexibleDate(dayStr);

            Integer num = null;
            if (numStr != null && !numStr.isBlank()) {
                num = Integer.valueOf(numStr.trim());
            }

            ShiftDao dao = new ShiftDao();
            dao.insert(userID.trim(), day, timetable, num);

            // 月表示に戻す
            String year = req.getParameter("year");
            String month = req.getParameter("month");

            String redirect = req.getContextPath() + "/owner/shift/edit";
            if (year != null && month != null && !year.isBlank() && !month.isBlank()) {
                redirect += "?year=" + year + "&month=" + month;
            }
            resp.sendRedirect(redirect);

        } catch (DateTimeParseException e) {
            // 日付だけは分かりやすく原因を出す
            throw new ServletException("日付形式が不正です。例: 2026-01-09 の形式で入力してください。", e);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
