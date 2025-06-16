<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>인터뷰버디</title>
<style>
body {
	margin: 0;
	font-family: 'Segoe UI', sans-serif;
	background-color: #f4f6f8;
}

.main-container {
	display: flex;
	flex-direction: column;
	align-items: center;
	gap: 30px;
	padding: 50px 20px;
}

.box-link {
	text-decoration: none;
	width: 80%;
	max-width: 600px;
}

.box {
	background-color: #5dade2;
	color: white;
	padding: 40px;
	border-radius: 15px;
	text-align: center;
	font-size: 1.5rem;
	box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
	transition: transform 0.2s, background-color 0.3s;
}

.box:hover {
	background-color: #2980b9;
	transform: translateY(-5px);
}
</style>
</head>
<body>
	<%@ include file="common/header.jsp" %>
	<div class="main-container">
		<a href="interview/page" class="box-link">
			<div class="box">인터뷰</div>
		</a> <a href="interview/userList" class="box-link">
			<div class="box">다른 사용자의 인터뷰 보기</div>
		<%--</a> <a href="board/list" class="box-link">
			<div class="box">게시판</div>
		</a>--%>
	</div>
	<%@ include file="common/footer.jsp" %>
</body>
</html>