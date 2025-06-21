<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>인터뷰버디</title>
	<style>
		body {
			font-family: Arial, sans-serif;
			background-color: #f7f9fb;
			padding: 30px;
		}
		.date-header {
			margin-top: 30px;
			margin-bottom: 10px;
			font-size: 20px;
			font-weight: bold;
			color: #2c3e50;
			border-bottom: 2px solid #ccc;
			padding-bottom: 5px;
		}
		.interview {
			background-color: #fff;
			border: 1px solid #ddd;
			border-radius: 10px;
			padding: 15px;
			margin-bottom: 15px;
			box-shadow: 0 2px 4px rgba(0,0,0,0.05);
		}
		.interview .question {
			font-weight: bold;
			color: #34495e;
		}
		.interview .answer, .suggest, .meta {
			margin-top: 10px;
		}
		.interview .meta {
			color: #888;
			font-size: 12px;
		}
	</style>
</head>
<body>
<!-- 🔙 뒤로가기 버튼 추가 -->
<button onclick="history.back()" style="
  background-color: #3498db;
  color: white;
  border: none;
  padding: 10px 20px;
  font-size: 14px;
  border-radius: 5px;
  cursor: pointer;
">
	← 뒤로가기
</button>

<%
	// 날짜별 구분을 위한 이전 날짜 저장 변수
	String lastDate = "";
%>
<c:forEach var="interview" items="${info}">
	<fmt:formatDate value="${interview.createdDate}" pattern="yyyy-MM-dd" var="currentDate" />
	
	<%-- 날짜가 바뀌었으면 날짜 헤더 출력 --%>
	<c:if test="${currentDate != lastDate}">
		<%-- 현재 날짜를 lastDate에 업데이트 --%>
		<%
			pageContext.setAttribute("lastDate", pageContext.getAttribute("currentDate"));
		%>
		<div class="date-header">${currentDate}</div>
	</c:if>

	<div class="interview">
		<div class="question">Q. ${interview.question}</div>

		<c:if test="${not empty interview.answer}">
			<div class="answer">A. ${interview.answer}</div>
		</c:if>

		<c:if test="${not empty interview.suggest}">
			<div class="suggest">💡 제안 답변: ${interview.suggest}</div>
		</c:if>

		<div class="meta">
			의도: ${interview.intention}, 감정: ${interview.emotion}, 등급: ${interview.grade}
		</div>
	</div>
</c:forEach>

</body>
</html>
