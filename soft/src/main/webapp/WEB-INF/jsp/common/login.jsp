<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-giJF6kkoqNQ00vy+HMDP7azOuL0xtbfIcaT9wjKHr8RbDVddVHyTfAAsrekwKmP1" crossorigin="anonymous">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/js/bootstrap.bundle.min.js"></script>
<title>ログイン画面</title>
</head>
<body>
	<div class="mx-auto" style="width: 300px;">
		<h1 style="text-align: center; margin-bottom: 10px; color: #333;">オートシフタ</h1>
		<h2 class="mb-3" style="text-align: center; font-size: 18px; color: #666;">ログイン画面</h2>
		
		<%
			String errorMessage = (String) request.getAttribute("errorMessage");
			if (errorMessage != null && !errorMessage.isEmpty()) {
		%>
		<div class="alert alert-danger alert-dismissible fade show" role="alert" style="margin-bottom: 20px;">
			<strong>ログイン失敗</strong><br><%= errorMessage %>
			<button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
		</div>
		<% } %>
		
		<form action="${pageContext.request.contextPath}/LoginServlet" method="post">
		  <div class="mb-3">
		    <label for="userID" class="form-label">ユーザーID</label>
		    <input type="text" class="form-control" id="userID" name="userID">
		  </div>
		  <div class="mb-3">
		    <label for="pass" class="form-label">パスワード</label>
		    <input type="password" class="form-control" id="pass" name="password">
		  </div>
		  <button type="submit" class="btn btn-primary">ログイン</button>
		</form>
	</div>
</body>
</html>