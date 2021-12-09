<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix ="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix ="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>login Form</title>
<link rel="stylesheet" type="text/css" href="resources/css/board.css"/>
<!-- 화면에 나오는 wabapp을 기준으로 폴더를 설정하면 된다. -->
<script src="resources/script/board.js"></script>
</head>
<body>
<form action="login" method="post" name="frm">
	<div class="box2"><div id="title">로그인</div></div>
	<div class="box2"><div class="attr1">아이디</div>
		<div class="attr2">&nbsp;&nbsp;<input type="text"size="20" name="id"
			 style="width:200px;height:20px;"></div>
	</div>
	<div class="box2">
		<div class="attr1">비밀번호</div>
		<div class="attr2">&nbsp;&nbsp;<input type="password"size="20" name="pw"
			 style="width:200px;height:20px;"></div>
	</div>
	<div class="box2">
		<div id="footer"><input type="submit" value="로그인" onclick="return loginCheck();"/>
			<input type="reset" value="다시작성"/>
			<input type="button" value="회원가입" onClick="location.href='memberJoinForm'"/>
		</div>
	</div>
	<div class="box2"><div id="footer">${message}</div></div>
</form>
</body>
</html>