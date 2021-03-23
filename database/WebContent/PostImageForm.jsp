<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Post an Image</title>
</head>
<body>
    <center>
        <h1>Post an Image</h1>
        <h2>
            <a href="showFeedPage">Back to Feed</a>
        </h2>
    </center>
    <div align="center">
            <form action="postImage" method="post">
        <table border="1" cellpadding="5">
            <caption>
                <h2>
                        Add a new image!
                </h2>
            </caption>
                <p>
                    <input type="hidden" name="imageid" id="imageid" value="<c:out value='${image.imageid}' />" />
                </p>
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
            <!--
            <tr>
                <th>Tags: </th>
                <td>
                    <input type="text" name="tags" required
                            value="<c:out value='${image.description}' />"
                    />
                </td>
            </tr>  -->
            <tr>
                <td colspan="2" align="center">
                    <input type="submit" value="Post" />
                    <input type="reset" value="Start Over">
                </td>
            </tr>
        </table>
        </form>
    </div>   
</body>
</html>
