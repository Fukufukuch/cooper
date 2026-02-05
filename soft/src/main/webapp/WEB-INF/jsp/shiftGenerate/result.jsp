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

        <c:if test="${not empty warnings}">
            <div style="margin-top:12px; padding:12px; border:1px solid #f5c26b; background:#fff8e6;">
                <strong>警告:</strong>
                <ul style="margin-top:8px;">
                    <c:forEach var="w" items="${warnings}">
                        <li>${w}</li>
                    </c:forEach>
                </ul>
            </div>
        </c:if>

        <c:if test="${not empty shortageSummary}">
            <div style="margin-top:12px; padding:12px; border:1px solid #f0d0b0; background:#fffaf0;">
                <strong>不足サマリ (日付 × 時間帯):</strong>
                <table style="width:100%; margin-top:8px; border-collapse:collapse;">
                    <tr style="background:#f5f5f5;"><th style="padding:6px;border:1px solid #eee; text-align:left;">日付</th><th style="padding:6px;border:1px solid #eee; text-align:left;">時間帯</th><th style="padding:6px;border:1px solid #eee; text-align:right;">件数</th></tr>
                    <c:forEach var="s" items="${shortageSummary}">
                        <tr>
                            <td style="padding:6px;border:1px solid #eee;"><c:out value="${s.date}"/></td>
                            <td style="padding:6px;border:1px solid #eee;"><c:out value="${s.timeSlot}"/></td>
                            <td style="padding:6px;border:1px solid #eee; text-align:right;"><c:out value="${s.count}"/></td>
                        </tr>
                    </c:forEach>
                </table>
            </div>
        </c:if>

        <c:if test="${not empty shortageSlots}">
            <div style="margin-top:12px; padding:12px; border:1px solid #f8bdbd; background:#fff5f5;">
                <strong>不足枠:</strong>
                <ul style="margin-top:8px;">
                    <c:forEach var="s" items="${shortageSlots}">
                        <li>
                            日付: <c:out value="${s.date}"/> ・
                            時間帯: <c:out value="${s.timeSlot != null ? s.timeSlot.name : '—'}"/> ・
                            ポジション: <c:out value="${s.position != null ? s.position.name : '—'}"/> ・
                            必要: <c:out value="${s.required}"/> ・ 割当: <c:out value="${s.assigned}"/> ・ 種別: <c:out value="${s.shortageType}"/>
                        </li>
                    </c:forEach>
                </ul>
            </div>
        </c:if>

        <c:if test="${not empty warningSlots}">
            <div style="margin-top:12px; padding:12px; border:1px solid #f0d9ff; background:#fbf8ff;">
                <strong>警告枠一覧:</strong>
                <ul style="margin-top:8px;">
                    <c:forEach var="wslot" items="${warningSlots}">
                        <li>
                            日付: <c:out value="${wslot.date}"/> ・
                            時間帯: <c:out value="${wslot.timeSlot != null ? wslot.timeSlot.name : '—'}"/> ・
                            ポジション: <c:out value="${wslot.position != null ? wslot.position.name : '—'}"/> ・
                            種別: <c:out value="${wslot.warningType}"/> ・ タグ: <c:out value="${wslot.nonconformTag}"/>
                            <c:if test="${not empty wslot.warningWorkers}">
                                <div>該当者:
                                    <c:forEach var="wid" items="${wslot.warningWorkers}" varStatus="vs">
                                        <c:choose>
                                            <c:when test="${not empty usernames[wid]}">${usernames[wid]}</c:when>
                                            <c:otherwise>${wid}</c:otherwise>
                                        </c:choose>
                                        <c:if test="${!vs.last}">, </c:if>
                                    </c:forEach>
                                </div>
                            </c:if>
                        </li>
                    </c:forEach>
                </ul>
            </div>
        </c:if>

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
                                    <c:choose>
                                        <c:when test="${not empty usernames[id]}">
                                            ${usernames[id]}
                                        </c:when>
                                        <c:otherwise>
                                            ${id}
                                        </c:otherwise>
                                    </c:choose>
                                    <c:if test="${!status.last}">, </c:if>
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

