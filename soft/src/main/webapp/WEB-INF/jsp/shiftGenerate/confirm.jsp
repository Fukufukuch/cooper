<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>条件確認</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/app.css">
</head>
<body>

<div class="container">

  <div class="h1">オートシフタ</div>
  <div class="sub">条件確認</div>

  <div class="card" style="margin-top:24px;">
    <div class="section-title">入力された条件</div>
    <p class="section-desc">以下の条件でシフトを生成します。自動生成には数分かかる場合があります。</p>

    <div style="margin-top:20px; padding:16px; background:var(--soft); border-radius:var(--radius-sm);">
      <%-- prepare display values: convert minutes to hours+minutes --%>
      <%
        Object firstDateObj = request.getAttribute("firstDate");
        String firstDate = firstDateObj!=null? firstDateObj.toString():"";

        Integer daysVal = (request.getAttribute("days")!=null)? (Integer)request.getAttribute("days") : null;
        Integer maxMonthVal = (request.getAttribute("maxMonth")!=null)? (Integer)request.getAttribute("maxMonth") : null;
        Integer maxDayVal = (request.getAttribute("maxDay")!=null)? (Integer)request.getAttribute("maxDay") : null;
        Integer newcomerVal = (request.getAttribute("newcomerMinutes")!=null)? (Integer)request.getAttribute("newcomerMinutes") : null;
        Integer seniorReq = (request.getAttribute("seniorRequired")!=null)? (Integer)request.getAttribute("seniorRequired") : null;

        String formatHM = "";
      %>

      <div style="margin-bottom:12px;">
        <strong>開始日：</strong><%= firstDate %>
      </div>
      <div style="margin-bottom:12px;">
        <strong>生成日数：</strong><%= (daysVal!=null? daysVal : "") %> 日
      </div>
      <div style="margin-bottom:12px;">
        <strong>月労働時間上限：</strong>
        <%
          if (maxMonthVal!=null) {
              out.print((maxMonthVal/60) + " 時間 " + (maxMonthVal%60) + " 分");
          }
        %>
      </div>
      <div>
        <strong>日労働時間上限：</strong>
        <%
          if (maxDayVal!=null) {
              out.print((maxDayVal/60) + " 時間 " + (maxDayVal%60) + " 分");
          }
        %>
      </div>
      <div style="margin-top:12px;">
        <strong>新人判定時間：</strong>
        <%
          if (newcomerVal!=null) {
              out.print((newcomerVal/60) + " 時間 " + (newcomerVal%60) + " 分");
          }
        %>
      </div>
      <div style="margin-top:12px;">
        <strong>必要ベテラン人数：</strong><%= (seniorReq!=null? seniorReq : "") %>
      </div>

    <form action="<%= request.getContextPath() %>/shiftGenerate/generate" method="post" style="margin-top:24px;">
      <button type="submit" class="btn primary wide">シフトを生成する</button>
    </form>

    <div style="margin-top:16px;">
      <form action="<%= request.getContextPath() %>/shiftGenerate/setting.jsp" method="post">
        <button type="submit" class="btn ghost wide">条件設定を変更する</button>
      </form>
    </div>

    <div style="margin-top:16px;">
      <a href="<%= request.getContextPath() %>/shiftGenerate/index.jsp" class="btn ghost wide">シフト生成画面に戻る</a>
    </div>
  </div>

</div>

</body>
</html>