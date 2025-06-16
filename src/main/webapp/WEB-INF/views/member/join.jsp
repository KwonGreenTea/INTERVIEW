<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<style>
  /* 폼 전체 스타일 */
  .join-content {
    max-width: 600px;
    margin: 0 auto;
    padding: 20px;
    background-color: #f9f9f9;
    border-radius: 8px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  }

  /* 제목 스타일 */
  .join-title {
    font-size: 16px;
    font-weight: bold;
    color: #333;
    margin-bottom: 8px;
  }

  /* 입력 필드 스타일 */
  .join-row input[type="text"],
  .join-row input[type="password"],
  .join-row select {
    width: 100%;
    padding: 12px;
    margin: 8px 0;
    border: 1px solid #ddd;
    border-radius: 4px;
    box-sizing: border-box;
    font-size: 14px;
  }

  /* 입력 필드 포커스 시 스타일 */
  .join-row input[type="text"]:focus,
  .join-row input[type="password"]:focus,
  .join-row select:focus {
    border-color: #007bff;
    outline: none;
  }

  /* 에러 메시지 스타일 */
  .join-row span {
    color: #ff6b6b;
    font-size: 12px;
  }

  /* 버튼 스타일 */
  #btnJoin {
    width: 100%; /* 입력 필드와 동일하게 너비를 맞춤 */
    padding: 12px; /* 패딩을 입력 필드와 동일하게 설정 */
    background-color: #007bff;
    color: white;
    border: none;
    border-radius: 4px;
    font-size: 14px; /* 입력 필드와 같은 폰트 크기 */
    cursor: pointer;
    transition: background-color 0.3s;
    box-sizing: border-box; /* 패딩을 포함한 너비를 계산 */
  }

  #btnJoin:hover {
    background-color: #0056b3;
  }

  /* 구분선 스타일 */
  hr {
    margin-top: 20px;
    border: 0;
    border-top: 1px solid #ddd;
  }

  /* 레이블 스타일 */
  label {
    display: block;
    font-size: 14px;
    color: #333;
  }

  /* 전체 폼의 반응형 처리 */
  @media screen and (max-width: 768px) {
    .join-content {
      padding: 15px;
    }

    .join-row input[type="text"],
    .join-row input[type="password"],
    .join-row select {
      font-size: 13px;
    }


  }
