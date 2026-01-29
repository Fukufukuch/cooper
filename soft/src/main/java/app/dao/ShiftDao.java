package app.dao;

import app.db.Db;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * shift テーブル（新定義）
 *  - id
 *  - date
 *  - workerID
 *  - positionID
 *  - start_minute / end_minute
 *
 * 画面（shift_edit.jsp）が旧DTO（shiftInfoDay/shiftTimetable/shiftTimetableNumber）を使っているので、
 * DBから minutes を読みつつ、表示用の値を組み立てて返す。
 */
public class ShiftDao {

    /** 一覧：日付で範囲指定（例：2026-01-01〜2026-01-31） */
    public List<ShiftRow> findByDateRange(LocalDate from, LocalDate to) throws SQLException {

        String sql =
            "SELECT s.id AS shiftID, s.workerID AS userID, u.username, " +
            "       s.date, s.start_minute, s.end_minute, s.positionID, s.shift_timetable, p.name AS positionName " +
            "FROM shift s " +
            "JOIN worker w ON s.workerID = w.workerID " +
            "JOIN users u  ON u.userID = w.workerID " +
            "LEFT JOIN position p ON s.positionID = p.id " +
            "WHERE s.date >= ? AND s.date <= ? " +
            "ORDER BY s.date ASC, s.start_minute ASC, s.id ASC";

        List<ShiftRow> list = new ArrayList<>();

        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(from));
            ps.setDate(2, Date.valueOf(to));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ShiftRow r = new ShiftRow();
                    r.shiftID = rs.getInt("shiftID");
                    r.userID = rs.getString("userID");
                    r.username = rs.getString("username");

                    Date d = rs.getDate("date");
                    r.shiftInfoDay = (d == null) ? null : d.toLocalDate();

                    int start = rs.getInt("start_minute");
                    int end = rs.getInt("end_minute");

                    // DB の shift_timetable を直接使用
                    r.shiftTimetable = rs.getString("shift_timetable");

                    // timetableNumber は minutes から生成（互換性のため）
                    ShiftType st = mapToShiftType(start, end);
                    r.shiftTimetableNumber = st.number;  // 1/2/3

                    // 追加情報（今後画面で使いたくなったら）
                    r.positionID = (Integer) rs.getObject("positionID");
                    r.positionName = rs.getString("positionName");
                    r.startMinute = start;
                    r.endMinute = end;

