<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="_csrf" content="${_csrf.token}"/>
<meta name="_csrf_header" content="${_csrf.headerName}"/>

<title>${boardDTO.boardTitle }</title>
<spring:htmlEscape defaultHtmlEscape="true" />

<!-- Google Fonts & jQuery -->
<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR&display=swap" rel="stylesheet">
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>

<!-- Custom CSS -->
<link rel="stylesheet" href="${pageContext.request.contextPath }/resources/css/image.css">
<link rel="stylesheet" href="${pageContext.request.contextPath }/resources/css/attach.css">

<style>
body {
    font-family: 'Noto Sans KR', sans-serif;
    margin: 0;
    padding: 0;
    background-color: #f9fafb;
    color: #333;
}

.container {
    max-width: 960px;
    margin: 30px auto;
    padding: 20px;
    background: #fff;
    box-shadow: 0 4px 10px rgba(0,0,0,0.05);
    border-radius: 12px;
}

h2 {
    margin-bottom: 20px;
    color: #222;
}

.section {
    margin-bottom: 30px;
}

textarea {
    width: 100%;
    padding: 10px;
    font-size: 16px;
    resize: vertical;
    border-radius: 6px;
    border: 1px solid #ccc;
    background: #fdfdfd;
}

button {
    background-color: #007bff;
    color: #fff;
    padding: 8px 16px;
    border: none;
    margin: 5px;
    border-radius: 6px;
    cursor: pointer;
}

button:hover {
    background-color: #0056b3;
}

.image-list, .attach-list {
    display: flex;
    flex-wrap: wrap;
    gap: 10px;
}

.image_item, .attach_item {
    background: #f1f3f5;
    padding: 10px;
    border-radius: 8px;
    text-align: center;
}

.image_item img {
    max-width: 100px;
    max-height: 100px;
    border-radius: 4px;
}

.reply-section input[type="text"] {
    width: 70%;
    padding: 8px;
    margin: 10px 0;
    border-radius: 6px;
    border: 1px solid #ccc;
}

@media (max-width: 600px) {
    .image-list, .attach-list {
        flex-direction: column;
        align-items: center;
    }
}
</style>
</head>
<body>

<%@ include file="../common/header.jsp" %>

<div class="container">
    <h2>글 보기</h2>

    <div class="section">
        <p><strong>글 번호:</strong> ${boardDTO.boardId }</p>
        <p><strong>제목:</strong> <c:out value="${boardDTO.boardTitle }" /></p>
        <p><strong>작성자:</strong> ${boardDTO.memberId }</p>
        <p><strong>작성일:</strong> <fmt:formatDate value="${boardDTO.boardDateCreated }"
				pattern="YY년 MM월 dd일 HH시 mm분" /></p>
    </div>

    <div class="section">
        <textarea rows="10" readonly>${boardDTO.boardContent }</textarea>
    </div>

    <div class="section">
        <button id="listBoard">글 목록</button>
        <sec:authentication property="principal" var="user"/>
        <sec:authorize access="isAuthenticated()">
            <c:if test="${boardDTO.memberId eq user.username}">
                <button id="modifyBoard">글 수정</button>
                <button id="deleteBoard">글 삭제</button>
            </c:if>
        </sec:authorize>
    </div>

    <form id="listForm" action="list" method="GET">
        <input type="hidden" name="pageNum">
        <input type="hidden" name="pageSize">
        <input type="hidden" name="type">
        <input type="hidden" name="keyword">
    </form>
    <form id="modifyForm" action="modify" method="GET">
        <input type="hidden" name="boardId">
        <input type="hidden" name="pageNum">
        <input type="hidden" name="pageSize">
        <input type="hidden" name="type">
        <input type="hidden" name="keyword">
    </form>
    <form id="deleteForm" action="delete" method="POST">
        <input type="hidden" name="boardId">
        <input type="hidden" name="pageNum">
        <input type="hidden" name="pageSize">
        <input type="hidden" name="type">
        <input type="hidden" name="keyword">
        <input type="hidden" name="memberId" value="${boardDTO.memberId}">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
    </form>

    <!-- 이미지 파일 -->
    <div class="section">
        <h2>이미지 파일</h2>
        <div class="image-list">
            <c:forEach var="attachDTO" items="${boardDTO.attachList}">
                <c:if test="${attachDTO.attachExtension eq 'jpg' || attachDTO.attachExtension eq 'jpeg' || attachDTO.attachExtension eq 'png' || attachDTO.attachExtension eq 'gif'}">
                    <div class="image_item">
                        <a href="../image/get?attachId=${attachDTO.attachId}" target="_blank">
                            <img src="../image/get?attachId=${attachDTO.attachId}&attachExtension=${attachDTO.attachExtension}" />
                        </a>
                    </div>
                </c:if>
            </c:forEach>
        </div>
    </div>

    <!-- 첨부 파일 -->
    <div class="section">
        <h2>첨부 파일</h2>
        <div class="attach-list">
            <c:forEach var="attachDTO" items="${boardDTO.attachList}">
                <c:if test="${not (attachDTO.attachExtension eq 'jpg' || attachDTO.attachExtension eq 'jpeg' || attachDTO.attachExtension eq 'png' || attachDTO.attachExtension eq 'gif')}">
                    <div class="attach_item">
                        <a href="../attach/download?attachId=${attachDTO.attachId}">${attachDTO.attachRealName}.${attachDTO.attachExtension}</a>
                    </div>
                </c:if>
            </c:forEach>
        </div>
    </div>

    <!-- 댓글 작성 -->
    <div class="section reply-section">
        <h2>댓글 작성</h2>
        <sec:authorize access="isAnonymous()">
            <p>* 댓글을 작성하려면 로그인해 주세요.</p>
        </sec:authorize>
        <sec:authorize access="isAuthenticated()">
            <div style="text-align: center;">
                <p>${user.username }</p>
                <input type="hidden" id="memberId" value="${user.username }">
                <input type="text" id="replyContent" maxlength="150">
                <button id="btnAdd">작성</button>
            </div>
        </sec:authorize>
        <input type="hidden" id="boardId" value="${boardDTO.boardId }">
        <div id="replies" style="margin-top: 20px;"></div>
    </div>
</div>

<%@ include file="../common/footer.jsp" %>

<script>
$(document).ajaxSend(function(e, xhr, opt){
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    xhr.setRequestHeader(header, token);
});

$(document).ready(function(){
    function setFormValues(form) {
        form.find("input[name='pageNum']").val("<c:out value='${pagination.pageNum }' />");
        form.find("input[name='pageSize']").val("<c:out value='${pagination.pageSize }' />");
        form.find("input[name='type']").val("<c:out value='${pagination.type }' />");
        form.find("input[name='keyword']").val("<c:out value='${pagination.keyword }' />");
    }

    $("#listBoard").click(function(){
        const form = $("#listForm");
        setFormValues(form);
        form.submit();
    });

    $("#modifyBoard").click(function(){
        const form = $("#modifyForm");
        form.find("input[name='boardId']").val("<c:out value='${boardDTO.boardId}' />");
        setFormValues(form);
        form.submit();
    });

    $("#deleteBoard").click(function(){
        if(confirm('삭제하시겠습니까?')) {
            const form = $("#deleteForm");
            form.find("input[name='boardId']").val("<c:out value='${boardDTO.boardId}' />");
            setFormValues(form);
            form.submit();
        }
    });
});
</script>

<script src="${pageContext.request.contextPath }/resources/js/reply.js"></script>

</body>
</html>
