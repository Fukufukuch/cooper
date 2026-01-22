<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
  request.setAttribute("activeTab", "setting");
  String error = (String) request.getAttribute("error");
%>

<!DOCTYPE html>
<html lang="ja">
    <head>
        <meta charset="UTF-8">
        <title>設定</title>
        <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/app.css">
    </head>

    <body>
        <div class="container">
        <div class="h1">シフト自動生成システム</div>

        <%@ include file="/WEB-INF/jsp/common/user_tabs.jspf" %>

        <main>
            <div class="settings-grid">
                <div class="settings-item" id="passwordChange">
                    <div class="settings-title">パスワード変更</div>
                </div>

                <div class="settings-item logout" id="logout">
                    <div class="settings-title">ログアウト</div>
                </div>
            </div>
        </main>
    </body>

    <script>
        document.getElementById("passwordChange").addEventListener("click", () => {
            location.href = "<%= request.getContextPath() %>/user/password";
        });
        document.getElementById("logout").addEventListener("click", () => {
            location.href = "<%= request.getContextPath() %>/login";
        });
    </script>
</html>