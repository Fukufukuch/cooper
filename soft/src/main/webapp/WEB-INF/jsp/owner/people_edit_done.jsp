<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  request.setAttribute("activeTab", "people");
  String userID = (String) request.getAttribute("userID");
%>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>編集完了</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/app.css">
</head>
<body>
<div class="container">
  <div class="h1">シフト自動生成システム</div>
  <div class="sub">スタッフ編集</div>

  <%@ include file="/WEB-INF/jsp/common/owner_tabs.jspf" %>

  <div class="card" style="margin-top:14px;">
    <div class="section-title">更新完了</div>

    <div class="note">
      スタッフ情報を更新しました。
      <% if (userID != null && !userID.isBlank()) { %>
        <br>対象ユーザーID：<strong><%= userID %></strong>
      <% } %>
    </div>

    <div style="margin-top:12px;">
      <a class="btn" href="<%= request.getContextPath() %>/owner/people">スタッフ一覧へ</a>
      <a class="btn" href="<%= request.getContextPath() %>/owner/setting/menu" style="margin-left:8px;">設定へ</a>
    </div>
  </div>
</div>
</body>
</html>
