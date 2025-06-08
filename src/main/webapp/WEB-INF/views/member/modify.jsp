<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원 정보 수정</title>
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>

<style>
  body {
    font-family: 'Segoe UI', sans-serif;
    background-color: #f9f9f9;
    padding: 30px;
  }
  .modify-content {
    max-width: 500px;
    margin: 0 auto;
    background: #ffffff;
    border-radius: 12px;
    box-shadow: 0 2px 10px rgba(0,0,0,0.1);
    padding: 30px;
  }
  .modify-title {
    font-size: 15px;
    margin-bottom: 6px;
    color: #333;
  }
  .modify-row input[type="password"],
  .modify-row input[type="text"] {
    width: 100%;
    padding: 10px;
    font-size: 14px;
    border: 1px solid #ddd;
    border-radius: 6px;
    margin-bottom: 10px;
    box-sizing: border-box;
  }
  #btnModify {
    display: block;
    width: 100%;
    max-width: 500px;
    margin: 20px auto;
    padding: 12px;
    background-color: #3498db;
    border: none;
    border-radius: 6px;
    color: white;
    font-size: 16px;
    cursor: pointer;
  }
  #btnModify:hover {
    background-color: #45a049;
  }
  .message {
    font-size: 13px;
    margin-bottom: 12px;
    display: block;
  }
  .message.error {
    color: red;
  }
  .message.success {
    color: green;
  }
</style>

</head>
<body>
  <sec:authentication property="principal" var="user"/>
  
  <form id="modifyForm" action="modify" method="POST">
    <div class="modify-content">
      
      <div class="modify-row">
        <label class="modify-title" for="memberId">아이디</label>
        <div style="margin-bottom: 15px;">${memberDTO.memberId}</div>
        <input type="hidden" id="memberId" name="memberId" value="${memberDTO.memberId}">
      </div>

      <div class="modify-row">
        <label class="modify-title" for="memberPw">비밀번호</label>
        <input id="memberPw" type="password" name="memberPw" maxlength="16">
        <span id="pwMsg" class="message"></span>
      </div>

      <div class="modify-row">
        <label class="modify-title" for="pwConfirm">비밀번호 재확인</label>
        <input id="pwConfirm" type="password" maxlength="16">
        <span id="pwConfirmMsg" class="message"></span>
      </div>

      <div class="modify-row">
        <label class="modify-title" for="memberName">별명</label>
        <input id="memberName" type="text" name="memberName" maxlength="10" value="${memberDTO.memberName}">
        <span id="nameMsg" class="message"></span>
      </div>
      
      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
    </div>
  </form>

  <button id="btnModify">회원 정보 수정</button>

  <script>
    $(document).ready(function(){
      let pwFlag = false;
      let pwConfirmFlag = false;
      let nameFlag = false;

      // 비밀번호 유효성 검사
      $('#memberPw').blur(function() {
        const memberPw = $(this).val();
        const pwRegExp = /^(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$^&*-]).{8,16}$/;

        if (memberPw === '') {
          $('#pwMsg').text('필수 입력입니다.').removeClass().addClass('message error');
          pwFlag = false;
        } else if (!pwRegExp.test(memberPw)) {
          $('#pwMsg').text('8~16자 영문 소문자, 숫자, 특수문자를 포함해야 합니다.').removeClass().addClass('message error');
          pwFlag = false;
        } else {
          $('#pwMsg').text('사용 가능한 비밀번호입니다.').removeClass().addClass('message success');
          pwFlag = true;
        }
      });

      // 비밀번호 확인 유효성 검사
      $('#pwConfirm').blur(function() {
        const pw = $('#memberPw').val();
        const confirm = $(this).val();

        if (confirm === '') {
          $('#pwConfirmMsg').text('필수 입력입니다.').removeClass().addClass('message error');
          pwConfirmFlag = false;
        } else if (pw !== confirm) {
          $('#pwConfirmMsg').text('비밀번호가 일치하지 않습니다.').removeClass().addClass('message error');
          pwConfirmFlag = false;
        } else {
          $('#pwConfirmMsg').text('비밀번호가 일치합니다.').removeClass().addClass('message success');
          pwConfirmFlag = true;
        }
      });

      // 이름 유효성 검사
      $('#memberName').blur(function() {
        const name = $(this).val().trim();
        if (name === '') {
          $('#nameMsg').text('필수 입력입니다.').removeClass().addClass('message error');
          nameFlag = false;
        } else {
          $('#nameMsg').text('').removeClass();
          nameFlag = true;
        }
      });

      // 제출
      $('#btnModify').click(function() {
        if (pwFlag && pwConfirmFlag && nameFlag) {
          $('#modifyForm').submit();
        } else {
          alert('모든 항목을 올바르게 입력해주세요.');
        }
      });
    });
  </script>
</body>
</html>
