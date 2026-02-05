<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>シフト生成条件設定</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/app.css">
</head>
<body>

<div class="container">

  <div class="h1">オートシフタ</div>
  <div class="sub">シフト生成条件設定</div>

  <div class="card" style="margin-top:24px;">
    <div class="section-title">条件入力</div>
    <p class="section-desc">シフト生成に必要な条件を入力してください</p>

    <form id="settingForm" action="<%= request.getContextPath() %>/shiftGenerate/confirm" method="post" style="margin-top:20px;">
      
      <div class="form-group">
        <label>開始日</label>
        <input type="date" name="firstDate" class="form-control" value="<%= (request.getAttribute("firstDate")!=null)? request.getAttribute("firstDate").toString() : "" %>" required>
      </div>

      <div class="form-group">
        <label>生成日数</label>
        <input type="number" name="generateDays" value="<%= (request.getAttribute("generateDays")!=null)? request.getAttribute("generateDays") : 7 %>" class="form-control" required>
      </div>

      <%-- compute default hour/min values from minutes --%>
      <%
        Integer maxWorkMonth = (request.getAttribute("maxWorkMonth")!=null)? (Integer)request.getAttribute("maxWorkMonth") : 8000;
        Integer maxWorkDay = (request.getAttribute("maxWorkDay")!=null)? (Integer)request.getAttribute("maxWorkDay") : 720;
        Integer newcomerMinutes = (request.getAttribute("newcomerMinutes")!=null)? (Integer)request.getAttribute("newcomerMinutes") : 5000;

        int maxWorkMonthH = maxWorkMonth / 60;
        int maxWorkMonthM = maxWorkMonth % 60;
        int maxWorkDayH = maxWorkDay / 60;
        int maxWorkDayM = maxWorkDay % 60;
        int newcomerH = newcomerMinutes / 60;
        int newcomerM = newcomerMinutes % 60;
      %>

      <div class="form-group">
        <label>月最大労働時間</label>
        <div style="display:flex; gap:8px;">
          <input type="number" id="maxWorkMonthHours" min="0" value="<%= maxWorkMonthH %>" class="form-control" style="width:120px;"> <span style="align-self:center">時間</span>
          <input type="number" id="maxWorkMonthMins" min="0" max="59" value="<%= maxWorkMonthM %>" class="form-control" style="width:120px;"> <span style="align-self:center">分</span>
        </div>
        <input type="hidden" name="maxWorkMonth" id="maxWorkMonthHidden" value="<%= maxWorkMonth %>">
      </div>

      <div class="form-group">
        <label>日最大労働時間</label>
        <div style="display:flex; gap:8px;">
          <input type="number" id="maxWorkDayHours" min="0" value="<%= maxWorkDayH %>" class="form-control" style="width:120px;"> <span style="align-self:center">時間</span>
          <input type="number" id="maxWorkDayMins" min="0" max="59" value="<%= maxWorkDayM %>" class="form-control" style="width:120px;"> <span style="align-self:center">分</span>
        </div>
        <input type="hidden" name="maxWorkDay" id="maxWorkDayHidden" value="<%= maxWorkDay %>">
      </div>

      <div class="form-group">
        <label>新人判定時間</label>
        <div style="display:flex; gap:8px;">
          <input type="number" id="newcomerHours" min="0" value="<%= newcomerH %>" class="form-control" style="width:120px;"> <span style="align-self:center">時間</span>
          <input type="number" id="newcomerMins" min="0" max="59" value="<%= newcomerM %>" class="form-control" style="width:120px;"> <span style="align-self:center">分</span>
        </div>
        <input type="hidden" name="newcomerMinutes" id="newcomerHidden" value="<%= newcomerMinutes %>">
      </div>

      <div class="form-group">
        <label>必要ベテラン人数</label>
        <input type="number" name="seniorRequired" value="<%= (request.getAttribute("seniorRequired")!=null)? request.getAttribute("seniorRequired") : 1 %>" class="form-control" required>
      </div>

      <button type="submit" class="btn primary wide" style="margin-top:20px;">次へ進む</button>
    </form>

    <script>
      (function(){
        var form = document.getElementById('settingForm');
        form.addEventListener('submit', function(){
          // combine hours and minutes into hidden minute fields
          var mh = parseInt(document.getElementById('maxWorkMonthHours').value||0,10);
          var mm = parseInt(document.getElementById('maxWorkMonthMins').value||0,10);
          document.getElementById('maxWorkMonthHidden').value = (mh*60 + mm);

          var dh = parseInt(document.getElementById('maxWorkDayHours').value||0,10);
          var dm = parseInt(document.getElementById('maxWorkDayMins').value||0,10);
          document.getElementById('maxWorkDayHidden').value = (dh*60 + dm);

          var nh = parseInt(document.getElementById('newcomerHours').value||0,10);
          var nm = parseInt(document.getElementById('newcomerMins').value||0,10);
          document.getElementById('newcomerHidden').value = (nh*60 + nm);
        });
      })();
    </script>
    </form>

    <div style="margin-top:16px;">
      <a href="<%= request.getContextPath() %>/shiftGenerate/confirm.jsp" class="btn ghost wide">保存せずに戻る</a>
    </div>
  </div>

</div>

</body>
</html>
