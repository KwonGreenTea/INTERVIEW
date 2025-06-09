<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>에러 페이지</title>
</head>
<body>
    <p>잘못된 접근입니다</p>
    <button onclick="goBack()">이전 페이지로 이동</button>

    <script>
        function goBack() {
            window.history.back();
        }
    </script>
</body>
</html>
