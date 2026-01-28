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

    <form action="<%= request.getContextPath() %>/shiftGenerate/confirm" method="post" style="margin-top:20px;">
      
      <div class="form-group">
        <label>開始日</label>
        <input type="date" name="firstDate" class="form-control" value="<%= (request.getAttribute("firstDate")!=null)? request.getAttribute("firstDate").toString() : "" %>" required>
      </div>

      <div class="form-group">
        <label>生成日数</label>
        <input type="number" name="generateDays" value="<%= (request.getAttribute("generateDays")!=null)? request.getAttribute("generateDays") : 7 %>" class="form-control" required>
      </div>

      <div class="form-group">
        <label>月最大労働時間（分）</label>
        <input type="number" name="maxWorkMonth" value="<%= (request.getAttribute("maxWorkMonth")!=null)? request.getAttribute("maxWorkMonth") : 8000 %>" class="form-control" required>
      </div>

      <div class="form-group">
        <label>日最大労働時間（分）</label>
        <input type="number" name="maxWorkDay" value="<%= (request.getAttribute("maxWorkDay")!=null)? request.getAttribute("maxWorkDay") : 720 %>" class="form-control" required>
      </div>

      <div class="form-group">
        <label>新人判定時間（分）</label>
        <input type="number" name="newcomerMinutes" value="<%= (request.getAttribute("newcomerMinutes")!=null)? request.getAttribute("newcomerMinutes") : 5000 %>" class="form-control" required>
      </div>

      <div class="form-group">
        <label>必要ベテラン人数</label>
        <input type="number" name="seniorRequired" value="<%= (request.getAttribute("seniorRequired")!=null)? request.getAttribute("seniorRequired") : 1 %>" class="form-control" required>
      </div>

      <button type="submit" class="btn primary wide" style="margin-top:20px;">次へ進む</button>
    </form>

    <div style="margin-top:16px;">
      <a href="<%= request.getContextPath() %>/shiftGenerate/confirm.jsp" class="btn ghost wide">保存せずに戻る</a>
    </div>
  </div>

</div>

</body>
</html>
