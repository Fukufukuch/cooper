<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>タイムスロット管理</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/app.css">
</head>
<body>

<div class="container">
  <div class="h1">オートシフタ</div>
  <div class="sub">タイムスロット管理</div>

  <div class="card" style="margin-top:24px;">
    <div class="section-title">既存タイムスロット</div>
    <% if (request.getAttribute("error") != null) { %>
      <div style="margin-top:12px; padding:10px; border-radius:10px; background:#fee2e2; color:#7f1d1d;">⚠️ <%= request.getAttribute("error") %></div>
    <% } %>
    <div class="table-responsive">
    <table class="table" style="width:100%; margin-top:12px;">
      <thead>
        <tr>
          <th>ID</th>
          <th>名前</th>
          <th>開始（時:分）</th>
          <th>終了（時:分）</th>
          <th>追加最小人数</th>
          <th>追加最大人数</th>
          <th>必要権限者数</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody>
        <%
          java.util.List<app.entity.TimeSlotEntity> timeslots = (java.util.List<app.entity.TimeSlotEntity>)request.getAttribute("timeslots");
          if (timeslots != null) {
            for (app.entity.TimeSlotEntity ts : timeslots) {
        %>
        <tr>
          <td><%= ts.getId() %></td>
          <td>
            <form action="<%= request.getContextPath() %>/admin/timeslot" method="post" onsubmit="return syncTimes(this)" style="display:flex; gap:8px; align-items:center;">
              <input type="hidden" name="action" value="update">
              <input type="hidden" name="id" value="<%= ts.getId() %>">
              <input type="text" name="name" value="<%= ts.getName() %>" class="form-control small-input">
          </td>
          <%
            int sh = ts.getStartMinute() / 60;
            int sm = ts.getStartMinute() % 60;
            int eh = ts.getEndMinute() / 60;
            int em = ts.getEndMinute() % 60;
          %>
          <td>
            <div style="display:flex; gap:6px; align-items:center;">
              <input type="number" name="startHour" value="<%= sh %>" min="0" class="form-control small-input"> <span style="align-self:center">時</span>
              <input type="number" name="startMin" value="<%= sm %>" min="0" max="59" class="form-control small-input"> <span style="align-self:center">分</span>
            </div>
            <input type="hidden" name="startMinute" value="<%= ts.getStartMinute() %>">
          </td>
          <td>
            <div style="display:flex; gap:6px; align-items:center;">
              <input type="number" name="endHour" value="<%= eh %>" min="0" class="form-control small-input"> <span style="align-self:center">時</span>
              <input type="number" name="endMin" value="<%= em %>" min="0" max="59" class="form-control small-input"> <span style="align-self:center">分</span>
            </div>
            <input type="hidden" name="endMinute" value="<%= ts.getEndMinute() %>">
          </td>
          <td><input type="number" name="minExtraWorkers" value="<%= ts.getMinExtraWorkers() %>" class="form-control tiny-input"></td>
          <td><input type="number" name="maxExtraWorkers" value="<%= ts.getMaxExtraWorkers() %>" class="form-control tiny-input"></td>
          <td><input type="number" name="requireAuthorityWorkers" value="<%= ts.getRequireAuthorityWorkers() %>" class="form-control tiny-input"></td>
          <td>
              <div style="display:flex; flex-direction:column; gap:8px;">
                <button type="submit" class="btn small">更新</button>
                </form>
                <form action="<%= request.getContextPath() %>/admin/timeslot" method="post">
                  <% if (ts.isActive()) { %>
                    <input type="hidden" name="action" value="delete">
                    <input type="hidden" name="id" value="<%= ts.getId() %>">
                    <button type="submit" class="btn ghost small" onclick="return confirm('無効化してよいですか？')">無効化</button>
                  <% } else { %>
                    <input type="hidden" name="action" value="reactivate">
                    <input type="hidden" name="id" value="<%= ts.getId() %>">
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
    </div>

    <div style="margin-top:20px;">
      <div class="section-title">新規タイムスロット追加</div>
      <form action="<%= request.getContextPath() %>/admin/timeslot" method="post" onsubmit="return syncTimes(this)" style="display:flex; gap:8px; align-items:center; margin-top:8px;">
        <input type="hidden" name="action" value="create">
        <input type="text" name="name" placeholder="名前" class="form-control small-input">
        <div style="display:flex; gap:6px; align-items:center;">
          <input type="number" name="startHour" placeholder="開始 時" min="0" class="form-control small-input"> <span style="align-self:center">時</span>
          <input type="number" name="startMin" placeholder="開始 分" min="0" max="59" class="form-control small-input"> <span style="align-self:center">分</span>
        </div>
        <div style="display:flex; gap:6px; align-items:center;">
          <input type="number" name="endHour" placeholder="終了 時" min="0" class="form-control small-input"> <span style="align-self:center">時</span>
          <input type="number" name="endMin" placeholder="終了 分" min="0" max="59" class="form-control small-input"> <span style="align-self:center">分</span>
        </div>
        <input type="hidden" name="startMinute">
        <input type="hidden" name="endMinute">
        <input type="number" name="minExtraWorkers" placeholder="追加最小人数" class="form-control tiny-input">
        <input type="number" name="maxExtraWorkers" placeholder="追加最大人数" class="form-control tiny-input">
        <input type="number" name="requireAuthorityWorkers" placeholder="権限者数" class="form-control tiny-input">
        <button type="submit" class="btn primary small">追加</button>
      </form>
    </div>

    <div style="margin-top:12px;">
      <a href="<%= request.getContextPath() %>/owner/setting/menu" class="btn ghost">戻る</a>
    </div>
  </div>
</div>

<script>
  function syncTimes(form){
    // Ensure we query inputs within the same table row (robust when layout wraps)
    var row = form.closest('tr') || form;
    function getVal(n){ var el = row.querySelector('[name="'+n+'"]'); if(!el) return null; var v = parseInt(el.value,10); return isNaN(v)? null : v; }
    var sh = getVal('startHour'), sm = getVal('startMin'), eh = getVal('endHour'), em = getVal('endMin');
    if (sh === null || sm === null || eh === null || em === null){ alert('時刻入力フィールドが見つからないか入力が不正です。画面のサイズを調整してから再試行してください。'); return false; }
    if (sm<0 || sm>59 || em<0 || em>59){ alert('分は0〜59の範囲で入力してください'); return false; }
    var s = sh*60+sm, e = eh*60+em;
    if (e<=s){ if (!confirm('終了時刻が開始時刻より前または同じです。保存しますか？')) return false; }
    var startHidden = form.querySelector('[name="startMinute"]'); if(!startHidden){ startHidden=document.createElement('input'); startHidden.type='hidden'; startHidden.name='startMinute'; form.appendChild(startHidden); }
    var endHidden = form.querySelector('[name="endMinute"]'); if(!endHidden){ endHidden=document.createElement('input'); endHidden.type='hidden'; endHidden.name='endMinute'; form.appendChild(endHidden); }
    startHidden.value = s; endHidden.value = e;
    return true;
  }
</script>

</body>
</html>
