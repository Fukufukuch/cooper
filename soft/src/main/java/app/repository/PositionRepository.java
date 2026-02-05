package app.repository;

import app.db.Db;
import app.entity.PositionEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PositionRepository {

    private static final String SQL =
        "SELECT id, name, min_workers, max_workers, require_Authority_Workers, active " +
        "FROM position ORDER BY id";

    public List<PositionEntity> findAll() {

        List<PositionEntity> list = new ArrayList<>();

        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new PositionEntity(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("min_workers"),
                    rs.getInt("max_workers"),
                    rs.getInt("require_authority_workers"),
                    rs.getBoolean("active")
                ));
            }

        } catch (Exception e) {
            throw new RuntimeException("Position 取得失敗", e);
        }

        return list;
    }

    public List<PositionEntity> findAllActive() {
        String sql = "SELECT id, name, min_workers, max_workers, require_authority_workers, active FROM position WHERE active=1 ORDER BY id";
        List<PositionEntity> list = new ArrayList<>();
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new PositionEntity(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("min_workers"),
                    rs.getInt("max_workers"),
                    rs.getInt("require_authority_workers"),
                    rs.getBoolean("active")
                ));
            }
        } catch (Exception e) {
            throw new RuntimeException("Position 取得失敗", e);
        }
        return list;
    }

    public void delete(int id) {
        String sql = "UPDATE position SET active=0 WHERE id=?";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Position delete(soft) 失敗", e);
        }
    }

    public void setActive(int id, boolean active) {
        String sql = "UPDATE position SET active=? WHERE id=?";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, active?1:0);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Position setActive 失敗", e);
        }
    }
    public int insert(String name, int minWorkers, int maxWorkers, int requireAuthorityWorkers) {
        String sql = "INSERT INTO position (name, min_workers, max_workers, require_authority_workers) VALUES (?, ?, ?, ?)";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, name);
            ps.setInt(2, minWorkers);
            ps.setInt(3, maxWorkers);
            ps.setInt(4, requireAuthorityWorkers);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
            return -1;
        } catch (Exception e) {
            throw new RuntimeException("Position insert 失敗", e);
        }
    }

    public void update(int id, String name, int minWorkers, int maxWorkers, int requireAuthorityWorkers) {
        String sql = "UPDATE position SET name=?, min_workers=?, max_workers=?, require_authority_workers=? WHERE id=?";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setInt(2, minWorkers);
            ps.setInt(3, maxWorkers);
            ps.setInt(4, requireAuthorityWorkers);
            ps.setInt(5, id);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Position update 失敗", e);
        }
    }

    public PositionEntity findById(int id) {
        String sql = "SELECT id, name, min_workers, max_workers, require_authority_workers, active FROM position WHERE id=?";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new PositionEntity(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("min_workers"),
                        rs.getInt("max_workers"),
                        rs.getInt("require_authority_workers"),
                        rs.getBoolean("active")
                    );
                }
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Position findById 失敗", e);
        }
    }
}
