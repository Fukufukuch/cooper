<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  request.setAttribute("activeTab", "people");
%>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>設定</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/app.css">
</head>
<body>
  <div class="container">
    <a class="backlink" href="<%= request.getContextPath() %>/owner/menu">
      <span class="arrow">←</span><span>メニューに戻る</span>
    </a>

    <div class="card">
      <div class="section-title">設定</div>
      <p class="section-desc">アカウント作成・削除、パスワード変更</p>

      <div style="margin-top:16px;">
        <div class="action-card">
          <div class="action-left">
            <div class="action-ico">👤</div>
            <div class="action-main">
              <div class="action-title">アカウント作成</div>
              <div class="action-sub">スタッフ用のID・パスワードを作成</div>
            </div>
          </div>
          <a class="btn primary" href="<%= request.getContextPath() %>/owner/account/create">開く</a>
        </div>

        <div class="action-card">
          <div class="action-left">
            <div class="action-ico">👥</div>
            <div class="action-main">
              <div class="action-title">アカウント一覧・削除</div>
              <div class="action-sub">スタッフ一覧の確認／削除</div>
            </div>
          </div>
          <a class="btn" href="<%= request.getContextPath() %>/owner/people">開く</a>
        </div>

        <div class="action-card">
          <div class="action-left">
            <div class="action-ico">🔑</div>
            <div class="action-main">
              <div class="action-title">パスワード変更</div>
              <div class="action-sub">管理者パスワードを変更</div>
            </div>
          </div>
          <a class="btn" href="<%= request.getContextPath() %>/owner/password">開く</a>
        </div>

        <p class="note">※ ここから先の画面も同じCSS（app.css）で統一する。</p>
      </div>
    </div>
  </div>

  <a class="help-fab" href="#" title="ヘルプ">?</a>
</body>
</html>
