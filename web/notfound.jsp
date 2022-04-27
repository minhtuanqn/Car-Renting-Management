<%-- 
    Document   : notfound
    Created on : Jan 19, 2021, 5:50:42 PM
    Author     : MINH TUAN
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1 style="text-align: center; margin-top: 200px;">This resouce page does not exist</h1><br/>
        <div style="text-align: center">
            <p>Eror 404: Not found page</p>
            <c:url value="onloadHome" var='url'>
            </c:url>
            <a href="${url}">Return to home page</a>
        </div>
        
    </body>
</html>
