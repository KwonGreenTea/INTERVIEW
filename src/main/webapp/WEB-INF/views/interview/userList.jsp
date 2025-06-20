<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<style>
body {
	font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
	background-color: #f9f9f9;
	padding: 30px;
}

h2 {
	text-align: center;
	color: #333;
	margin-bottom: 20px;
}

table {
	margin: 0 auto;
	border-collapse: collapse;
	width: 80%;
	background-color: #ffffff;
	box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
	border-radius: 8px;
	overflow: hidden;
}

th, td {
	padding: 14px 18px;
	text-align: left;
}

th {
	background-color: #7fc481;
	color: white;
	font-weight: bold;
}

tr:nth-child(even) {
	background-color: #f2f2f2;
}

tr:hover {
	background-color: #e0f7e9;
}
</style>
<!-- jquery 라이브러리 import -->
<script src="https://code.jquery.com/jquery-3.7.1.js">
	
</script>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta charset="UTF-8">
<title>인터뷰버디</title>
</head>
<body>
	<%@ include file="../common/header.jsp"%>

	<h2>다른 사용자 답변 목록</h2>
<table>
    <tr>
        <th>직무</th>
        <th>성별</th>
        <th>결과등급</th>
    </tr>
    <c:forEach var="interview" items="${InterviewDTO}">
        <tr class="detail_button" data-id="${interview.interviewId}">
            <td>
                <c:choose>
                    <c:when test="${interview.sector == 'BM'}">비즈니스 매니저</c:when>
                    <c:when test="${interview.sector == 'SM'}">영업 매니저</c:when>
                    <c:when test="${interview.sector == 'PS'}">제품 전문가</c:when>
                    <c:when test="${interview.sector == 'RND'}">연구 개발 부서</c:when>
                    <c:when test="${interview.sector == 'ICT'}">정보통신기술</c:when>
                    <c:when test="${interview.sector == 'ARD'}">응용 연구 개발</c:when>
                    <c:when test="${interview.sector == 'MM'}">마케팅 매니저</c:when>
                    <c:otherwise>알 수 없는 직군</c:otherwise>
                </c:choose>
            </td>
            <td>
                <c:choose>
                    <c:when test="${interview.gender == 'Male'}">남성</c:when>
                    <c:when test="${interview.gender == 'Female'}">여성</c:when>
                    <c:otherwise>기타</c:otherwise>
                </c:choose>
            </td>
            <td>${interview.grade}</td>
        </tr>
    </c:forEach>
</table>

<!-- form은 비워 두고, action을 JS에서 동적으로 설정 -->
<form id="detailForm" method="get"></form>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
    $(".detail_button").on("click", function () {
        var interviewId = $(this).data("id");
        var form = $("#detailForm");

        // action에 Path Variable을 포함해 동적으로 설정
        form.attr("action", "/interview/userList/" + interviewId);

        // 폼 제출
        form.submit();
    });
</script>

</body>
</html>















