package app.repository;

import app.db.Db;
import app.entity.WorkerEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class WorkerRepository {

    private static final String SQL =
        "SELECT workerID, has_authority, monthly_work_minutes, total_work_minutes " +
        "FROM worker ORDER BY workerID";

    public List<WorkerEntity> findAll() {

        List<WorkerEntity> list = new ArrayList<>();

        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new WorkerEntity(
                    rs.getString("workerID"),
                    rs.getInt("has_authority"),
                    rs.getInt("monthly_work_minutes"),
                    rs.getInt("total_work_minutes")
                ));
            }

        } catch (Exception e) {
            throw new RuntimeException("worker 取得失敗", e);
        }

        return list;
    }
}