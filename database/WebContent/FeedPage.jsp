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
			<a class="active" href="showFeedPage">Home</a>
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
    		<tr>
                <th>ImageId</th>
                <th>Image</th>
                <th>Description</th>
                <th>Poster</th>
                <!--<th>Postdate</th>-->
                <th>Posttime</th>
                <th>Likes</th>
                <th>Tags</th>
                <th>Comments</th>
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
                    <td>
                    
                    	<table>
                    		<td>
			                    <c:set var="commented" value="false" />
								<c:forEach var="imageid" items="${listComments}">
								  	<c:if test="${imageid eq pair.key.imageid}">
								    	<c:set var="commented" value="true" />
								  	</c:if>
								</c:forEach>
								<c:choose>
								
								
									<c:when test="${commented eq true}">
										<!-- display edit and delete -->
										<form action="showEditCommentForm?imageid=<c:out value='${pair.key.imageid}'/>" method="post">
											<c:if test="${pair.key != null}">
												<!--<c:out value='${pair.key.imageid}' />-->
								            	<input type="hidden" name="imageid" id="imageid" value="<c:out value='${pair.key.imageid}' />" />
								            </c:if>
										    <input type="submit" value="edit" />
										</form>
										<form action="deleteComment?imageid=<c:out value='${pair.key.imageid}'/>" method="post">
											<c:if test="${pair.key != null}">
												<!--<c:out value='${pair.key.imageid}' />-->
								            	<input type="hidden" name="imageid" id="imageid" value="<c:out value='${pair.key.imageid}' />" />
								            </c:if>
										    <input type="submit" value="delete" />
										</form>
									</c:when>
									
									
									<c:otherwise>
										<!-- display comment button -->
										<form action="showCommentForm?imageid=<c:out value='${pair.key.imageid}'/>" method="post">
											<c:if test="${pair.key != null}">
												<!--<c:out value='${pair.key.imageid}' />-->
								            	<input type="hidden" name="imageid" id="imageid" value="<c:out value='${pair.key.imageid}' />" />
								            </c:if>
										    <input type="submit" value="comment" />
										</form>
									</c:otherwise>
									
									
								</c:choose>
								</td>
                    		
                    			<c:forEach var="pare" items="${treemapImageAndComment[pair.key.imageid]}">
                    				<tr><td><c:out value="${pare.description}"/></td></tr>	
                    			</c:forEach>
                    	</table>
                    
                    </td>
                    <td><a href="showEditImageForm?imageid=<c:out value='${pair.key.imageid}'/>">EDIT</a></td>
                    <td><a href="deleteImage?imageid=<c:out value='${pair.key.imageid}'/>">DELETE</a></td>
                    <td>
                    <c:set var="contains" value="false" />
					<c:forEach var="imageid" items="${listLikes}">
					  	<c:if test="${imageid eq pair.key.imageid}">
					    	<c:set var="contains" value="true" />
					  	</c:if>
					</c:forEach>
					<c:choose>
					
					
						<c:when test="${contains eq true}">
							<!-- display unlike -->
							<form action="unlike" method="post">
								<c:if test="${pair.key != null}">
									<!--<c:out value='${pair.key.imageid}' />-->
					            	<input type="hidden" name="imageid" id="imageid" value="<c:out value='${pair.key.imageid}' />" />
					            </c:if> 
							    <input type="submit" value="unlike" />
							</form>
							<c:set var="buttonid" value="${buttonid = buttonid + 1}"/>
						</c:when>
						
						
						<c:otherwise>
							<!-- display like button -->
							<form action="like" method="post">
								<c:if test="${pair.key != null}">
									<!--<c:out value='${pair.key.imageid}' />-->
					            	<input type="hidden" name="imageid" id="imageid" value="<c:out value='${pair.key.imageid}' />" />
					            </c:if>
							    <input type="submit" value="like" />
							</form>
							<c:set var="buttonid" value="${buttonid = buttonid + 1}"/>
						</c:otherwise>
						
						
					</c:choose>
					</td>
					
          			<script type="text/javascript">
          			
          			function change(id)
          			{
          				 
          				 var btn = document.getElementById(id);
          				 
                         if (btn.value == "Follow") {
                        	 alert("hello (btn.value == follow)");
                             btn.value = "unFollow";
                             btn.innerHTML = "unFollow";
                             
                         }
                         else {
                        	 alert("hello (btn.value != follow)");
                             btn.value = "Follow";
                             btn.innerHTML = "Follow";
                         }
          			}

          			</script>
                </tr>
            </c:forEach>
        </table>
    	<!-- Each post also needs to have like and unlike feature -->
    	<!-- Each post by the currentUser must be editable -->
    </div>

</body>
</html>