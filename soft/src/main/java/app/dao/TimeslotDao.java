package app.dao;

import app.db.Db;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TimeslotDao {

    public static class Timeslot {
        public int timeslotID;
        public String name;
        public LocalTime start;
        public LocalTime end;
    }

    public List<Timeslot> listAll() throws SQLException {
        String sql = "SELECT timeslotID, name, start_time, end_time FROM timeslot ORDER BY timeslotID";
        List<Timeslot> list = new ArrayList<>();

        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Timeslot t = new Timeslot();
                t.timeslotID = rs.getInt("timeslotID");
                t.name = rs.getString("name");

                Time s = rs.getTime("start_time");
                Time e = rs.getTime("end_time");
                t.start = (s == null) ? null : s.toLocalTime();
                t.end   = (e == null) ? null : e.toLocalTime();

                list.add(t);
            }
        }
        return list;
    }

    public Timeslot findById(int timeslotID) throws SQLException {
        String sql = "SELECT timeslotID, name, start_time, end_time FROM timeslot WHERE timeslotID = ?";
        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, timeslotID);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                Timeslot t = new Timeslot();
                t.timeslotID = rs.getInt("timeslotID");
                t.name = rs.getString("name");
                t.start = rs.getTime("start_time").toLocalTime();
                t.end = rs.getTime("end_time").toLocalTime();
                return t;
            }
        }
    }

    public void update(int timeslotID, String name, LocalTime start, LocalTime end) throws SQLException {
        String sql = "UPDATE timeslot SET name=?, start_time=?, end_time=? WHERE timeslotID=?";
        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setTime(2, Time.valueOf(start));
            ps.setTime(3, Time.valueOf(end));
            ps.setInt(4, timeslotID);
            ps.executeUpdate();
        }
    }
}
