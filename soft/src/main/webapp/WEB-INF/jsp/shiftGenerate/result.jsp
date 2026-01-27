<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<html>
<head>
    <title>シフト表</title>
    <style>
        table { border-collapse: collapse; margin-bottom: 20px; }
        th, td { border: 1px solid black; padding: 5px 10px; text-align: center; }
        th { background-color: #f0f0f0; }
    </style>
</head>
<body>

<c:forEach var="entry" items="${shifts}">
    <c:set var="date" value="${entry.key}" />
    <c:set var="dayMap" value="${entry.value}" />

    <h2>${date}</h2>
    <table>
        <tr>
            <th>Time Slot</th>
            <c:forEach var="pos" items="${positions}">
                <th>${pos.name}</th>
            </c:forEach>
        </tr>

        <c:forEach var="slot" items="${timeSlots}">
            <tr>
                <td>${slot.name}</td>
                <c:forEach var="pos" items="${positions}">
                    <td>
                        <c:set var="slotMap" value="${dayMap[slot]}" />
                        <c:choose>
                            <c:when test="${slotMap[pos] != null && !slotMap[pos].isEmpty()}">
                                <c:forEach var="id" items="${slotMap[pos]}" varStatus="status">
                                    ${id}<c:if test="${!status.last}">, </c:if>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>-</c:otherwise>
                        </c:choose>
                    </td>
                </c:forEach>
            </tr>
        </c:forEach>
    </table>
</c:forEach>

<a href="index.jsp">最初の画面に戻る</a>

</body>
</html>

