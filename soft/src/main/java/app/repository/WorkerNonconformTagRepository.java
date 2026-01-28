package app.repository;

import app.db.Db;
import app.entity.WorkerNonconformTagEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class WorkerNonconformTagRepository {

    private static final String SQL =
        "SELECT workerID, nonconformID " +
        "FROM worker_nonconform_tag ORDER BY workerID";

    public List<WorkerNonconformTagEntity> findAll() {

        List<WorkerNonconformTagEntity> list = new ArrayList<>();

        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new WorkerNonconformTagEntity(
                    rs.getString("workerID"),
                    rs.getInt("nonconformID")
                ));
            }

        } catch (Exception e) {
            throw new RuntimeException("worker_nonconform_tag 取得失敗", e);
        }

        return list;
    }
}