<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="app.model.User" %>
<%@ page import="java.time.*" %>

<%
  request.setAttribute("activeTab", "people");
  String error = (String) request.getAttribute("error");
  User u = (User) request.getAttribute("user");

  String userID = (u == null) ? "" : u.getUserID();
  String username = (u == null) ? "" : u.getUsername();
  String email = (u == null) ? "" : u.getEmail();
  String phone = (u == null) ? "" : u.getPhoneNumber();
  java.sql.Date dob = (u == null) ? null : u.getDateOfBirth();
  String dobStr = (dob == null) ? "" : dob.toString();
  int tag = (u == null) ? 0 : u.getTag();
  int position = (u == null) ? 1 : u.getPosition();
  String workPlace = (u == null) ? "" : u.getWorkPlace();
%>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>スタッフ編集</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/app.css">
</head>
<body>
<div class="container">
  <div class="h1">オートシフタ</div>
  <div class="sub">スタッフ編集</div>

  <%@ include file="/WEB-INF/jsp/common/owner_tabs.jspf" %>

  <a class="backlink" href="<%= request.getContextPath() %>/owner/people">← スタッフ一覧へ</a>

  <div class="card" style="margin-top:14px;">
    <div class="section-title">スタッフ情報の編集</div>

    <% if (error != null) { %>
      <div class="alert danger"><%= error %></div>
    <% } %>

    <form method="post" action="<%= request.getContextPath() %>/owner/people/edit">
      <div class="form-row">
        <label>ユーザーID（変更不可）</label>
        <input class="input" type="text" name="userID" value="<%= userID %>" readonly>
      </div>

      <div class="form-row">
        <label>氏名</label>
        <input class="input" type="text" name="username" value="<%= username %>" required>
      </div>

      <div class="form-row">
        <label>メール</label>
        <input class="input" type="email" name="email" value="<%= email %>" required>
      </div>

      <div class="form-row">
        <label>電話番号</label>
        <input class="input" type="text" name="phone_number" value="<%= phone %>" maxlength="11" required>
      </div>

      <div class="form-row">
        <label>生年月日</label>
        <input class="input" type="date" name="date_of_birth" value="<%= dobStr %>" required>
      </div>

      <div class="form-row">
        <label>タグ（数値）</label>
        <input class="input" type="number" name="tag" value="<%= tag %>" min="0">
      </div>

      <div class="form-row">
        <label>役職ID（position.id）</label>
        <input class="input" type="number" name="position" value="<%= position %>" min="1">
        <div class="note" style="margin-top:6px;">
          ※ position テーブルの id を入力（存在しない場合は「未設定(1)」に自動補正します）
        </div>
      </div>

      <div class="form-row">
        <label>勤務地</label>
        <input class="input" type="text" name="work_place" value="<%= workPlace %>" required>
      </div>

      <div class="form-actions">
        <button class="btn" type="submit">更新</button>
      </div>
    </form>
  </div>
</div>
</body>
</html>
