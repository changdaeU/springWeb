<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="resources/css/board.css"/>
<script type="text/javascript">
if(window.name == "update"){
	window.opener.location.href="boardUpdateForm?num=${num}";
}else if(window.name=="delete"){
	window.opener.location.href="boardDelete?num=${num}";
}
self.close();
</script>
</head>
<body>

</body>
</html>