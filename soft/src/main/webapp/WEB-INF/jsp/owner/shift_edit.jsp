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

  <div class="h1">シフト自動生成システム</div>
  <div class="sub">シフト編集</div>

  <%@ include file="/WEB-INF/jsp/common/owner_tabs.jspf" %>

  <!-- ===== 一覧 ===== -->
  <div class="card" style="margin-top:14px;">
    <div class="section-title">今月のシフト（<%= year %>/<%= month %>）</div>
    <p class="section-desc">shift テーブルから表示しています。</p>

    <% if (rows == null || rows.isEmpty()) { %>
      <div class="note">まだシフトがありません</div>
    <% } else { %>

      <% for (ShiftRow r : rows) { %>
        <div class="action-card">
          <div>
            <div style="font-weight:800;">
              <%= r.shiftInfoDay %> ／ <%= r.shiftTimetable %>
              <% if (r.shiftTimetableNumber != null) { %>
                （<%= r.shiftTimetableNumber %>）
              <% } %>
            </div>
            <div class="note">
              shiftID: <%= r.shiftID %> ／ <%= r.userID %> ／ <%= r.username %>
            </div>
          </div>

          <form method="post"
                action="<%= request.getContextPath() %>/owner/shift/delete"
                style="margin:0;">
            <input type="hidden" name="shiftID" value="<%= r.shiftID %>">
            <input type="hidden" name="year" value="<%= year %>">
            <input type="hidden" name="month" value="<%= month %>">

            <button class="btn" type="submit"
              onclick="return confirm('このシフトを削除しますか？');">
              削除
            </button>
          </form>
        </div>
      <% } %>

    <% } %>
  </div>

  <!-- ===== 追加 ===== -->
  <div class="card" style="margin-top:14px;">
    <div class="section-title">シフト追加</div>
    <p class="section-desc">日付はカレンダーから選択できます。</p>

    <form class="form"
          method="post"
          action="<%= request.getContextPath() %>/owner/shift/add">

      <input type="hidden" name="year" value="<%= year %>">
      <input type="hidden" name="month" value="<%= month %>">

      <div style="margin-top:10px;">
        <div class="note">ユーザーID</div>
        <input class="input" name="userID" required maxlength="10"
               placeholder="U000000001">
      </div>

      <div style="margin-top:10px;">
        <div class="note">日付</div>
        <input class="input" type="date" name="day" required>
      </div>

      <div style="margin-top:10px;">
        <div class="note">勤務区分</div>
        <input class="input" name="timetable" placeholder="早番">
      </div>

      <div style="margin-top:10px;">
        <div class="note">勤務番号</div>
        <input class="input" name="timetableNumber" placeholder="1">
      </div>

      <div style="margin-top:14px;">
        <button class="btn" type="submit">追加</button>
      </div>
    </form>
  </div>

</div>
</body>
</html>
