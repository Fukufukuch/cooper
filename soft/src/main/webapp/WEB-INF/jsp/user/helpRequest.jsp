<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%
  request.setAttribute("activeTab", "help");
%>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>ヘルプ募集</title>
  <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/app.css">
</head>

<body>
<div class="container">

  <!-- ===== タイトル ===== -->
  <div class="h1">オートシフタ</div>

  <!-- ===== 共通タブ ===== -->
  <%@ include file="/WEB-INF/jsp/common/user_tabs.jspf" %>

  <!-- ===== メイン ===== -->
  <main class="container help-request">

    <div class="help-layout">

      <!-- ===== 左：募集フォーム ===== -->
      <section class="card left">
        <h2>ヘルプ募集</h2>

        <form action="<%= request.getContextPath() %>/HelpRequestServlet" method="post">

          <div class="form-group">
            <label>代わってほしいシフト</label>
            <select name="shift_id" class="form-select" required>
                <option value="" disabled selected>シフトを選択</option>
                <%
                    List<Map<String, Object>> confirmedShifts =
                    (List<Map<String, Object>>) request.getAttribute("confirmedShifts");
                    if (confirmedShifts != null && !confirmedShifts.isEmpty()) {
                    for (Map<String, Object> s : confirmedShifts) {
                %>
                    <option value="<%= s.get("shift_id") %>">
                    <%= s.get("date") %> (<%= s.get("shift_timetable") %>)
                    </option>
                <%
                    }
                    } else {
                %>
                    <option disabled>シフトがありません</option>
                <%
                    }
                %>
            </select>
          </div>

          <div class="form-group">
            <label>理由</label>
            <textarea name="help_reason" rows="3" placeholder="理由を入力してください"></textarea>
          </div>

          <button type="submit" class="btn submit-btn">
            募集する
          </button>
        </form>
      </section>

      <!-- ===== 右：募集履歴 ===== -->
      <section class="card right">
        <div class="list-header">
          <h2>あなたの募集履歴</h2>
        </div>

        <div class="help-list">
          <%
            List<Map<String, String>> helpList =
              (List<Map<String, String>>) request.getAttribute("helpList");

            if (helpList != null && !helpList.isEmpty()) {
              List<Map<String, String>> reverse = new ArrayList<>(helpList);
              Collections.reverse(reverse);

              for (Map<String, String> help : reverse) {
                String status = help.get("status");
          %>
            <div class="help-card">
              <div class="help-info">
                <div class="help-date">📅 <%= help.get("date") %></div>
                <div class="help-time">⏰ <%= help.get("time") %></div>
                <div class="help-reason">理由：<%= help.get("reason") %></div>
              </div>

              <% if ("0".equals(status)) { %>
                <span class="badge waiting">募集中</span>
              <% } else { %>
                <span class="badge pending">承認待ち</span>
              <% } %>
            </div>
          <%
              }
            } else {
          %>
            <p class="empty">募集はまだありません</p>
          <%
            }
          %>
        </div>
      </section>

    </div>
  </main>
</div>
</body>
</html>