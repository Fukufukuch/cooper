<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <title>条件確認</title>
</head>
<body>

<h2>入力された条件</h2>

<ul>
    <li>生成日数：${days} 日</li>
    <li>月労働時間上限：${maxMonth}</li>
    <li>日労働時間上限：${maxDay}</li>
</ul>

<form action="/shift-generator/setting.jsp" method="post">
    <button type="submit">条件設定を変更する</button>
  </form>

<form action="/shift-generator/generate" method="post">
    <button type="submit">シフトを生成する</button>
  </form>

<a href="index.jsp">シフト生成画面に戻る</a>

</body>
</html>