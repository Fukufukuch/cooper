<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>時間帯設定</title>
  <style>
    body { font-family: sans-serif; background: #f6f6f6; }
    .wrap { max-width: 900px; margin: 30px auto; padding: 0 12px; }
    .card { background:#fff; padding:18px; border-radius:10px; box-shadow:0 2px 10px rgba(0,0,0,.06); }
    table { width:100%; border-collapse:collapse; }
    th, td { border-bottom:1px solid #eee; padding:10px; }
    th { background:#fafafa; }
    .input { padding:8px; border:1px solid #ddd; border-radius:8px; }
    .btn { padding:8px 12px; border:none; border-radius:8px; cursor:pointer; background:#2563eb; color:#fff; }
    a { color:#2563eb; text-decoration:none; }
  </style>
</head>
<body>
<div class="wrap">
  <div class="card">
    <h2>時間帯設定（管理者）</h2>
    <p><a href="${pageContext.request.contextPath}/owner/shift/edit">← シフト編集へ戻る</a></p>

    <table>
      <thead>
      <tr>
        <th>ID</th>
        <th>名前</th>
        <th>開始</th>
        <th>終了</th>
        <th>更新</th>
      </tr>
      </thead>
      <tbody>
      <c:forEach var="t" items="${timeslotList}">
        <tr>
          <form method="post" action="${pageContext.request.contextPath}/owner/timeslot/edit">
            <td>
              ${t.timeslotID}
              <input type="hidden" name="timeslotID" value="${t.timeslotID}">
            </td>
            <td><input class="input" type="text" name="name" value="${t.name}" required></td>
            <td><input class="input" type="time" name="start" value="${t.start}" required></td>
            <td><input class="input" type="time" name="end" value="${t.end}" required></td>
            <td><button class="btn" type="submit">更新</button></td>
          </form>
        </tr>
      </c:forEach>
      </tbody>
    </table>

  </div>
</div>
</body>
</html>
