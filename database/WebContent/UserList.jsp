<%@ page import="java.util.List" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-1">
	<title>User List</title>
	<style>
	h1, h2 {
		text-align: center;
	}
	
	</style>
</head>
<body>
	<nav>
		<h1>User List</h1>
	</nav>
    
    <div align="center">
        <table border="1" cellpadding="5">
            <caption>List of Users</caption>
            <!-- <tr> table row tag -->
            <tr>
            	<!--  <th> table header tag -->
                <th>Email</th>
                <th>Password</th>
                <th>Birthday</th>
                <th>First Name</th>
                <th>Last Name</th>
                <th>Gender</th>
                <th>Followers</th>
                <th>Following</th>
                <th>Follow/Unfollow</th>
            </tr>
            
            <!-- Iterate over collection of items -->
            <!--  ${XXXXX} get attribute from request object -->
            <c:forEach var="user" items="${listUser}">
                <tr>
                	<!-- <td> table cell basically -->
                    <td><c:out value="${user.email}" /></td>
                    <td><c:out value="${user.password}" /></td>
                    <td><c:out value="${user.birthday}" /></td>
                    <td><c:out value="${user.firstName}" /></td>
                    <td><c:out value="${user.lastName}" /></td>
                    <td><c:out value="${user.gender}" /></td>
                    <td><c:out value="${user.numFollowers}" /></td>
                    <td><c:out value="${user.numFollowing}" /></td>
                    <!--  
                    <td>
                        <a href="edit?id=<c:out value='${people.id}' />">Edit</a>
                        &nbsp;&nbsp;&nbsp;&nbsp;
                        <a href="delete?id=<c:out value='${people.id}' />">Delete</a>                     
                    </td>
                    -->
                </tr>
            </c:forEach>
        </table>
    </div>

</body>
</html>