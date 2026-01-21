<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="jp.ac.kochi.tech.Shift" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<html>
<head>
    <title>シフト管理（管理者）</title>
</head>
<body>

<h2>シフト入力</h2>

<form action="adminShift" method="post">
    ユーザー：
    <select name="userID" required>
        <c:forEach var="u" items="${userList}">
            <option value="${u.userId}">
                ${u.userId} (${u.userName})
            </option>
        </c:forEach>
    </select><br>

    日付：
    <input type="date" name="workDate" required><br>

    開始（時間帯）：
    <select name="timetable" required>
        <option value="01">01</option>
        <option value="02">02</option>
        <option value="03">03</option>
        <option value="04">04</option>
    </select><br>

    <button type="submit">追加</button>
</form>

<hr>

<h2>シフト一覧</h2>

<%
    List<Shift> list = (List<Shift>) request.getAttribute("shiftList");
    if (list != null) {
        for (Shift s : list) {
%>
    <p>
        ユーザーID：<%= s.getUserID() %> /
        日付：<%= s.getWorkDate() %> /
        時間帯：<%= s.getStartTime() %>
    </p>
<%
        }
    }
%>

<hr>

<h2>シフト修正</h2>
<form action="<%= request.getContextPath() %>/ShiftEditListServlet" method="get">
    <button type="submit">シフト修正一覧へ</button>
</form>

</body>
</html>
