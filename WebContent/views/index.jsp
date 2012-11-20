<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Unsecured Part</title>
</head>
<body>
<p>This is the unsecured part of the application.</p>
<p>Please log in:</p>
<p>
<form:form action="logIn.do" commandName="indexModel" method="POST">
	<table>
		<tr>
			<td>UserName:</td>
			<td><form:input path="userName" /></td>
		</tr>
		<tr>
			<td>Password:</td>
			<td><form:input path="password" /></td>
		</tr>
	</table>
	<p style="color:red;">${indexModel.error}</p>
	<input type="submit" name="LogInButton" value="Log In"/>
</form:form>
</p>
</body>
</html>