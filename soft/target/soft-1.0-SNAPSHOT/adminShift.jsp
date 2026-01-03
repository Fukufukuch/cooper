<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*" %>

<html>
<head>
    <title>シフト管理</title>
</head>
<body>

<h2>シフト入力</h2>

<form action="adminShift" method="post">
    名前：<input type="text" name="name"><br>
    日付：<input type="date" name="date"><br>
    時間：<input type="text" name="time"><br>
    <button type="submit">追加</button>
</form>

<hr>

<h2>シフト一覧（仮保存）</h2>

<%
    List<Map<String, String>> list =
        (List<Map<String, String>>) request.getAttribute("shiftList");

    if (list != null) {
        for (Map<String, String> s : list) {
%>
    <p>
        名前：<%= s.get("name") %> /
        日付：<%= s.get("date") %> /
        時間：<%= s.get("time") %>
    </p>
<%
        }
    }
%>

</body>
</html>
