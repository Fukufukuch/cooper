package jp.ac.kochi.tech;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jp.ac.kochi.tech.Admin;
import jp.ac.kochi.tech.Login;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;


	//ログイン画面を表示させる
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher dispatcher =
				request.getRequestDispatcher("/WEB-INF/jsp/common/login.jsp");
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 文字コードの設定
		response.setContentType("text/html; charset=UTF-8");
		request.setCharacterEncoding("UTF-8");

		// ログイン画面で入力された値を取得
		String user_id = request.getParameter("userID");
		String password = request.getParameter("password");

		// ログイン画面で入力された値をもとに
		// データベースに登録された管理者の値を取得
		// 入力された情報でデータベースから値が取得できない場合
		// ログイン失敗
		Login login = new Login();
		Admin admin = login.check(user_id, password);

		System.out.println("ログイン判定なう");
		System.out.println(admin.isLogin_flag());

		if(admin.isLogin_flag()) {
			// ログイン成功 → ユーザータイプに基づいてカレンダー画面へ遷移
			System.out.println("ログイン成功");
			request.getSession().setAttribute("userID", user_id);
			request.getSession().setAttribute("userName", admin.getName());
			request.getSession().setAttribute("usertype", admin.getUsertype());
			response.sendRedirect(request.getContextPath() + "/calendar");
		} else {
			// ログイン失敗 → ログイン画面へ遷移
			System.out.println("ログイン失敗");
			request.setAttribute("errorMessage", "ユーザーIDまたはパスワードが間違っています");
			RequestDispatcher dispatcher =
					request.getRequestDispatcher("/WEB-INF/jsp/common/login.jsp");
			dispatcher.forward(request, response);
		}
	}
}