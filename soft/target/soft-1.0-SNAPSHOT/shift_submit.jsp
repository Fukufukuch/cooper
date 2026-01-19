<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <title>シフト自動生成システム</title>

    <!-- ===== CSS ===== -->
    <style>
        /* ===== 全体 ===== */
        * {
          box-sizing: border-box;
          margin: 0;
          padding: 0;
        }

        body {
          font-family: "Segoe UI", "Hiragino Kaku Gothic ProN", sans-serif;
          background: linear-gradient(180deg, #f3f7ff 0%, #eef2fb 100%);
          color: #1f2933;
          min-height: 100vh;
        }

        /* ===== ヘッダー ===== */
        .header {
          text-align: center;
          padding: 32px 16px 16px;
        }

        .header h1 {
          font-size: 28px;
          font-weight: 700;
          margin-bottom: 16px;
        }

        .menu {
          display: inline-flex;
          background: #f1f1f1;
          border-radius: 999px;
          padding: 6px;
          gap: 4px;
        }

        .menu button {
          border: none;
          background: transparent;
          padding: 10px 18px;
          border-radius: 999px;
          cursor: pointer;
          font-size: 14px;
          color: #555;
          transition: all 0.2s;
        }

        .menu button.active,
        .menu button:hover {
          background: #ffffff;
          color: #000;
          box-shadow: 0 2px 6px rgba(0,0,0,0.08);
        }

        /* ===== メイン ===== */
        .container {
          max-width: 1100px;
          margin: 40px auto;
          display: flex;
          gap: 32px;
          padding: 0 16px;
        }

        /* ===== カード共通 ===== */
        .card {
          background: #ffffff;
          border-radius: 20px;
          padding: 24px 24px 28px;
          box-shadow: 0 10px 30px rgba(0,0,0,0.06);
        }

        .left {
          flex: 1;
        }

        .right {
          flex: 1;
          display: flex;
          flex-direction: column;
        }

        /* ===== タイトル ===== */
        .card h2 {
          font-size: 18px;
          font-weight: 700;
          margin-bottom: 16px;
        }

        /* ===== フォーム ===== */
        .form-group {
          margin-bottom: 16px;
          display: flex;
          flex-direction: column;
        }

        .form-group label {
          font-size: 13px;
          color: #6b7280;
          margin-bottom: 6px;
        }

        .form-group input[type="date"],
        .form-group input[type="time"] {
          padding: 10px 12px;
          border-radius: 10px;
          border: 1px solid #e5e7eb;
          font-size: 14px;
          outline: none;
          transition: border 0.2s;
        }

        .form-group input:focus {
          border-color: #6366f1;
        }

        /* チェック */
        .form-group.checkbox {
          flex-direction: row;
          align-items: center;
          gap: 8px;
          margin-bottom: 12px;
        }

        .form-group.checkbox label {
          margin: 0;
          font-size: 14px;
          color: #374151;
        }

        /* ===== ボタン ===== */
        .btn {
          width: 100%;
          border: none;
          border-radius: 12px;
          padding: 12px;
          font-size: 15px;
          font-weight: 600;
          cursor: pointer;
          transition: all 0.2s;
        }

        .add-btn {
          background: linear-gradient(180deg, #0f172a, #020617);
          color: #fff;
          margin-top: 12px;
        }

        .submit-btn {
          background: linear-gradient(180deg, #0f172a, #020617);
          color: #fff;
          margin-top: auto;
        }

        .btn:hover {
          opacity: 0.9;
          transform: translateY(-1px);
        }

        /* ===== 右側：一覧 ===== */
        .list-header {
          display: flex;
          justify-content: space-between;
          align-items: center;
          margin-bottom: 16px;
        }

        .list-header span {
          font-size: 13px;
          color: #6b7280;
        }

        .shift-list {
          display: flex;
          flex-direction: column;
          gap: 12px;
          margin-bottom: 24px;
        }

        .shift-card {
          background: #f9fafb;
          border-radius: 14px;
          padding: 14px 16px;
          display: flex;
          justify-content: space-between;
          align-items: center;
          box-shadow: inset 0 0 0 1px #e5e7eb;
        }

        .shift-info {
          display: flex;
          flex-direction: column;
          gap: 4px;
          font-size: 14px;
          line-height: 1.4;
        }

        .shift-date {
          font-size: 14px;
          font-weight: 600;
        }

        .shift-time {
          font-size: 13px;
          color: #6b7280;
        }

        .delete-btn {
          border: none;
          background: transparent;
          cursor: pointer;
          font-size: 18px;
          color: #ef4444;
          transition: transform 0.2s;
        }

        .delete-btn:hover {
          transform: scale(1.1);
        }

        .empty {
          font-size: 14px;
          color: #9ca3af;
          text-align: center;
          margin-top: 40px;
        }
    </style>
</head>

<body>

<!-- ===== ヘッダー ===== -->
<header class="header">
    <h1>シフト自動生成システム</h1>
    <nav class="menu">
        <button>📅 カレンダー</button>
        <button class="active">📝 シフト情報入力</button>
        <button>🤝 ヘルプ募集</button>
        <button>👤 スタッフ</button>
        <button>⚙ 設定</button>
    </nav>
</header>

<!-- ===== メイン ===== -->
<main class="container">

    <!-- 左：シフト追加 -->
    <section class="card left">
        <h2>シフト希望を追加</h2>

        <div class="form-group">
            <label>日付を選択</label>
            <input type="date" id="shiftDate">
        </div>

        <div class="form-group checkbox">
            <label>
                <input type="checkbox" id="allDay">
                終日勤務可能
            </label>
        </div>

        <div class="form-group">
            <label>開始時刻</label>
            <input type="time" id="startTime">
        </div>

        <div class="form-group">
            <label>終了時刻</label>
            <input type="time" id="endTime">
        </div>

        <button class="btn add-btn" onclick="addShift()">追加</button>
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

        <button class="btn submit-btn" onclick="submitShifts()">提出する</button>
    </section>

</main>

<!-- ===== JavaScript ===== -->
<script>
    let shiftRequests = [];

    const dateInput = document.getElementById("shiftDate");
    const allDayCheckbox = document.getElementById("allDay");
    const startTimeInput = document.getElementById("startTime");
    const endTimeInput = document.getElementById("endTime");
    const shiftList = document.getElementById("shiftList");
    const countSpan = document.getElementById("count");

    allDayCheckbox.addEventListener("change", () => {
        const disabled = allDayCheckbox.checked;
        startTimeInput.disabled = disabled;
        endTimeInput.disabled = disabled;
        if (disabled) {
            startTimeInput.value = "";
            endTimeInput.value = "";
        }
    });

    function addShift() {
        const date = dateInput.value;
        const allDay = allDayCheckbox.checked;
        const startTime = startTimeInput.value;
        const endTime = endTimeInput.value;

        if (!date) {
            alert("日付を選択してください");
            return;
        }

        if (!allDay && (!startTime || !endTime)) {
            alert("開始・終了時刻を入力してください");
            return;
        }

        shiftRequests.push({
            date,
            startTime: allDay ? null : startTime,
            endTime: allDay ? null : endTime,
            allDay
        });

        clearForm();
        renderShiftList();
    }

    function clearForm() {
        dateInput.value = "";
        allDayCheckbox.checked = false;
        startTimeInput.disabled = false;
        endTimeInput.disabled = false;
        startTimeInput.value = "";
        endTimeInput.value = "";
    }

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

            const timeText = shift.allDay
                ? "終日勤務可能"
                : shift.startTime + " 〜 " + shift.endTime;

            card.innerHTML =
              '<div class="shift-info">' +
                '<div class="shift-date">📅 ' + shift.date + '</div>' +
                '<div class="shift-time">⏰ ' + timeText + '</div>' +
              '</div>' +
              '<button class="delete-btn" onclick="removeShift(' + index + ')">🗑</button>';

            shiftList.appendChild(card);
        });

        countSpan.textContent = shiftRequests.length + "件";
    }

    function removeShift(index) {
        shiftRequests.splice(index, 1);
        renderShiftList();
    }

    function submitShifts() {
      if (shiftRequests.length === 0) {
          alert("提出するシフト希望がありません");
          return;
      }

      fetch("submitShift", {
          method: "POST",
          headers: {
              "Content-Type": "application/json"
          },
          body: JSON.stringify(shiftRequests)
      })
      .then(response => response.json())
      .then(data => {
          if (data.status === "success") {
              alert("シフト希望を提出しました！");
              shiftRequests = [];
              renderShiftList();
          } else {
              alert("送信に失敗しました");
          }
      })
      .catch(error => {
          console.error("エラー:", error);
          alert("通信エラーが発生しました");
      });
    }

</script>

</body>
</html>
