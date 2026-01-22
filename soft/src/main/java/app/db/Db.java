package app.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Db {

    private static final String HOST = "3.221.253.103";
    private static final String PORT = "3306";
    private static final String DB   = "shift_db";

    private static final String USER = "cooper";
    private static final String PASS = "CooperG10!";   // ← Cooperのパス

    private static final String URL =
            "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB +
            "?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Tokyo" +
            "&allowPublicKeyRetrieval=true&useSSL=false";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}

