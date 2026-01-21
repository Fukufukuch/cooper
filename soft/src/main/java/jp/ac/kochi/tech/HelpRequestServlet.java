package jp.ac.kochi.tech;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/HelpRequestServlet")
public class HelpRequestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// ★ DB接続情報を追加 ★
    private static final String URL =
        "jdbc:mysql://localhost:3306/shift_db?useSSL=false&serverTimezone=Asia/Tokyo";
    private static final String USER = "cooper";
    private static final String PASSWORD = "CooperG10!";

	private static List<Map<String, String>> helpList = new ArrayList<>();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		

		List<Map<String, Object>> timeSlotList = new ArrayList<>();
    	List<String> shiftDateList = new ArrayList<>();
		String userId = "USER_KOSHIRO"; // 本来はセッション
		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {

        // --- TimeSlot 取得 ---
        String slotSql = "SELECT id, name FROM TimeSlot WHERE is_active = true";
        try (PreparedStatement ps = conn.prepareStatement(slotSql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> slot = new HashMap<>();
                slot.put("id", rs.getInt("id"));
                slot.put("name", rs.getString("name"));
                timeSlotList.add(slot);
            }
        }

        // --- Shift 日付取得 ---
        String shiftSql =
            "SELECT DISTINCT date FROM Shift WHERE workerID = ? ORDER BY date";
        try (PreparedStatement ps = conn.prepareStatement(shiftSql)) {
            ps.setString(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                shiftDateList.add(rs.getDate("date").toString());
            }
        }

    } catch (Exception e) {
        throw new ServletException(e);
    }

		request.setAttribute("timeSlotList", timeSlotList);
    	request.setAttribute("shiftDateList", shiftDateList);
		// 現在の募集リストをリクエスト属性にセット
		request.setAttribute("helpList", helpList);//保存されている全募集データのリストを、次に表示するJSP画面へ「荷物」として預けてい
		
		// 規約に基づきWEB-INF配下のJSPへフォワード
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/user/helpRequest.jsp");//jspの場所を指定
		dispatcher.forward(request, response);//指定したJSP画面に処理をバトンタッチ（転送）し、画面を表示
	}

	/**
	 * ヘルプ募集フォームからの投稿を処理する
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {

		// 文字コードとコンテンツタイプの設定（LoginServlet.java参照）
		response.setContentType("text/html; charset=UTF-8");
		request.setCharacterEncoding("UTF-8");

		// フォームから入力値を取得（修正箇所）
		String date = request.getParameter("help_date");
		String shiftType = request.getParameter("shift_type"); // プルダウンから取得
		String reason = request.getParameter("help_reason");
		
		String userId = "USER_KOSHIRO"; 

		// 1件分データ
		Map<String, String> help = new HashMap<>();
		help.put("id", UUID.randomUUID().toString());
		help.put("userId", userId);
		help.put("date", date);
		// キー名は "time" のままにしておくことで、JSP側の表示ロジックを変更せずに済みます
		help.put("time", shiftType); 
		help.put("reason", reason);
		help.put("status", "0"); 

		helpList.add(help);

		request.setAttribute("helpList", helpList);
		RequestDispatcher dispatcher = request.getRequestDispatcher("/helpRequest.jsp");
		dispatcher.forward(request, response);
	}

	public static List<Map<String, String>> getHelpList() {
		return helpList;
	}
}