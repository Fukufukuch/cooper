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
    let shiftRequests = [];
    let timeslots = []; // timeslotデータを保持

    const dateInput = document.getElementById("shiftDate");
    const timeSlotSelect = document.getElementById("timeSlot");
    const allDayCheckbox = document.getElementById("allDay");
    //const startTimeInput = document.getElementById("startTime");
    //const endTimeInput = document.getElementById("endTime");
    const shiftList = document.getElementById("shiftList");
    const countSpan = document.getElementById("count");

    // ページロード時にtimeslotをロード
    window.addEventListener("load", loadTimeslots);

    function loadTimeslots() {
        console.log("loadTimeslots called");
        fetch("<%= request.getContextPath() %>/user/shift/submit/api")
            .then(res => {
                console.log("response status:", res.status);
                return res.json();
            })
            .then(data => {
                console.log("timeslots:", data);
                timeslots = data; // 保存
                data.forEach(timeslot => {
                    const option = document.createElement("option");
                    option.value = timeslot.id;
                    option.textContent = timeslot.name;
                    timeSlotSelect.appendChild(option);
                });
            })
            .catch(err => console.error("timeslot load error:", err));
    }

    // 終日チェック時の制御
    allDayCheckbox.addEventListener("change", () => {
        /*const disabled = allDayCheckbox.checked;
        startTimeInput.disabled = disabled;
        endTimeInput.disabled = disabled;
        if (disabled) {
            startTimeInput.value = "";
            endTimeInput.value = "";
        }*/
    });

    // リストへの追加
    function addShift() {
        const date = dateInput.value;
        const timeSlotId = timeSlotSelect.value;
        //const allDay = allDayCheckbox.checked;
        //const startTime = startTimeInput.value;
        //const endTime = endTimeInput.value;

        if (!date) {
            alert("日付を選択してください");
            return;
        }

        if (!timeSlotId) {
            alert("シフト区分を選択してください");
            return;
        }

        /*if (!allDay && (!startTime || !endTime)) {
            alert("開始・終了時刻を入力してください");
            return;
        }*/

        // timeslotのnameを取得
        const selectedTimeslot = timeslots.find(t => t.id == timeSlotId);
        //const timeSlotName = selectedTimeslot ? selectedTimeslot.name : timeSlotId;

        shiftRequests.push({
            //timeSlotName: timeSlotName,
            timeSlotId: parseInt(timeSlotId),
            timeSlotName: selectedTimeslot ? selectedTimeslot.name : timeSlotId,
            helpDay: date
        });

        //clearForm();
        renderShiftList();
    }

    /*function clearForm() {
        dateInput.value = "";
        timeSlotSelect.value = "";
        allDayCheckbox.checked = false;
        startTimeInput.disabled = false;
        endTimeInput.disabled = false;
        startTimeInput.value = "";
        endTimeInput.value = "";
    }*/

    // 表示の更新
    function renderShiftList() {
        shiftList.innerHTML = "";

        if (shiftRequests.length === 0) {
            shiftList.innerHTML = "<p class='empty'>シフト希望がありません</p>";
            countSpan.textContent = "0件";
            return;
        }

        shiftRequests.forEach((shift, index) => {
            const card = document.createElement("div");
            card.className = "shift-card";

            card.innerHTML = `
                <div class="shift-info">
                    <div class="shift-date">📅 ${shift.helpDay}</div>
                    <div class="shift-timeslot">⏰ ${shift.timeSlotName}</div>
                </div>
                <button class="delete-btn" onclick="removeShift(${index})">🗑</button>
            `;

            shiftList.appendChild(card);
        });

        countSpan.textContent = `${shiftRequests.length}件`;
    }

    function removeShift(index) {
        shiftRequests.splice(index, 1);
        renderShiftList();
    }

    // サーバーへの送信
    function submitShifts() {
        if (shiftRequests.length === 0) {
            alert("提出するシフト希望がありません");
            return;
        }

        fetch("<%= request.getContextPath() %>/user/shift/submit/api", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(shiftRequests[0])
        })
        .then(res => res.json())
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
            console.error(err);
            alert("送信に失敗しました");
        });
    }
</script>

</body>
</html>
