<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>設定</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css">
</head>
<body>
<div class="container">
  <div class="h1">オートシフタ</div>
  <div class="sub">設定</div>

  <%@ include file="/WEB-INF/jsp/common/owner_tabs.jspf" %>

  <div class="card" style="margin-top:14px;">
    <div class="section-title">設定メニュー</div>
    <p class="section-desc">各機能を選んでください。</p>

    <div class="form" style="margin-top:14px;">
      <div class="action-card">
        <div>
          <div style="font-weight:800;">アカウント作成</div>
          <div class="note">スタッフ用のID・パスワードを作成</div>
        </div>
        <a class="btn" href="${pageContext.request.contextPath}/owner/account/create">開く</a>
      </div>

 

      <div class="action-card">
        <div>
          <div style="font-weight:800;">パスワード変更</div>
          <div class="note">管理者パスワードを変更</div>
        </div>
        <a class="btn" href="${pageContext.request.contextPath}/owner/password">開く</a>
      </div>

      <div class="action-card">
        <div>
          <div style="font-weight:800;">ログアウト</div>
          <div class="note">システムからログアウト</div>
        </div>
        <a class="btn" href="${pageContext.request.contextPath}/Logout">実行</a>
      </div>

      <div class="action-card">
        <div>
          <div style="font-weight:800;">タイムスロット管理</div>
          <div class="note">シフト生成で使う時間帯の追加・編集・無効化</div>
        </div>
        <a class="btn" href="${pageContext.request.contextPath}/admin/timeslot">開く</a>
      </div>

      <div class="action-card">
        <div>
          <div style="font-weight:800;">ポジション管理</div>
          <div class="note">ポジション（役割）の追加・編集・無効化</div>
        </div>
        <a class="btn" href="${pageContext.request.contextPath}/admin/position">開く</a>
      </div>
    </div>
  </div>
</div>
</body>
</html>
