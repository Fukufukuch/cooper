<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*, jp.ac.kochi.tech.soft.model.Shift" %>

<%
int year = (int) request.getAttribute("year");
int month = (int) request.getAttribute("month");
int daysInMonth = (int) request.getAttribute("daysInMonth");
int startDayOfWeek = (int) request.getAttribute("startDayOfWeek");
String userName = (String) request.getAttribute("userName");

Map<Integer, List<Shift>> shiftMap =
    (Map<Integer, List<Shift>>) request.getAttribute("shiftMap");

int prevYear = month == 1 ? year - 1 : year;
int prevMonth = month == 1 ? 12 : month - 1;
int nextYear = month == 12 ? year + 1 : year;
int nextMonth = month == 12 ? 1 : month + 1;

request.setAttribute("activeTab", "calendar");
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>カレンダー</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/calendar.css">
</head>
<body>

<div class="container">
  <div class="h1">オートシフタ</div>
  <div class="sub">カレンダー</div>

  <%@ include file="/WEB-INF/jsp/common/owner_tabs.jspf" %>

  <div class="card" style="margin-top:14px;">
    <div class="section-title"><%= year %>年 <%= month %>月</div>
    <% if (userName != null) { %>
      <div style="margin-bottom: 14px; font-size: 14px; color: #666;">ログイン中: <strong><%= userName %></strong></div>
    <% } %>

    <div style="display: flex; justify-content: center; gap: 20px; margin-bottom: 14px;">
      <a href="calendar?year=<%= prevYear %>&month=<%= prevMonth %>" class="btn">◀ 前月</a>
      <a href="calendar?year=<%= nextYear %>&month=<%= nextMonth %>" class="btn">次月 ▶</a>
    </div>

    <table class="calendar">
      <tr>
        <th>月</th><th>火</th><th>水</th><th>木</th><th>金</th>
        <th class="sat">土</th><th class="sun">日</th>
      </tr>

      <tr>
        <%
        int cell = 1;
        for (int i = 1; i < startDayOfWeek; i++) {
        %>
        <td class="empty"></td>
        <%
        cell++;
        }

        for (int day = 1; day <= daysInMonth; day++) {
        %>
        <td class="day">
          <div class="date"><%= day %></div>

          <%
          List<Shift> shifts = shiftMap.get(day);
          if (shifts != null) {
              for (Shift s : shifts) {
          %>
          <div class="shift"><%= s.getTimetable() %></div>
          <%
              }
          }
          %>
        </td>
        <%
        if (cell % 7 == 0) {
        %></tr><tr><%
        }
        cell++;
        }
        %>
      </tr>
    </table>
  </div>
</div>
</body>
</html>
