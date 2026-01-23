package app.dao;

import app.db.Db;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ShiftDao {

    public static class ShiftRow {
        public int id;
        public LocalDate date;
        public String workerID;
        public String username;
        public int positionID;

        public int timeslotID;
        public String timeslotName;
        public LocalTime startTime;
        public LocalTime endTime;
    }

    /** 期間検索（OwnerShiftEditServlet が使う） */
    public List<ShiftRow> findByDateRange(LocalDate from, LocalDate to) throws SQLException {

        String sql =
                "SELECT s.id, s.date, s.workerID, u.username, s.positionID, " +
                "       s.timeslotID, t.name AS timeslot_name, t.start_time, t.end_time " +
                "FROM shift s " +
                "LEFT JOIN users u ON u.userID = s.workerID " +
                "LEFT JOIN timeslot t ON t.timeslotID = s.timeslotID " +
                "WHERE s.date BETWEEN ? AND ? " +
                "ORDER BY s.date ASC, s.id ASC";

        List<ShiftRow> list = new ArrayList<>();

        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(from));
            ps.setDate(2, Date.valueOf(to));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ShiftRow r = new ShiftRow();
                    r.id = rs.getInt("id");

                    Date d = rs.getDate("date");
                    r.date = (d == null) ? null : d.toLocalDate();

                    r.workerID = rs.getString("workerID");
                    r.username = rs.getString("username");
                    r.positionID = rs.getInt("positionID");

                    r.timeslotID = rs.getInt("timeslotID");
                    r.timeslotName = rs.getString("timeslot_name");

                    Time s = rs.getTime("start_time");
                    Time e = rs.getTime("end_time");
                    r.startTime = (s == null) ? null : s.toLocalTime();
                    r.endTime   = (e == null) ? null : e.toLocalTime();

                    list.add(r);
                }
            }
        }
        return list;
    }

    /** 追加（OwnerShiftAddServlet が使う） */
    public void insert(LocalDate day, String userID, int positionID, int timeslotID) throws SQLException {

        String ensureWorker = "INSERT IGNORE INTO worker(workerID) VALUES (?)";

        String sql =
                "INSERT INTO shift(date, workerID, positionID, timeslotID) " +
                "VALUES(?,?,?,?)";

        try (Connection con = Db.getConnection()) {
            con.setAutoCommit(false);

            // worker行確保（FK対策）
            try (PreparedStatement ps = con.prepareStatement(ensureWorker)) {
                ps.setString(1, userID);
                ps.executeUpdate();
            }

            // shift追加
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setDate(1, Date.valueOf(day));
                ps.setString(2, userID);
                ps.setInt(3, positionID);
                ps.setInt(4, timeslotID);
                ps.executeUpdate();
            }

            con.commit();
            con.setAutoCommit(true);
        }
    }

    /** 削除（OwnerShiftDeleteServlet が使う） */
    public void deleteByShiftId(int shiftId) throws SQLException {
        String sql = "DELETE FROM shift WHERE id = ?";
        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, shiftId);
            ps.executeUpdate();
        }
    }
}
