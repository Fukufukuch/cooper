package sql;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import config.DBconfig;
import object.Admin;

public class Login {

	public Admin check(String admin_id, String password) throws FileNotFoundException {

		// データベースへの接続情報をプロパティファイルから取得
		DBconfig db_info = new DBconfig();
		String url = db_info.getDBinfo().get("url");
		String user = db_info.getDBinfo().get("user");
		String pass = db_info.getDBinfo().get("password");

		// 実行SQL
		String login_sql = "select * from admin_tb "
				+ "where admin_id = ? and password = ?;";
		// 管理者のオブジェクトを作成
		Admin admin = new Admin();

		// データベースへの接続
		// try〜catch〜resources構文を使用
		try(Connection conn = DriverManager.getConnection(url,user,pass)) {
			PreparedStatement stmt = conn.prepareStatement(login_sql);

			// 変数login_sqlの一番目の?に引数のuser_idをセット
			stmt.setString(1, admin_id);
			// 変数login_sqlの二番目の?に引数のpasswordをセット
			stmt.setString(2, password);

			// SQLを実行し、結果を取得
			ResultSet rs = stmt.executeQuery();

			// データベースから取得した値をAdminオブジェクトに格納
			// 値がなければ、login_flag（false）のみ格納
			if(rs.next()) {
				admin.setId(rs.getInt("admin_id"));
				admin.setName(rs.getString("name"));
				admin.setPassword(rs.getString("password"));
				admin.setLogin_flag(true);
			} else {
				admin.setLogin_flag(false);
			}
		} catch (SQLException e) {
			System.out.println("データベースとの接続を閉じます");
			e.printStackTrace();
		}
		// データベースから取得した値を返す
		return admin;
	}
}