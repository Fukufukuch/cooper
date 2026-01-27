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
        "SELECT id, name, min_workers, max_workers, require_Authority_Workers " +
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
                    rs.getInt("require_authority_workers")
                ));
            }

        } catch (Exception e) {
            throw new RuntimeException("Position 取得失敗", e);
        }

        return list;
    }
}
