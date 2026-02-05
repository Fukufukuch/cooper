package app.repository;

import app.db.Db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.List;

public class ShiftRepository {

    private static final String INSERT_SQL =
        "INSERT INTO shift (date, workerID, positionID, start_minute, end_minute, shift_timetable) " +
        "VALUES (?, ?, ?, ?, ?, ?)";

    private static final String DELETE_SQL =
        "DELETE FROM shift WHERE date BETWEEN ? AND ?";

    private static final String SUM_MINUTES_BY_WORKER_SQL =
        "SELECT workerID, SUM(end_minute - start_minute) AS minutes FROM shift WHERE date BETWEEN ? AND ? GROUP BY workerID";

    private static final String SUM_MINUTES_BY_WORKER_FOR_MONTH_SQL =
        "SELECT workerID, SUM(end_minute - start_minute) AS minutes FROM shift " +
        "WHERE YEAR(date)=? AND MONTH(date)=? GROUP BY workerID";

    private static final String SUM_MINUTES_BY_WORKER_ALL_SQL =
        "SELECT workerID, SUM(end_minute - start_minute) AS minutes FROM shift GROUP BY workerID";

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

    /**
     * 指定期間の削除対象シフトの労働分数合計をワーカー毎に返す（削除前に呼ぶことを想定）
     */
    public java.util.Map<String, Integer> sumMinutesByWorkerBetween(LocalDate start, LocalDate end) {
        java.util.Map<String, Integer> map = new java.util.HashMap<>();
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(SUM_MINUTES_BY_WORKER_SQL)) {

            ps.setDate(1, java.sql.Date.valueOf(start));
            ps.setDate(2, java.sql.Date.valueOf(end));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String workerId = rs.getString("workerID");
                    int minutes = rs.getInt("minutes");
                    map.put(workerId, minutes);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("shift 集計失敗", e);
        }
        return map;
    }

    /** 指定した年/月のワーカー別合計分数を返す */
    public java.util.Map<String, Integer> sumMinutesByWorkerForMonth(int year, int month) {
        java.util.Map<String, Integer> map = new java.util.HashMap<>();
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(SUM_MINUTES_BY_WORKER_FOR_MONTH_SQL)) {

            ps.setInt(1, year);
            ps.setInt(2, month);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String workerId = rs.getString("workerID");
                    int minutes = rs.getInt("minutes");
                    map.put(workerId, minutes);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("shift 月間集計失敗", e);
        }
        return map;
    }

    /** 全期間のワーカー別合計分数を返す */
    public java.util.Map<String, Integer> sumMinutesByWorkerAll() {
        java.util.Map<String, Integer> map = new java.util.HashMap<>();
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(SUM_MINUTES_BY_WORKER_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String workerId = rs.getString("workerID");
                int minutes = rs.getInt("minutes");
                map.put(workerId, minutes);
            }

        } catch (Exception e) {
            throw new RuntimeException("shift 全期間集計失敗", e);
        }
        return map;
    }

    public int insert(LocalDate date, String workerId, int positionId,
                       int startMinute, int endMinute, String timetable) {
        return insert(date, workerId, positionId, startMinute, endMinute, timetable, null);
    }

    /**
     * Insert with optional warnings collector. On Data truncation errors will retry with truncated timetable and
     * add a warning message to the provided warnings list when truncation was required.
     */
    public int insert(LocalDate date, String workerId, int positionId,
                       int startMinute, int endMinute, String timetable, List<String> warnings) {

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
            try (PreparedStatement ps = conn.prepareStatement(INSERT_SQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
                ps.setDate(1, java.sql.Date.valueOf(date));
                ps.setString(2, workerId);
                ps.setInt(3, positionId);
                ps.setInt(4, startMinute);
                ps.setInt(5, endMinute);
                ps.setString(6, timetable);

                try {
                    ps.executeUpdate();
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (keys.next()) {
                            int id = keys.getInt(1);
                            conn.commit();
                            return id;
                        }
                    }
                } catch (Exception e) {
                    // If it's a data truncation, try truncating the timetable and retry once
                    String em = e.getMessage() == null ? "" : e.getMessage();
                    if (em.contains("Data truncation") || em.contains("Data too long") || (e instanceof java.sql.SQLDataException)) {
                        // attempt to truncate timetable to a safe short label
                        String truncated = timetable == null ? null : (timetable.length() > 8 ? timetable.substring(0, 8) : timetable);
                        try (PreparedStatement ps2 = conn.prepareStatement(INSERT_SQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
                            ps2.setDate(1, java.sql.Date.valueOf(date));
                            ps2.setString(2, workerId);
                            ps2.setInt(3, positionId);
                            ps2.setInt(4, startMinute);
                            ps2.setInt(5, endMinute);
                            ps2.setString(6, truncated);
                            ps2.executeUpdate();
                            try (ResultSet keys = ps2.getGeneratedKeys()) {
                                if (keys.next()) {
                                    int id = keys.getInt(1);
                                    conn.commit();
                                    if (warnings != null) {
                                        warnings.add(String.format("timetable が DB 列長で切り詰められました: original='%s' truncated='%s' date=%s worker=%s", timetable, truncated, date, workerId));
                                    }
                                    return id;
                                }
                            }
                        } catch (Exception ex2) {
                            String msg = String.format("shift INSERT失敗: date=%s workerId=%s positionId=%d start=%d end=%d timetable=%s => %s",
                                    date, workerId, positionId, startMinute, endMinute, timetable, ex2.getMessage());
                            conn.rollback();
                            throw new RuntimeException(msg, ex2);
                        }
                    }

                    String msg = String.format("shift INSERT失敗: date=%s workerId=%s positionId=%d start=%d end=%d timetable=%s => %s",
                            date, workerId, positionId, startMinute, endMinute, timetable, e.getMessage());
                    conn.rollback();
                    throw new RuntimeException(msg, e);
                }
            }
            conn.commit();
            return -1;

        } catch (Exception e) {
            throw new RuntimeException("shift INSERT失敗: " + e.getMessage(), e);
        }
    }
}
