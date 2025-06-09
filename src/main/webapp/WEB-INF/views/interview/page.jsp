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
	margin: 0;
	padding: 0;
}

.chat-box {
	max-width: 1200px;
	margin: 20px auto;
	background-color: #e6f0f5;
	padding: 30px;
	border-radius: 12px;
	height: 500px;
	overflow-y: auto;
	box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.message {
	margin: 10px 0;
	padding: 14px 18px;
	border-radius: 10px;
	max-width: 80%;
	clear: both;
	line-height: 1.5;
	font-size: 15px;
	word-break: break-word;
}

.message.user {
	background-color: #ffe26f;
	float: right;
	text-align: left;
}

.message.server {
	background-color: white;
	float: left;
	text-align: left;
}

.message.server.suggest {
	border-left: 5px solid #007bff;
	background-color: #f0f7ff;
}

.message.server.analysis {
	border-left: 5px solid #28a745;
	background-color: #f0fff4;
}

.message.server.feedback {
	border-left: 5px solid #ffc107;
	background-color: #fffdf0;
}

.message.server.feedback ul {
	margin: 5px 0 0 15px;
	padding: 0;
}

.input-box {
	margin-top: 20px;
	text-align: center;
}

input[type="text"] {
	width: 70%;
	padding: 12px;
	font-size: 16px;
	border-radius: 6px;
	border: 1px solid #ccc;
	box-shadow: inset 0 1px 2px rgba(0, 0, 0, 0.05);
}

button {
	padding: 10px 18px;
	font-size: 16px;
	border-radius: 6px;
	background-color: #4a90e2;
	color: white;
	border: none;
	cursor: pointer;
	transition: background-color 0.3s ease;
}

button:hover {
	background-color: #357ABD;
}

</style>
<!-- jquery 라이브러리 import -->
<script src="https://code.jquery.com/jquery-3.7.1.js">
	
</script>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>인터뷰버디</title>
</head>
<body>
	<%@ include file="../common/header.jsp"%>
	
	<div class="chat-box" id="chatBox">
		<div class="message server">해당 채팅을 누르면 질문이 생성됩니다!</div>
	</div>

	<div class="input-box">
		<form onsubmit="sendMessage(); return false;">
			<input type="text" id="userInput" placeholder="답변을 입력하세요..." disabled />
			<button type="submit">보내기</button>
		</form>
	</div>

	<script>
		const userInput = document.getElementById("userInput");
		
		// 데이터 기입 확인
		var result = "${result}";
		if(result !== "1") {
			alert(result);
			window.history.back();
		}
		
		// 질문 생성
		document.addEventListener("DOMContentLoaded", function() {
			const serverMsg = document.querySelector(".message.server");
	
			if (serverMsg) {
				serverMsg.style.cursor = "pointer";
				
				const handleClick = function() {
					if(confirm("질문을 출력할까요?")){
						serverMsg.removeEventListener("click", handleClick);
						serverMsg.style.cursor = "default";
						serverMsg.textContent = "질문 생성중...";
						
						// $.ajax로 송수신
						$.ajax({
							type : 'GET', // 메서드 타입
							url : '/interview/question', // url 
							success : function(result) { // 전송 성공 시 서버에서 result 값 전송
								console.log(result);
								
								// 이벤트 제거해서 한번만 실행
								serverMsg.textContent = result;
								
								userInput.disabled = false;
							},
							error: function(xhr, status, error) {
								alert("다시 시도해주세요");
								serverMsg.addEventListener("click", handleClick);
								serverMsg.style.cursor = "pointer";
								serverMsg.textContent = "해당 채팅을 누르면 질문이 생성됩니다!";
					    	}
						}); // end ajax
					} // end comfirm
				};
	
				serverMsg.addEventListener("click", handleClick);
			}
		});
	
		function sendMessage() {
			const chatBox = document.getElementById("chatBox");
			const input = document.getElementById("userInput");
			const message = input.value;

			// 예외처리
			if (message.trim() === "") return;

			// 사용자 메시지 출력
			const userMessage = document.createElement("div");
			userMessage.className = "message user";
			userMessage.textContent = message;
			chatBox.appendChild(userMessage);

			if (confirm("수정할 수 없습니다. 답변을 전송하시나요?")) {
				// 입력창 초기화
				input.value = "";
				// 서버 처리 메시지
				const serverMsg = document.createElement("div");
				serverMsg.className = "message server";
				serverMsg.textContent = "답변 생성중...";
				chatBox.appendChild(serverMsg);

				// $.ajax로 송수신
				$.ajax({
					type: 'GET',
					url: '/interview/answer',
					data: { answer: message }, // GET은 객체 그대로 전달
					success: function (result) {
						input.disabled = true;

						serverMsg.textContent = "답변이 완료되었습니다!";
						
						// 분석 정보
						const analysisBox = document.createElement("div");
						analysisBox.className = "message server analysis";
						analysisBox.innerHTML = `
							<strong>📊 분석 결과</strong><br/>
							🧠 의도: ${result.intention} (${result.intentionScore}점)<br/>
							😃 감정: ${result.emotion} (${result.emotionScore}점)<br/>
							📝 길이 점수: ${result.lengthScore}점<br/>
							🎯 품질 점수: ${result.qualityScore}점<br/>
							🔢 총점: ${result.totalScore}점<br/>
							🧮 단어 수: ${result.wordCount}개<br/>
							🏅 등급: <strong>${result.grade}</strong>
						`;
						chatBox.appendChild(analysisBox);

						// 피드백 목록
						if (result.feedbackList && result.feedbackList.length > 0) {
							const feedbackBox = document.createElement("div");
							feedbackBox.className = "message server feedback";
							feedbackBox.innerHTML = `<strong>📝 피드백:</strong><ul>` + 
								result.feedbackList.map(fb => `<li>${fb}</li>`).join("") + 
								`</ul>`;
							chatBox.appendChild(feedbackBox);
						}
						
						// 제안
						const suggestBox = document.createElement("div");
						suggestBox.className = "message server suggest";
						suggestBox.innerHTML = `<strong>💡 제안:</strong> ${result.suggest}`;
						chatBox.appendChild(suggestBox);
					},
					error: function (xhr, status, error) {
						alert("다시 시도해주세요");
						serverMsg.textContent = "다시 시도해주세요";
						input.value = message;
					}
				});
			}

			// 스크롤 내리기
			chatBox.scrollTop = chatBox.scrollHeight;
		}

	</script>

</body>
</html>















