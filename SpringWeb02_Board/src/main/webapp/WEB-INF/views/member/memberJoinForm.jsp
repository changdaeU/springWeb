<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix ="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix ="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="resources/css/board.css"/>
<script src="resources/script/board.js"></script>
</head>
<body>
<form name="frm" method="post" action="memberJoin">
<table>
	<tr><th>아이디</th><td><input type="text" name="id" size="20">*
		<input type="button" value="중복체크" onclick="idCheck();">
		<input type="hidden" name="re_id"></td></tr>
	<tr><th>암호</th><td><input type="password" name="pw" size="20">*</td></tr>
	<tr><th>확인</th><td><input type="password" name="pw_check" size="20">*</td></tr>
	<tr><th>이름</th><td><input type="text" name="name" size="20">*</td></tr>
	<tr><th>전화번호</th><td><input type="text" name="phone1" size="5">-
		<input type="text" name="phone2" size="7">-
		<input type="text" name="phone3" size="7"></td></tr>
	<tr><th>이메일</th><td><input type="text" name="email" size="20"></td></tr>
</table><br><br>
<input type="submit" value="등록" onclick="return joinCheck()">
	<input type="reset" value="다시작성">
	<input type="button" value="로그인페이지로" onclick="history.go(-1);">
	<input type="button" value="로그인페이지로" onclick="location.href='/spboard/'">
</form>
</body>
</html>