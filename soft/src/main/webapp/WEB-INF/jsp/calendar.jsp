<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%
    int year = (int) request.getAttribute("year");
    int month = (int) request.getAttribute("month");
    List<Integer> days = (List<Integer>) request.getAttribute("days");
    int startDayOfWeek = (int) request.getAttribute("startDayOfWeek");

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

    <!-- ===== ヘッダー ===== -->
<header class="header">
    <h1>シフト自動生成システム</h1>
    <nav class="menu">
        <button>📅 カレンダー</button>
        <button class="active">📝 シフト情報入力</button>
        <button>🤝 ヘルプ募集</button>
        <button>👤 スタッフ</button>
        <button>⚙ 設定</button>
    </nav>
</header>

<div class="container">
    <h1>管理者ダッシュボード</h1>
    <h2>月間シフト編集</h2>

    <div class="month-nav">
        <a href="calendar?year=<%= prevYear %>&month=<%= prevMonth %>">◀ 前の月</a>
        <span><%= year %>年 <%= month %>月</span>
        <a href="calendar?year=<%= nextYear %>&month=<%= nextMonth %>">次の月 ▶</a>
    </div>

    <table class="calendar">
        <tr>
            <th>月</th>
            <th>火</th>
            <th>水</th>
            <th>木</th>
            <th>金</th>
            <th class="sat">土</th>
            <th class="sun">日</th>
        </tr>

        <tr>
        <%
            int cellCount = 1;

            for (int i = 1; i < startDayOfWeek; i++) {
        %>
            <td class="empty"></td>
        <%
                cellCount++;
            }

            for (int day : days) {
        %>
            <td class="day">
                <div class="date"><%= day %></div>
            </td>
        <%
                if (cellCount % 7 == 0) {
        %>
        </tr><tr>
        <%
                }
                cellCount++;
            }
        %>
        </tr>
    </table>
</div>

</body>
</html>
