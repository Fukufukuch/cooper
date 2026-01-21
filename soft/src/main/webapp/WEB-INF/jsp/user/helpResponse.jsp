<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html lang="ja">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>ヘルプ一覧 | オートシフタ</title>
	<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
	<style>
		/* shift.jsp のスタイルを反映 */
		body { 
			background-color: #f4f7f6; 
			font-family: sans-serif;
		}
		.navbar { background-color: #007bff; }
		.help-card { 
			border: 1px solid #ddd; 
			border-radius: 8px; 
			background-color: white;
			box-shadow: 0 2px 5px rgba(0,0,0,0.1);
			transition: 0.2s;
		}
		.help-card:hover { transform: translateY(-2px); box-shadow: 0 4px 8px rgba(0,0,0,0.12); }
		.reason-box { 
			background-color: #f9f9f9; 
			border-radius: 4px;
			padding: 10px;
			font-size: 0.9rem;
			border-left: 3px solid #007bff;
		}
		.btn-success { background-color: #28a745; border: none; } /* shift.jsp の追加ボタン色 */
	</style>
</head>
<body>
	<nav class="navbar navbar-expand-lg navbar-dark mb-4 shadow-sm">
		<div class="container">
			<a class="navbar-brand fw-bold" href="#">📅 オートシフタ</a>
			<div class="navbar-nav">
				<a class="nav-link" href="shift.jsp">シフト希望提出</a>
				<a class="nav-link" href="HelpRequestServlet">ヘルプ募集</a>
				<a class="nav-link active" href="HelpResponseServlet">ヘルプ一覧</a>
			</div>
		</div>
	</nav>

	<div class="container">
		<div class="row mb-3 align-items-center">
			<div class="col">
				<h4 class="fw-bold mb-0">🔍 募集中シフト一覧</h4>
			</div>
		</div>

		<div class="row">
			<%
				List<Map<String, String>> helpList = (List<Map<String, String>>) request.getAttribute("helpList");
				if (helpList != null && !helpList.isEmpty()) {
					for (Map<String, String> help : helpList) {
			%>
				<div class="col-md-4 mb-4">
					<div class="card help-card h-100">
						<div class="card-body">
							<div class="d-flex justify-content-between align-items-start mb-2">
								<h5 class="card-title fw-bold text-dark mb-0">📅 <%= help.get("date") %></h5>
							</div>
							<p class="card-text text-primary small mb-3 fw-bold">
								⏰ <%= help.get("time") %>
							</p>
							<div class="reason-box mb-3">
								<small class="text-secondary fw-bold">理由:</small> <%= help.get("reason") %>
							</div>
							<div class="text-end mb-3">
								<small class="text-muted">👤 依頼者: <%= help.get("userId") %></small>
							</div>
						</div>
						
						<div class="card-footer bg-white border-0 pb-3">
							<% if ("0".equals(help.get("status"))) { %>
								<form action="${pageContext.request.contextPath}/HelpResponseServlet" method="post">
									<input type="hidden" name="help_id" value="<%= help.get("id") %>">
									<button type="submit" class="btn btn-success w-100 fw-bold py-2">このシフトに応募する</button>
								</form>
							<% } else { %>
								<button class="btn btn-secondary w-100 fw-bold py-2" disabled>応募済み</button>
							<% } %>
						</div>
					</div>
				</div>
			<%
					}
				} else {
			%>
				<div class="col-12 text-center py-5 mt-4">
					<div class="p-5 bg-white rounded shadow-sm border">
						<p class="text-muted mb-0">現在、募集されているシフトはありません。</p>
					</div>
				</div>
			<% } %>
		</div>
	</div>
</body>
</html>