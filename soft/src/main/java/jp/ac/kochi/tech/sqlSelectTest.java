package jp.ac.kochi.tech;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class sqlSelectTest {
    public static void main(String[] args) {
        String url = "jdbc:mysql://3.221.253.103:3306/shift_db?useSSL=false&serverTimezone=Asia/Tokyo";
        String user = "cooper";
        String pass = "CooperG10!"; // 実際のパスワードに変更
        String sql = "SELECT userID, username, usertype, work_place FROM user";

        try (
            Connection conn = DriverManager.getConnection(url, user, pass);
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
        ) {
            while (rs.next()) {
                String userID = rs.getString("userID");
                String username = rs.getString("username");
                int usertype = rs.getInt("usertype");
                String workPlace = rs.getString("work_place");

                System.out.println(
                    userID + " | " +
                    username + " | " +
                    usertype + " | " +
                    workPlace
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
