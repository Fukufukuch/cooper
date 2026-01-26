<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="java.time.*" %>
<%@ page import="app.dao.ShiftDao.ShiftRow" %>



<%
  request.setAttribute("activeTab", "shift");
  Integer year = (Integer)request.getAttribute("year");
  Integer month = (Integer)request.getAttribute("month");
  List<ShiftRow> rows = (List<ShiftRow>)request.getAttribute("rows");
  
  // シフト情報をマップに整理（日付 + ユーザーID → シフト情報）
  LocalDate startDate = LocalDate.of(year, month, 1);
  LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
  
  Map<String, Map<String, List<ShiftRow>>> shiftMap = new TreeMap<>();
  Map<String, String> userIdToName = new LinkedHashMap<>();
  Set<String> userSet = new TreeSet<>();
  
  if (rows != null && !rows.isEmpty()) {
    for (ShiftRow r : rows) {
      String dateKey = r.shiftInfoDay.toString();
      String userKey = r.userID;
      
      shiftMap.computeIfAbsent(dateKey, k -> new TreeMap<>())
              .computeIfAbsent(userKey, k -> new ArrayList<>())
              .add(r);
      userSet.add(userKey);
      userIdToName.put(userKey, r.username);
    }
  }
%>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>シフト編集</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/app.css">
  <style>
    .shift-table-wrapper {
      overflow-x: auto;
      margin: 0 -6px;
      padding: 0 6px;
    }
    .shift-calendar {
      width: 100%;
      border-collapse: collapse;
      font-size: 11px;
      min-width: 600px;
    }
    .shift-calendar thead {
      background-color: #f0f0f0;
      position: sticky;
      top: 0;
    }
    .shift-calendar th {
      border: 1px solid #ccc;
      padding: 6px 4px;
      font-weight: bold;
      text-align: center;
      white-space: nowrap;
    }
    .shift-calendar td {
      border: 1px solid #ccc;
      padding: 4px;
      vertical-align: top;
      height: 80px;
      overflow-y: auto;
      font-size: 10px;
    }
    .shift-calendar tbody tr:nth-child(odd) {
      background-color: #fafafa;
    }
    .shift-user-cell {
      font-weight: bold;
      color: #333;
      text-align: center;
      white-space: nowrap;
      width: 80px;
      min-width: 80px;
    }
    .shift-date-header {
      font-weight: bold;
      color: #333;
      min-width: 50px;
    }
    .shift-item {
      background-color: #e3f2fd;
      border-left: 3px solid #1976d2;
      padding: 3px;
      margin-bottom: 2px;
      border-radius: 2px;
      font-size: 9px;
      word-wrap: break-word;
      word-break: break-all;
    }
    .shift-user {
      font-size: 9px;
      color: #666;
      margin-bottom: 1px;
    }
    .shift-time {
      font-size: 9px;
      color: #1976d2;
      font-weight: bold;
      margin-bottom: 1px;
    }
    .shift-delete-btn {
      font-size: 8px;
      padding: 1px 3px;
      margin-top: 1px;
      width: 100%;
    }
  </style>
</head>
<body>

<div class="container">

  <div class="h1">オートシフタ</div>
  <div class="sub">シフト編集</div>

  <%@ include file="/WEB-INF/jsp/common/owner_tabs.jspf" %>

  <!-- ===== 一覧（表形式） ===== -->
  <div class="card" style="margin-top:14px;">
    <div class="section-title">今月のシフト（<%= year %>/<%= month %>）</div>
    <p class="section-desc">エクセル形式で表示（削除できます）</p>

    <% if (rows == null || rows.isEmpty()) { %>
      <div class="note">この月のシフトはありません。</div>
    <% } else { %>

      <div class="shift-table-wrapper">
        <table class="shift-calendar">
          <thead>
            <tr>
              <th style="min-width: 80px;">ユーザー名</th>
              <% 
                for (LocalDate d = startDate; !d.isAfter(endDate); d = d.plusDays(1)) {
                  int dayOfMonth = d.getDayOfMonth();
                  String dayOfWeekStr = d.getDayOfWeek().toString().substring(0, 3);
              %>
                <th style="min-width: 60px;">
                  <div class="shift-date-header"><%= dayOfMonth %></div>
                  <div style="font-size: 8px; color: #999;">(<%= dayOfWeekStr %>)</div>
                </th>
              <% } %>
            </tr>
          </thead>
          <tbody>
            <% for (String userId : userSet) { %>
              <tr>
                <td class="shift-user-cell"><%= userIdToName.get(userId) %></td>
                <% 
                  for (LocalDate d = startDate; !d.isAfter(endDate); d = d.plusDays(1)) {
                    String dateKey = d.toString();
                %>
                  <td>
                    <% 
                      Map<String, List<ShiftRow>> userShifts = shiftMap.get(dateKey);
                      if (userShifts != null && userShifts.containsKey(userId)) {
                        for (ShiftRow r : userShifts.get(userId)) {
                    %>
                      <div class="shift-item">
                        <div class="shift-user"><%= r.username %></div>
                        <div class="shift-time"><%= r.shiftTimetable %></div>
                        <form method="post" action="<%= request.getContextPath() %>/owner/shift/delete" style="margin-top: 1px;">
                          <input type="hidden" name="shiftID" value="<%= r.shiftID %>">
                          <input type="hidden" name="year" value="<%= year %>">
                          <input type="hidden" name="month" value="<%= month %>">
                          <button class="btn danger shift-delete-btn" type="submit">削除</button>
                        </form>
                      </div>
                    <% 
                        }
                      }
                    %>
                  </td>
                <% } %>
              </tr>
            <% } %>
          </tbody>
        </table>
      </div>

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