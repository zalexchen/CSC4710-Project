<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-1">
	<title>Login Form</title>
	<style>
		h1 {
			text-align: center;
		}
		h2 {
			text-align: center;
		}
	
	</style>
</head>
<body>
	<h1>Login</h1>
	<h2>
		<a href="newUser">Register</a>
		<!-- 
	    	&nbsp;&nbsp;&nbsp;
	    <a href="list">List Users</a>
	    -->
	</h2>
	<!-- Create Login Form with action "login" -->
	<form action="login" method="post">
		<!--  Grouping element -->
		<fieldset>
		<legend>Login with your Account Below</legend>
		<ul>
			<li><label for="email">Email:</label> <input type ="text" name="email" id="email">
			<li><label for="password">Password</label> <input type="password" name="password" id="password">
		</ul>
		<br>
		</fieldset>
	<!-- Create Submit and Reset Buttons -->	
	<p>
		<input type="submit" value="Login">
		<input type="reset" value="Start Over">
	</p>
	</form>
</body>
</html>