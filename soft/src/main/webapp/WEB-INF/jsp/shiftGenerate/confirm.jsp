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
      <div style="margin-bottom:12px;">
        <strong>生成日数：</strong>${days} 日
      </div>
      <div style="margin-bottom:12px;">
        <strong>月労働時間上限：</strong>${maxMonth} 分
      </div>
      <div>
        <strong>日労働時間上限：</strong>${maxDay} 分
      </div>
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