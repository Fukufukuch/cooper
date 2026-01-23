<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>シフト修正・公開</title>
  <style>
    body { font-family: sans-serif; background: #f6f6f6; }
    .wrap { max-width: 980px; margin: 30px auto; padding: 0 12px; }
    .card { background: #fff; padding: 18px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,.06); margin-bottom: 16px; }
    .title { font-size: 20px; font-weight: 700; margin-bottom: 10px; }
    .grid { display: grid; grid-template-columns: 1fr 1fr; gap: 10px; }
    .row { display: flex; flex-direction: column; gap: 6px; }
    .input, select { padding: 10px; border: 1px solid #ddd; border-radius: 8px; }
    table { width: 100%; border-collapse: collapse; }
    th, td { border-bottom: 1px solid #eee; padding: 10px; text-align: left; }
    th { background: #fafafa; }
    .btn { padding: 10px 14px; border: none; border-radius: 8px; cursor: pointer; }
    .btn-primary { background: #2563eb; color: #fff; }
    .btn-danger { background: #ef4444; color: #fff; }
    .muted { color: #666; font-size: 12px; }
  </style>
</head>

<body>
<div class="wrap">

  <div class="card">
    <div class="title">シフト追加</div>

    <form action="${pageContext.request.contextPath}/owner/shift/add" method="post">
      <div class="grid">
        <div class="row">
          <label>日付</label>
          <input class="input" type="date" name="date" required>
        </div>

        <div class="row">
          <label>ユーザーID（workerID）</label>
          <input class="input" type="text" name="userID" required>
          <div class="muted">※ users.userID と一致するID</div>
        </div>

        <div class="row">
          <label>ポジションID</label>
          <input class="input" type="number" name="positionID" required>
        </div>

        <div class="row">
          <label>時間帯（早番/中番/遅番/午前/午後 など）</label>
          <select class="input" name="timeslotID" required>
            <c:forEach var="t" items="${timeslotList}">
              <option value="${t.timeslotID}">
                ${t.timeslotID}：${t.name}（${t.start}〜${t.end}）
              </option>
            </c:forEach>
          </select>
        </div>
      </div>

      <div style="margin-top:12px;">
        <button class="btn btn-primary" type="submit">追加</button>
        <a class="btn" href="${pageContext.request.contextPath}/owner/timeslot/edit" style="text-decoration:none; display:inline-block; margin-left:8px; background:#111827; color:#fff;">時間帯の設定</a>
      </div>
    </form>
  </div>

  <div class="card">
    <div class="title">シフト一覧（直近）</div>

    <table>
      <thead>
      <tr>
        <th>ID</th>
        <th>日付</th>
        <th>workerID</th>
        <th>名前</th>
        <th>positionID</th>
        <th>時間帯</th>
        <th>削除</th>
      </tr>
      </thead>

      <tbody>
      <c:forEach var="s" items="${shiftList}">
        <tr>
          <td>${s.id}</td>
          <td>${s.date}</td>
          <td>${s.workerID}</td>
          <td>${s.username}</td>
          <td>${s.positionID}</td>
          <td>
            <c:choose>
              <c:when test="${not empty s.timeslotName}">
                ${s.timeslotName}（${s.startTime}〜${s.endTime}）
              </c:when>
              <c:otherwise>
                （時間帯未設定）
              </c:otherwise>
            </c:choose>
          </td>
          <td>
            <form action="${pageContext.request.contextPath}/owner/shift/delete" method="post" style="margin:0;">
              <input type="hidden" name="id" value="${s.id}">
              <button class="btn btn-danger" type="submit">削除</button>
            </form>
          </td>
        </tr>
      </c:forEach>
      </tbody>
    </table>

  </div>

</div>
</body>
</html>
