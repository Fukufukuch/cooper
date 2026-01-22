<%@ page contentType="text/html; charset=UTF-8" %>
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
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>月間シフト編集</title>
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/calendar.css">
</head>
<body>

<div class="container">
<div class="header">
    <h1>オートシフタ</h1>
    <div class="user-info">ログインユーザー（管理者）: <strong><%= userName %></strong></div>
</div>

<%@ include file="/WEB-INF/jsp/common/owner_tabs.jspf" %>

<h2><%= year %>年 <%= month %>月</h2>

<div class="month-nav">
<a href="calendar?year=<%= prevYear %>&month=<%= prevMonth %>">◀</a>
<a href="calendar?year=<%= nextYear %>&month=<%= nextMonth %>">▶</a>
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

</body>
</html>
