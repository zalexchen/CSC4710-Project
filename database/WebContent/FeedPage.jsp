<%@ page import="java.util.List" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-1">
	<title>Feed Page</title>
	<!-- No idea why but I cannot import CSS file into jsp -->
	<style>
		h1, h2 {
				text-align: center;
		}
		
		body {
		  margin: 0;
		  font-family: Arial, Helvetica, sans-serif;
		}
		
		.topnav {
		  overflow: hidden;
		  background-color: #e9e9e9;
		}
		
		.topnav a {
		  float: left;
		  display: block;
		  color: black;
		  text-align: center;
		  padding: 14px 16px;
		  text-decoration: none;
		  font-size: 17px;
		}
		
		.topnav a:hover {
		  background-color: #ddd;
		  color: black;
		}
		
		.topnav a.active {
		  background-color: #2196F3;
		  color: white;
		}
		
		.topnav .search-container {
		  float: right;
		}
		
		.topnav input[type=text] {
		  padding: 6px;
		  margin-top: 8px;
		  font-size: 17px;
		  border: none;
		}
		
		.topnav .search-container button {
		  float: right;
		  padding: 6px 10px;
		  margin-top: 8px;
		  margin-right: 16px;
		  background: #ddd;
		  font-size: 17px;
		  border: none;
		  cursor: pointer;
		}
		
		.topnav .search-container button:hover {
		  background: #ccc;
		}
		
		@media screen and (max-width: 600px) {
		  .topnav .search-container {
		    float: none;
		  }
		  .topnav a, .topnav input[type=text], .topnav .search-container button {
		    float: none;
		    display: block;
		    text-align: left;
		    width: 100%;
		    margin: 0;
		    padding: 14px;
		  }
		  .topnav input[type=text] {
		    border: 1px solid #ccc;  
		  }
		}
	</style>
</head>

<body>
	<h1>Feed Page</h1>
	<nav>
		<div class="topnav">
			<a class="active" href="feedpage">Home</a>
			<a href="showPostImageForm">Post an Image</a>
			<a href="logout">Logout</a>
			<div class="search-container">
			    <form action="search">
			      	<input type="text" placeholder="Search for Users..." name="searchText" id="searchText" required>
			      	<select name="searchParameter" id="searchParameter" size="1">
				      	<option>First Name</option>
				      	<option>Last Name</option>
				      	<option>Both</option>
				    </select>
			      	<button type="submit">Submit</button>
			    </form>
			</div>
		</div>
		<!-- Create post navigation -->
	</nav>
    
    <div align="center">
    	<!-- List all posts in chronological order -->
    	<h2>Image Post Listing</h2>
    	<table>
    		<c:forEach var="image" items="${listImage}">
    			
                <tr>
                	<!-- <td> table cell basically -->
                    <td><c:out value="${image.imageid}"/></td>-->
                    <td><img src="<c:out value="${image.url}" />"></td>
                    <td><c:out value="${image.description}" /></td>
                    <td><c:out value="${image.postuser}" /></td>
                    <td><c:out value="${image.postdate}" /></td>
                    <td><c:out value="${image.posttime}" /></td>
                    <td><a href="showEditImageForm?imageid=<c:out value='${image.imageid}'/>">EDIT</a></td>
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
    	<!-- Each post also needs to have like and unlike feature -->
    	<!-- Each post by the currentUser must be editable -->
    </div>

</body>
</html>