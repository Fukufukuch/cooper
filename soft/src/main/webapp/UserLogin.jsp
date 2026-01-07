<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <title>ログイン - オートシフタ</title>
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/main_container.css">
</head>
<body class="bg-light">
    <div class="main_container">
        <div class="card shadow-sm" style="width: 400px;">
            <div class="card-body">
                <h3 class="card-title text-center mb-4">オートシフタ</h3>
                
                <% String error = (String)request.getAttribute("errorMessage"); %>
                <% if(error != null){ %>
                    <div class="alert alert-danger" role="alert">
                        <%= error %>
                    </div>
                <% } %>

                <form action="authLogin" method="post">
                    <div class="mb-3">
                        <label for="userID" class="form-label">ユーザーID</label>
                        <input type="text" name="userID" id="userID" class="form-control" 
                               placeholder="10桁の半角英数字" maxlength="10" required>
                    </div>
                    <div class="mb-3">
                        <label for="password" class="form-label">パスワード</label>
                        <input type="password" name="password" id="password" class="form-control" required>
                    </div>
                    <button type="submit" class="btn btn-primary w-100">ログイン</button>
                </form>
            </div>
        </div>
    </div>
</body>
</html>