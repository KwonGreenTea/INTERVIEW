<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
	<meta charset="UTF-8">
    <title>인터뷰버디</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://code.jquery.com/jquery-3.7.1.js"></script>
    <style>
        .board-section {
            max-width: 900px;
            margin: 30px auto;
        }
        .pagination li {
            margin: 0 3px;
        }
        .detail_button {
		    cursor: pointer;
		}
    </style>
</head>
<body>
    <%@ include file="../common/header.jsp" %>

    <div class="board-section">
        <!-- 글 작성 버튼 -->
        <div class="d-flex justify-content-between mb-3">
            <a href="register" class="btn btn-primary">글 작성</a>
            <select id="selectSize" class="form-select w-auto">
                <option value="" disabled selected>게시글 수</option>
                <option value="5">5</option>
                <option value="10">10</option>
                <option value="15">15</option>
                <option value="20">20</option>
            </select>
        </div>

        <!-- 게시판 테이블 -->
        <table class="table table-bordered table-hover text-center">
            <thead class="table-light">
                <tr>
                    <th style="width: 50%">제목</th>
                    <th style="width: 20%">작성자</th>
                    <th style="width: 30%">작성일</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="boardDTO" items="${boardList}">
                    <tr class="detail_button" data-id="${boardDTO.boardId}">
					    <td><c:out value="${boardDTO.boardTitle}" /></td>
					    <td>${boardDTO.memberId}</td>
					    <fmt:formatDate value="${boardDTO.boardDateCreated}" pattern="yyyy-MM-dd HH:mm:ss" var="boardDateCreated" />
					    <td>${boardDateCreated}</td>
					</tr>
                </c:forEach>
            </tbody>
        </table>

        <!-- 검색 폼 -->
        <form id="searchForm" action="list" method="get" class="d-flex justify-content-center gap-2 mb-4">
            <input type="hidden" name="pageNum">
            <input type="hidden" name="pageSize">
            <select name="type" class="form-select w-auto">
                <option value="title">제목</option>
                <option value="content">내용</option>
                <option value="writer">작성자</option>
            </select>
            <input type="text" name="keyword" class="form-control w-50" placeholder="검색어 입력">
            <button class="btn btn-secondary">검색</button>
        </form>

        <!-- 페이징 -->
        <ul class="pagination justify-content-center">
            <c:if test="${pageMaker.isPrev()}">
                <li class="page-item">
                    <a class="page-link" href="${pageMaker.startNum - 1}">이전</a>
                </li>
            </c:if>
            <c:forEach begin="${pageMaker.startNum}" end="${pageMaker.endNum}" var="num">
                <li class="page-item ${pageMaker.pagination.pageNum == num ? 'active' : ''}">
                    <a class="page-link" href="${num}">${num}</a>
                </li>
            </c:forEach>
            <c:if test="${pageMaker.isNext()}">
                <li class="page-item">
                    <a class="page-link" href="${pageMaker.endNum + 1}">다음</a>
                </li>
            </c:if>
        </ul>

        <!-- 히든 폼들 -->
        <form id="detailForm" action="detail" method="get">
            <input type="hidden" name="boardId">
            <input type="hidden" name="pageNum">
            <input type="hidden" name="pageSize">
            <input type="hidden" name="type">
            <input type="hidden" name="keyword">
        </form>

        <form id="listForm" action="list" method="get">
            <input type="hidden" name="pageNum">
            <input type="hidden" name="pageSize">
            <input type="hidden" name="type">
            <input type="hidden" name="keyword">
        </form>
    </div>

    <%@ include file="../common/footer.jsp" %>

    <script>
        $(function () {
            $("#selectSize").on("change", function () {
                const listForm = $("#listForm");
                const pageNum = "<c:out value='${pageMaker.pagination.pageNum}' />";
                const pageSize = $(this).val();
                const type = "<c:out value='${pageMaker.pagination.type}' />";
                const keyword = "<c:out value='${pageMaker.pagination.keyword}' />";

                listForm.find("input[name='pageNum']").val(pageNum);
                listForm.find("input[name='pageSize']").val(pageSize);
                listForm.find("input[name='type']").val(type);
                listForm.find("input[name='keyword']").val(keyword);
                listForm.submit();
            });

            $(".pagination a").on("click", function (e) {
                e.preventDefault();
                const listForm = $("#listForm");
                const pageNum = $(this).attr("href");
                const pageSize = "<c:out value='${pageMaker.pagination.pageSize}' />";
                const type = "<c:out value='${pageMaker.pagination.type}' />";
                const keyword = "<c:out value='${pageMaker.pagination.keyword}' />";

                listForm.find("input[name='pageNum']").val(pageNum);
                listForm.find("input[name='pageSize']").val(pageSize);
                listForm.find("input[name='type']").val(type);
                listForm.find("input[name='keyword']").val(keyword);
                listForm.submit();
            });

            $(".detail_button").on("click", function () {
                var boardId = $(this).data("id");
                var pageNum = "<c:out value='${pageMaker.pagination.pageNum}' />";
                var pageSize = "<c:out value='${pageMaker.pagination.pageSize}' />";
                var type = "<c:out value='${pageMaker.pagination.type}' />";
                var keyword = "<c:out value='${pageMaker.pagination.keyword}' />";

                var form = $("#detailForm");
                form.find("input[name='boardId']").val(boardId);
                form.find("input[name='pageNum']").val(pageNum);
                form.find("input[name='pageSize']").val(pageSize);
                form.find("input[name='type']").val(type);
                form.find("input[name='keyword']").val(keyword);
                form.submit();
            });

            $("#searchForm button").on("click", function (e) {
                e.preventDefault();
                const searchForm = $("#searchForm");
                const keywordVal = searchForm.find("input[name='keyword']").val();

                if (keywordVal.trim() === '') {
                    alert('검색 내용을 입력하세요.');
                    return;
                }

                searchForm.find("input[name='pageNum']").val(1);
                const pageSize = "<c:out value='${pageMaker.pagination.pageSize}' />";
                searchForm.find("input[name='pageSize']").val(pageSize);
                searchForm.submit();
            });
        });
    </script>
</body>
</html>
