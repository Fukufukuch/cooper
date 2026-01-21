<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="app.dao.PositionDao" %>
<%
  request.setAttribute("activeTab", "setting");
  String error = (String)request.getAttribute("error");
  List<PositionDao.PositionItem> positions =
      (List<PositionDao.PositionItem>) request.getAttribute("positions");
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

  <a class="backlink" href="<%= request.getContextPath() %>/owner/setting/menu">← 戻る</a>

  <div class="h1">アカウント作成</div>
  <div class="sub">管理者 / スタッフ を作成</div>

  <%@ include file="/WEB-INF/jsp/common/owner_tabs.jspf" %>

  <div class="card" style="margin-top:14px;">
    <div class="section-title">ユーザー情報を入力してください</div>

    <% if (error != null) { %>
      <div class="note" style="color:#ef4444; font-weight:800;"><%= error %></div>
    <% } %>

    <form class="form" method="post" action="<%= request.getContextPath() %>/owner/account/create">

      <div>
        <div class="label">作成する種類</div>
        <div style="display:flex; gap:12px; align-items:center;">
          <label style="display:flex; gap:6px; align-items:center;">
            <input type="radio" name="usertype" value="1" checked> スタッフ
          </label>
          <label style="display:flex; gap:6px; align-items:center;">
            <input type="radio" name="usertype" value="0"> 管理者
          </label>
        </div>
      </div>

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
        <div class="label">勤務地（work_place）</div>
        <input class="input" name="work_place" maxlength="255" placeholder="例：東京本社" required>
      </div>

      <div>
        <div class="label">タグ（Tag）</div>
        <input class="input" name="tag" type="number" min="0" value="0">
      </div>

      <div>
        <div class="label">役職（Position）</div>
        <select class="input" name="positionID">
          <% if (positions != null && !positions.isEmpty()) { %>
            <% for (PositionDao.PositionItem p : positions) { %>
              <option value="<%= p.id %>" <%= (p.id == 1 ? "selected" : "") %>>
                <%= p.id %>：<%= p.name %>
              </option>
            <% } %>
          <% } else { %>
            <option value="1" selected>1：未設定</option>
          <% } %>
        </select>
        <div class="note" style="margin-top:6px;">
          ※ positionテーブルの内容が出ます（無い場合は未設定(1)）
        </div>
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
