<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ja">
    <head>
        <meta charset="UTF-8">
        <title>労働者設定画面</title>
    </head>
    
    <style>
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

        /* ===== 設定画面メイン ===== */
        main {
            display: flex;
            justify-content: center;
            margin-top: 40px;
        }

        /* カード全体 */
        .settings-grid {
            display: grid;
            grid-template-columns: repeat(2, 1fr);
            gap: 20px;
            width: 420px;
        }

        /* 各設定カード */
        .settings-item {
            background: #ffffff;
            border-radius: 16px;
            padding: 24px 20px;
            box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
            cursor: pointer;
            transition: transform 0.15s, box-shadow 0.15s, background 0.15s;
        }

        .settings-item:hover {
            background: #f3f7ff;
            transform: translateY(-2px);
            box-shadow: 0 12px 28px rgba(0, 0, 0, 0.12);
        }

        /* タイトル */
        .settings-title {
            font-size: 16px;
            font-weight: 600;
            margin-bottom: 6px;
        }

        /* 説明文 */
        .settings-desc {
            font-size: 13px;
            color: #6b7280;
        }

        /* ログアウトだけ強調 */
        .settings-item.logout {
            background: #fff5f5;
            color: #c0392b;
        }

        .settings-item.logout:hover {
            background: #fdeaea;
        }
    </style>

    <body>
        <header class="header">
            <h1>シフト自動生成システム</h1>
            <nav class="menu">
                <button>カレンダー</button>
                <button>シフト情報入力</button>
                <button>ヘルプ募集</button>
                <button>ヘルプ応答</button>
                <button class="active">設定</button>
            </nav>
        </header>

        <main>
            <div class="settings-grid">
                <div class="settings-item" id="passwordChange">
                    <div class="settings-title">パスワード変更</div>
                </div>

                <div class="settings-item logout" id="logout">
                    <div class="settings-title">ログアウト</div>
                </div>
            </div>
        </main>
    </body>

    <script>
        document.getElementById("passwordChange").addEventListener("click", () => {
            location.href = "<%= request.getContextPath() %>/user/password";
        });
        document.getElementById("logout").addEventListener("click", () => {
            location.href = "<%= request.getContextPath() %>/login";
        });
    </script>
</html>