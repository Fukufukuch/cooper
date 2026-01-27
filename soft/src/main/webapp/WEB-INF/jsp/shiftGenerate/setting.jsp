<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
  <title>シフト生成条件設定</title>
</head>
<body>
<h2>シフト生成条件設定</h2>

<form action="confirm" method="post">

  開始日: <input type="date" name="firstDate"><br>

  生成日数: <input type="number" name="generateDays" value="7"><br>

  月最大労働時間（分）：
    <input type="number" name="maxWorkMonth" value="8000"><br>

    日最大労働時間（分）：
    <input type="number" name="maxWorkDay" value="720"><br>

    新人判定時間（分）：
    <input type="number" name="newcomerMinutes" value="5000"><br>

    必要ベテラン人数：
    <input type="number" name="seniorRequired" value="1"><br>


  <input type="submit" value="保存">
</form>

<a href="confirm.jsp">保存せずに戻る</a>

</body>
</html>
