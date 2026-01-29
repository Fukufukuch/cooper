package app.repository;

import app.db.Db;
import app.entity.OptionEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.time.LocalDate;

public class OptionRepository {

    private static final String SQL =
        "SELECT id, max_worktime_of_month, max_worktime_of_day, newcomer_threshold_minutes, " +
        "required_senior_workers, generateDays, firstdate " +
        "FROM `option` ORDER BY id";

    public OptionEntity find() {
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return new OptionEntity(
                    rs.getInt("id"),
                    rs.getInt("max_worktime_of_month"),
                    rs.getInt("max_worktime_of_day"),
                    rs.getInt("newcomer_threshold_minutes"),
                    rs.getInt("required_senior_workers"),
                    rs.getInt("generateDays"),
                    rs.getDate("firstdate").toLocalDate()
                );
            }
            throw new IllegalStateException("optionsテーブルにデータがありません");

        } catch (Exception e) {
            throw new RuntimeException("option 取得失敗", e);
        }
    }

    public void update(OptionEntity option) {
        String SQL = "UPDATE `option` SET " +
            "max_worktime_of_month=?, " +
            "max_worktime_of_day=?, " +
            "newcomer_threshold_minutes=?, " +
            "required_senior_workers=?, " +
            "generateDays=?, " +
            "firstdate=? " +
            "WHERE id=1";

    
        try (Connection conn = Db.getConnection();
            PreparedStatement ps = conn.prepareStatement(SQL)) {

            ps.setInt(1, option.getMaxWorktimeofMonth());
            ps.setInt(2, option.getMaxWorktimeofDay());
            ps.setInt(3, option.getNewcomerThresholdMinutes());
            ps.setInt(4, option.getRequiredSeniorWorkers());
            ps.setInt(5, option.getGenerateDays());
            ps.setDate(6, java.sql.Date.valueOf(option.getFirstdate()));

            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("option 更新失敗", e);
        }
    }
}
