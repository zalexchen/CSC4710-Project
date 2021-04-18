<%@ page import="java.util.List" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-1">
	<title>Initialize Database</title>
</head>
<body>
	<!-- Include all buttons for root user special functions (10) -->
	<!-- 1: [Cool images]: List the images that are liked by at least 5 users. -->
	<form action="coolImages" method="GET">
		<input type="submit" value="[Cool images]" />
	</form>
	<!-- 2: [New images]: List those images that are just posted TODAY. -->
	<form action="newImages" method="GET">
		<input type="submit" value="[New images]" />
	</form>
	<!-- 3: [Viral images]: List the top 3 images who received the most number of likes, from the most to the least. -->
	<form action="viralImages" method="GET">
		<input type="submit" value="[Viral images]" />
	</form>
	<!-- 4: [Top users]: List users that have the most number of postings. List the top user if there is no tie, list all the tied top users if there is a tie. -->
	<form action="topUsers" method="GET">
		<input type="submit" value="[Top users]" />
	</form>
	<!-- 5: [Popular users]: List those users that  are followed by at least 5 followers. -->
	<form action="popularUsers" method="GET">
		<input type="submit" value="[Popular users]" />
	</form>
	<!-- 6: [Common users] Given two users, X, and Y, which are specified by the user with two dropdown menu lists, list the common users that both X and Y have followed. -->
	<form action="commonUsers" method="GET">
		<input type="submit" value="[Common users]" />
		<!-- two dropdown lists? -->
		<select name="user1" id="user1" size="1">
			<c:forEach var="user" items="${listUser}">
				<option><c:out value="${user.email}" /></option>
			</c:forEach>
		</select>
		<select name="user2" id="user2" size="1">
		    <c:forEach var="user" items="${listUser}">
				<option><c:out value="${user.email}" /></option>
			</c:forEach>
		</select>
	</form>
	<!-- 7: [Top tags]: list those tags that are used by at least 3 users. -->
	<form action="topTags" method="GET">
		<input type="submit" value="[Top tags]" />
	</form>
	<!-- 8: [Positive users] List those users that give a like to each image that are posed by their followings. -->
	<form action="positiveUsers" method="GET">
		<input type="submit" value="[Positive users]" />
	</form>
	<!-- 9: [Poor  images]. List those images that nobody has given any like and nobody has given any comment. -->
	<form action="poorImages" method="GET">
		<input type="submit" value="[Poor images]" />
	</form>
	<!-- 10: [Inactive users]. List those users who have never posted any image, followed any other user, and given any like or comment. -->
	<form action="inactiveUsers" method="GET">
		<input type="submit" value="[Inactive users]" />
	</form>
	
	<form action="initDB" method="POST">
		<p>
		<input type="submit" value="Initialize Database">
		</p>
	</form>
</body>
</html>