<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <title>シフト希望提出</title>
    <style>
        body { font-family: sans-serif; margin: 20px; background-color: #f4f7f6; }
        .container { max-width: 500px; margin: auto; background: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 5px rgba(0,0,0,0.1); }
        .form-group { margin-bottom: 15px; }
        label { display: block; margin-bottom: 5px; font-weight: bold; }
        input[type="date"], input[type="time"], select { width: 100%; padding: 8px; box-sizing: border-box; }
        .checkbox-group { display: flex; align-items: center; gap: 10px; }
        button { cursor: pointer; padding: 10px 15px; border: none; border-radius: 4px; }
        .add-btn { background-color: #28a745; color: white; width: 100%; font-size: 16px; }
        .submit-btn { background-color: #007bff; color: white; width: 100%; margin-top: 20px; }
        
        .shift-list { margin-top: 20px; border-top: 2px solid #eee; padding-top: 10px; }
        .shift-card { background: #f9f9f9; border: 1px solid #ddd; padding: 10px; margin-bottom: 10px; border-radius: 4px; display: flex; justify-content: space-between; align-items: center; }
        .shift-info { font-size: 14px; }
        .delete-btn { background-color: #dc3545; color: white; padding: 5px 10px; }
        .empty { color: #888; text-align: center; }
    </style>
</head>
<body>

<div class="container">
    <h2>📅 シフト希望入力</h2>
    
    <div class="form-group">
        <label for="shiftDate">日付</label>
        <input type="date" id="shiftDate">
    </div>

    <div class="form-group">
        <label for="timeSlot">シフト区分</label>
        <select id="timeSlot">
            <option value="">選択してください</option>
        </select>
    </div>

    <div class="form-group checkbox-group">
        <input type="checkbox" id="allDay">
        <label for="allDay">終日勤務可能</label>
    </div>

    <div class="form-group">
        <label>時間</label>
        <div style="display: flex; gap: 10px; align-items: center;">
            <input type="time" id="startTime"> 〜 <input type="time" id="endTime">
        </div>
    </div>

    <button class="add-btn" onclick="addShift()">リストに追加</button>

    <div class="shift-list">
        <h3>追加済みの希望 (<span id="count">0件</span>)</h3>
        <div id="shiftList">
            <p class="empty">シフト希望がありません</p>
        </div>
    </div>

    <button class="submit-btn" onclick="submitShifts()">この内容で提出する</button>
</div>

<script>
    let shiftRequests = [];

    const dateInput = document.getElementById("shiftDate");
    const timeSlotSelect = document.getElementById("timeSlot");
    const allDayCheckbox = document.getElementById("allDay");
    const startTimeInput = document.getElementById("startTime");
    const endTimeInput = document.getElementById("endTime");
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
            .then(timeslots => {
                console.log("timeslots:", timeslots);
                timeslots.forEach(timeslot => {
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
        const disabled = allDayCheckbox.checked;
        startTimeInput.disabled = disabled;
        endTimeInput.disabled = disabled;
        if (disabled) {
            startTimeInput.value = "";
            endTimeInput.value = "";
        }
    });

    // リストへの追加
    function addShift() {
        const date = dateInput.value;
        const timeSlotId = timeSlotSelect.value;
        const timeSlotName =
        timeSlotSelect.options[timeSlotSelect.selectedIndex].text;
        const allDay = allDayCheckbox.checked;
        const startTime = startTimeInput.value;
        const endTime = endTimeInput.value;

        if (!date) {
            alert("日付を選択してください");
            return;
        }

        if (!timeSlotId) {
            alert("シフト区分を選択してください");
            return;
        }

        if (!allDay && (!startTime || !endTime)) {
            alert("開始・終了時刻を入力してください");
            return;
        }

        shiftRequests.push({
            timeSlotId: parseInt(timeSlotId),
            timeSlotName: timeSlotName,         // 表示用
            helpDay: date,
            reason: "希望"
        });

        clearForm();
        renderShiftList();
    }

    function clearForm() {
        dateInput.value = "";
        timeSlotSelect.value = "";
        allDayCheckbox.checked = false;
        startTimeInput.disabled = false;
        endTimeInput.disabled = false;
        startTimeInput.value = "";
        endTimeInput.value = "";
    }

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
            body: JSON.stringify(shiftRequests)
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