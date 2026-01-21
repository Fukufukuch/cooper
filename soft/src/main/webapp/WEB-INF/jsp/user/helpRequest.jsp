<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html lang="ja">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>ヘルプ募集 | オートシフタ</title>
	<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
	<style>
		body { background-color: #f4f7f6; font-family: sans-serif; }
		.navbar { background-color: #007bff; }
		.card { border: none; border-radius: 8px; box-shadow: 0 2px 5px rgba(0,0,0,0.1); }
		.btn-primary { background-color: #007bff; border: none; }
		.status-badge { font-size: 0.8rem; border-radius: 4px; }
		.list-group-item { border-left: none; border-right: none; }
	</style>
</head>
<body>
	<nav class="navbar navbar-expand-lg navbar-dark mb-4 shadow-sm">
		<div class="container">
			<a class="navbar-brand fw-bold" href="#">📅 オートシフタ</a>
			<div class="navbar-nav">
				<a class="nav-link" href="shift.jsp">シフト希望提出</a>
				<a class="nav-link active" href="HelpRequestServlet">ヘルプ募集</a>
				<a class="nav-link" href="HelpResponseServlet">ヘルプ一覧</a>
			</div>
		</div>
	</nav>

	<div class="container">
		<div class="row">
			<div class="col-md-5 mb-4">
				<div class="card p-4">
					<h5 class="fw-bold mb-4">📝 新しく募集を投稿</h5>
					<form action="${pageContext.request.contextPath}/HelpRequestServlet" method="post">
						<div class="mb-3">
							<label class="form-label small fw-bold text-secondary">代わってほしい日付</label>
							<select name="help_date" class="form-select" required>
							<option value="" disabled selected>日付を選択してください</option>
							<%
								List<String> shiftDateList =
									(List<String>) request.getAttribute("shiftDateList");
								for (String d : shiftDateList) {
							%>
								<option value="<%= d %>"><%= d %></option>
							<%
								}
							%>
							</select>
						</div>
						
						<div class="mb-3">
							<label class="form-label small fw-bold text-secondary">シフト区分</label>
								<select name="shift_type" class="form-select" required>
							<option value="" disabled selected>区分を選択してください</option>
							<%
								List<Map<String, Object>> timeSlotList =
									(List<Map<String, Object>>) request.getAttribute("timeSlotList");
								for (Map<String, Object> slot : timeSlotList) {
							%>
								<option value="<%= slot.get("id") %>">
									<%= slot.get("name") %>
								</option>
							<%
								}
							%>
						</select>	
						</div>

						<div class="mb-4">
							<label class="form-label small fw-bold text-secondary">代行を依頼する理由</label>
							<textarea name="help_reason" class="form-control" rows="3" placeholder="理由を入力してください"></textarea>
						</div>
						<button type="submit" class="btn btn-primary w-100 fw-bold py-2">募集を投稿する</button>
					</form>
				</div>
			</div>

			<div class="col-md-7">
				<div class="d-flex justify-content-between align-items-center mb-3">
					<h5 class="fw-bold mb-0">あなたの募集履歴</h5>
				</div>
				
				<div class="list-group shadow-sm">
				<%
					List<Map<String, String>> helpList = (List<Map<String, String>>) request.getAttribute("helpList");
					if (helpList != null && !helpList.isEmpty()) {
						List<Map<String, String>> reverseList = new ArrayList<>(helpList);
						Collections.reverse(reverseList);
						for (Map<String, String> help : reverseList) {
							String status = help.get("status");
				%>
					<div class="list-group-item p-3 bg-white">
						<div class="d-flex w-100 justify-content-between align-items-center">
							<h6 class="mb-1 fw-bold text-primary">📅 <%= help.get("date") %></h6>
							<% if ("0".equals(status)) { %>
								<span class="badge bg-warning text-dark status-badge">募集中</span>
							<% } else { %>
								<span class="badge bg-info text-white status-badge">承認待ち</span>
							<% } %>
						</div>
						<p class="mb-1 small">📋 シフト: <%= help.get("time") %></p>
						<small class="text-muted d-block mt-1">
							<strong>理由:</strong> <%= help.get("reason") %>
						</small>
					</div>
				<%
						}
					} else {
				%>
					<div class="list-group-item text-center py-5 text-muted bg-white">
						現在、投稿された募集はありません。
					</div>
				<% } %>
				</div>
			</div>
		</div>
	</div>
</body>
</html>