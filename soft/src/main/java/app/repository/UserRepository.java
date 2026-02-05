package app.repository;

import app.db.Db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class UserRepository {

    /**
     * 指定した userID の集合に対して userID->username マップを返す。
     * 空集合を渡すと空マップを返す。
     */
    public Map<String, String> findUsernamesByIds(Set<String> ids) {
        Map<String, String> map = new HashMap<>();
        if (ids == null || ids.isEmpty()) return map;

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT userID, username FROM users WHERE userID IN (");
        for (int i = 0; i < ids.size(); i++) {
            if (i > 0) sb.append(',');
            sb.append('?');
        }
        sb.append(')');

        try (Connection conn = Db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sb.toString())) {

            int idx = 1;
            for (String id : ids) ps.setString(idx++, id);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    map.put(rs.getString("userID"), rs.getString("username"));
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("users 取得失敗", e);
        }

        return map;
    }
}
