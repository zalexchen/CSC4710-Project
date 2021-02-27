<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-1">
	<title>Register Form</title>
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
	<h1>Account Registration</h1>
	<h2>
		<a href="home">Login</a>
	    	&nbsp;&nbsp;&nbsp;
	    <a href="list">List All People</a>
	             
	</h2>
	<!-- Create Login Form with action "login" -->
	<form action="register" method="post">
		<!--  Grouping element -->
		<fieldset>
		<legend>Put in your account information below</legend>
		<ul>
			<li><label for="email">Email: </label> <input type ="text" name="email" id="email" required placeholder="Enter your email">
			<li><label for="password">Password: </label> <input type="password" name="password" id="password" required placeholder="Enter your password">
	<!-- 	<li><label for="password-confirmation">Confirm Password: </label> <input type="password" name="password-confirm" id="password-confirm" required> -->
			<li><label for="bday">Birthday: </label> <input type="date" name="bday" id="bday" required placeholder="YYYY-MM-DD">
			<li><label for="firstName">First Name: </label> <input type="text" name="firstName" id="firstName" required placeholder="John">
			<li><label for="lastName">Last Name: </label> <input type="text" name="lastName" id="lastName" required placeholder="Smith">
			<li><label for="gender">Gender: </label>
			<select name="gender" id="gender" size="1">
		      	<option>Male</option>
		      	<option>Female</option>
		      	<option>Other</option>
		    </select>
		</ul>
		<br>
		</fieldset>
	<!-- Create Submit and Reset Buttons -->	
	<p>
		<input type="submit" value="Register">
		<input type="reset" value="Start Over">
	</p>
	</form>
</body>
</html>