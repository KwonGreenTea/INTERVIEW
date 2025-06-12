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
  /* select 박스 스타일 */
  .modify-row select {
    width: 100%; /* 부모 요소의 너비에 맞춰서 꽉 채움 */
    padding: 10px; /* 적당한 패딩 */
    margin: 8px 0; /* 위와 아래에 여백을 추가 */
    border: 1px solid #ccc; /* 테두리 색상 */
    border-radius: 4px; /* 둥근 모서리 */
    font-size: 14px; /* 폰트 크기 */
    background-color: #fff; /* 배경색 */
    color: #333; /* 글자색 */
    box-sizing: border-box; /* 패딩을 포함한 너비 계산 */
    transition: border-color 0.3s ease, box-shadow 0.3s ease; /* 포커스 시 효과 */
  }

  /* select 박스 포커스 시 */
  .modify-row select:focus {
    border-color: #007bff; /* 포커스 시 테두리 색 */
    box-shadow: 0 0 5px rgba(0, 123, 255, 0.3); /* 포커스 시 그림자 */
    outline: none; /* 기본 포커스 효과 제거 */
  }

  /* 라벨 스타일 */
  .modify-title {
    font-size: 16px;
    font-weight: bold;
    color: #333;
    display: block; /* 블록 형식으로 라벨 표시 */
    margin-bottom: 6px; /* 라벨과 select 사이 여백 */
  }

  /* 메시지 스타일 */
  .message {
    color: #ff6b6b; /* 에러 메시지 색상 */
    font-size: 12px; /* 메시지 크기 */
    margin-top: 4px; /* 메시지 위쪽 여백 */
  }

  /* 수정된 영역 (행) */
  .modify-row {
    margin-bottom: 20px; /* 각 행 간의 여백 */
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

      <div class="modify-row">
        <label class="modify-title" for="gender">성별</label>
        <select name="sector" id="gender">
          <option value="BM" ${memberDTO.sector == '비즈니스 매니저' ? 'selected' : ''}>비즈니스 매니저</option>
          <option value="SM" ${memberDTO.sector == '영업 매니저' ? 'selected' : ''}>영업 매니저</option>
          <option value="PS" ${memberDTO.sector == '제품 전문가' ? 'selected' : ''}>제품 전문가</option>
          <option value="RND" ${memberDTO.sector == '연구 개발 부서' ? 'selected' : ''}>연구 개발 부서</option>
          <option value="ICT" ${memberDTO.sector == '정보통신기술' ? 'selected' : ''}>정보통신기술</option>
          <option value="ARD" ${memberDTO.sector == '응용 연구 개발' ? 'selected' : ''}>응용 연구 개발</option>
          <option value="MM" ${memberDTO.sector == '마케팅 매니저' ? 'selected' : ''}>마케팅 매니저</option>
        </select>
        <span id="genderMsg" class="message"></span>
      </div>

      <div class="modify-row">
        <label class="modify-title" for="memberName">별명</label>
        <select name="gender" id="sector">
          <option value="male" ${memberDTO.gender == '남성' ? 'selected' : ''}>남성</option>
          <option value="female" ${memberDTO.gender == '여성' ? 'selected' : ''}>여성</option>
        </select>
        <span id="sectorMsg" class="message"></span>
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
