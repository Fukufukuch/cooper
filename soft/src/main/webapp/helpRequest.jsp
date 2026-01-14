<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html lang="ja">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>ヘルプ募集 | オートシフタ</title>
	<!-- Bootstrap 5 CSS -->
	<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
	<style>
		body { background-color: #f8f9fa; }
		.card { border-radius: 12px; border: none; box-shadow: 0 4px 10px rgba(0,0,0,0.08); }
		.navbar { background-color: #343a40; }
		.status-badge { font-size: 0.8rem; }
	</style>
</head>
<body>
	<!-- ヘッダー -->
	<nav class="navbar navbar-expand-lg navbar-dark mb-4">
		<div class="container">
			<a class="navbar-brand fw-bold" href="#">オートシフタ</a>
			<span class="navbar-text text-white-50">ヘルプ募集システム</span>
		</div>
	</nav>

	<div class="container">
		<div class="row">
			<!-- 募集入力エリア -->
			<div class="col-md-5 mb-4">
				<div class="card p-4">
					<h5 class="fw-bold mb-4">新しく募集を投稿</h5>
					<!-- Servletへデータを送信 -->
					<form action="${pageContext.request.contextPath}/HelpRequestServlet" method="post">
						<div class="mb-3">
							<label class="form-label small fw-bold text-secondary">代わってほしい日付</label>
							<input type="date" name="help_date" class="form-control" required>
						</div>
						<div class="row">
							<div class="col-6 mb-3">
								<label class="form-label small fw-bold text-secondary">開始時間</label>
								<input type="time" name="time_start" class="form-control" required>
							</div>
							<div class="col-6 mb-3">
								<label class="form-label small fw-bold text-secondary">終了時間</label>
								<input type="time" name="time_end" class="form-control" required>
							</div>
						</div>
						<div class="mb-4">
							<label class="form-label small fw-bold text-secondary">代行を依頼する理由</label>
							<textarea name="help_reason" class="form-control" rows="3" placeholder="理由を入力してください"></textarea>
						</div>
						<button type="submit" class="btn btn-primary w-100 fw-bold py-2">募集を投稿する</button>
					</form>
				</div>
			</div>

			<!-- 投稿履歴エリア -->
			<div class="col-md-7">
				<div class="d-flex justify-content-between align-items-center mb-3">
					<h5 class="fw-bold mb-0">あなたの募集履歴</h5>
					<a href="HelpResponseServlet" class="btn btn-sm btn-outline-secondary">他者の募集を見る &raquo;</a>
				</div>
				
				<div class="list-group shadow-sm">
				<%
					// Servletから渡された全募集リストを受け取る
					List<Map<String, String>> helpList = (List<Map<String, String>>) request.getAttribute("helpList");
					if (helpList != null && !helpList.isEmpty()) {
						// 逆順（最新順）に表示
						Collections.reverse(helpList);
						for (Map<String, String> help : helpList) {
							String status = help.get("status");
				%>
					<div class="list-group-item p-3 border-0 border-bottom">
						<div class="d-flex w-100 justify-content-between">
							<h6 class="mb-1 fw-bold text-primary"><%= help.get("date") %></h6>
							<!-- ステータス表示の切り替え -->
							<% if ("0".equals(status)) { %>
								<span class="badge bg-warning text-dark status-badge">募集中</span>
							<% } else { %>
								<span class="badge bg-info text-white status-badge">承認待ち</span>
							<% } %>
						</div>
						<p class="mb-1 small">勤務時間: <%= help.get("time") %></p>
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

	<!-- Bootstrap JS -->
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>