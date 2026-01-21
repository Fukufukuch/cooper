<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
	request.setAttribute("activeTab", "menu");
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>管理者メニュー</title>
	<link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/app.css">
</head>
<body>
<div class="container">
	<div class="page-title">シフト自動生成システム</div>
	<div class="sub">管理者メニュー</div>

	<jsp:include page="/WEB-INF/jsp/common/owner_tabs.jspf" />

	<div class="grid-2">
		<a class="tile" href="<%= request.getContextPath() %>/owner/setting">
			<div class="icon">⚙️</div>
			<div>
				<div class="title">設定</div>
				<p class="desc">アカウント作成・削除、パスワード変更</p>
			</div>
		</a>

		<a class="tile" href="<%= request.getContextPath() %>/owner/people">
			<div class="icon">👥</div>
			<div>
				<div class="title">アカウント一覧</div>
				<p class="desc">スタッフ一覧・削除</p>
			</div>
		</a>
	</div>
</div>
</body>
</html>
