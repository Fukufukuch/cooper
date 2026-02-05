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

  <%@ include file="/WEB-INF/jsp/common/user_tabs.jspf" %>

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

  <%
    List<Map<String, Object>> approvedHelps =
      (List<Map<String, Object>>) request.getAttribute("approvedHelps");
    String userId = (String) session.getAttribute("userID");
    if (approvedHelps != null && !approvedHelps.isEmpty()) {
  %>
    <div class="card" style="margin-top:20px;">
      <div class="section-title">承認済みヘルプ一覧</div>
       <% for (Map<String, Object> h : approvedHelps) {
         Integer helpID = (Integer) h.get("helpID");
           String wantUserID = (String) h.get("wantUserID");
           String wantUsername = (String) h.get("wantUsername");
           String helperUserID = (String) h.get("helperUserID");
           String helperUsername = (String) h.get("helperUsername");
           
           java.sql.Date wantShiftDate = (java.sql.Date) h.get("wantShiftDate");
           Object wantStartMinObj = h.get("wantStartMin");
           Object wantEndMinObj = h.get("wantEndMin");
           
           java.sql.Date helperShiftDate = (java.sql.Date) h.get("helperShiftDate");
           Object helperStartMinObj = h.get("helperStartMin");
           Object helperEndMinObj = h.get("helperEndMin");
           
           String wantTimeStr = "";
           String helperTimeStr = "";
           
           if (wantStartMinObj != null && wantEndMinObj != null) {
             int wantStart = ((Number) wantStartMinObj).intValue();
             int wantEnd = ((Number) wantEndMinObj).intValue();
             wantTimeStr = String.format("%02d:%02d-%02d:%02d",
               wantStart / 60, wantStart % 60, wantEnd / 60, wantEnd % 60);
           }
           
           if (helperStartMinObj != null && helperEndMinObj != null) {
             int helperStart = ((Number) helperStartMinObj).intValue();
             int helperEnd = ((Number) helperEndMinObj).intValue();
             helperTimeStr = String.format("%02d:%02d-%02d:%02d",
               helperStart / 60, helperStart % 60, helperEnd / 60, helperEnd % 60);
           }
           
           String wantShiftStr = (wantShiftDate != null && !wantTimeStr.isEmpty())
             ? wantShiftDate + " " + wantTimeStr
             : "シフト情報なし";
           String helperShiftStr = (helperShiftDate != null && !helperTimeStr.isEmpty())
             ? helperShiftDate + " " + helperTimeStr
             : "シフト情報なし";
           
           boolean isRequester = userId != null && userId.equals(wantUserID);
           boolean isResponder = userId != null && userId.equals(helperUserID);
           String yourShiftStr = isRequester ? wantShiftStr : helperShiftStr;
           String roleParam = isRequester ? "want" : "helper";
      %>
      <div style="background: white; border: 1px solid #d9d9d9; border-radius: 8px; padding: 14px; margin-bottom: 10px; box-shadow: 0 1px 3px rgba(0,0,0,0.08);">
        <div style="display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 10px;">
          <div>
            <div style="margin-bottom: 8px;">
              <span style="display: inline-block; background: #e3f2fd; color: #1976d2; padding: 3px 8px; border-radius: 4px; font-size: 12px; font-weight: bold; margin-right: 6px;">募集者</span>
              <span style="font-weight: bold; color: #333;"><%= isRequester ? "あなた" : wantUsername %></span>
            </div>
            <div>
              <span style="display: inline-block; background: #f3e5f5; color: #7b1fa2; padding: 3px 8px; border-radius: 4px; font-size: 12px; font-weight: bold; margin-right: 6px;">応答者</span>
              <span style="font-weight: bold; color: #333;"><%= isResponder ? "あなた" : helperUsername %></span>
            </div>
          </div>
          <div style="text-align: right; color: #666; font-size: 12px;">
            シフト変更
          </div>
        </div>
        <hr style="margin: 10px 0; border: none; border-top: 1px solid #f0f0f0;">
        <div style="background: #fafafa; padding: 10px; border-radius: 6px; margin-bottom: 10px;">
          <div style="margin-bottom: 6px;">
            <span style="color: #666; font-size: 13px;">
              <strong>変更前：</strong>募集者のシフト
            </span>
            <div style="color: #333; font-size: 14px; font-weight: 500; margin-left: 10px;"><%= wantShiftStr %></div>
          </div>
          <div>
            <span style="color: #666; font-size: 13px;">
              <strong>変更後：</strong>応答者のシフト
            </span>
            <div style="color: #333; font-size: 14px; font-weight: 500; margin-left: 10px;"><%= helperShiftStr %></div>
          </div>
          <div style="margin-top: 8px; padding-top: 8px; border-top: 1px dashed #e0e0e0;">
            <span style="color: #666; font-size: 13px;">
              <strong>変更されたあなたのシフト：</strong>
            </span>
            <div style="color: #333; font-size: 14px; font-weight: 600; margin-left: 10px;"><%= yourShiftStr %></div>
          </div>
        </div>
        <div style="text-align: right;">
          <form method="post" action="<%= request.getContextPath() %>/user/help/ack" style="display:inline;">
            <input type="hidden" name="helpID" value="<%= helpID %>">
            <input type="hidden" name="role" value="<%= roleParam %>">
            <button type="submit" style="background: #f5f5f5; border: 1px solid #d0d0d0; padding: 6px 16px; border-radius: 4px; cursor: pointer; font-size: 12px; font-weight: 500; color: #666; transition: all 0.2s;" onmouseover="this.style.background='#e8e8e8'" onmouseout="this.style.background='#f5f5f5'">
              確認済み
            </button>
          </form>
        </div>
      </div>
      <% } %>
    </div>
  <% } %>
</div>
</body>
</html>
