<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  request.setAttribute("activeTab", "setting");
%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>アカウント作成</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/app.css">
</head>
<body>
<div class="container">

  <a class="backlink" href="<%= request.getContextPath() %>/owner/setting">← 戻る</a>

  <div class="h1">アカウント作成</div>
  <div class="sub">スタッフ用のID・パスワードを作成</div>

  <%@ include file="/WEB-INF/jsp/common/owner_tabs.jspf" %>

  <div class="card" style="margin-top:14px;">
    <div class="section-title">スタッフ情報を入力してください</div>

    <% String error = (String)request.getAttribute("error"); %>
    <% if (error != null) { %>
      <div class="note" style="color:#ef4444; font-weight:800;"><%= error %></div>
    <% } %>

    <form class="form" method="post" action="<%= request.getContextPath() %>/owner/account/create">
      <div>
        <div class="label">ユーザー名</div>
        <input class="input" name="username" maxlength="16" required>
      </div>

      <div>
        <div class="label">メールアドレス</div>
        <input class="input" name="email" type="email" required>
      </div>

      <div>
        <div class="label">電話番号（11桁）</div>
        <input class="input" name="phone" maxlength="11" required>
      </div>

      <div>
        <div class="label">生年月日</div>
        <input class="input" name="dob" type="date" required>
      </div>

      <div>
        <div class="label">初期パスワード</div>
        <input class="input" name="password" type="password" minlength="4" maxlength="20" required>
      </div>

      <div>
        <div class="label">初期パスワード（確認）</div>
        <input class="input" name="passwordConfirm" type="password" minlength="4" maxlength="20" required>
      </div>

      <button class="btn primary" type="submit">作成する</button>
    </form>
  </div>

</div>
</body>
</html>
