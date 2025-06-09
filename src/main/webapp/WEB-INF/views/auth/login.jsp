<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta charset="UTF-8">
<title>로그인 페이지</title>
<style>
	.login-wrapper {
		display: flex;
		justify-content: center;
		align-items: center;
		height: calc(100vh - 80px); 	
		background-color: #f4f6f8;
		font-family: 'Segoe UI', sans-serif;
	}

	.login-container {
		background-color: #ffffff;
		padding: 40px 30px;
		border-radius: 12px;
		box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
		text-align: center;
		width: 320px;
	}

	.login-container h1 {
		margin-bottom: 20px;
		color: #333;
	}

	.login-container h2 {
		color: red;
		font-size: 14px;
		margin: 10px 0;
	}

	input[type="text"],
	input[type="password"] {
		width: 100%;
		padding: 10px;
		margin: 8px 0;
		border: 1px solid #ccc;
		border-radius: 5px;
		box-sizing: border-box;
	}

	input[type="submit"] {
		width: 100%;
		padding: 10px;
		margin-top: 15px;
		background-color: #3498db;
		color: #fff;
		border: none;
		border-radius: 5px;
		cursor: pointer;
		font-size: 16px;
	}

	input[type="submit"]:hover {
		background-color: #2980b9;
	}
</style>
</head>
<body>

	<%@ include file="../common/header.jsp"%>

	<div class="login-wrapper">
		<div class="login-container">
			<h1>로그인</h1>
			<br>
			
			<!-- 에러 메시지 출력 -->
			<h2>${errorMsg}</h2>

			<!-- 로그아웃 메시지 출력 -->
			<h2>${logoutMsg}</h2>

			<!-- 로그인 폼 -->
			<form action="../auth/login" method="POST">
				<input type="text" name="username" placeholder="아이디"><br>
				<input type="password" name="password" placeholder="비밀번호"><br>
				<input type="submit" value="로그인">
				<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
			</form>
		</div>
	</div>

</body>
</html>
