package app.repository;

import app.db.Db;
import app.entity.WorkerShiftRequestEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class WorkerShiftRequestRepository {

    private static final String SQL =
        "SELECT workerID, date, timeslotID " +
        "FROM worker_shift_request ORDER BY workerID";

    public List<WorkerShiftRequestEntity> findAll() {

        List<WorkerShiftRequestEntity> list = new ArrayList<>();

        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new WorkerShiftRequestEntity(
                    rs.getString("workerID"),
                    rs.getDate("date").toLocalDate(),
                    rs.getInt("timeslotID")
                ));
            }

        } catch (Exception e) {
            throw new RuntimeException("worker_shift_request 取得失敗", e);
        }

        return list;
    }
}