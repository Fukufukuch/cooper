<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  request.setAttribute("activeTab", "people");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>アカウント一覧</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/app.css">
</head>
<body>
<div class="container">
    <div class="page-title">シフト自動生成システム</div>
    <div class="sub">スタッフ一覧</div>

     <%@ include file="/WEB-INF/jsp/common/owner_tabs.jspf" %>

    <div class="card">
        <div class="card-title">スタッフ一覧</div>

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
                <tr>
                    <td>1001</td>
                    <td>山田 太郎</td>
                    <td>スタッフ</td>
                    <td>
                        <form method="post" action="<%= request.getContextPath() %>/owner/people/delete" style="display:inline;">
                            <input type="hidden" name="userId" value="1001">
                            <button class="btn danger" type="submit">削除</button>
                        </form>
                    </td>
                </tr>
                <tr>
                    <td>1002</td>
                    <td>佐藤 花子</td>
                    <td>スタッフ</td>
                    <td>
                        <form method="post" action="<%= request.getContextPath() %>/owner/people/delete" style="display:inline;">
                            <input type="hidden" name="userId" value="1002">
                            <button class="btn danger" type="submit">削除</button>
                        </form>
                    </td>
                </tr>
            </tbody>
        </table>

        <div class="note">
            ※今はDB未接続の仮表示。あとでDAO接続して一覧・削除を実装する。
        </div>

        <div style="margin-top:16px;">
            <a class="btn" href="<%= request.getContextPath() %>/owner/menu">メニューに戻る</a>
        </div>
    </div>
</div>
</body>
</html>
