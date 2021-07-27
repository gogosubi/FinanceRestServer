<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
    
<%@include file="../layout/header.jsp" %>
<div class="container">
	<form action="/auth/loginProc" method="post">
	  <div class="form-group">
	    <label for="username">User Name:</label>
	    <input type="text" name="username" class="form-control" placeholder="Enter user name" id="username">
	  </div>
	  <div class="form-group">
	    <label for="pwd">Password:</label>
	    <input type="password" name="password" class="form-control" placeholder="Enter password" id="password">
	  </div>
	  <div class="form-group form-check">
	    <label class="form-check-label">
	      <input name="remember" class="form-check-input" type="checkbox"> Remember me
	    </label>
	  </div>
	  <button id="btn-login" class="btn btn-primary">로그인</button>
	  <a href="https://kauth.kakao.com/oauth/authorize?client_id=0eb4722c771f7faca22eeb0910771009&redirect_uri=http://localhost:8000/auth/kakao/callback&response_type=code"><img height="38px" src="/image/kakao_login_button.png"></a>
	</form>
</div>

<!-- 
전통적인 방식의 로그인 방법(삭제)
spring security를 이용하면서 주소도 변경함. 
<script src="/js/user.js"></script>
-->
<%@include file="../layout/footer.jsp" %>