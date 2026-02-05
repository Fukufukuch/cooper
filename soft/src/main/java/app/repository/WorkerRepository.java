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

    /** 月労働時間と累計労働時間を加算して更新します。 */
    public void addMonthlyMinutes(String workerId, int minutes) {
        String sql = "UPDATE worker SET monthly_work_minutes = monthly_work_minutes + ?, total_work_minutes = total_work_minutes + ? WHERE workerID = ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, minutes);
            ps.setInt(2, minutes);
            ps.setString(3, workerId);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("worker 月労働時間更新失敗", e);
        }
    }

    /** 指定ワーカーの月間労働時間を上書きします。存在しない場合は何もしません。 */
    public void setMonthlyMinutes(String workerId, int minutes) {
        String sql = "UPDATE worker SET monthly_work_minutes = ? WHERE workerID = ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, minutes);
            ps.setString(2, workerId);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("worker 月間労働時間上書き失敗", e);
        }
    }

    /** 指定ワーカーの累計労働時間を上書きします。存在しない場合は何もしません。 */
    public void setTotalMinutes(String workerId, int minutes) {
        String sql = "UPDATE worker SET total_work_minutes = ? WHERE workerID = ?";
        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, minutes);
            ps.setString(2, workerId);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("worker 累計労働時間上書き失敗", e);
        }
    }
}