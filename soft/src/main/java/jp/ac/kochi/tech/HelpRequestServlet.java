package jp.ac.kochi.tech;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.*;

@WebServlet("/HelpRequestServlet")
public class HelpRequestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static List<Map<String, String>> helpList = new ArrayList<>();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		request.setAttribute("helpList", helpList);
		RequestDispatcher dispatcher = request.getRequestDispatcher("/helpRequest.jsp");
		dispatcher.forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {

		response.setContentType("text/html; charset=UTF-8");
		request.setCharacterEncoding("UTF-8");

		// フォームから入力値を取得（修正箇所）
		String date = request.getParameter("help_date");
		String shiftType = request.getParameter("shift_type"); // プルダウンから取得
		String reason = request.getParameter("help_reason");
		
		String userId = "USER_KOSHIRO"; 

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