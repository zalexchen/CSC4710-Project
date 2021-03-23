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
		<h2>
            <a href="showFeedPage">Back to Feed</a>
        </h2>
	</nav>
    
    <div align="center">
        <table border="1" cellpadding="5">
            <caption>List of Users</caption>
            <!-- <tr> table row tag -->
            <tr>
            	<!--  <th> table header tag -->
                <th>Email</th>
                <!--<th>Password</th>-->
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
           	<c:set var="buttonid" value="0" />
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
                    <!--  if currentUser is following the user listed, display unfollow button, else display follow button -->
                    <!--  if user.email is in the currentUser's "following list" - display unfollow button-->
                    <td>
                    <%
                    if (session != null) {
                    	
                    } else {
                    	response.sendRedirect("Login.jsp");
                    }
                   	%>
                   	<c:set var="contains" value="false" />
					<c:forEach var="email" items="${listCurrentUserFollowing}">
					  	<c:if test="${email eq user.email}">
					    	<c:set var="contains" value="true" />
					  	</c:if>
					</c:forEach>
					<c:choose>
					
					
						<c:when test="${contains eq true}">
							<!-- display un-follow button -->
							<c:out value='unfollow button'/>
							<!-- <button onclick="change(this.id)" id="${buttonid}" value="unFollow">unFollow</button> -->
							<form action="unfollow" method="post">
								<c:if test="${user != null}">
									<c:out value='${user.email}' />
					            	<input type="hidden" name="useremail" id="useremail" value="<c:out value='${user.email}' />" />
					            </c:if> 
							    <input type="submit" value="unfollow" />
							</form>
							<c:set var="buttonid" value="${buttonid = buttonid + 1}"/>
						</c:when>
						
						
						<c:otherwise>
							<!-- display follow button -->
							<c:out value='follow button'/>
							<form action="follow" method="post">
								<c:if test="${user != null}">
									<c:out value='${user.email}' />
					            	<input type="hidden" name="useremail" id="useremail" value="<c:out value='${user.email}' />" />
					            </c:if> 
							    <input type="submit" value="follow" />
							</form>
							<!-- <button onclick="change(this.id)" id="${buttonid}" value="Follow">Follow</button> -->
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
                             //UserDAO userDAO = new UserDAO();
                             //userDAO.followUser(email,(String)session.getAttribute("currentUserEmail"));
                             
                         }
                         else {
                        	 alert("hello (btn.value != follow)");
                             btn.value = "Follow";
                             btn.innerHTML = "Follow";
                             //UserDAO userDAO = new UserDAO(); %>
                             //userDAO.deleteFollowUser(user.email, session.getAttribute("currentUserEmail"));
                         }
          			}

          			</script>
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