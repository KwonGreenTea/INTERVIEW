<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="_csrf" content="${_csrf.token}"/>
<meta name="_csrf_header" content="${_csrf.headerName}"/>
<!-- css 파일 불러오기 -->
<link rel="stylesheet"
	href="${pageContext.request.contextPath }/resources/css/image.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath }/resources/css/attach.css">
<!-- jquery 라이브러리 import -->
<script src="https://code.jquery.com/jquery-3.7.1.js">
</script>
<title>${boardDTO.boardTitle }</title>
</head>
<body>
	<%@ include file="../common/header.jsp" %>
	<h2>글 수정 페이지</h2>
	<form id="modifyForm" action="modify" method="POST">
		<input type="hidden" name="pageNum" value="${pagination.pageNum}">
		<input type="hidden" name="pageSize" value="${pagination.pageSize}">
		<input type="hidden" name="type" value="${pagination.type}">
		<input type="hidden" name="keyword" value="${pagination.keyword}">
		
		<div>
			<p>번호 :</p>
			<input type="text" name="boardId" value="${boardDTO.boardId }"
				readonly>
		</div>
		<div>
			<p>제목 :</p>
			<input type="text" name="boardTitle" placeholder="제목 입력"
				maxlength="20" value="<c:out value="${boardDTO.boardTitle }" />" required>
		</div>
		<div>
			<p>작성자 : ${boardDTO.memberId}</p>
			<input type="hidden" name="memberId" value="${boardDTO.memberId}">
		</div>
		<div>
			<p>내용 :</p>
			<textarea rows="20" cols="120" name="boardContent"
				placeholder="내용 입력" maxlength="300" required>${boardDTO.boardContent }</textarea>
		</div>
		<!-- 기존 첨부 파일 리스트 데이터 구성 -->
		<c:forEach var="attachDTO" items="${boardDTO.attachList}" varStatus="status">
			<input type="hidden" class="input-attach-list" name="attachList[${status.index }].attachPath" value="${attachDTO.attachPath }">
			<input type="hidden" class="input-attach-list" name="attachList[${status.index }].attachRealName" value="${attachDTO.attachRealName }">
			<input type="hidden" class="input-attach-list" name="attachList[${status.index }].attachChgName" value="${attachDTO.attachChgName }">
			<input type="hidden" class="input-attach-list" name="attachList[${status.index }].attachExtension" value="${attachDTO.attachExtension }">
		</c:forEach>
		<input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }">
	</form>
	
	<hr>
	
	<button id="change-upload">파일 변경</button>
	<!-- 이미지 파일 영역 -->
	<div class="image-upload">
		<div class="image-view">
			<h2>이미지 파일 리스트</h2>
			<div class="image-list">
				<!-- 이미지 파일 처리 코드 -->
				<c:forEach var="attachDTO" items="${boardDTO.attachList}">
				    <c:if test="${attachDTO.attachExtension eq 'jpg' or 
				    			  attachDTO.attachExtension eq 'jpeg' or 
				    			  attachDTO.attachExtension eq 'png' or 
				    			  attachDTO.attachExtension eq 'gif'}">
				        <div class="image_item">
				        	<a href="../image/get?attachId=${attachDTO.attachId }" target="_blank">
					        <img width="100px" height="100px" 
					        src="../image/get?attachId=${attachDTO.attachId }&attachExtension=${attachDTO.attachExtension}"/></a>
				        </div>
				    </c:if>
				</c:forEach>
			</div>
		</div>
		<div class="image-modify" style="display : none;">
			<h2>이미지 파일 업로드</h2>
			<p>* 이미지 파일은 최대 3개까지 가능합니다.</p>
			<p>* 최대 용량은 10MB 입니다.</p>
			<div class="image-drop"></div>
			<h2>선택한 이미지 파일 :</h2>
			<div class="image-list"></div>
		</div>
	</div>
	
	<!-- 첨부 파일 영역 -->
	<div class="attach-upload">
		<div class="attach-view">
			<h2>첨부 파일 리스트</h2>
			<div class="attach-list">
			<c:forEach var="attachDTO" items="${boardDTO.attachList}">
		 		<c:if test="${not (attachDTO.attachExtension eq 'jpg' or 
			    			  attachDTO.attachExtension eq 'jpeg' or 
			    			  attachDTO.attachExtension eq 'png' or 
			    			  attachDTO.attachExtension eq 'gif')}">
			    	<div class="attach_item">
			    	<p><a href="../attach/download?attachId=${attachDTO.attachId }">${attachDTO.attachRealName }.${attachDTO.attachExtension }</a></p>
			    	</div>
			    </c:if>
			</c:forEach>		
			</div>
		</div>
		<div class="attach-modify" style="display : none;">
			<h2>첨부 파일 업로드</h2>
			<p>* 첨부 파일은 최대 3개까지 가능합니다.</p>
			<p>* 최대 용량은 10MB 입니다.</p>
			<input type="file" id="attachInput" name="files" multiple="multiple"><br>
			<h2>선택한 첨부 파일 정보 :</h2>
			<div class="attach-list"></div>
		</div>
	</div>
	
	<div class="attachDTOImg-list">
	</div>
	
	<div class="attachDTOFile-list">
	</div>

	<button id="modifyBoard">등록</button>

	<script src="${pageContext.request.contextPath }/resources/js/image.js"></script>
	<script	src="${pageContext.request.contextPath }/resources/js/attach.js"></script>
	
	<script type="text/javascript">
	
	$(document).ajaxSend(function(e, xhr, opt){
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		
		xhr.setRequestHeader(header, token);
	});
	
	$(document).ready(function(){
	    // 이미지 변경 버튼 클릭 시
	    $('#change-upload').click(function(){
	    	if(!confirm('기존에 업로드 파일들은 삭제됩니다. 계속 하시겠습니까?')){
	    		return;
	    	}
	        $('.image-modify').show();
	        $('.image-view').hide();
	        $('.attach-modify').show();
	        $('.attach-view').hide();
	        $('.input-attach-list').remove(); // input-attach-list 삭제
	    });

		// modifyForm 데이터 전송
		$('#modifyBoard').click(function() {
			// form 객체 참조
			var modifyForm = $('#modifyForm');
			
			// attachDTOImg-list의 각 input 태그 접근
			var i = 0;
			$('.attachDTOImg-list input[name="attachDTO"]').each(function(){
				console.log(this);
				// JSON attachDTO 데이터를 object 변경
				var attachDTO = JSON.parse($(this).val());
				// attachPath input 생성
				var inputPath = $('<input>').attr('type', 'hidden')
						.attr('name', 'attachList[' + i + '].attachPath');
				inputPath.val(attachDTO.attachPath);
				
				// attachRealName input 생성
				var inputRealName = $('<input>').attr('type', 'hidden')
						.attr('name', 'attachList[' + i + '].attachRealName');
				inputRealName.val(attachDTO.attachRealName);
				
				// attachChgName input 생성
				var inputChgName = $('<input>').attr('type', 'hidden')
						.attr('name', 'attachList[' + i + '].attachChgName');
				inputChgName.val(attachDTO.attachChgName);
				
				// attachExtension input 생성
				var inputExtension = $('<input>').attr('type', 'hidden')
						.attr('name', 'attachList[' + i + '].attachExtension');
				inputExtension.val(attachDTO.attachExtension);
				
				// form에 태그 추가
				modifyForm.append(inputPath);
				modifyForm.append(inputRealName);
				modifyForm.append(inputChgName);
				modifyForm.append(inputExtension);
				
				i++;
			});
			
			// attachDTOFile-list의 각 input 태그 접근
			$('.attachDTOFile-list input[name="attachDTO"]').each(function(){
				console.log(this);
				// JSON attachDTO 데이터를 object 변경
				var attachDTO = JSON.parse($(this).val());
				// attachPath input 생성
				var inputPath = $('<input>').attr('type', 'hidden')
						.attr('name', 'attachList[' + i + '].attachPath');
				inputPath.val(attachDTO.attachPath);
				
				// attachRealName input 생성
				var inputRealName = $('<input>').attr('type', 'hidden')
						.attr('name', 'attachList[' + i + '].attachRealName');
				inputRealName.val(attachDTO.attachRealName);
				
				// attachChgName input 생성
				var inputChgName = $('<input>').attr('type', 'hidden')
						.attr('name', 'attachList[' + i + '].attachChgName');
				inputChgName.val(attachDTO.attachChgName);
				
				// attachExtension input 생성
				var inputExtension = $('<input>').attr('type', 'hidden')
						.attr('name', 'attachList[' + i + '].attachExtension');
				inputExtension.val(attachDTO.attachExtension);
				
				// form에 태그 추가
				modifyForm.append(inputPath);
				modifyForm.append(inputRealName);
				modifyForm.append(inputChgName);
				modifyForm.append(inputExtension);
				
				i++;
			});
			modifyForm.submit();
		});
	});
	</script>
</body>
</html>