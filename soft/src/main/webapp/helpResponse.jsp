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
		body { background-color: #f1f3f5; }
		.navbar { background-color: #0d6efd; }
		.help-card { border: none; border-radius: 12px; transition: 0.2s; }
		.help-card:hover { transform: translateY(-3px); }
		.reason-box { background-color: #f8f9fa; border-left: 4px solid #dee2e6; font-size: 0.9rem; }
	</style>
</head>
<body>
	<!-- ヘッダー -->
	<nav class="navbar navbar-dark mb-4 shadow-sm">
		<div class="container">
			<a class="navbar-brand fw-bold" href="#">オートシフタ</a>
			<span class="navbar-text text-white">ヘルプ募集一覧</span>
		</div>
	</nav>

	<div class="container">
		<div class="row mb-3">
			<div class="col d-flex justify-content-between align-items-center">
				<h4 class="fw-bold mb-0">募集中シフト一覧</h4>
				<a href="HelpRequestServlet" class="btn btn-sm btn-outline-primary">募集を作成する</a>
			</div>
		</div>

		<div class="row">
			<%
				List<Map<String, String>> helpList = (List<Map<String, String>>) request.getAttribute("helpList");
				if (helpList != null && !helpList.isEmpty()) {
					for (Map<String, String> help : helpList) {
			%>
				<div class="col-md-4 mb-4">
					<div class="card help-card shadow-sm h-100">
						<div class="card-body">
							<div class="d-flex justify-content-between align-items-start mb-2">
								<h5 class="card-title fw-bold text-dark mb-0"><%= help.get("date") %></h5>
							</div>
							<p class="card-text text-primary small mb-3 fw-bold">
								<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-clock me-1" viewBox="0 0 16 16">
									<path d="M8 3.5a.5.5 0 0 0-1 0V9a.5.5 0 0 0 .252.434l3.5 2a.5.5 0 0 0 .496-.868L8 8.71V3.5z"/>
									<path d="M8 16A8 8 0 1 0 8 0a8 8 0 0 0 0 16zm7-8A7 7 0 1 1 1 8a7 7 0 1 1 14 0z"/>
								</svg>
								<%= help.get("time") %>
							</p>
							<div class="reason-box p-2 mb-3">
								<small class="text-secondary fw-bold">募集理由:</small><br>
								<%= help.get("reason") %>
							</div>
							<div class="text-end">
								<small class="text-muted" style="font-size: 0.7rem;">依頼者: <%= help.get("userId") %></small>
							</div>
						</div>
						
						<div class="card-footer bg-white border-0 pb-3">
							<!-- statusが 0(募集中) の場合のみ応募ボタンを表示 -->
							<% if ("0".equals(help.get("status"))) { %>
								<form action="${pageContext.request.contextPath}/HelpResponseServlet" method="post">
									<input type="hidden" name="help_id" value="<%= help.get("id") %>">
									<button type="submit" class="btn btn-success w-100 fw-bold">このシフトに応募する</button>
								</form>
							<% } else { %>
								<button class="btn btn-secondary w-100 fw-bold" disabled>応募済み（承認待ち）</button>
							<% } %>
						</div>
					</div>
				</div>
			<%
					}
				} else {
			%>
				<!-- 募集がない場合の表示 -->
				<div class="col-12 text-center py-5 mt-4">
					<div class="p-5 bg-white rounded shadow-sm">
						<p class="text-muted mb-0">現在、募集されているシフトはありません。</p>
					</div>
				</div>
			<% } %>
		</div>
	</div>

	<!-- Bootstrap JS -->
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>