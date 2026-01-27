package app.repository;

import app.db.Db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;

public class ShiftRepository {

    private static final String INSERT_SQL =
        "INSERT INTO shift (date, workerID, positionID, start_minute, end_minute, shift_timetable) " +
        "VALUES (?, ?, ?, ?, ?, ?)";

    private static final String DELETE_SQL =
        "DELETE FROM shift WHERE date BETWEEN ? AND ?";

    public void deleteBetween(LocalDate start, LocalDate end) {
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_SQL)) {

            ps.setDate(1, java.sql.Date.valueOf(start));
            ps.setDate(2, java.sql.Date.valueOf(end));
            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("shift削除失敗", e);
        }
    }

    public void insert(LocalDate date, String workerId, int positionId,
                       int startMinute, int endMinute, String timetable) {

        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL)) {

            ps.setDate(1, java.sql.Date.valueOf(date));
            ps.setString(2, workerId);
            ps.setInt(3, positionId);
            ps.setInt(4, startMinute);
            ps.setInt(5, endMinute);
            ps.setString(6, timetable);

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("shift INSERT失敗", e);
        }
    }
}
