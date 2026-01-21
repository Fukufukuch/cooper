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
<style>
    /* ===== ヘッダー ===== */
    .header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 20px;
        border-bottom: 2px solid #0066cc;
        padding-bottom: 10px;
    }

    .header-left {
        display: flex;
        flex-direction: column;
    }

    .header h1 {
        margin: 0;
        color: #0066cc;
        font-size: 24px;
    }

    .header .user-info {
        font-size: 14px;
        color: #666;
        margin-top: 5px;
    }

    .header .user-info strong {
        color: #0066cc;
    }

    .menu {
        display: inline-flex;
        background: #f1f1f1;
        border-radius: 999px;
        padding: 6px;
        gap: 4px;
    }

    .menu button {
        border: none;
        background: transparent;
        padding: 10px 18px;
        border-radius: 999px;
        cursor: pointer;
        font-size: 14px;
        color: #555;
        transition: all 0.2s;
    }

    .menu button.active,
    .menu button:hover {
        background: #ffffff;
        color: #000;
        box-shadow: 0 2px 6px rgba(0,0,0,0.08);
    }
</style>
</head>
<body>

<div class="container">
<div class="header">
    <div class="header-left">
        <h1>オートシフタ</h1>
        <div class="user-info">ログインユーザー（労働者）: <strong><%= userName %></strong></div>
    </div>
    <nav class="menu">
        <button class="active">📅 カレンダー</button>
        <button>📝 シフト情報入力</button>
        <button>🤝 ヘルプ募集</button>
        <button>👤 スタッフ</button>
        <button>⚙ 設定</button>
    </nav>
</div>

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
