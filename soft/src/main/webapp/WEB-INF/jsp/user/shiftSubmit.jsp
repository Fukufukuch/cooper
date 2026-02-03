<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
  request.setAttribute("activeTab", "submit");
  String error = (String) request.getAttribute("error");
%>

<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <title>シフト希望提出</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/app.css">
</head>

<body>
  <div class="container">
  <div class="h1">シフト自動生成システム</div>

  <%@ include file="/WEB-INF/jsp/common/user_tabs.jspf" %>

<!-- ===== メイン ===== -->
<main class="container shift-submit">

    <!-- 左：シフト追加 -->
    <section class="card left">
        <h2>シフト希望を追加</h2>

        <div class="form-group">
            <label for="shiftDate">日付</label>
            <input type="date" id="shiftDate">
        </div>

        <div class="form-group checkbox">
            <label for="allDay">
                <input type="checkbox" id="allDay">
                終日勤務可能
            </label>
        </div>

        <div class="form-group">
            <label for="timeSlot">シフト区分</label>
            <select id="timeSlot">
                <option value="">選択してください</option>
            </select>
        </div>

        <button type="button" class="btn add-btn" onclick="addShift()">
            追加
        </button>
    </section>

    <!-- 右：一覧 -->
    <section class="card right">
        <div class="list-header">
            <h2>シフト希望一覧</h2>
            <span id="count">0件</span>
        </div>

        <div id="shiftList" class="shift-list">
            <p class="empty">シフト希望がありません</p>
        </div>

        <button type="button" class="btn submit-btn" onclick="submitShifts()">
            提出する
        </button>
    </section>

</main>

<!-- ===== JavaScript ===== -->
<script>
document.addEventListener("DOMContentLoaded", () => {

    // ===== 状態（1回だけ作る）=====
    let shiftRequests = [];
    let timeslots = [];

    // ===== DOM =====
    const dateInput = document.getElementById("shiftDate");
    const timeSlotSelect = document.getElementById("timeSlot");
    const shiftList = document.getElementById("shiftList");
    const countSpan = document.getElementById("count");

    // ===== timeslot 読み込み =====
    fetch("<%= request.getContextPath() %>/user/shift/submit/api")
        .then(res => res.json())
        .then(data => {
            timeslots = data;
            data.forEach(t => {
                const opt = document.createElement("option");
                opt.value = t.id;
                opt.textContent = t.name;
                timeSlotSelect.appendChild(opt);
            });
        });

    // ===== 追加ボタン =====
    window.addShift = function () {

        const date = dateInput.value;
    const timeSlotId = timeSlotSelect.value;
    const timeSlotName =
        timeSlotSelect.options[timeSlotSelect.selectedIndex].text;

    // ★ ここでログを出す
    console.log("date:", date);
    console.log("timeSlotId:", timeSlotId);
    console.log("timeSlotName:", timeSlotName);

    if (!date) {
        alert("日付を選択してください");
        return;
    }
    if (!timeSlotId) {
        alert("シフト区分を選択してください");
        return;
    }

    shiftRequests.push({
        helpDay: date,
        timeSlotId: parseInt(timeSlotId),
        timeSlotName: timeSlotName
    });

    renderShiftList();

    };

    // ===== 表示 =====
    function renderShiftList() {
        shiftList.innerHTML = "";

        if (shiftRequests.length === 0) {
            shiftList.innerHTML = "<p>シフト希望がありません</p>";
            countSpan.textContent = "0件";
            return;
        }

        shiftRequests.forEach(s => {
            console.log("s.helpDay:", s.helpDay);
            console.log("s.timeSlotName:", s.timeSlotName);
            const div = document.createElement("div");
            div.innerHTML = `
                <div>📅`+ s.helpDay + `</div>
                <div>⏰`+ s.timeSlotName + `</div>
                <button onclick="removeShift(` + shiftRequests.indexOf(s) + `)">削除</button>
            `
            shiftList.appendChild(div);
        });

        countSpan.textContent = shiftRequests.length + "件";
        
    }

        window.removeShift = function (i) {
        shiftRequests.splice(i, 1);
        renderShiftList();
        };

        // ===== サーバーへの送信 =====
    window.submitShifts = function () {

    // 送信データが空なら中断
    if (shiftRequests.length === 0) {
        alert("提出するシフト希望がありません");
        return;
    }

    fetch("<%= request.getContextPath() %>/user/shift/submit/api", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(shiftRequests)
    })
    .then(res => {
        // HTTPエラー対策
        if (!res.ok) {
            throw new Error("HTTP status " + res.status);
        }
        return res.json();
    })
    .then(data => {
        if (data.status === "success") {
            alert("提出完了！");
            shiftRequests = [];
            renderShiftList();
        } else {
            alert("エラー: " + data.status);
        }
    })
    .catch(err => {
        console.error("submit error:", err);
        alert("送信に失敗しました");
    });
};

});
</script>


</body>
</html>
