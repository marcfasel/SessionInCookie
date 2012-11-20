<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Logged In</title>
</head>
<body>
<p>Hi ${userName}! You are now logged in and in the secure part of the application. Please proceed to the next page.</p>
<form action="secure/secureNext.do" method="POST">
	<input type="submit" name="Next" value="next"/>
</form>
</body>
</html>