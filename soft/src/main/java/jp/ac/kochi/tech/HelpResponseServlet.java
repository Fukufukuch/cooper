package jp.ac.kochi.tech;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.*;

/**
 * 募集中のヘルプに対して他の労働者が応募（応答）処理を行うサーブレット
 */
@WebServlet("/HelpResponseServlet")
public class HelpResponseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L; // Javaのクラスを管理するための識別番号

	/**
	 * ヘルプ募集一覧画面を表示する
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) //「ヘルプ一覧を見たい」というリクエスト（GET送信）が来たときに動く部分
			throws ServletException, IOException {
		
		// HelpRequestServletから「募集リスト」を読み取ってリストを取得してセット
		request.setAttribute("helpList", HelpRequestServlet.getHelpList());
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("/helpResponse.jsp");
		dispatcher.forward(request, response);//指定したJSP画面に処理を転送し、画面を表示
	}

	/**
	 * 特定のヘルプ募集に対する応募処理を実行する
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)//一覧画面で「応募する」ボタンが押されたとき処理 
			throws ServletException, IOException {//「このメソッド内でエラーが起きたら、自分では処理せず呼び出し元（サーバー）に丸投げしますよ」 という宣言

		// 文字コードとコンテンツタイプの設定
		response.setContentType("text/html; charset=UTF-8");//文字化け対策
		request.setCharacterEncoding("UTF-8");//通信の文字コードをUTF-8に設定


		// 応募対象のIDを取得
		String targetId = request.getParameter("help_id");//どの募集に応募したのかを特定するため、画面から送られてきた固有のIDを受け取ってい
		String helperId = "USER_HELPER"; // 応募者ID（本来はセッションから取得）
        /**
         * HttpSession session = request.getSession();サーバーが管理している「その人専用のデータ箱（セッション）」とプログラムが結びつき、ログイン情報などを取り出せるようになりる
	     * String helperId = (String) session.getAttribute("userId");'userId' というラベルが貼られた荷物を取り出し、応募者IDとして使う
         * ※注意：これを使うには、事前に LoginServlet などで session.setAttribute("userId", "実際のID") と保存しておく必要があり
	     */

		// ヘルプリストを取得して該当データを検索・更新==保存されている全募集データのリストを呼び出し
		List<Map<String, String>> helpList = HelpRequestServlet.getHelpList();
		
		for (Map<String, String> help : helpList) {//保存されているデータをチェックしていくループ処理
			// IDが一致し、かつまだ募集中(status=0)の場合のみ更新
			if (help.get("id").equals(targetId) && "0".equals(help.get("status"))) {//「IDが一致するデータか？」かつ「まだ誰も応募していない（ステータスが0か？）」をダブルチェック
				help.put("status", "1"); // 1:承認待ち（応募済み）
				help.put("helperId", helperId);//「誰が助けてくれるのか（応募者）」の情報をデータに追記
				break;
			}
		}

		// 更新後のリストを表示用JSPへ渡す
		request.setAttribute("helpList", helpList);
		RequestDispatcher dispatcher = request.getRequestDispatcher("/helpResponse.jsp");
		dispatcher.forward(request, response);
	}
}

/**処理の大まかな流れ
 * 1 helpResponse.jsp を表示
 * 
 * 2 ヘルプに応募する (doPost)
 * 　1 ユーザーが特定の募集の「応募する」ボタンを押す
 * 　2 今ボタンを押したのが誰か (helperId) をセッションから取得
 * 　3 どの募集に応募したのかを「固有のID (help_id)」で判断
 * 　4 保存されているリストの中から、その固有のIDを持つデータを探し出す
 * 　5 見つけたデータの status を 0 (募集) から 1 (承認待ち) に書き換え、
 * 　　helperId に自分の名前を書き込み
 * 　6 一覧画面を再表示
 */