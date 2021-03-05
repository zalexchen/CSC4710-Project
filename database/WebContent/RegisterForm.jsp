<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
		<!--
	    	&nbsp;&nbsp;&nbsp;
	    <a href="list">List Users</a>
	    -->
	             
	</h2>
	<!-- Create Login Form with action "login" -->
	<c:if test="${failed != null}">
		Account Already Exists Failure
	</c:if>
	<form action="register" method="post">
		<!--  Grouping element -->
		<fieldset>
		<legend>Put in your account information below</legend>
		<ul>
			<li><label for="email">Email: </label> <input type ="text" name="email" id="email" required placeholder="Enter your email">
			<li><label for="password">Password: </label> <input type="password" name="password" id="password" required placeholder="Enter your password">
	<!-- 	<li><label for="password-confirmation">Confirm Password: </label> <input type="password" name="password-confirm" id="password-confirm" required> -->
			<li><label for="password-confirmation">Confirm Password: </label> <input type="password" name="password-confirm" id="password-confirm" 
			 onchange="check()" required placeholder="Re-enter your password">
			<span id='message'></span>
			
			<li><label for="birthday">Birthday: </label> <input type="date" name="birthday" id="birthday" required placeholder="YYYY-MM-DD">
			<li><label for="firstName">First Name: </label> <input type="text" name="firstName" id="firstName" required placeholder="John">
			<li><label for="lastName">Last Name: </label> <input type="text" name="lastName" id="lastName" required placeholder="Smith">
			<li><label for="gender">Gender: </label>
			<select name="gender" id="gender" size="1">
		      	<option>Male</option>
		      	<option>Female</option>
		      	<option>Other</option>
		    </select>
		</ul>
		<script type="text/javascript">
			function check() {
			    if(document.getElementById('password').value ===
			            document.getElementById('password-confirm').value) {
			        document.getElementById('message').innerHTML = "match";
			        document.getElementById('submits').disabled = false;
			    } else {
			        document.getElementById('message').innerHTML = "no match";
			    	document.getElementById('submits').disabled = true;
			    }
			}
		</script>

		
		<br>
		</fieldset>
	<!-- Create Submit and Reset Buttons -->	
	<p>
		<input type="submit" id="submits" value="Register">
		<input type="reset" value="Start Over">
	</p>
	</form>
</body>
</html>