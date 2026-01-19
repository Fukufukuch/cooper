package jp.ac.kochi.tech;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jp.ac.kochi.tech.DBconfig;
import jp.ac.kochi.tech.Admin;

public class Login {

	public Admin check(String userID, String password) throws FileNotFoundException {

		System.out.println("★★ check() 開始 ★★");

		// データベースへの接続情報をプロパティファイルから取得
		DBconfig db_info = new DBconfig();
		String url = db_info.getDBinfo().get("url");
		String user = db_info.getDBinfo().get("user");
		String pass = db_info.getDBinfo().get("password");

		// 実行SQL
		String login_sql = "select * from admin_tb "
				+ "where userID = ? and password = ?;";
		// 管理者のオブジェクトを作成
		Admin admin = new Admin();

		// ===== ① JDBCドライバ確認（ここに入れる）=====
		try {
    		Class.forName("com.mysql.cj.jdbc.Driver");
    		System.out.println("★★ JDBCドライバOK ★★");
		} catch (ClassNotFoundException e) {
    		System.out.println("★★ JDBCドライバNG ★★");
    		e.printStackTrace();
		}

		/*System.out.println(url);
		System.out.println(user);
		System.out.println(pass);
		*/
		
		// データベースへの接続
		// try〜catch〜resources構文を使用
		try(Connection conn = DriverManager.getConnection(url,user,pass)) {
			PreparedStatement stmt = conn.prepareStatement(login_sql);
			//このtry文が死んでいる
			System.out.println("★★ 本当にDB接続成功 ★★");

			// 変数login_sqlの一番目の?に引数のuser_idをセット
			stmt.setString(1, userID);
			// 変数login_sqlの二番目の?に引数のpasswordをセット
			stmt.setString(2, password);

			// SQLを実行し、結果を取得
			ResultSet rs = stmt.executeQuery();
			System.out.println("★★ SQL実行済み ★★");

			// データベースから取得した値をAdminオブジェクトに格納
			// 値がなければ、login_flag（false）のみ格納
			if(rs.next()) {
				admin.setId(rs.getInt("userID"));
				admin.setName(rs.getString("name"));
				admin.setPassword(rs.getString("password"));
				admin.setLogin_flag(true);
				System.out.println("★★ ヒットあり ★★");
				//System.out.println("あどみんIDisなに？");
				//System.out.println(rs.getInt("admin_id"));
			} else {
				admin.setLogin_flag(false);
				System.out.println("確定失敗でしぬぅ");
			}
		} catch (SQLException e) {
			System.out.println("データベースとの接続を閉じます");
			e.printStackTrace();
		}
		// データベースから取得した値を返す
		return admin;
	}

}