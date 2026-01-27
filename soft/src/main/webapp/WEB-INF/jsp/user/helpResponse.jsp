<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%
request.setAttribute("activeTab", "helpResponse");
%>
<!DOCTYPE html>
<html lang="ja">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>ヘルプ応答 | オートシフタ</title>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css">
	<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
	<style>
		body { 
			background-color: #f4f7f6; 
			font-family: sans-serif;
		}
		.help-card { 
			border: 1px solid #ddd; 
			border-radius: 8px; 
			background-color: white;
			box-shadow: 0 2px 5px rgba(0,0,0,0.1);
			transition: 0.2s;
		}
		.help-card:hover { transform: translateY(-2px); box-shadow: 0 4px 8px rgba(0,0,0,0.12); }
		.reason-box { 
			background-color: #f9f9f9; 
			border-radius: 4px;
			padding: 10px;
			font-size: 0.9rem;
			border-left: 3px solid #007bff;
		}
		.btn-success { background-color: #28a745; border: none; }
		.help-list {
			margin-top: 20px;
		}
		.help-item {
			background-color: #fff;
			border: 1px solid #ddd;
			border-radius: 8px;
			padding: 15px;
			margin-bottom: 10px;
			box-shadow: 0 2px 5px rgba(0,0,0,0.1);
		}
		.help-date {
			font-size: 1.2rem;
			font-weight: bold;
			color: #333;
		}
		.help-time {
			font-size: 1rem;
			color: #007bff;
		}
		.help-reason {
			font-size: 0.9rem;
			color: #666;
			margin: 5px 0;
		}
		.apply-btn {
			background-color: #28a745;
			color: white;
			border: none;
			padding: 10px 15px;
			border-radius: 4px;
			cursor: pointer;
			transition: background-color 0.3s;
		}
		.apply-btn:hover {
			background-color: #218838;
		}
	</style>
</head>
<body>

<div class="container">
	<div class="h1">オートシフタ</div>
	<div class="sub">ヘルプ応答</div>

	<%@ include file="/WEB-INF/jsp/common/user_tabs.jspf" %>
		<div class="row mb-3 align-items-center">
			<div class="col">
				<h4 class="fw-bold mb-0">🔍 募集中シフト一覧</h4>
			</div>
		</div>

		<div class="row">
			<%
				List<Map<String, String>> helpList = (List<Map<String, String>>) request.getAttribute("helpList");
				if (helpList != null && !helpList.isEmpty()) {
					for (Map<String, String> help : helpList) {
			%>
				<div class="col-md-4 mb-4">
					<div class="card help-card h-100">
						<div class="card-body">
							<div class="d-flex justify-content-between align-items-start mb-2">
								<h5 class="card-title fw-bold text-dark mb-0">📅 <%= help.get("date") %></h5>
							</div>
							<p class="card-text text-primary small mb-3 fw-bold">
								⏰ <%= help.get("time") %>
							</p>
							<div class="reason-box mb-3">
								<small class="text-secondary fw-bold">理由:</small> <%= help.get("reason") %>
							</div>
							<div class="text-end mb-3">
								<small class="text-muted">👤 依頼者: <%= help.get("userId") %></small>
							</div>
						</div>
						
						<div class="card-footer bg-white border-0 pb-3">
							<% if ("0".equals(help.get("status"))) { %>
								<form action="${pageContext.request.contextPath}/HelpResponseServlet" method="post">
									<input type="hidden" name="help_id" value="<%= help.get("id") %>">
									<button type="submit" class="btn btn-success w-100 fw-bold py-2">このシフトに応募する</button>
								</form>
							<% } else { %>
								<button class="btn btn-secondary w-100 fw-bold py-2" disabled>応募済み</button>
							<% } %>
						</div>
					</div>
				</div>
			<%
					}
				} else {
			%>
				<div class="col-12 text-center py-5 mt-4">
					<div class="p-5 bg-white rounded shadow-sm border">
						<p class="text-muted mb-0">現在、募集されているシフトはありません。</p>
					</div>
				</div>
			<% } %>
		</div>

        <div class="help-list">
        <%
            List<Map<String, String>> availableHelps = (List<Map<String, String>>) request.getAttribute("availableHelps");
            if (availableHelps != null && !availableHelps.isEmpty()) {
                for (Map<String, String> h : availableHelps) {
        %>
            <div class="help-item">
                <div class="help-date"><%= h.get("date") %></div>
                <div class="help-time"><%= h.get("time") %></div>
                <div class="help-reason">理由: <%= h.get("reason") %></div>
                <form action="<%= request.getContextPath() %>/HelpResponseServlet" method="post" style="display:inline;">
                    <input type="hidden" name="help_id" value="<%= h.get("helpID") %>">
                    <button type="submit" class="btn apply-btn">このシフトに応募する</button>
                </form>
            </div>
        <%
                }
            } else {
        %>
            <p>募集中のヘルプがありません。</p>
        <%
            }
        %>
        </div>
	</div>
</body>
</html>