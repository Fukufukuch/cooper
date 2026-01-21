package app.db;

public class DBConfig {
    // AWSの値に合わせて変えてOK
    public static final String HOST = "localhost";
    public static final int PORT = 3306;
    public static final String DB_NAME = "shift_db";
    public static final String USER = "root";
    public static final String PASS = "CooperG10!";

    public static String url() {
        return "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB_NAME
                + "?useUnicode=true"
                + "&characterEncoding=utf8"
                + "&connectionCollation=utf8mb4_general_ci"
                + "&serverTimezone=Asia/Tokyo";
    }
}
