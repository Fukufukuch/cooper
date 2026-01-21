package app.dao;

import app.db.Db;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ShiftDao {

    /** 一覧：日付で範囲指定（例：2026-01-01〜2026-01-31） */
    public List<ShiftRow> findByDateRange(LocalDate from, LocalDate to) throws SQLException {
        String sql =
                "SELECT s.shiftID, s.userID, u.username, s.shift_info_day, s.shift_timetable, s.shift_timetable_number " +
                "FROM shift s " +
                "JOIN users u ON u.userID = s.userID " +
                "WHERE s.shift_info_day >= ? AND s.shift_info_day <= ? " +
                "ORDER BY s.shift_info_day ASC, s.shift_timetable_number ASC, s.shiftID ASC";

        List<ShiftRow> list = new ArrayList<>();

        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(from));
            ps.setDate(2, Date.valueOf(to));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ShiftRow r = new ShiftRow();
                    r.shiftID = rs.getInt("shiftID");
                    r.userID = rs.getString("userID");
                    r.username = rs.getString("username");
                    Date d = rs.getDate("shift_info_day");
                    r.shiftInfoDay = (d == null) ? null : d.toLocalDate();
                    r.shiftTimetable = rs.getString("shift_timetable"); // 例: "早番"
                    Integer num = (Integer) rs.getObject("shift_timetable_number");
                    r.shiftTimetableNumber = num;
                    list.add(r);
                }
            }
        }
        return list;
    }

    /** 削除：主キー shiftID で消す（安全：1件だけ） */
    public boolean deleteByShiftId(int shiftID) throws SQLException {
        String sql = "DELETE FROM shift WHERE shiftID = ?";
        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, shiftID);
            return ps.executeUpdate() == 1;
        }
    }

    /** 追加：最小構成でINSERT（shiftIDはauto） */
    public int insert(String userID, LocalDate day, String timetable, Integer timetableNumber) throws SQLException {
        String sql =
                "INSERT INTO shift (userID, shift_info_day, shift_timetable, shift_timetable_number) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, userID);
            ps.setDate(2, day == null ? null : Date.valueOf(day));
            ps.setString(3, timetable);
            if (timetableNumber == null) ps.setNull(4, Types.INTEGER);
            else ps.setInt(4, timetableNumber);

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        }
        return -1;
    }

    /** JSPに渡す用のDTO */
    public static class ShiftRow {
        public int shiftID;
        public String userID;
        public String username;
        public LocalDate shiftInfoDay;
        public String shiftTimetable;
        public Integer shiftTimetableNumber;
    }
}
