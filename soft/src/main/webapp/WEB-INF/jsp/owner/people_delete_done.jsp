<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  request.setAttribute("activeTab", "people");
  Boolean deleted = (Boolean) request.getAttribute("deleted");
  String userID = (String) request.getAttribute("userID");
%>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>削除結果</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/app.css">
</head>
<body>
<div class="container">
  <div class="h1">シフト自動生成システム</div>
  <div class="sub">スタッフ削除</div>

  <%@ include file="/WEB-INF/jsp/common/owner_tabs.jspf" %>

  <div class="card" style="margin-top:14px;">
    <div class="section-title">削除結果</div>

    <% if (deleted != null && deleted) { %>
      <div class="note">ユーザー <strong><%= userID %></strong> を削除しました。</div>
    <% } else { %>
      <div class="alert danger">削除に失敗しました（対象が存在しない/権限不一致など）。</div>
    <% } %>

    <a class="btn" href="<%= request.getContextPath() %>/owner/people" style="margin-top:10px; display:inline-block;">スタッフ一覧へ戻る</a>
  </div>
</div>
</body>
</html>
