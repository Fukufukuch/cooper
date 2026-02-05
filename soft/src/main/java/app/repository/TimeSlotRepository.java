package app.repository;

import app.db.Db;
import app.entity.TimeSlotEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TimeSlotRepository {

    private static final String SQL =
        "SELECT id, name, start_minute, end_minute, " +
        "min_extra_workers, max_extra_workers, require_authority_workers, active " +
        "FROM timeslot ORDER BY id";

    public List<TimeSlotEntity> findAll() {

        List<TimeSlotEntity> list = new ArrayList<>();

        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new TimeSlotEntity(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("start_minute"),
                    rs.getInt("end_minute"),
                    rs.getInt("min_extra_workers"),
                    rs.getInt("max_extra_workers"),
                    rs.getInt("require_authority_workers"),
                    rs.getBoolean("active")
                ));
            }

        } catch (Exception e) {
            throw new RuntimeException("TimeSlot 取得失敗", e);
        }

        return list;
    }

    public List<TimeSlotEntity> findAllActive() {
        String sql = "SELECT id, name, start_minute, end_minute, min_extra_workers, max_extra_workers, require_authority_workers, active FROM timeslot WHERE active=1 ORDER BY id";
        List<TimeSlotEntity> list = new ArrayList<>();
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new TimeSlotEntity(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("start_minute"),
                    rs.getInt("end_minute"),
                    rs.getInt("min_extra_workers"),
                    rs.getInt("max_extra_workers"),
                    rs.getInt("require_authority_workers"),
                    rs.getBoolean("active")
                ));
            }
        } catch (Exception e) {
            throw new RuntimeException("TimeSlot 取得失敗", e);
        }
        return list;
    }

    public void delete(int id) {
        String sql = "UPDATE timeslot SET active=0 WHERE id=?";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("TimeSlot delete(soft) 失敗", e);
        }
    }

    public void setActive(int id, boolean active) {
        String sql = "UPDATE timeslot SET active=? WHERE id=?";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, active?1:0);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("TimeSlot setActive 失敗", e);
        }
    }
    public int insert(String name, int startMinute, int endMinute, int minExtraWorkers, int maxExtraWorkers, int requireAuthorityWorkers) {
        String sql = "INSERT INTO timeslot (name, start_minute, end_minute, min_extra_workers, max_extra_workers, require_authority_workers) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, name);
            ps.setInt(2, startMinute);
            ps.setInt(3, endMinute);
            ps.setInt(4, minExtraWorkers);
            ps.setInt(5, maxExtraWorkers);
            ps.setInt(6, requireAuthorityWorkers);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
            return -1;
        } catch (Exception e) {
            throw new RuntimeException("TimeSlot insert 失敗", e);
        }
    }

    public void update(int id, String name, int startMinute, int endMinute, int minExtraWorkers, int maxExtraWorkers, int requireAuthorityWorkers) {
        String sql = "UPDATE timeslot SET name=?, start_minute=?, end_minute=?, min_extra_workers=?, max_extra_workers=?, require_authority_workers=? WHERE id=?";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setInt(2, startMinute);
            ps.setInt(3, endMinute);
            ps.setInt(4, minExtraWorkers);
            ps.setInt(5, maxExtraWorkers);
            ps.setInt(6, requireAuthorityWorkers);
            ps.setInt(7, id);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("TimeSlot update 失敗", e);
        }
    }

    public TimeSlotEntity findById(int id) {
        String sql = "SELECT id, name, start_minute, end_minute, min_extra_workers, max_extra_workers, require_authority_workers, active FROM timeslot WHERE id=?";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new TimeSlotEntity(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("start_minute"),
                        rs.getInt("end_minute"),
                        rs.getInt("min_extra_workers"),
                        rs.getInt("max_extra_workers"),
                        rs.getInt("require_authority_workers"),
                        rs.getBoolean("active")
                    );
                }
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("TimeSlot findById 失敗", e);
        }
    }
}
