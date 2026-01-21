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
      <div class="note">承認待ちはありません。</div>
    <% } else { %>

      <table class="table">
        <thead>
          <tr>
            <th>申請ID</th>
            <th>ユーザーID</th>
            <th>氏名</th>
            <th>日付</th>
            <th>開始</th>
            <th>終了</th>
            <th style="width:170px;">操作</th>
          </tr>
        </thead>
        <tbody>
          <% for (RequestRow r : rows) { %>
            <tr>
              <td><%= r.requestID %></td>
              <td><%= r.userID %></td>
              <td><%= r.username %></td>
              <td><%= r.day %></td>
              <td><%= r.start %></td>
              <td><%= r.end %></td>
              <td>
                <form method="post" action="<%= request.getContextPath() %>/owner/help/approve" style="display:inline;">
                  <input type="hidden" name="requestID" value="<%= r.requestID %>">
                  <button class="btn" type="submit">承認</button>
                </form>

                <form method="post" action="<%= request.getContextPath() %>/owner/help/reject" style="display:inline; margin-left:6px;">
                  <input type="hidden" name="requestID" value="<%= r.requestID %>">
                  <button class="btn danger" type="submit">却下</button>
                </form>
              </td>
            </tr>
          <% } %>
        </tbody>
      </table>

    <% } %>
  </div>
</div>
</body>
</html>
