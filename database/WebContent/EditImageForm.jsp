<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Edit an Image</title>
</head>
<body>
    <center>
        <h1>Edit Image</h1>
        <h2>
            <a href="showFeedPage">Back to Feed</a>
        </h2>
    </center>
    <div align="center">
    
    	<!--  Form action UPDATE -->
        <form action="editImage" method="post">
        <table border="1" cellpadding="5">
            <caption>
                <h2>Edit an existing Image!</h2>
            </caption>
            <c:if test="${image != null}">
            	<input type="hidden" name="imageid" value="<c:out value='${image.imageid}' />" />
            </c:if> 
            <tr>
                <th>URL: </th>
                <td>
                    <input type="text" name="url" id="url" required
                            value="<c:out value='${image.url}' />"
                        />
                </td>
            </tr>
            <tr>
                <th>Description: </th>
                <td>
                    <input type="text" name="description" required
                            value="<c:out value='${image.description}' />"
                    />
                </td>
            </tr>
            <tr>
                <th>Tags: </th>
                <td>
                    <input type="text" name="description" required
                            value="<c:out value='${image.description}' />"
                    />
                </td>
            </tr>
            <!--
            <tr>
            	<th>Postdate + Posttime: </th>
            	<td>
			        <p name="postdate" id="postdate" value="<c:out value='${image.postdate}'/>"></p>
			        <p name="posttime" id="posttime" value="<c:out value='${image.posttime}'/>"></p>
            	</td>
            </tr>
            -->
            <tr>
                <td colspan="2" align="center">
                    <input type="submit" value="Save" />
                </td>
            </tr>
        </table>
        </form>
    </div>   
</body>
</html>
