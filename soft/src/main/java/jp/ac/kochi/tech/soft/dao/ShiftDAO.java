package jp.ac.kochi.tech.soft.dao;

import jp.ac.kochi.tech.soft.model.Shift;
import jp.ac.kochi.tech.DBconfig;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class ShiftDAO {

    public Map<Integer, List<Shift>> getMonthlyShifts(
            String userID, int year, int month) {

        Map<Integer, List<Shift>> result = new HashMap<>();

        // DBconfig から接続情報を取得
        DBconfig db_info = new DBconfig();
        String url = db_info.getDBinfo().get("url");
        String user = db_info.getDBinfo().get("user");
        String pass = db_info.getDBinfo().get("password");

        String sql = """
            SELECT date, shift_timetable
            FROM shift
            WHERE workerID = ?
              AND YEAR(date) = ?
              AND MONTH(date) = ?
            ORDER BY date
        """;

        try {
            // JDBCドライバの確認
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("★★ ShiftDAO: JDBCドライバOK ★★");
        } catch (ClassNotFoundException e) {
            System.out.println("★★ ShiftDAO: JDBCドライバNG ★★");
            e.printStackTrace();
        }

        try (Connection conn = DriverManager.getConnection(url, user, pass);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            System.out.println("★★ ShiftDAO: DB接続成功 ★★");
            System.out.println("接続情報: " + url);
            System.out.println("userID: " + userID + ", year: " + year + ", month: " + month);

            ps.setString(1, userID);
            ps.setInt(2, year);
            ps.setInt(3, month);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                LocalDate day = rs.getDate("date").toLocalDate();
                String timetable = rs.getString("shift_timetable");

                int dayOfMonth = day.getDayOfMonth();

                result
                    .computeIfAbsent(dayOfMonth, k -> new ArrayList<>())
                    .add(new Shift(day, timetable));
            }

            System.out.println("★★ ShiftDAO: 取得したシフト数: " + result.size() + " ★★");

        } catch (SQLException e) {
            System.out.println("★★ ShiftDAO: SQL実行エラー ★★");
            e.printStackTrace();
        }

        return result;
    }
}
