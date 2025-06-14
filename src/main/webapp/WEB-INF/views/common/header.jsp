<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<style>
  header {
    background-color: #fff;
    color: #ecf0f1;
    padding: 15px 30px;
    box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    position: sticky;
    top: 0;
    z-index: 999;
  }

  .header-container {
    display: flex;
    justify-content: space-between;
    align-items: center;
    max-width: 1200px;
    margin: 0 auto;
  }

  .header-container a {
    color: #2c2c2c;
    text-decoration: none;
    margin-right: 20px;
    font-weight: bold;
    transition: color 0.3s;
  }

  .header-container a:hover {
    color: #5dade2;
  }

  .header-section {
    display: flex;
    align-items: center;
  }

  .header-section p {
    margin: 0;
    margin-right: 15px;
    font-weight: bold;
  }

  .header-section form {
    margin: 0;
  }

  .header-section input[type="submit"] {
    background-color: #e74c3c;
    border: none;
    color: white;
    padding: 5px 10px;
    border-radius: 4px;
    cursor: pointer;
    transition: background-color 0.3s;
  }

  .header-section input[type="submit"]:hover {
    background-color: #c0392b;
  }
</style>

<header>
	<div class="header-container">
		<a href="../main">메인</a>
		<div class="header-section">
			<!-- 로그아웃 상태 -->
			<sec:authorize access="isAnonymous()">
				<a href="../member/join">회원가입</a>
				<a href="../auth/login">로그인</a>
			</sec:authorize>

			<!-- 로그인 상태 -->
			<sec:authorize access="isAuthenticated()">
				<p><a href="../web_war/member/info">
					<sec:authentication property="principal.username"/></a></p>
				<form action="../auth/logout" method="post">
					<input type="submit" value="로그아웃">
					<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
				</form>
			</sec:authorize>
		</div>
	</div>
</header>
