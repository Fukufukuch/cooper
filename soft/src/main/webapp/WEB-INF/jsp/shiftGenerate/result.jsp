<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>生成されたシフト</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/app.css">
    <style>
        .shift-result-table { 
            border-collapse: collapse; 
            margin-bottom: 20px; 
            width: 100%;
            border: 1px solid var(--line);
        }
        .shift-result-table th, .shift-result-table td { 
            border: 1px solid var(--line); 
            padding: 8px 12px; 
            text-align: center; 
        }
        .shift-result-table th { 
            background-color: var(--soft);
            font-weight: 600;
        }
    </style>
</head>
<body>

<div class="container">

  <div class="h1">オートシフタ</div>
  <div class="sub">生成結果</div>

  <div class="card" style="margin-top:24px;">
    <div class="section-title">シフト生成完了</div>
    <p class="section-desc">以下のシフトが自動生成されました</p>

    <div style="margin-top:20px;">

<c:forEach var="entry" items="${shifts}">
    <c:set var="date" value="${entry.key}" />
    <c:set var="dayMap" value="${entry.value}" />

    <h3 style="margin-top:24px; margin-bottom:12px; font-size:18px; font-weight:700;">${date}</h3>
    <table class="shift-result-table">
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

    <div style="margin-top:32px;">
      <a href="<%= request.getContextPath() %>/shiftGenerate/index.jsp" class="btn primary wide">シフト生成画面に戻る</a>
    </div>

    <div style="margin-top:16px;">
      <a href="<%= request.getContextPath() %>/owner/shift/edit" class="btn ghost wide">シフト編集に戻る</a>
    </div>

    </div>
  </div>

</div>

</body>
</html>

