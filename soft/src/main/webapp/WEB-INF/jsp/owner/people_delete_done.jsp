<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  request.setAttribute("activeTab", "people");
%>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>削除完了</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/app.css">
</head>
<body>
<div class="container">
  <div class="page-title">シフト自動生成システム</div>
  <div class="sub">アカウント</div>

   <%@ include file="/WEB-INF/jsp/common/owner_tabs.jspf" %>

  <div class="card">
    <div class="card-title">削除完了（仮）</div>
    <div class="note">
      ユーザーID：<b><%= (id == null ? "-" : id) %></b> を削除した想定です（DB未接続）。
    </div>

    <div class="actions" style="margin-top:14px;">
      <a class="btn" href="<%= request.getContextPath() %>/owner/people">一覧へ戻る</a>
      <a class="btn ghost" href="<%= request.getContextPath() %>/owner/menu">メニュー</a>
    </div>
  </div>
</div>
</body>
</html>
