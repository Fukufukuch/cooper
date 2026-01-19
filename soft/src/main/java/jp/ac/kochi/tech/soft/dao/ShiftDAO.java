package jp.ac.kochi.tech.soft.dao;

import jp.ac.kochi.tech.soft.model.Shift;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class ShiftDAO {

    private static final String URL =
        "jdbc:mysql://localhost:3306/your_db_name?useSSL=false&serverTimezone=Asia/Tokyo";
    private static final String USER = "cooper";
    private static final String PASSWORD = "CooperG10!";

    public Map<Integer, List<Shift>> getMonthlyShifts(
            String userID, int year, int month) {

        Map<Integer, List<Shift>> result = new HashMap<>();

        String sql = """
            SELECT shift_info_day, shift_timetable
            FROM shift
            WHERE userID = ?
              AND YEAR(shift_info_day) = ?
              AND MONTH(shift_info_day) = ?
            ORDER BY shift_info_day, shift_timetable_number
        """;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, userID);
            ps.setInt(2, year);
            ps.setInt(3, month);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                LocalDate day = rs.getDate("shift_info_day").toLocalDate();
                String timetable = rs.getString("shift_timetable");

                int dayOfMonth = day.getDayOfMonth();

                result
                    .computeIfAbsent(dayOfMonth, k -> new ArrayList<>())
                    .add(new Shift(day, timetable));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }
}
