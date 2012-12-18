<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Secure Part</title>
</head>
<body>
<p>${user.userName}, this is still the secure part of the application.</p>
<p> We now have a user object with a few attributes in the session.</p>
	<table>
		<tr>
			<td>First Name:</td>
			<td>${user.userName}</td>
		</tr>
		<tr>
			<td>Company:</td>
			<td>${user.companyName}</td>
		</tr>
		<tr>
			<td>Street:</td>
			<td>${user.street}</td>
		</tr>
		<tr>
			<td>Suburb:</td>
			<td>${user.city}</td>
		</tr>
		<tr>
			<td>State:</td>
			<td>${user.state}</td>
		</tr>
		<tr>
			<td>Postcode:</td>
			<td>${user.code}</td>
		</tr>
		<tr>
			<td>Country:</td>
			<td>${user.country}</td>
		</tr>
	</table>

	<form action="../logOut.do" method="POST">
		<input type="submit" name="LogOut" value="log out"/>
	</form>
</body>
</html>