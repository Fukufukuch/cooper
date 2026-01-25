<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="app.dao.ShiftDao.ShiftRow" %>

<%
  request.setAttribute("activeTab", "shift");
  Integer year = (Integer)request.getAttribute("year");
  Integer month = (Integer)request.getAttribute("month");
  List<ShiftRow> rows = (List<ShiftRow>)request.getAttribute("rows");
%>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>シフト編集</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/app.css">
</head>
<body>

<div class="container">

  <div class="h1">オートシフタ</div>
  <div class="sub">シフト編集</div>

  <%@ include file="/WEB-INF/jsp/common/owner_tabs.jspf" %>

  <!-- ===== 一覧 ===== -->
  <div class="card" style="margin-top:14px;">
    <div class="section-title">今月のシフト（<%= year %>/<%= month %>）</div>
    <p class="section-desc">shift テーブルから表示（削除できます）</p>

    <% if (rows == null || rows.isEmpty()) { %>
      <div class="note">この月のシフトはありません。</div>
    <% } else { %>

      <table class="table">
        <thead>
        <tr>
          <th>ID</th>
          <th>ユーザーID</th>
          <th>氏名</th>
          <th>日付</th>
          <th>時間帯</th>
          <th style="width:120px;">操作</th>
        </tr>
        </thead>
        <tbody>
        <% for (ShiftRow r : rows) { %>
          <tr>
            <td><%= r.shiftID %></td>
            <td><%= r.userID %></td>
            <td><%= r.username %></td>
            <td><%= r.shiftInfoDay %></td>
            <td><%= r.shiftTimetable %></td>
            <td>
              <form method="post" action="<%= request.getContextPath() %>/owner/shift/delete" style="display:inline;">
                <input type="hidden" name="shiftID" value="<%= r.shiftID %>">
                <input type="hidden" name="year" value="<%= year %>">
                <input type="hidden" name="month" value="<%= month %>">
                <button class="btn danger" type="submit">削除</button>
              </form>
            </td>
          </tr>
        <% } %>
        </tbody>
      </table>

    <% } %>
  </div>

  <!-- ===== 追加 ===== -->
  <div class="card" style="margin-top:14px;">
    <div class="section-title">シフト追加</div>
    <p class="section-desc">（画面互換のため、時間帯は「早番/中番/遅番」の入力でOK）</p>

    <form method="post" action="<%= request.getContextPath() %>/owner/shift/add">
      <input type="hidden" name="year" value="<%= year %>">
      <input type="hidden" name="month" value="<%= month %>">

      <div class="form-row">
        <label>ユーザーID</label>
        <input class="input" type="text" name="userID" maxlength="10" required>
      </div>

      <div class="form-row">
        <label>日付</label>
        <input class="input" type="date" name="day" required>
      </div>

      <div class="form-row">
        <label>時間帯</label>
        <select class="input" name="timetable">
          <option value="早番">早番</option>
          <option value="中番">中番</option>
          <option value="遅番">遅番</option>
        </select>
      </div>

      <div class="form-row">
        <label>時間帯番号（任意）</label>
        <input class="input" type="number" name="timetableNumber" min="1" max="3" placeholder="1=早番,2=中番,3=遅番">
      </div>

      <div class="form-actions">
        <button class="btn" type="submit">追加</button>
      </div>
    </form>
  </div>

</div>
</body>
</html>