                    list.add(r);
                }
            }
        }
        return list;
    }

    /** 削除：主キー id で消す（安全：1件だけ） */
    public boolean deleteByShiftId(int shiftID) throws SQLException {
        String sql = "DELETE FROM shift WHERE id = ?";
        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, shiftID);
            return ps.executeUpdate() == 1;
        }
    }

    /**
     * 追加：旧入力（userID/day/timetable/timetableNumber）を受け取り、
     * 新shift（date/workerID/positionID/start_minute/end_minute/shift_timetable）に変換して INSERT。
     */
    public int insert(String userID, LocalDate day, String timetable, Integer timetableNumber) throws SQLException {

        // timetable / timetableNumber から開始・終了分へ変換
        ShiftType st = mapToShiftType(timetable, timetableNumber);
        int startMinute = st.startMinute;
        int endMinute = st.endMinute;
        String shiftTimetable = st.shiftTimetable;  // "早"/"中"/"遅" など

        // worker が無いと shift のFKで落ちるので確保（存在してれば何もしない）
        String ensureWorker = "INSERT IGNORE INTO worker(workerID) VALUES (?)";

        // positionID は users.Position をそのまま使う（DBに既に列がある）
        String getPositionId = "SELECT Position FROM users WHERE userID = ?";

        String insertShift =
                "INSERT INTO shift (date, workerID, positionID, start_minute, end_minute, shift_timetable) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = Db.getConnection()) {
            con.setAutoCommit(false);

            // 1) worker 行を確保
            try (PreparedStatement ps = con.prepareStatement(ensureWorker)) {
                ps.setString(1, userID);
                ps.executeUpdate();
            }

            // 2) positionID を users から取得
            int positionID;
            try (PreparedStatement ps = con.prepareStatement(getPositionId)) {
                ps.setString(1, userID);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        con.rollback();
                        throw new SQLException("usersに該当ユーザーがいません: " + userID);
                    }
                    positionID = rs.getInt("Position");
                }
            }

            // 2.5) positionID が position テーブルに存在するかチェック。無ければ「未設定」にフォールバック
            String existsPos = "SELECT 1 FROM position WHERE id = ? LIMIT 1";
            try (PreparedStatement ps = con.prepareStatement(existsPos)) {
                ps.setInt(1, positionID);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        // 未設定ポジションを確保
                            String ensureDefaultPos =
                                "INSERT INTO `position`(name, min_workers, max_workers, require_authority_workers) " +
                                "SELECT '未設定', 0, 0, 0 FROM DUAL " +
                                "WHERE NOT EXISTS (SELECT 1 FROM position WHERE name='未設定')";
                        try (PreparedStatement ps2 = con.prepareStatement(ensureDefaultPos)) {
                            ps2.executeUpdate();
                        }

                        // 未設定のidを使う
                        String getDefaultPos = "SELECT id FROM position WHERE name='未設定' ORDER BY id LIMIT 1";
                        try (PreparedStatement ps3 = con.prepareStatement(getDefaultPos);
                            ResultSet rs3 = ps3.executeQuery()) {
                            if (!rs3.next()) {
                                con.rollback();
                                throw new SQLException("positionの未設定が取得できません");
                            }
                            positionID = rs3.getInt("id");
                        }
                    }
                }
            }


            // 3) shift INSERT
            try (PreparedStatement ps = con.prepareStatement(insertShift, Statement.RETURN_GENERATED_KEYS)) {
                ps.setDate(1, day == null ? null : Date.valueOf(day));
                ps.setString(2, userID);
                ps.setInt(3, positionID);
                ps.setInt(4, startMinute);
                ps.setInt(5, endMinute);
                ps.setString(6, shiftTimetable);
                ps.executeUpdate();

                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        int id = keys.getInt(1);
                        con.commit();
                        con.setAutoCommit(true);
                        return id;
                    }
                }
            }

            con.commit();
            con.setAutoCommit(true);
        }

        return -1;
    }

    // ------- helper -------

    private static class ShiftType {
        final String label;
        final Integer number;
        final int startMinute;
        final int endMinute;
        final String shiftTimetable;  // shift_timetableカラム用（"早番"/"中番"/"遅番"など）

        ShiftType(String label, Integer number, int startMinute, int endMinute) {
            this.label = label;
            this.number = number;
            this.startMinute = startMinute;
            this.endMinute = endMinute;
            // label全体（"早番"/"中番"/"遅番"など）をshift_timetableに使用
            this.shiftTimetable = label != null ? label : "";
        }
    }

    /** timetable / timetableNumber から minutes を決める（旧ルール互換） */
    private ShiftType mapToShiftType(String timetable, Integer timetableNumber) {

        // timetableNumber優先（1/2/3）
        if (timetableNumber != null) {
            if (timetableNumber == 1) return new ShiftType("早番", 1, 9 * 60, 13 * 60);
            if (timetableNumber == 2) return new ShiftType("中番", 2, 13 * 60, 17 * 60);
            return new ShiftType("遅番", 3, 17 * 60, 21 * 60);
        }

        // timetable文字で判断（フォーム入力の「早番"/"中番"/"遅番"に対応）
        if (timetable != null) {
            if (timetable.contains("早番")) return new ShiftType("早番", 1, 9 * 60, 13 * 60);
            if (timetable.contains("中番")) return new ShiftType("中番", 2, 13 * 60, 17 * 60);
            if (timetable.contains("遅番")) return new ShiftType("遅番", 3, 17 * 60, 21 * 60);
        }

        // 迷ったら「落ちない」こと優先で遅番に寄せる
        return new ShiftType("遅番", 3, 17 * 60, 21 * 60);
    }

    /** minutesから旧表示用ラベルを作る */
    private ShiftType mapToShiftType(int startMinute, int endMinute) {
        // 例: 9-13=早番(1)、13-17=中番(2)、17-21=遅番(3)
        if (startMinute <= 9 * 60 && endMinute <= 13 * 60) {
            return new ShiftType("早番", 1, startMinute, endMinute);
        }
        if (startMinute <= 13 * 60 && endMinute <= 17 * 60) {
            return new ShiftType("中番", 2, startMinute, endMinute);
        }
        return new ShiftType("遅番", 3, startMinute, endMinute);
    }

    /** 画面(JSP)に渡す用のDTO（旧画面互換 + 今後用に少しだけ追加） */
    public static class ShiftRow {
        public int shiftID;                 // = shift.id
        public String userID;               // = shift.workerID
        public String username;
            public String positionName;
        public LocalDate shiftInfoDay;      // = shift.date
        public String shiftTimetable;       // minutesから生成
        public Integer shiftTimetableNumber;// minutesから生成

        // 追加（今は画面で使ってなくてもOK）
        public Integer positionID;
        public Integer startMinute;
        public Integer endMinute;
    }
}