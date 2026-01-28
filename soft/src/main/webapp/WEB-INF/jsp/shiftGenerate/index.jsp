<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>シフト自動生成</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/app.css">
</head>
<body>

<div class="container">

  <div class="h1">オートシフタ</div>
  <div class="sub">シフト自動生成</div>

  <div class="card" style="margin-top:24px;">
    <div class="section-title">シフト生成条件の設定</div>
    <p class="section-desc">シフトを自動生成するための条件を設定します</p>

    <form action="<%= request.getContextPath() %>/shiftGenerate/setting.jsp" method="post" style="margin-top:20px;">
      <button type="submit" class="btn primary wide">シフト生成条件設定</button>
    </form>

    <div style="margin-top:20px;">
      <a href="<%= request.getContextPath() %>/owner/shift/edit" class="btn ghost wide">シフト編集に戻る</a>
    </div>
  </div>

</div>

</body>
</html>
