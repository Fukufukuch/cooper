<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="app.dao.HelpDao.HelpRow" %>

<%
  request.setAttribute("activeTab", "help");
  List<HelpRow> rows = (List<HelpRow>) request.getAttribute("rows");
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
  <div class="h1">オートシフタ</div>
  <div class="sub">承認待ち（ヘルプ）</div>

  <%@ include file="/WEB-INF/jsp/common/owner_tabs.jspf" %>

  <div class="card" style="margin-top:14px;">
    <div class="section-title">承認待ち一覧</div>
    <p class="section-desc">help(apply=1) を表示（承認/却下で状態更新）</p>

    <% if (rows == null || rows.isEmpty()) { %>
      <div class="note">承認待ちはありません。</div>
    <% } else { %>
      <table class="table">
        <thead>
          <tr>
            <th>helpID</th>
            <th>依頼者ID</th>
            <th>依頼者名</th>
            <th>応募者ID</th>
            <th>応募者名</th>
            <th>日付</th>
            <th>開始</th>
            <th>終了</th>
            <th>理由</th>
            <th style="width:170px;">操作</th>
          </tr>
        </thead>
        <tbody>
          <% for (HelpRow r : rows) { %>
            <tr>
              <td><%= r.helpID %></td>
              <td><%= r.helpWantUserID %></td>
              <td><%= r.wantUsername %></td>
              <td><%= r.helperUserID %></td>
              <td><%= r.helperUsername %></td>
              <td><%= r.day %></td>
              <td><%= r.start %></td>
              <td><%= r.end %></td>
              <td><%= r.reason %></td>
              <td>
                <form method="post" action="<%= request.getContextPath() %>/owner/help/approve" style="display:inline;">
                  <input type="hidden" name="helpID" value="<%= r.helpID %>">
                  <button class="btn" type="submit">承認</button>
                </form>

                <form method="post" action="<%= request.getContextPath() %>/owner/help/reject" style="display:inline; margin-left:6px;">
                  <input type="hidden" name="helpID" value="<%= r.helpID %>">
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