</style>
<script src="https://code.jquery.com/jquery-3.7.1.min.js">
</script>
<title>회원가입</title>
</head>
<body>
  <%@ include file="../common/header.jsp"%>
  <form id="joinForm" action="join" method="POST">
    <div class="join-content">
      <div class="row-group">
        <div class="join-row">
          <h3 class="join-title">
            <label for="memberId">아이디</label>
          </h3>
          <span>
            <input id="memberId" type="text" name="memberId" title="아이디" maxlength="10" >
            <br>
          </span>
          <span id="idMsg" ></span>
        </div>
        
        <div class="join-row">
          <h3 class="join-title">
            <label for="memberPw">비밀번호</label>
          </h3>
          <span>
            <input id="memberPw" type="password" name="memberPw" title="비밀번호" maxlength="16" >
            <br>
          </span>
          <span id="pwMsg" ></span>
          
          <h3 class="join-title">
            <label for="pwConfirm">비밀번호 재확인</label>
          </h3>
          <span>
            <input id="pwConfirm" type="password" title="비밀번호 확인" maxlength="16" >
            <br>
          </span>
          <span id="pwConfirmMsg"></span>

          <div class="join-row">
            <h3 class="join-title">
              <label for="memberName">별명</label>
            </h3>
            <span>
              <input id="memberName" type="text" name="memberName" title="이름" maxlength="10" >
              <br>
            </span>
            <span id="nameMsg"></span>
          </div>

          <div class="join-row">
            <h3 class="join-title">
              <label for="gender">성별</label>
            </h3>
            <span>
              <select name="gender" id="gender">
                <option value="Male">남성</option>
                <option value="Female">여성</option>
              </select>
              <br>
            </span>
            <span id="genderMsg"></span>
          </div>

          <div class="join-row">
            <h3 class="join-title">
              <label for="sector">직군</label>
            </h3>
            <span>
              <select name="sector" id="sector">
                  <option value="BM">비즈니스 매니저</option>
                  <option value="SM">영업 매니저</option>
                  <option value="PS">제품 전문가</option>
                  <option value="RND">연구 개발 부서</option>
                  <option value="ICT">정보통신기술</option>
                  <option value="ARD">응용 연구 개발</option>
                  <option value="MM">마케팅 매니저</option>
              </select>
              <br>
            </span>
            <span id="sectorMsg"></span>
          </div>

          <div class="join-row">
            <h3 class="join-title">
              <label for="career">경력</label>
            </h3>
            <span>
              <input name="career" id="career" title="이름" maxlength="10" type="text" placeholder="년 단위로 입력하세요. 없다면 0으로 입력해주세요.">
              <br>
            </span>
            <span id="careerMsg"></span>
          </div>
        </div>
      </div>
      <!-- 스프링 시큐리티를 사용하면 모든 post 전송에 csrf 토큰을 추가해야 함 -->
      <input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }">
      <hr>
      <button id="btnJoin">제출</button>
    </div>
  </form>
  

  
  <script type="text/javascript">
  	$(document).ready(function(){

  		var idFlag = false; // memberId 유효성 변수 
  		var pwFlag = false; // memberPw 유효성 변수 
  		var pwConfirmFlag = false; // pwConfirm 유효성 변수 
  		var nameFlag = false; // memberName 유효성 변수 

  		// blur() : input 태그에서 탭 키나 마우스로 다른 곳을 클릭할 때 이벤트 발생
  		// 아이디 유효성 검사
	  	$('#memberId').blur(function(){ 
	  		console.log('blur()');
			var memberId = $('#memberId').val(); // 입력한 아이디 값
		  	var idRegExp = /^[a-z0-9][a-z0-9_\-]{4,9}$/; 
		  	// 5 ~ 10자 사이의 소문자나 숫자로 시작하고, 소문자, 숫자 밑줄 또는 하이픈을 포함하는 정규표현식
		  	
		  	if(memberId === '') {  // 아이디가 입력되지 않은 경우
		  		$('#idMsg').html('필수 입력입니다.');
		  		$('#idMsg').css('color', 'red');
		  		idFlag = false; // 유효성 false
		  	   	return;
		  	} 
		  	
		  	
				
		  	if(!idRegExp.test(memberId)){ // 입력한 아이디와 정규표현식이 일치하지 않는 경우
		  		$('#idMsg').html('5~10자의 영문 소문자, 숫자와 특수기호(_),(-)만 사용 가능합니다.');
		  		$('#idMsg').css('color', 'red');
		  		idFlag = false; // 유효성 false
		  	} else {
		  		checkId(memberId); // 아이디 중복 확인
		  	} 
		}); // end memberId.blur()
		
	  	
	  	// 아이디 중복 확인 함수
	  	function checkId(memberId) {	
	  		// MemberController의 check()로 memberId 전송 
 			$.ajax({
  				type : 'GET', 
  				url : 'check/' + memberId,
  				success : function(result){
  					if(result == 0) { // 중복되지 않은 경우
  					  	$('#idMsg').html('멋진 아이디네요!');
  					  	$('#idMsg').css('color', 'green'); 
  					    idFlag = true; // 유효성 true
  					} else { // 중복된 경우
  					  	$('#idMsg').html('중복된 아이디입니다!');
  					  	$('#idMsg').css('color', 'red');  
  					  	idFlag = false; // 유효성 false
  					}
 	
  				}
  			}); // end ajax()	  		
	  	} // end checkId()
		
	  	// 비밀번호 유효성 검사
	  	$('#memberPw').blur(function() {
	  		var memberPw = $('#memberPw').val();
	  		var pwRegExp = /^(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$^&*-]).{8,16}$/; 
	  		// 8 ~ 16 사이의 소문자, 숫자, 특수문자를 1개 이상 포함하는 정규 표현식
	  			
	  		if(memberPw === '') { // 비밀번호가 입력되지 않은 경우
	  	  		$('#pwMsg').html('필수 입력입니다.');
	  	  		$('#pwMsg').css('color', 'red');
	  			pwFlag = false; // 유효성 false
	  	  		return;
	  	  	}
	  	  	  		
	  	  	if(!pwRegExp.test(memberPw)) { // 입력한 비밀번호와 정규표현식이 일치하지 않는 경우
	  	  		pwFlag = false; // 유효성 false
	  	  		$('#pwMsg').html('8~16자 영문 소문자, 숫자, 특수문자를 모두 포함하세요');
	  	  		$('#pwMsg').css('color', 'red');
	  	  	} else {
	  	  		$('#pwMsg').html('가능한 비밀번호입니다.');
	  	  		$('#pwMsg').css('color', 'green');
	  	  		pwFlag = true; // 유효성 true
	  	  	}
	  	}); // end memberPw.blur()
	  	
	  	// 비밀번호 확인 유효성 검사
	  	$('#pwConfirm').blur(function() {
	  		var memberPw = $('#memberPw').val();
	  	  	var pwConfirm = $('#pwConfirm').val();
	  	  	
	  	 	// 비밀번호 확인을 입력하지 않은 경우
	  	  	if(pwConfirm === '') {
	  	  		$('#pwConfirmMsg').html('필수 입력입니다.');
	  	  		$('#pwConfirmMsg').css('color', 'red');
	  	  		pwConfirmFlag = false; // 유효성 false
	  	  		
	  	  		return;
	  	 	}
	  	  
	  	 	// 입력한 비밀번호와 비밀번호 확인이 일치하는 경우
	  	  	if(memberPw === pwConfirm) {
	  	  		$('#pwConfirmMsg').html('비밀번호가 일치합니다.');
	  	  		$('#pwConfirmMsg').css('color', 'green');
	  	  		pwConfirmFlag = true; // 유효성 true
	  	  		
	  	  	} else {
	  	  		$('#pwConfirmMsg').html('비밀번호가 일치하지 않습니다.');
	  	  		$('#pwConfirmMsg').css('color', 'red');
	  	  		pwConfirmFlag = false; // 유효성 false
	  	  		
	  	  	}
	  	  
	  	});
	  	
	  	// 이름 유효성 검사
	  	$('#memberName').blur(function() {
			var memberName = $('#memberName').val(); // 입력한 데이터 값

		  	if(memberName.trim() === '') {  // 이름이 입력되지 않았을 경우
		  		$('#nameMsg').html('필수 입력입니다.');
		  		$('#nameMsg').css('color', 'red');
		  		nameFlag = false; // 유효성 false
		  	   	return;
		  	} else {
		  		$('#nameMsg').html('');
		  		nameFlag = true; // 유효성 true
		  	} 

		});
	  	
	 	// 경력 유효성 검사
	  	$('#career').blur(function() {
			var career = $('#career').val(); // 입력한 데이터 값

		  	if(career.trim() === '') {  // 이름이 입력되지 않았을 경우
		  		$('#careerMsg').html('필수 입력입니다.');
		  		$('#careerMsg').css('color', 'red');
		  		nameFlag = false; // 유효성 false
		  	   	return;
		  	} else {
		  		$('#careerMsg').html('');
		  		nameFlag = true; // 유효성 true
		  	} 

		});
	  	
	  	// 회원 정보 form 데이터 전송
	  	$('#btnJoin').click(function(){
	  		console.log('idFlag : ' + idFlag);
	  		console.log('pwFlag : ' + pwFlag);
	  		console.log('pwConfirmFlag : ' + pwConfirmFlag);
	  		console.log('nameFlag : ' + nameFlag);
	  		// setTimeout() : 특정 시간 이후에 코드를 실행하는 함수. Millisecond
	  		setTimeout(function(){
		  		if(idFlag && pwFlag && pwConfirmFlag && nameFlag) { // 입력된 데이터가 모두 유효한 경우
		  			$('#joinForm').submit(); // form 전송 실행
		  		}	  			
	  		}, 500); // 0.5초 후에 실행
	  	});
	  	
  });

  </script>
  
</body>
</html>













