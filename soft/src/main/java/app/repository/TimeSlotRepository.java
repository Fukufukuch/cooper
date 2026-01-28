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
        "min_extra_workers, max_extra_workers, require_authority_workers " +
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
                    rs.getInt("require_authority_workers")
                ));
            }

        } catch (Exception e) {
            throw new RuntimeException("TimeSlot 取得失敗", e);
        }

        return list;
    }
}
