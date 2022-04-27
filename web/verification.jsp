<%-- 
    Document   : verification
    Created on : Mar 1, 2021, 4:45:24 PM
    Author     : MINH TUAN
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <Font color="red">Wellcome,${param.name}</font>
        <div style="text-align: center; margin-top: 100px;">
            <h3>Please check mail and verify this account</h3>
            <a href="loginPage">Return to login page</a>
        </div>
    </body>
</html>
