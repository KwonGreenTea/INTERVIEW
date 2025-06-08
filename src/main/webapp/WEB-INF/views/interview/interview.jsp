<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<style>
body {
	background-color: #f4f6f8;
	font-family: 'Arial', sans-serif;
}

.chat-box {
	max-width: 1200px;
	margin: 20px auto 0;
	background-color: #e6f0f5;
	padding: 45px;
	border-radius: 10px;
	height: 500px;
	overflow-y: scroll;
}

.message {
	margin: 5px 0;
	padding: 12px 16px;
	border-radius: 10px;
	max-width: 80%;
	clear: both;
}

.user {
	background-color: #ffe26f;
	float: right;
	text-align: right;
}

.bot {
	background-color: white;
	float: left;
}

.input-box {
	margin-top: 20px;
	text-align: center;
}

input[type="text"] {
	width: 70%;
	padding: 10px;
	font-size: 16px;
	border-radius: 5px;
	border: 1px solid #ccc;
}

button {
	padding: 10px 15px;
	font-size: 16px;
	border-radius: 5px;
	background-color: #4a90e2;
	color: white;
	border: none;
	cursor: pointer;
}
</style>
<!-- jquery 라이브러리 import -->
<script src="https://code.jquery.com/jquery-3.7.1.js">
	
</script>
<meta charset="UTF-8">
<title>인터뷰 페이지</title>
</head>
<body>
	<%@ include file="../common/header.jsp"%>
	<div class="chat-box" id="chatBox">
		<div class="message bot">안녕하세요! 무엇을 도와드릴까요?</div>
	</div>

	<div class="input-box">
		<form onsubmit="sendMessage(); return false;">
			<input type="text" id="userInput" placeholder="답변을 입력하세요..." />
			<button type="submit">보내기</button>
		</form>
	</div>

	<script>
		function sendMessage() {
			const input = document.getElementById("userInput");
			const message = input.value.trim();
			if (message === "")
				return;

			const chatBox = document.getElementById("chatBox");

			// 사용자 메시지 출력
			const userMessage = document.createElement("div");
			userMessage.className = "message user";
			userMessage.textContent = message;
			chatBox.appendChild(userMessage);

			// 자동 응답 메시지
			const botMessage = document.createElement("div");
			botMessage.className = "message bot";
			botMessage.textContent = generateBotReply(message);
			chatBox.appendChild(botMessage);

			// 스크롤 내리기
			chatBox.scrollTop = chatBox.scrollHeight;

			input.value = "";
		}

		function generateBotReply(userMessage) {
			// 간단한 키워드 응답 예시
			if (userMessage.includes("루피")) {
				return "루피짱 🐵";
			} else if (userMessage.includes("ㅋㅋ") || userMessage.includes("ㅎㅎ")) {
				return "재밌죠? 😄";
			} else {
				return "그게 무슨 말이죠?";
			}
		}
	</script>

</body>
</html>















