package app.repository;

import app.db.Db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

        String ensureWorkerSql = "INSERT IGNORE INTO worker(workerID) VALUES (?)";
        String existsPosSql = "SELECT 1 FROM `position` WHERE id = ? LIMIT 1";
        String ensureDefaultPosSql = "INSERT INTO `position`(name, min_workers, max_workers, require_authority_workers) " +
                                     "SELECT '未設定', 0, 0, 0 FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM `position` WHERE name='未設定')";
        String getDefaultPosSql = "SELECT id FROM `position` WHERE name='未設定' ORDER BY id LIMIT 1";

        try (Connection conn = Db.getConnection()) {
            conn.setAutoCommit(false);

            // 0) ensure worker exists to satisfy FK
            try (PreparedStatement ps = conn.prepareStatement(ensureWorkerSql)) {
                ps.setString(1, workerId);
                ps.executeUpdate();
            }

            // 1) ensure position exists; if not, create default and use that id
            try (PreparedStatement ps = conn.prepareStatement(existsPosSql)) {
                ps.setInt(1, positionId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        try (PreparedStatement ps2 = conn.prepareStatement(ensureDefaultPosSql)) {
                            ps2.executeUpdate();
                        }
                        try (PreparedStatement ps3 = conn.prepareStatement(getDefaultPosSql);
                             ResultSet rs3 = ps3.executeQuery()) {
                            if (rs3.next()) {
                                positionId = rs3.getInt(1);
                            } else {
                                conn.rollback();
                                throw new RuntimeException("positionの未設定が取得できません");
                            }
                        }
                    }
                }
            }

            // 2) insert shift
            try (PreparedStatement ps = conn.prepareStatement(INSERT_SQL)) {
                ps.setDate(1, java.sql.Date.valueOf(date));
                ps.setString(2, workerId);
                ps.setInt(3, positionId);
                ps.setInt(4, startMinute);
                ps.setInt(5, endMinute);
                ps.setString(6, timetable);

                try {
                    ps.executeUpdate();
                } catch (Exception e) {
                    String msg = String.format("shift INSERT失敗: date=%s workerId=%s positionId=%d start=%d end=%d timetable=%s => %s",
                            date, workerId, positionId, startMinute, endMinute, timetable, e.getMessage());
                    conn.rollback();
                    throw new RuntimeException(msg, e);
                }
            }

            conn.commit();

        } catch (Exception e) {
            throw new RuntimeException("shift INSERT失敗: " + e.getMessage(), e);
        }
    }
}
