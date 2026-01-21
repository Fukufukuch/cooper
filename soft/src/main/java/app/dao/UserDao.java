package app.dao;

import app.db.Db;
import app.model.User;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    private static final String TABLE = "users";

    // ==================================================
    // staff一覧（usertype=1のみ）
    // ==================================================
    public List<User> listStaff() throws SQLException {

        String sql =
                "SELECT userID, username, usertype, email, phone_number, " +
                "       date_of_birth, Tag, Position, work_place " +
                "FROM " + TABLE + " " +
                "WHERE usertype = b'1' " +
                "ORDER BY userID";

        List<User> list = new ArrayList<>();

        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                User u = new User();
                u.setUserID(rs.getString("userID"));
                u.setUsername(rs.getString("username"));
                u.setUsertype(rs.getString("usertype"));
                u.setEmail(rs.getString("email"));
                u.setPhoneNumber(rs.getString("phone_number"));
                u.setDateOfBirth(rs.getDate("date_of_birth"));
                u.setTag(rs.getInt("Tag"));
                u.setPosition(rs.getInt("Position"));
                u.setWorkPlace(rs.getString("work_place"));
                list.add(u);
            }
        }
        return list;
    }

    // ==================================================
    // staff削除（usertype=1のみ）
    // ==================================================
    public boolean deleteStaff(String userID) throws SQLException {
        // worker -> users の順で消さないと、shift のFK等で落ちる
        String deleteWorker = "DELETE FROM worker WHERE workerID = ?";
        String deleteUsers  = "DELETE FROM " + TABLE + " WHERE userID = ? AND usertype = b'1'";

        try (Connection con = Db.getConnection()) {
            con.setAutoCommit(false);

            // worker が無い場合もあるので 0件でもOK
            try (PreparedStatement ps = con.prepareStatement(deleteWorker)) {
                ps.setString(1, userID);
                ps.executeUpdate();
            }

            int deleted;
            try (PreparedStatement ps = con.prepareStatement(deleteUsers)) {
                ps.setString(1, userID);
                deleted = ps.executeUpdate();
            }

            con.commit();
            con.setAutoCommit(true);
            return deleted == 1;
        }
    }

    // ==================================================
    // スタッフ作成
    // ==================================================
    public String createStaff(
            String username,
            String email,
            String phoneNumber,
            Date dateOfBirth,
            String password
    ) throws SQLException {

        String userID = generateUserId10();
        while (existsUserId(userID)) {
            userID = generateUserId10();
        }

        String insertUsers =
                "INSERT INTO " + TABLE +
                " (userID, username, usertype, password, date_of_birth, " +
                "  phone_number, email, TotalWorking, Tag, Position, work_place) " +
                "VALUES (?, ?, b'1', ?, ?, ?, ?, 0, 0, 0, ?)";

        // shift のFKで必要になるので、スタッフ作成時に worker も必ず作る
        String insertWorker = "INSERT INTO worker(workerID) VALUES (?)";

        try (Connection con = Db.getConnection()) {
            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(insertUsers)) {
                ps.setString(1, userID);
                ps.setString(2, username);
                ps.setString(3, password);
                ps.setDate(4, dateOfBirth);
                ps.setString(5, phoneNumber);
                ps.setString(6, email);

                // work_place は NOT NULL なので必ず入れる
                ps.setString(7, "未設定");

                ps.executeUpdate();
            }

            try (PreparedStatement ps = con.prepareStatement(insertWorker)) {
                ps.setString(1, userID);
                ps.executeUpdate();
            }

            con.commit();
            con.setAutoCommit(true);
            return userID;
        }
    }

    // ==================================================
    // パスワード変更（本人用）
    // ==================================================
    public boolean changePassword(String userID, String oldPassword, String newPassword) throws SQLException {
        String sql = "UPDATE users SET password = ? WHERE userID = ? AND password = ?";

        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, newPassword);
            ps.setString(2, userID);
            ps.setString(3, oldPassword);

            return ps.executeUpdate() == 1;
        }
    }

    // ==================================================
    // 管理者ID取得（usertype=0）
    // ==================================================
    public String findOwnerId() throws SQLException {
        String sql = "SELECT userID FROM users WHERE usertype = b'0' ORDER BY userID LIMIT 1";
        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getString("userID");
            return null;
        }
    }

    // ==================================================
    // 内部
    // ==================================================
    private boolean existsUserId(String userID) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE userID = ? LIMIT 1";
        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, userID);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private static final SecureRandom RAND = new SecureRandom();

    // 10桁の数字ID（char(10)）
    private String generateUserId10() {
        StringBuilder sb = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            sb.append(RAND.nextInt(10));
        }
        return sb.toString();
    }
    // ==================================================
