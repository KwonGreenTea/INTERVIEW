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
		var auth = '${auth}';
		if(auth !== "1") {
			alert(auth);
			window.history.back();
		}
		
		  // 채팅박스를 맨 아래로 스크롤하는 헬퍼 함수
		  function scrollChatToBottom() {
		    const chatBox = document.getElementById("chatBox");
		    chatBox.scrollTo({
		      top: chatBox.scrollHeight,
		      behavior: 'smooth'
		    });
		  }

		  // 페이지 로드 후, 첫 질문 생성용 클릭 핸들러 세팅
		  document.addEventListener("DOMContentLoaded", function() {
		    const serverMsg = document.querySelector(".message.server");
		    if (!serverMsg) return;

		    serverMsg.style.cursor = "pointer";
		    const handleClick = function() {
		      if (!confirm("질문을 출력할까요?")) return;

		      serverMsg.removeEventListener("click", handleClick);
		      serverMsg.style.cursor = "default";
		      serverMsg.textContent = "질문 생성중...";
		      scrollChatToBottom();

		      $.ajax({
		        type: 'GET',
		        url: '/interview/question',
		        success: function(result) {
		          console.log(result);
		          serverMsg.textContent = result;
		          scrollChatToBottom();
		          document.getElementById("userInput").disabled = false;
		        },
		        error: function() {
		          alert("다시 시도해주세요");
		          serverMsg.addEventListener("click", handleClick);
		          serverMsg.style.cursor = "pointer";
		          serverMsg.textContent = "해당 채팅을 누르면 질문이 생성됩니다!";
		          scrollChatToBottom();
		        }
		      });
		    };

		    serverMsg.addEventListener("click", handleClick);
		  });

		  // 사용자 답변 전송 함수
		  function sendMessage() {
		    const chatBox = document.getElementById("chatBox");
		    const input = document.getElementById("userInput");
		    const message = input.value.trim();
		    if (!message) return;

		    // 1) 사용자 메시지 표시
		    const userMessage = document.createElement("div");
		    userMessage.className = "message user";
		    userMessage.textContent = message;
		    chatBox.appendChild(userMessage);
		    scrollChatToBottom();

		    // 확인
		    if (!confirm("수정할 수 없습니다. 답변을 전송하시나요?")) {
		      return;
		    }
		    input.value = "";

		    // 2) 로딩 메시지
		    const loadingMsg = document.createElement("div");
		    loadingMsg.className = "message server";
		    loadingMsg.textContent = "답변 생성중...";
		    chatBox.appendChild(loadingMsg);
		    scrollChatToBottom();

		    // 3) 서버로 Ajax 요청
		    $.ajax({
		      type: 'GET',
		      url: '/interview/answer',
		      data: { answer: message },
		      success: function(result) {
		        input.disabled = true;

		        // 3-1) 분석 결과
		        loadingMsg.textContent = "답변이 완료되었습니다!";
		        const analysisBox = document.createElement("div");
		        analysisBox.className = "message server analysis";

		        const strongAnalysis = document.createElement("strong");
		        strongAnalysis.textContent = "📊 분석 결과";
		        analysisBox.appendChild(strongAnalysis);
		        analysisBox.appendChild(document.createElement("br"));

		        analysisBox.appendChild(document.createTextNode("🧠 의도 : " + result.intention));
		        analysisBox.appendChild(document.createElement("br"));
		        analysisBox.appendChild(document.createTextNode("😃 감정 : " + result.emotion));
		        analysisBox.appendChild(document.createElement("br"));

		        const gradeText = document.createElement("span");
		        gradeText.appendChild(document.createTextNode("🏅 등급 : "));
		        const gradeStrong = document.createElement("strong");
		        gradeStrong.textContent = result.grade;
		        gradeText.appendChild(gradeStrong);

		        analysisBox.appendChild(gradeText);
		        chatBox.appendChild(analysisBox);
		        scrollChatToBottom();

		        // 3-2) 피드백 목록
		        if (result.feedbackList && result.feedbackList.length > 0) {
		          const feedbackBox = document.createElement("div");
		          feedbackBox.className = "message server feedback";

		          const strongFeedback = document.createElement("strong");
		          strongFeedback.textContent = "📝 피드백 : ";
		          feedbackBox.appendChild(strongFeedback);

		          const ul = document.createElement("ul");
		          result.feedbackList.forEach(fb => {
		            const li = document.createElement("li");
		            li.textContent = fb;
		            ul.appendChild(li);
		          });
		          feedbackBox.appendChild(ul);
		          chatBox.appendChild(feedbackBox);
		          scrollChatToBottom();
		        }

		        // 3-3) 제안 답변
		        const suggestBox = document.createElement("div");
		        suggestBox.className = "message server suggest";

		        const strongSuggest = document.createElement("strong");
		        strongSuggest.textContent = "💡 제안 답변 : ";
		        suggestBox.appendChild(strongSuggest);

		        suggestBox.appendChild(document.createTextNode(result.suggest));
		        chatBox.appendChild(suggestBox);
		        scrollChatToBottom();
		      },
		      error: function() {
		        alert("다시 시도해주세요");
		        loadingMsg.textContent = "다시 시도해주세요";
		        input.value = message;
		        scrollChatToBottom();
		      }
		    });
		  }
		</script>


</body>
</html>















