<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="app.dao.RequestDao.RequestRow" %>

<%
  request.setAttribute("activeTab", "help");
  List<RequestRow> rows = (List<RequestRow>) request.getAttribute("rows");
%>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>承認待ち</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/app.css">
</head>
<body>
<div class="container">
  <div class="h1">シフト自動生成システム</div>
  <div class="sub">承認待ち</div>

  <%@ include file="/WEB-INF/jsp/common/owner_tabs.jspf" %>

  <div class="card" style="margin-top:14px;">
    <div class="section-title">承認待ち一覧</div>
    <p class="section-desc">request から表示（承認すると shift に反映、却下は削除）</p>

    <% if (rows == null || rows.isEmpty()) { %>
      <div class="note">承認待ちはありません</div>
    <% } else { %>

      <% for (RequestRow r : rows) { %>
        <div class="action-card">
          <div>
            <div style="font-weight:800;">
              <%= r.day %>　<%= r.start %>〜<%= r.end %>
            </div>
            <div class="note">
              requestID: <%= r.requestID %> / <%= r.userID %> / <%= r.username %>
            </div>
          </div>

          <div style="display:flex; gap:8px;">
            <form method="post" action="<%= request.getContextPath() %>/owner/help/approve" style="margin:0;">
              <input type="hidden" name="requestID" value="<%= r.requestID %>">
              <button class="btn" type="submit">承認</button>
            </form>

            <form method="post" action="<%= request.getContextPath() %>/owner/help/reject" style="margin:0;">
              <input type="hidden" name="requestID" value="<%= r.requestID %>">
              <button class="btn" type="submit"
                onclick="return confirm('却下して削除しますか？');">却下</button>
            </form>
          </div>
        </div>
      <% } %>

    <% } %>
  </div>

</div>
</body>
</html>