// staff 1件取得（編集画面用）
// ==================================================
public User findStaffById(String userID) throws SQLException {

    String sql =
            "SELECT userID, username, usertype, email, phone_number, " +
            "       date_of_birth, Tag, Position, work_place " +
            "FROM users " +
            "WHERE userID = ? AND usertype = b'1' " +
            "LIMIT 1";

    try (Connection con = Db.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, userID);

        try (ResultSet rs = ps.executeQuery()) {
            if (!rs.next()) return null;

            User u = new User();
            u.setUserID(rs.getString("userID"));
            u.setUsername(rs.getString("username"));
            u.setUsertype(rs.getString("usertype"));
            u.setEmail(rs.getString("email"));
            u.setPhoneNumber(rs.getString("phone_number"));
            u.setDateOfBirth(rs.getDate("date_of_birth"));
            u.setTag(rs.getInt("Tag"));
            u.setPosition(rs.getInt("Position"));
            u.setWorkPlace(rs.getString("work_place"));
            return u;
        }
    }
}

// ==================================================
// staff 更新（usersテーブル更新）
// ==================================================
public boolean updateStaff(
        String userID,
        String username,
        String email,
        String phoneNumber,
        Date dateOfBirth,
        int tag,
        int position,
        String workPlace
) throws SQLException {

    // position が存在しなかったら 1（未設定）に落とす（FK事故防止）
    String existsPos = "SELECT 1 FROM position WHERE id = ? LIMIT 1";
    try (Connection con = Db.getConnection();
         PreparedStatement ps = con.prepareStatement(existsPos)) {
        ps.setInt(1, position);
        try (ResultSet rs = ps.executeQuery()) {
            if (!rs.next()) {
                position = 1;
            }
        }
    }

    String sql =
            "UPDATE users SET " +
            " username = ?, " +
            " email = ?, " +
            " phone_number = ?, " +
            " date_of_birth = ?, " +
            " Tag = ?, " +
            " Position = ?, " +
            " work_place = ? " +
            "WHERE userID = ? AND usertype = b'1'";

    try (Connection con = Db.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, username);
        ps.setString(2, email);
        ps.setString(3, phoneNumber);
        ps.setDate(4, dateOfBirth);
        ps.setInt(5, tag);
        ps.setInt(6, position);
        ps.setString(7, workPlace);
        ps.setString(8, userID);

        return ps.executeUpdate() == 1;
    }
}
// ==================================================
// 管理者/スタッフ 両対応の作成
// usertype: 0=管理者, 1=スタッフ
// ==================================================
public String createUser(
        String username,
        String email,
        String phoneNumber,
        Date dateOfBirth,
        String password,
        int usertype,
        int tag,
        int position,
        String workPlace
) throws SQLException {

    // usertypeの安全化
    if (usertype != 0) usertype = 1;

    // position が存在しなかったら 1（未設定）に落とす（FK事故防止）
    String existsPos = "SELECT 1 FROM position WHERE id = ? LIMIT 1";

    String userID = generateUserId10();
    while (existsUserId(userID)) {
        userID = generateUserId10();
    }

    String insertUsers =
            "INSERT INTO users " +
            " (userID, username, usertype, password, date_of_birth, " +
            "  phone_number, email, TotalWorking, Tag, Position, work_place) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, 0, ?, ?, ?)";

    String insertWorker = "INSERT INTO worker(workerID) VALUES (?)";

    try (Connection con = Db.getConnection()) {
        con.setAutoCommit(false);

        // positionチェック（同じTxで見たいので con を使う）
        try (PreparedStatement ps = con.prepareStatement(existsPos)) {
            ps.setInt(1, position);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) position = 1;
            }
        }

        try (PreparedStatement ps = con.prepareStatement(insertUsers)) {
            ps.setString(1, userID);
            ps.setString(2, username);

            // bit(1) に入るように 0/1 をそのまま
            ps.setInt(3, usertype);

            ps.setString(4, password);
            ps.setDate(5, dateOfBirth);
            ps.setString(6, phoneNumber);
            ps.setString(7, email);

            ps.setInt(8, tag);
            ps.setInt(9, position);

            // NOT NULL なので必ず入れる
            ps.setString(10, workPlace);

            ps.executeUpdate();
        }

        // スタッフだけ worker 行を作る（shiftのFK対策）
        if (usertype == 1) {
            try (PreparedStatement ps = con.prepareStatement(insertWorker)) {
                ps.setString(1, userID);
                ps.executeUpdate();
            }
        }

        con.commit();
        con.setAutoCommit(true);
        return userID;
    }
}
// ==================================================
// 全ユーザー一覧（管理者 + スタッフ）
// ==================================================
public List<User> listAllUsers() throws SQLException {

    String sql =
            "SELECT userID, username, usertype, email, phone_number, " +
            "       date_of_birth, Tag, Position, work_place " +
            "FROM users " +
            "ORDER BY usertype ASC, userID ASC"; // 0(管理者)→1(スタッフ)

    List<User> list = new ArrayList<>();

    try (Connection con = Db.getConnection();
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            User u = new User();
            u.setUserID(rs.getString("userID"));
            u.setUsername(rs.getString("username"));
            u.setUsertype(rs.getString("usertype"));
            u.setEmail(rs.getString("email"));
            u.setPhoneNumber(rs.getString("phone_number"));
            u.setDateOfBirth(rs.getDate("date_of_birth"));
            u.setTag(rs.getInt("Tag"));
            u.setPosition(rs.getInt("Position"));
            u.setWorkPlace(rs.getString("work_place"));
            list.add(u);
        }
    }
    return list;
}
// ==================================================
// user 1件取得（管理者/スタッフ両方）
// ==================================================
public User findUserById(String userID) throws SQLException {

    String sql =
            "SELECT userID, username, usertype, email, phone_number, " +
            "       date_of_birth, Tag, Position, work_place " +
            "FROM users " +
            "WHERE userID = ? " +
            "LIMIT 1";

    try (Connection con = Db.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, userID);

        try (ResultSet rs = ps.executeQuery()) {
            if (!rs.next()) return null;

            User u = new User();
            u.setUserID(rs.getString("userID"));
            u.setUsername(rs.getString("username"));
            u.setUsertype(rs.getString("usertype"));
            u.setEmail(rs.getString("email"));
            u.setPhoneNumber(rs.getString("phone_number"));
            u.setDateOfBirth(rs.getDate("date_of_birth"));
            u.setTag(rs.getInt("Tag"));
            u.setPosition(rs.getInt("Position"));
            u.setWorkPlace(rs.getString("work_place"));
            return u;
        }
    }
}
// ==================================================
// user 更新（管理者/スタッフ両方）
// ==================================================
public boolean updateUser(
        String userID,
        String username,
        String email,
        String phoneNumber,
        Date dateOfBirth,
        int tag,
        int position,
        String workPlace
) throws SQLException {

    // positionが存在しなければ 1（未設定）へ
    String existsPos = "SELECT 1 FROM position WHERE id = ? LIMIT 1";
    try (Connection con = Db.getConnection();
         PreparedStatement ps = con.prepareStatement(existsPos)) {

        ps.setInt(1, position);
        try (ResultSet rs = ps.executeQuery()) {
            if (!rs.next()) position = 1;
        }
    }

    String sql =
            "UPDATE users SET " +
            " username = ?, " +
            " email = ?, " +
            " phone_number = ?, " +
            " date_of_birth = ?, " +
            " Tag = ?, " +
            " Position = ?, " +
            " work_place = ? " +
            "WHERE userID = ?";

    try (Connection con = Db.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, username);
        ps.setString(2, email);
        ps.setString(3, phoneNumber);
        ps.setDate(4, dateOfBirth);
        ps.setInt(5, tag);
        ps.setInt(6, position);
        ps.setString(7, workPlace);
        ps.setString(8, userID);

        return ps.executeUpdate() == 1;
    }
}

    
}
