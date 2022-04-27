<%-- 
    Document   : createAccount
    Created on : Jan 23, 2021, 1:53:32 PM
    Author     : MINH TUAN
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <style>
            .container {
                margin: auto;
                display: div;
            }
            h1 {
                text-align: center;
            }
            .infor {
                display: block;
                border: groove;
                padding: 50px;
                width: 350px;
                margin-left:35%;
                margin-top: 50px;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <h1>Registration New Account</h1>
            <c:set var="error" value="${requestScope.ERROR}"/>
            <form action="createAccount" method="post">
                <div class="infor">
                    Input email for account: <input type="text" name="txtEmail" value="${param.txtEmail}" /><br/><br/>
                    <c:if test="${error.blankEmail ne null}">
                        <font color="red">
                            ${error.blankEmail}
                        </font><br/><br/>
                    </c:if>
                    <c:if test="${error.invalidEmail ne null}">
                        <font color="red">
                            ${error.invalidEmail}
                        </font><br/><br/>
                    </c:if>
                    <c:if test="${error.emailDuplicate ne null}">
                        <font color="red">
                            ${error.emailDuplicate}
                        </font><br/><br/>
                    </c:if>
                        Input account password: <input type="password" name="txtPassword" value="" /><br/><br/>
                    <c:if test="${error.invalidPassword ne null}">
                        <font color="red">
                            ${error.invalidPassword}
                        </font><br/><br/>
                    </c:if>
                        Input password again: <input type="password" name="txtPasswordAgain" value="" /><br/><br/>
                    <c:if test="${error.passwordNotDuplicate ne null}">
                        <font color="red">
                            ${error.passwordNotDuplicate}
                        </font><br/><br/>
                    </c:if>
                        Input student name: <input type="text" name="txtName" value="${param.txtName}" /><br/><br/>
                    <c:if test="${error.blankName ne null}">
                        <font color="red">
                            ${error.blankName}
                        </font><br/><br/>
                    </c:if>
                        Input phone number: <input type="text" name="txtPhone" value="${param.txtPhone}" /><br/><br/>
                    <c:if test="${error.blankPhone ne null}">
                        <font color="red">
                            ${error.blankPhone}
                        </font><br/><br/>
                    </c:if>
                    <c:if test="${error.invalidPhone ne null}">
                        <font color="red">
                            ${error.invalidPhone}
                        </font><br/><br/>
                    </c:if>
                        Input address: <input type="text" name="txtAddress" value="${param.txtAddress}" /><br/><br/>
                    <c:if test="${error.blankAddress ne null}">
                        <font color="red">
                            ${error.blankAddress}
                        </font><br/><br/>
                    </c:if>
                    <div style="text-align: center;">
                        <input type="submit" name="btnAction" value="Create" /><br/><br/>
                        <c:if test="${requestScope.STATUS_CREATE ne null}">
                            <font color="red">
                                ${requestScope.STATUS_CREATE}
                            </font><br/><br/>
                        </c:if>
                        <c:url value="loginPage" var="url">
                        </c:url>
                        <a href="${url}">Back to login page</a>
                    </div>
                </div>

            </form>

        </div>

    </body>
</html>
