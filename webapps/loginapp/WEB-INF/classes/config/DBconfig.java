package config;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

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

        Map<String, String> map = new HashMap<>();
        map.put("url", dbInfo.getProperty("db.url"));
        map.put("user", dbInfo.getProperty("db.user"));
        map.put("password", dbInfo.getProperty("db.password"));

        return map;
    }
}
