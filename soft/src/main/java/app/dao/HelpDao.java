package app.dao;

import app.db.Db;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class HelpDao {

    public static class HelpRow {
        public int helpID;
        public String helpWantUserID;
        public String helperUserID;

        public LocalDate day;
        public LocalTime start;
        public LocalTime end;

        public int apply;
        public String reason;

        public String wantUsername;
        public String helperUsername;

        // 交代確定用（後で help に追加する）
        public Integer shiftId;
    }

    /** 承認待ち一覧（apply=1） */
    public List<HelpRow> listPending() throws SQLException {
        String sql =
            "SELECT h.helpID, h.help_want_userID, h.helper_userID, h.help_want_day, " +
            "       h.help_want_time_start, h.help_want_time_end, h.apply, h.help_reason, " +
            "       uw.username AS want_username, uh.username AS helper_username " +
            "FROM help h " +
            "LEFT JOIN users uw ON uw.userID = h.help_want_userID " +
            "LEFT JOIN users uh ON uh.userID = h.helper_userID " +
            "WHERE h.apply = 1 " +
            "ORDER BY h.helpID DESC";

        List<HelpRow> list = new ArrayList<>();

        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                HelpRow r = new HelpRow();
                r.helpID = rs.getInt("helpID");
                r.helpWantUserID = rs.getString("help_want_userID");
                r.helperUserID = rs.getString("helper_userID");

                Date d = rs.getDate("help_want_day");
                Time s = rs.getTime("help_want_time_start");
                Time e = rs.getTime("help_want_time_end");

                r.day = (d == null) ? null : d.toLocalDate();
                r.start = (s == null) ? null : s.toLocalTime();
                r.end = (e == null) ? null : e.toLocalTime();

                r.apply = rs.getInt("apply");
                r.reason = rs.getString("help_reason");

                r.wantUsername = rs.getString("want_username");
                r.helperUsername = rs.getString("helper_username");

                list.add(r);
            }
        }
        return list;
    }

    public void approve(int helpID) throws SQLException {
        String sql = "UPDATE help SET apply = 2 WHERE helpID = ?";
        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, helpID);
            ps.executeUpdate();
        }
    }

    public void reject(int helpID) throws SQLException {
        String sql = "UPDATE help SET apply = 3 WHERE helpID = ?";
        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, helpID);
            ps.executeUpdate();
        }
    }
}
