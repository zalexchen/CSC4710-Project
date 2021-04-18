<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Comment on Image</title>
</head>
<body>
    <center>
        <h1>Comment on Image</h1>
        <h2>
            <a href="showFeedPage">Back to Feed</a>
        </h2>
    </center>
    <div align="center">
            <form action="editComment" method="post">
        <table border="1" cellpadding="5">
            <caption>
                <h2>Edit Comment</h2>
            </caption>
            <c:if test="${existingComment != null}">
            	<input type="hidden" name="imageid" value="<c:out value='${existingComment.imageid}' />" />
            </c:if>
            <tr>
                <th>Description: </th>
                <td>
                    <input type="text" name="description" required value="<c:out value='${existingComment.description}' />"/>
                </td>
            </tr>
            <tr>
                <td colspan="2" align="center">
                    <input type="submit" value="Comment" />
                    <input type="reset" value="Start Over">
                </td>
            </tr>
        </table>
        </form>
    </div>   
</body>
</html>
