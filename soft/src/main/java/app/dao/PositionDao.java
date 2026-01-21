package app.dao;

import app.db.Db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PositionDao {

    public static class PositionItem {
        public int id;
        public String name;

        public PositionItem(int id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    public List<PositionItem> list() throws SQLException {
        String sql = "SELECT id, name FROM position ORDER BY id";

        List<PositionItem> list = new ArrayList<>();
        try (Connection con = Db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new PositionItem(rs.getInt("id"), rs.getString("name")));
            }
        }
        return list;
    }
}
