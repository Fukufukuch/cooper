<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  request.setAttribute("activeTab", "setting");
%>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>設定</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/app.css">
</head>
<body>
<div class="container">
  <div class="h1">オートシフタ</div>
  <div class="sub">設定</div>

  <%@ include file="/WEB-INF/jsp/common/owner_tabs.jspf" %>

  <div class="card" style="margin-top:14px;">
    <div class="section-title">設定メニュー</div>

    <div class="menu">
      <a class="menu-item" href="<%= request.getContextPath() %>/owner/people">スタッフ一覧</a>
      <a class="menu-item" href="<%= request.getContextPath() %>/owner/account/create">スタッフ作成</a>
      <a class="menu-item" href="<%= request.getContextPath() %>/owner/password/change">パスワード変更</a>
    </div>
  </div>
</div>
</body>
</html>
