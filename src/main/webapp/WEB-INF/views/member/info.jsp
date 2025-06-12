<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원 정보</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<style>
  body {
    font-family: 'Segoe UI', sans-serif;
    background-color: #f4f6f8;
    margin: 0;
    padding: 0;
  }

  .container {
    max-width: 600px;
    margin: 50px auto;
    background-color: #ffffff;
    padding: 40px;
    border-radius: 12px;
    box-shadow: 0 4px 10px rgba(0,0,0,0.1);
  }

  .container h2 {
    text-align: center;
    margin-bottom: 30px;
    color: #34495e;
  }

  .info {
    font-size: 1.1rem;
    margin-bottom: 15px;
    color: #333;
  }

  .btn-group {
    text-align: center;
    margin-top: 30px;
  }

  .btn-group button {
    background-color: #3498db;
    border: none;
    color: white;
    padding: 10px 20px;
    margin: 0 10px;
    border-radius: 6px;
    font-size: 1rem;
    cursor: pointer;
    transition: background-color 0.3s;
  }

  .btn-group button:hover {
    background-color: #2980b9;
  }

  .btn-group #deleteMember {
    background-color: #e74c3c;
  }

  .btn-group #deleteMember:hover {
    background-color: #c0392b;
  }
</style>
</head>
<body>
	<%@ include file="../common/header.jsp" %>

	<div class="container">
		<h2>회원 정보</h2>
		<p class="info">아이디 : ${memberDTO.memberId }</p>
		<p class="info">회원 이름 : ${memberDTO.memberName }</p>
		<p class="info">성    별 : ${memberDTO.gender }</p>
		<p class="info">직    군 : ${memberDTO.sector }</p>
		<p class="info">회원 이름 : ${memberDTO.memberName }</p>
		<p class="info">회원 등록일 : 
			<fmt:formatDate value="${memberDTO.createdDate }"
				pattern="YY년 MM월 dd일 HH시 mm분" /></p>
		<p class="info">정보 수정일 : 
			<fmt:formatDate value="${memberDTO.updatedDate }"
				pattern="YY년 MM월 dd일 HH시 mm분" /></p>

		<div class="btn-group">
			<button id="modifyMember">정보 수정</button>
			<button id="deleteMember">회원 탈퇴</button>
		</div>

		<form id="deleteForm" action="delete" method="POST" style="display: none;">
			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
		</form>
	</div>

	<script type="text/javascript">
	$(document).ready(function(){
		$("#modifyMember").click(function(){
			window.location.href = 'modify';
		});
		
		$('#deleteMember').click(function(){
			if(confirm('탈퇴하시겠습니까?')) {
				$("#deleteForm").submit();
			}
		});
	});
	</script>
</body>
</html>
