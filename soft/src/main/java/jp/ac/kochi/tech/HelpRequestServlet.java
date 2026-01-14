package jp.ac.kochi.tech;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.*;

/**
 * 労働者がヘルプ募集（シフト代行依頼）を投稿し、一時保存するサーブレット
 */
@WebServlet("/HelpRequestServlet")
public class HelpRequestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	private static List<Map<String, String>> helpList = new ArrayList<>();//データを一時的に保存するための箱

	/**
	 * ヘルプ募集画面を表示する
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		// 現在の募集リストをリクエスト属性にセット
		request.setAttribute("helpList", helpList);//保存されている全募集データのリストを、次に表示するJSP画面へ「荷物」として預けてい
		
		// 規約に基づきWEB-INF配下のJSPへフォワード
		RequestDispatcher dispatcher = request.getRequestDispatcher("/helpRequest.jsp");//jspの場所を指定
		dispatcher.forward(request, response);//指定したJSP画面に処理をバトンタッチ（転送）し、画面を表示
	}

	/**
	 * ヘルプ募集フォームからの投稿を処理する
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) //フォームの「投稿」ボタンが押された時
			throws ServletException, IOException {

		// 文字コードとコンテンツタイプの設定（LoginServlet.java参照）
		response.setContentType("text/html; charset=UTF-8");//文字化け対策
		request.setCharacterEncoding("UTF-8");//通信の文字コードをUTF-8に設定

		// フォームから入力値を取得
		String date = request.getParameter("help_date");//以下4行はJSPの入力フォームに入力された値を受け取る
		String startTime = request.getParameter("time_start");
		String endTime = request.getParameter("time_end");
		String reason = request.getParameter("help_reason");
		
		// 本来はセッション等から取得するが、プロトタイプのため固定値を設定(一旦無視)
		//String userId = (String) session.getAttribute("userId");に変更すると保存領域の中から、userId という名前で保管されているデータを取り出
		String userId = "USER_KOSHIRO"; 

		// 1件分のヘルプ募集データ（Mapを使用）
		Map<String, String> help = new HashMap<>();//1件分の募集データを作成するための箱
		help.put("id", UUID.randomUUID().toString()); // 一意識別のためのID
		help.put("userId", userId);//日付、時間、理由、初期ステータスを箱に詰め込んでいる
		help.put("date", date);
		help.put("time", startTime + " 〜 " + endTime);
		help.put("reason", reason);
		help.put("status", "0"); // 0:ヘルプ待ち（初期状態）

		// リストへ保存
		helpList.add(help);//完成した1件分のデータを、全体の保存リストに追加

		// リストを属性にセットして再表示
		request.setAttribute("helpList", helpList);
		RequestDispatcher dispatcher = request.getRequestDispatcher("/helpRequest.jsp");
		dispatcher.forward(request, response);
	}

	/**
	 * 他のサーブレットからヘルプリストを参照するための公開メソッド
	 * 他のプログラムから、
	 * この保存リストを覗き見ることができるようにするための窓口
	 */
	public static List<Map<String, String>> getHelpList() {
		return helpList;
	}
}

/**処理の大まかな流れ
 * 1 helpRequest.jsp を表示
 * 
 * 2 募集を投稿する (doPost)
 * 　1 ユーザーが日付や理由を入力して「投稿」ボタンを押す
 * 　2 現在ログインしているのが誰か (userId) を特定
 * 　3 画面から入力された「日付」「時間」「理由」を読み取る
 * 　4 この募集を特定するためのID生成
 * 　5 「誰が・いつ・何の理由で」という情報をセットにし、
 * 　　　状態を 0 (募集中) として共有リストに追加
 * 　6 再び募集画面を表示
 */