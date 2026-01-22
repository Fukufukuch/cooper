<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  request.setAttribute("activeTab", "setting");
  String error = (String) request.getAttribute("error");
%>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>パスワード変更</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/app.css">
</head>
<body>
<div class="container">
  <div class="h1">シフト自動生成システム</div>
  <div class="sub">パスワード変更</div>

  <%@ include file="/WEB-INF/jsp/common/owner_tabs.jspf" %>

  <a class="backlink" href="<%= request.getContextPath() %>/owner/setting/menu">← 設定に戻る</a>

  <div class="card" style="margin-top:14px;">
    <div class="section-title">パスワード変更</div>

    <% if (error != null) { %>
      <div class="alert danger"><%= error %></div>
    <% } %>

    <form method="post" action="<%= request.getContextPath() %>/owner/password">
      <div class="form-row">
        <label>ユーザーID</label>
        <input class="input" type="text" name="userID" required>
      </div>

      <div class="form-row">
        <label>現在のパスワード</label>
        <input class="input" type="password" name="oldPassword" required>
      </div>

      <div class="form-row">
        <label>新しいパスワード</label>
        <input class="input" type="password" name="newPassword" minlength="8" maxlength="20" required>
      </div>

      <div class="form-actions">
        <button class="btn" type="submit">変更</button>
      </div>
    </form>
  </div>
</div>
</body>
</html>
