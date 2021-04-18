<%@ page import="java.util.List" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Root User Page</title>
</head>
<body>
	<div align="center">
    	<!-- List all posts in chronological order -->
    	<h2><c:out value="${headerTag}"/></h2>
    	<c:if test="${headerTag eq 'Images'}">
    		<table>
    		<tr>
                <th>ImageId</th>
                <th>Image</th>
                <th>Description</th>
                <th>Poster</th>
                <!--<th>Postdate</th>-->
                <th>Posttime</th>
                <th>Likes</th>
                <th>Tags</th>
            </tr>
    		<c:forEach var="pair" items="${treemapImageAndTag}">
                <tr>
                	<!-- <td> table cell basically -->
                    <td><c:out value="${pair.key.imageid}"/></td>
                    <td><img src="<c:out value="${pair.key.url}" />"></td>
                    <td><c:out value="${pair.key.description}" /></td>
                    <td><c:out value="${pair.key.postuser}" /></td>
                    <!--<td><c:out value="${image.postdate}" /></td>-->
                    <td><c:out value="${pair.key.posttime}" /></td>
                    <td><p>Likes:</p><c:out value="${pair.key.numLikes}" /></td>
                    <td><c:out value='${pair.value}'/></td>
                    <td><a href="showEditImageForm?imageid=<c:out value='${pair.key.imageid}'/>">EDIT</a></td>
                    <td><a href="deleteImage?imageid=<c:out value='${pair.key.imageid}'/>">DELETE</a></td>
            </c:forEach>
        </table>
		</c:if>
		<c:if test="${headerTag eq 'Users'}">
    		<table border="1" cellpadding="5">
            <tr>
                <th>Email</th>
                <!--<th>Password</th>-->
                <th>Birthday</th>
                <th>First Name</th>
                <th>Last Name</th>
                <th>Gender</th>
                <th>Followers</th>
                <th>Following</th>
            </tr>
            <c:forEach var="user" items="${listUser}">
                <tr>
                	<!-- <td> table cell basically -->
                    <td><c:out value="${user.email}" /></td>
                    <!-- <td><c:out value="${user.password}" /></td>-->
                    <td><c:out value="${user.birthday}" /></td>
                    <td><c:out value="${user.firstName}" /></td>
                    <td><c:out value="${user.lastName}" /></td>
                    <td><c:out value="${user.gender}" /></td>
                    <td><c:out value="${user.numFollowers}" /></td>
                    <td><c:out value="${user.numFollowing}" /></td>
                </tr>
            </c:forEach>
        </table>
		</c:if>
		<c:if test="${headerTag eq 'Tags'}">
    		<table border="1" cellpadding="5">
            <tr>
                <th>Tag Name</th>
            </tr>
            <c:forEach var="tag" items="${listTags}">
                <tr>
                    <td><c:out value="${tag}" /></td>
                </tr>
            </c:forEach>
        </table>
		</c:if>
    </div>

</body>
</html>