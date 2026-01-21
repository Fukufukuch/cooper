package app.dao;

import app.db.Db;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class RequestDao {

    // 画面表示用
    public static class RequestRow {
        public int requestID;
        public String userID;
        public String username; // usersから
        public LocalDate day;
        public LocalTime start;
        public LocalTime end;
    }

    /** 承認待ち一覧（request + users） */
    public List<RequestRow> findAll() throws SQLException {

        String sql =
            "SELECT r.requestID, r.userID, u.username, " +
            "       r.shift_request_day, r.shift_request_time_start, r.shift_request_time_end " +
            "FROM request r " +
            "JOIN users u ON u.userID = r.userID " +     // ★ここ重要：users
            "ORDER BY r.requestID DESC";

        List<RequestRow> list = new ArrayList<>();

        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                RequestRow row = new RequestRow();
                row.requestID = rs.getInt("requestID");
                row.userID = rs.getString("userID");
                row.username = rs.getString("username");
                row.day = rs.getDate("shift_request_day").toLocalDate();
                row.start = rs.getTime("shift_request_time_start").toLocalTime();
                row.end = rs.getTime("shift_request_time_end").toLocalTime();
                list.add(row);
            }
        }
        return list;
    }

    /** 却下：requestから削除 */
    public boolean deleteById(int requestID) throws SQLException {
        String sql = "DELETE FROM request WHERE requestID = ?";
        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, requestID);
            return ps.executeUpdate() == 1;
        }
    }

    /** 承認：shiftに反映してrequest削除（トランザクション） */
    public void approveToShift(int requestID) throws SQLException {

        String select =
            "SELECT userID, shift_request_day, shift_request_time_start, shift_request_time_end " +
            "FROM request WHERE requestID = ?";

        String insertShift =
            "INSERT INTO shift (userID, shift_info_day, shift_timetable, shift_timetable_number) " +
            "VALUES (?, ?, ?, ?)";

        String deleteReq = "DELETE FROM request WHERE requestID = ?";

        try (Connection con = Db.getConnection()) {
            con.setAutoCommit(false);

            String userID;
            LocalDate day;
            LocalTime start;
            LocalTime end;

            try (PreparedStatement ps = con.prepareStatement(select)) {
                ps.setInt(1, requestID);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        con.rollback();
                        return; // もう消えてるとか
                    }
                    userID = rs.getString("userID");
                    day = rs.getDate("shift_request_day").toLocalDate();
                    start = rs.getTime("shift_request_time_start").toLocalTime();
                    end = rs.getTime("shift_request_time_end").toLocalTime();
                }
            }

            // 時間 → 勤務区分（適当に決める。必要なら後で調整）
            ShiftType type = mapToShiftType(start, end);

            try (PreparedStatement ps = con.prepareStatement(insertShift)) {
                ps.setString(1, userID);
                ps.setDate(2, Date.valueOf(day));
                ps.setString(3, type.label);        // "早番" / "遅番" / "中番"
                if (type.number == null) ps.setNull(4, Types.INTEGER);
                else ps.setInt(4, type.number);
                ps.executeUpdate();
            }

            try (PreparedStatement ps = con.prepareStatement(deleteReq)) {
                ps.setInt(1, requestID);
                ps.executeUpdate();
            }

            con.commit();
            con.setAutoCommit(true);
        }
    }

    // ------- helper -------

    private static class ShiftType {
        String label;
        Integer number;
        ShiftType(String label, Integer number) { this.label = label; this.number = number; }
    }

    private ShiftType mapToShiftType(LocalTime start, LocalTime end) {
        // 例ルール：9-13=早番(1)、13-17=中番(2)、17-21=遅番(3)
        if (!start.isAfter(LocalTime.of(9,0)) && !end.isAfter(LocalTime.of(13,0))) {
            return new ShiftType("早番", 1);
        }
        if (!start.isAfter(LocalTime.of(13,0)) && !end.isAfter(LocalTime.of(17,0))) {
            return new ShiftType("中番", 2);
        }
        return new ShiftType("遅番", 3);
    }
}
