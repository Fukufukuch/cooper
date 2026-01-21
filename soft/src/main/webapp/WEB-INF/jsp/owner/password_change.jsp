<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  request.setAttribute("activeTab", "setting");
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

  <a class="backlink" href="<%= request.getContextPath() %>/owner/setting">← 設定に戻る</a>

  <div class="h1">パスワード変更</div>
  <div class="sub">管理者パスワードを変更</div>

  <%@ include file="/WEB-INF/jsp/common/owner_tabs.jspf" %>

  <div class="card" style="margin-top:14px;">
    <div class="section-title">入力</div>

    <% String error = (String)request.getAttribute("error"); %>
    <% String success = (String)request.getAttribute("success"); %>

    <% if (error != null) { %>
      <div class="note" style="color:#ef4444; font-weight:900;"><%= error %></div>
    <% } %>
    <% if (success != null) { %>
      <div class="note" style="color:#16a34a; font-weight:900;"><%= success %></div>
    <% } %>

    <form class="form" method="post" action="<%= request.getContextPath() %>/owner/password">
      <div>
        <div class="label">現在のパスワード</div>
        <input class="input" type="password" name="oldPassword" maxlength="20" required>
      </div>

      <div>
        <div class="label">新しいパスワード</div>
        <input class="input" type="password" name="newPassword" maxlength="20" required>
      </div>

      <div>
        <div class="label">新しいパスワード（確認）</div>
        <input class="input" type="password" name="confirmPassword" maxlength="20" required>
      </div>

      <button class="btn primary" type="submit">変更する</button>
    </form>
  </div>

</div>
</body>
</html>
