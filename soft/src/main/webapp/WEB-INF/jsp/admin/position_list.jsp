<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>ポジション管理</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/app.css">
</head>
<body>

<div class="container">
  <div class="h1">オートシフタ</div>
  <div class="sub">ポジション管理</div>

  <div class="card" style="margin-top:24px;">
    <div class="section-title">既存ポジション</div>
    <% if (request.getAttribute("error") != null) { %>
      <div style="margin-top:12px; padding:10px; border-radius:10px; background:#fee2e2; color:#7f1d1d;">⚠️ <%= request.getAttribute("error") %></div>
    <% } %>

    <div class="table-responsive">
    <table class="table" style="width:100%; margin-top:12px;">
      <thead>
        <tr>
          <th>ID</th>
          <th>名前</th>
          <th>最小人数</th>
          <th>最大人数</th>
          <th>必要権限者数</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody>
        <%
          java.util.List<app.entity.PositionEntity> positions = (java.util.List<app.entity.PositionEntity>)request.getAttribute("positions");
          if (positions != null) {
            for (app.entity.PositionEntity p : positions) {
        %>
        <tr>
          <td><%= p.getId() %></td>
          <td>
            <form action="<%= request.getContextPath() %>/admin/position" method="post" style="display:flex; gap:8px; align-items:center;">
              <input type="hidden" name="action" value="update">
              <input type="hidden" name="id" value="<%= p.getId() %>">
              <input type="text" name="name" value="<%= p.getName() %>" class="form-control small-input">
          </td>
          <td><input type="number" name="minWorkers" value="<%= p.getMinWorkers() %>" class="form-control tiny-input"></td>
          <td><input type="number" name="maxWorkers" value="<%= p.getMaxWorkers() %>" class="form-control tiny-input"></td>
          <td><input type="number" name="requireAuthorityWorkers" value="<%= p.getRequireAuthorityWorkers() %>" class="form-control tiny-input"></td>
          <td>
              <div style="display:flex; flex-direction:column; gap:8px;">
                <button type="submit" class="btn small">更新</button>
                </form>
                <form action="<%= request.getContextPath() %>/admin/position" method="post">
                  <% if (p.isActive()) { %>
                    <input type="hidden" name="action" value="delete">
                    <input type="hidden" name="id" value="<%= p.getId() %>">
                    <button type="submit" class="btn ghost small" onclick="return confirm('無効化してよいですか？')">無効化</button>
                  <% } else { %>
                    <input type="hidden" name="action" value="reactivate">
                    <input type="hidden" name="id" value="<%= p.getId() %>">
                    <button type="submit" class="btn primary small">再有効化</button>
                  <% } %>
                </form>
          </td>
        </tr>
        <%    }
          }
        %>
      </tbody>
    </table>

    <div style="margin-top:20px;">
      <div class="section-title">新規ポジション追加</div>
      <form action="<%= request.getContextPath() %>/admin/position" method="post" style="display:flex; gap:8px; align-items:center; margin-top:8px;">
        <input type="hidden" name="action" value="create">
        <input type="text" name="name" placeholder="名前" class="form-control small-input">
        <input type="number" name="minWorkers" placeholder="最小人数" class="form-control tiny-input">
        <input type="number" name="maxWorkers" placeholder="最大人数" class="form-control tiny-input">
        <input type="number" name="requireAuthorityWorkers" placeholder="権限者数" class="form-control tiny-input">
        <button type="submit" class="btn primary small">追加</button>
      </form>
    </div>

    <div style="margin-top:12px;">
      <a href="<%= request.getContextPath() %>/owner/setting/menu" class="btn ghost">戻る</a>
    </div>
  </div>
</div>

</body>
</html>
