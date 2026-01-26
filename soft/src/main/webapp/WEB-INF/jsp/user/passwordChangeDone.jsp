<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  request.setAttribute("activeTab", "setting");
  String userID = (String) request.getAttribute("userID");
%>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>パスワード変更完了</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/app.css">
</head>
<body>
<div class="container">
  <div class="h1">シフト自動生成システム</div>
  <div class="sub">パスワード変更</div>

  <%@ include file="/WEB-INF/jsp/common/user_tabs.jspf" %>

  <a class="backlink" href="<%= request.getContextPath() %>/user/setting/menu">← 設定に戻る</a>

  <div class="card" style="margin-top:14px;">
    <div class="section-title">変更完了</div>

    <div class="note">
      パスワードを変更しました。
      <% if (userID != null && !userID.isBlank()) { %>
        <br>対象ユーザーID：<strong><%= userID %></strong>
      <% } %>
    </div>

    <div style="margin-top:12px;">
      <a class="btn" href="<%= request.getContextPath() %>/user/setting/menu">設定メニューへ</a>
    </div>
  </div>
</div>
</body>
</html>
