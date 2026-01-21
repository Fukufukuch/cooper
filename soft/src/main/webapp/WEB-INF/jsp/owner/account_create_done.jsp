<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  request.setAttribute("activeTab", "setting");
  String createdId = (String) request.getAttribute("createdId");
%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>作成完了</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/app.css">
</head>
<body>
<div class="container">
  <div class="h1">アカウント作成</div>
  <div class="sub">完了</div>

  <%@ include file="/WEB-INF/jsp/common/owner_tabs.jspf" %>

  <div class="card" style="margin-top:14px;">
    <div class="section-title">作成しました</div>
    <p class="section-desc">発行されたユーザーID：</p>
    <div style="font-size:22px; font-weight:900; margin-top:8px;"><%= createdId %></div>

    <div style="margin-top:16px;">
      <a class="btn" href="<%= request.getContextPath() %>/owner/setting">設定に戻る</a>
    </div>
  </div>
</div>
</body>
</html>
