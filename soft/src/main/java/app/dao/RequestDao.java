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
        public String username;
        public LocalDate day;
        public LocalTime start;
        public LocalTime end;
    }

    /** 一覧（help_list.jsp 用） */
    public List<RequestRow> listAll() throws SQLException {

        String sql =
            "SELECT r.requestID, r.userID, u.username, r.shift_request_day, " +
            "       r.shift_request_time_start, r.shift_request_time_end " +
            "FROM request r " +
            "JOIN users u ON r.userID = u.userID " +
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

                Date d = rs.getDate("shift_request_day");
                row.day = (d == null) ? null : d.toLocalDate();

                Time s = rs.getTime("shift_request_time_start");
                Time e = rs.getTime("shift_request_time_end");
                row.start = (s == null) ? null : s.toLocalTime();
                row.end = (e == null) ? null : e.toLocalTime();

                list.add(row);
            }
        }
        return list;
    }

    /** 承認：shiftに反映してrequest削除（トランザクション） */
    public void approveToShift(int requestID) throws SQLException {

        String select =
            "SELECT userID, shift_request_day, shift_request_time_start, shift_request_time_end " +
            "FROM request WHERE requestID = ?";

        // shift は新定義：date/workerID/positionID/start_minute/end_minute
        // worker が無いとFKで落ちるので、先に worker を確保する
        String ensureWorker = "INSERT IGNORE INTO worker(workerID) VALUES (?)";

        // users.Position を positionID として使う
        String getPos = "SELECT Position FROM users WHERE userID = ?";

        String insertShift =
            "INSERT INTO shift (date, workerID, positionID, start_minute, end_minute) " +
            "VALUES (?, ?, ?, ?, ?)";

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
                        throw new SQLException("requestが見つかりません: requestID=" + requestID);
                    }
                    userID = rs.getString("userID");
                    Date d = rs.getDate("shift_request_day");
                    Time s = rs.getTime("shift_request_time_start");
                    Time e = rs.getTime("shift_request_time_end");

                    day = d.toLocalDate();
                    start = s.toLocalTime();
                    end = e.toLocalTime();
                }
            }

            // worker 行を確保
            try (PreparedStatement ps = con.prepareStatement(ensureWorker)) {
                ps.setString(1, userID);
                ps.executeUpdate();
            }

            // positionID 取得
            int positionID;
            try (PreparedStatement ps = con.prepareStatement(getPos)) {
                ps.setString(1, userID);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        con.rollback();
                        throw new SQLException("usersが見つかりません: userID=" + userID);
                    }
                    positionID = rs.getInt("Position");
                }
            }

            // time -> minute
            int startMin = start.getHour() * 60 + start.getMinute();
            int endMin = end.getHour() * 60 + end.getMinute();

            // shift INSERT
            try (PreparedStatement ps = con.prepareStatement(insertShift)) {
                ps.setDate(1, Date.valueOf(day));
                ps.setString(2, userID);
                ps.setInt(3, positionID);
                ps.setInt(4, startMin);
                ps.setInt(5, endMin);
                ps.executeUpdate();
            }

            // request DELETE
            try (PreparedStatement ps = con.prepareStatement(deleteReq)) {
                ps.setInt(1, requestID);
                ps.executeUpdate();
            }

            con.commit();
            con.setAutoCommit(true);
        }
    }

    /** 却下（request削除だけ） */
    public void reject(int requestID) throws SQLException {
        String sql = "DELETE FROM request WHERE requestID = ?";
        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, requestID);
            ps.executeUpdate();
        }
    }
}
