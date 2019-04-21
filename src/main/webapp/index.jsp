<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>标题</title>
    <script src="js/jquery.min.js"></script>
</head>
<body>
<button id="btn" onclick="test()">按钮</button>
</body>
<script>
    console.log("打印");
//    $("#btn").click(function () {
//        console.log("============");
//    });
   function test() {
       console.log("=======test=====");
       window.navigate("https://www.baidu.com");
//       location.reload();
   }
</script>
</html>
