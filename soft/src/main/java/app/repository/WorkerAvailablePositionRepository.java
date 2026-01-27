package app.repository;

import app.db.Db;
import app.entity.WorkerAvailablePositionEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class WorkerAvailablePositionRepository {

    private static final String SQL =
        "SELECT workerID, positionID " +
        "FROM worker_available_position ORDER BY workerID";

    public List<WorkerAvailablePositionEntity> findAll() {

        List<WorkerAvailablePositionEntity> list = new ArrayList<>();

        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new WorkerAvailablePositionEntity(
                    rs.getString("workerID"),
                    rs.getInt("positionID")
                ));
            }

        } catch (Exception e) {
            throw new RuntimeException("worker_available_position 取得失敗", e);
        }

        return list;
    }
}