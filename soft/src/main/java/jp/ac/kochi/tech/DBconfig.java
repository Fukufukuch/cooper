package jp.ac.kochi.tech;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Map;
import java.sql.SQLException;

public class DBconfig {

    public Map<String, String> getDBinfo() {

        Properties dbInfo = new Properties();

        try (InputStream is =
                Thread.currentThread()
                      .getContextClassLoader()
                      .getResourceAsStream("DBconfig.properties")) {

            if (is == null) {
                throw new RuntimeException("DBconfig.properties が見つかりません");
            }

            dbInfo.load(is);

        } catch (Exception e) {
            throw new RuntimeException("DB設定の読み込みに失敗しました", e);
        }

        //DBconfig.propertiesのキーから値を取得する
		String db_url = dbInfo.getProperty("url");
		String db_user = dbInfo.getProperty("user");
		String db_pass = dbInfo.getProperty("password");

		// 取得したデータベースの接続情報をMapに格納する
		Map<String,String> getDBinfoForMap = new HashMap<>();

		getDBinfoForMap.put("url", db_url);
		getDBinfoForMap.put("user", db_user);
		getDBinfoForMap.put("password", db_pass);

		// DBconfigクラスの
		// getDBinfoメソッドが呼び出された際に
		// 『接続情報、ユーザ名、パスワード』の情報を返す
		return getDBinfoForMap;
    }

    public static Connection getConnection() throws Exception {
        DBconfig config = new DBconfig();
        Map<String, String> info = config.getDBinfo();

        Class.forName("com.mysql.cj.jdbc.Driver");

        return DriverManager.getConnection(
            info.get("url"),
            info.get("user"),
            info.get("password")
        );
    }
}