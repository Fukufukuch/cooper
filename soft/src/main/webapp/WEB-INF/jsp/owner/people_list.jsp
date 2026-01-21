<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="app.model.User" %>

<%
  // OwnerPeopleServlet から来る
  List<User> list = (List<User>) request.getAttribute("list");
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
  <div class="h1">シフト自動生成システム</div>
  <div class="sub">スタッフ一覧・削除</div>

  <%@ include file="/WEB-INF/jsp/common/owner_tabs.jspf" %>

  <a class="backlink" href="<%= request.getContextPath() %>/owner/setting">← 設定に戻る</a>

  <div class="card" style="margin-top:14px;">
    <div class="section-title">スタッフ一覧</div>
    <p class="section-desc">DBの users テーブル（usertype=1）を表示しています。</p>

    <% if (list == null || list.isEmpty()) { %>
      <div class="note">スタッフがまだいません</div>
    <% } else { %>

      <% for (User u : list) { %>
        <div class="action-card">
          <div>
            <div style="font-weight:800;">
              <%= u.getUsername() %>（<%= u.getUserID() %>）
            </div>
            <div class="note">
              email: <%= u.getEmail() %> /
              phone: <%= u.getPhoneNumber() %> /
              place: <%= u.getWorkPlace() %> /
              Tag: <%= u.getTag() %> /
              Position: <%= u.getPosition() %>
            </div>
          </div>

          <form method="post" action="<%= request.getContextPath() %>/owner/people/delete" style="margin:0;">
            <input type="hidden" name="userID" value="<%= u.getUserID() %>">
            <button class="btn" type="submit"
                    onclick="return confirm('このスタッフを削除しますか？\\nuserID: <%= u.getUserID() %>');">
              削除
            </button>
          </form>
        </div>
      <% } %>

    <% } %>
  </div>

  <div class="card" style="margin-top:14px;">
    <div class="section-title">アカウント作成</div>
    <p class="section-desc">スタッフ用のID・パスワードを作成します。</p>
    <a class="btn" href="<%= request.getContextPath() %>/owner/account/create">作成へ</a>
  </div>

</div>
</body>
</html>
