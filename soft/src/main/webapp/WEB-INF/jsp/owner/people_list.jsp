<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="app.model.User" %>

<%
  request.setAttribute("activeTab", "people");
  List<User> list = (List<User>) request.getAttribute("staffList");
%>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>スタッフ</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/app.css">
</head>
<body>
<div class="container">
  <div class="h1">オートシフタ</div>
  <div class="sub">スタッフ一覧・削除</div>

  <%@ include file="/WEB-INF/jsp/common/owner_tabs.jspf" %>

 

  <div class="card" style="margin-top:14px;">
    <div class="section-title">スタッフ一覧</div>

    <% if (list == null || list.isEmpty()) { %>
      <div class="note">スタッフがまだいません。</div>
    <% } else { %>
      <table class="table">
        <thead>
          <tr>
            <th>ユーザーID</th>
            <th>氏名</th>
            <th>権限</th>
            <th style="width:120px;">操作</th>
          </tr>
        </thead>
        <tbody>
        <% for (User u : list) { 
            String type = u.getUsertype();
boolean isOwner = "0x00".equals(type) || "0".equals(type) || "false".equalsIgnoreCase(type);
String role = isOwner ? "管理者" : "スタッフ";

        %>
          <tr>
            <td><%= u.getUserID() %></td>
            <td><%= u.getUsername() %></td>
            <td><%= role %></td>
            <td>
  <div class="actions">
    <a class="btn" href="<%= request.getContextPath() %>/owner/people/edit?userID=<%= u.getUserID() %>">編集</a>

    <form method="post" action="<%= request.getContextPath() %>/owner/people/delete" class="actions-form">
      <input type="hidden" name="userID" value="<%= u.getUserID() %>">
      <button class="btn danger" type="submit">削除</button>
    </form>
  </div>
</td>


          </tr>
        <% } %>
        </tbody>
      </table>
    <% } %>

    <div style="margin-top:12px;">
      <a class="btn" href="<%= request.getContextPath() %>/owner/account/create">＋ スタッフ作成</a>
    </div>
  </div>
</div>
</body>
</html>
