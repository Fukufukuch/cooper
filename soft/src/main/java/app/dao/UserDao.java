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

    private static final SecureRandom RAND = new SecureRandom();
    private static final String TABLE = "users";

    // ==================================================
    // 全ユーザー取得（スタッフ一覧など）
    // ==================================================
    public List<User> findAllUsers() throws SQLException {

        String sql =
                "SELECT userID, username, usertype, email, phone_number, " +
                "       work_place, Tag, Position, date_of_birth " +
                "FROM users " +
                "ORDER BY userID DESC";

        List<User> list = new ArrayList<>();

        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                User u = new User();

                u.setUserID(rs.getString("userID"));
                u.setUsername(rs.getString("username"));

                // usertype: 0=管理者 / 1=スタッフ
                u.setUsertype(rs.getBoolean("usertype"));

                u.setEmail(rs.getString("email"));
                u.setPhoneNumber(rs.getString("phone_number"));
                u.setWorkPlace(rs.getString("work_place"));
                u.setTag(rs.getInt("Tag"));
                u.setPosition(rs.getInt("Position"));

                Date dob = rs.getDate("date_of_birth");
                if (dob != null) {
                    u.setDateOfBirth(dob.toLocalDate());
                }

                list.add(u);
            }
        }
        return list;
    }

    // ==================================================
    // スタッフ一覧（usertype = 1）
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

                // スタッフ固定（usertype=1）
                u.setUsertype(true);

                u.setEmail(rs.getString("email"));
                u.setPhoneNumber(rs.getString("phone_number"));
                u.setWorkPlace(rs.getString("work_place"));
                u.setTag(rs.getInt("Tag"));
                u.setPosition(rs.getInt("Position"));

                Date dob = rs.getDate("date_of_birth");
                if (dob != null) {
                    u.setDateOfBirth(dob.toLocalDate());
                }

                list.add(u);
            }
        }
        return list;
    }

    // ==================================================
    // スタッフ削除（安全：usertype=1のみ）
    // ==================================================
    public boolean deleteStaff(String userID) throws SQLException {
        String sql = "DELETE FROM " + TABLE + " WHERE userID = ? AND usertype = b'1'";

        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, userID);
            return ps.executeUpdate() == 1;
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

        String sql =
                "INSERT INTO " + TABLE +
                " (userID, username, usertype, password, date_of_birth, " +
                "  phone_number, email, TotalWorking, Tag, Position, work_place) " +
                "VALUES (?, ?, b'1', ?, ?, ?, ?, 0, 0, 0, ?)";

        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

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

        return userID;
    }

    // ==================================================
    // パスワード変更
    // ==================================================
    public boolean changePassword(String userID, String oldPassword, String newPassword)
            throws SQLException {

        String sql =
                "UPDATE " + TABLE +
                " SET password = ? WHERE userID = ? AND password = ?";

        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, newPassword);
            ps.setString(2, userID);
            ps.setString(3, oldPassword);

            return ps.executeUpdate() == 1;
        }
    }

    // ==================================================
    // 管理者ID取得（usertype=0 の先頭1件）
    // ==================================================
    public String findAdminUserId() throws SQLException {

        String sql = "SELECT userID FROM " + TABLE + " WHERE usertype = b'0' LIMIT 1";

        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getString("userID");
            }
        }
        return null;
    }

    // ==================================================
    // private
    // ==================================================
    private boolean existsUserId(String userID) throws SQLException {

        String sql = "SELECT 1 FROM " + TABLE + " WHERE userID = ? LIMIT 1";

        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, userID);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    /** 10桁の数字IDを作る（例：7392695024） */
    private String generateUserId10() {
        long n = Math.abs(RAND.nextLong());
        String s = Long.toString(n);
        while (s.length() < 10) {
            s += RAND.nextInt(10);
        }
        return s.substring(0, 10);
    }
}
