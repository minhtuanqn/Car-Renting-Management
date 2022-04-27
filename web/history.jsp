<%-- 
    Document   : history
    Created on : Mar 5, 2021, 7:32:26 PM
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
            #header {
                width: 100%;
                height: 30px;
                background: #8080801c;
                text-align: center;
                padding-top: 10px;
                text-decoration: none;
                font-size: 120%;
            }

            #content {
                display: block;
                width: 100%;
                margin: 0 auto;
                margin-top: 20px;
                text-align: center;
            }

            table, th, td{
                border-top:1px solid #ccc;
                border-bottom:1px solid #ccc;
                padding: 10px;
            }
            table{
                width: 100%;
                border-collapse:collapse;
            }

        </style>
    </head>
    <body>
        <font color="red">Wellcome,${sessionScope.WELLCOME_NAME}</font>
        <c:if test="${sessionScope.EMAIL ne null}">
            <a href="logout">Logout</a>
        </c:if>
        <c:if test="${sessionScope.EMAIL eq null}">
            <a href="loginPage">Login</a>
        </c:if>
        <div id="header">
            <a href="onloadHome">Home</a>
            <c:if test="${sessionScope.EMAIL ne null}">
                <a href="onloadCard">View Cart</a>
            </c:if>
            <c:if test="${sessionScope.EMAIL ne null}">
                <a href="onloadHistory">History</a>
            </c:if>
        </div>
        <div id="searchForm" style="margin-top: 15px; text-align: center; border: groove; padding: 15px">
            <form action="searchHistory">
                Order ID: <input type="text" name="txtOrderId" value="${param.txtOrderId}" /><br/><br/>
                Renting date: <input type="date" name="txtRentingDate" value="${param.txtRentingDate}" /><br/><br/>
                Return date: <input type="date" name="txtReturnDate" value="${param.txtReturnDate}" /><br/><br/>
                <input type="submit" value="Search" />
            </form>
        </div>
        <c:set var="histories" value="${requestScope.HISTORY}"/>
        <c:if test="${histories ne null && histories.size() > 0}">
            <div id="content">
                <c:forEach var="history" items="${histories}" >
                    <div class="carInfoBlock" style="margin-top: 50px; margin-bottom: 50px">
                        <c:if test="${history[0].percenOfDiscount ne null && history[0].percenOfDiscount ne ''}">
                            <div style="width: 98.5%; border: 2px solid #ccc; font-size: 120%; padding: 10px">
                                <h3>Order ID: ${history[0].id} - Booking date: ${history[0].createDateText} - Percent of discount: ${history[0].percenOfDiscount}%<br/></h3>
                                <h3>Total before discount: ${history[0].totalBeforeDiscount} - Total after discount: ${history[0].totalAfterDiscount}</h3>
                                <form action="deleteHistory">
                                    <input type="hidden" name="txtOrderId" value="${history[0].id}" />
                                    <input type="submit" value="Delete" />
                                </form>
                            </div>
                        </c:if>
                        <c:if test="${history[0].percenOfDiscount eq null || history[0].percenOfDiscount eq ''}">
                            <div style="width: 98.5%; border: 2px solid #ccc; font-size: 120%; padding: 10px;">
                                <h3>Order ID: ${history[0].id} - Booking date: ${history[0].createDateText} <br/></h3>
                                <h3>Total price: ${history[0].totalBeforeDiscount}</h3>
                                <form action="deleteHistory">
                                    <input type="hidden" name="txtOrderId" value="${history[0].id}" />
                                    <input type="submit" value="Delete" />
                                </form>
                            </div>
                        </c:if>

                        <table border="1">
                            <thead>
                                <tr>
                                    <th>Car name</th>
                                    <th>Quantity</th>
                                    <th>Category</th>
                                    <th>Price</th>
                                    <th>Total price</th>
                                    <th>Renting date</th>
                                    <th>Return date</th>
                                    <th>Rating - Integer(0-10)</th>
                                    <th>Feedback</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:set value="${history[1]}" var="item"/>
                                <c:forEach var="detail" items="${item}" varStatus="counter">
                                    <tr>
                                        <td>${detail.carId}</td>
                                        <td>${detail.quantity}</td>
                                        <td>${detail.category}</td>
                                        <td>${detail.price}</td>
                                        <td>${detail.totalPrice}</td>
                                        <td>${detail.dateFromText}</td>
                                        <td>${detail.dateToText}</td>
                                        <form action="updateRatingAndFeedback">
                                            <input type="hidden" name="txtId" value="${detail.orderId}"/>
                                            <input type="hidden" name="txtCarId" value="${detail.carId}" />
                                            <input type="hidden" name="txtDateFrom" value="${detail.dateFromText}" />
                                            <input type="hidden" name="txtDateTo" value="${detail.dateToText}" />
                                            <input type="hidden" name="txtRentingDate" value="${param.txtRentingDate}" />
                                            <input type="hidden" name="txtReturnDate" value="${param.txtReturnDate}" />
                                            <input type="hidden" name="txtOrderId" value="${param.txtOrderId}" />
                                                <td>
                                                    <select name="cbbRating">
                                                        <c:if test="${detail.rating < 0}">
                                                            <option value="-1" selected="selected">Choose an option</option>
                                                        </c:if>
                                                        <c:if test="${detail.rating >= 0}">
                                                            <option value="-1" >Choose an option</option>
                                                        </c:if>
                                                        <c:if test="${detail.rating eq '0'}">
                                                            <option value="0" selected="selected">0</option>
                                                        </c:if>
                                                        <c:if test="${detail.rating ne '0'}">
                                                            <option value="0">0</option>
                                                        </c:if>
                                                        <c:if test="${detail.rating eq '1'}">
                                                            <option value="1" selected="selected">1</option>
                                                        </c:if>
                                                        <c:if test="${detail.rating ne '1'}">
                                                            <option value="1">1</option>
                                                        </c:if>
                                                        <c:if test="${detail.rating eq '2'}">
                                                            <option value="2" selected="selected">2</option>
                                                        </c:if>
                                                        <c:if test="${detail.rating ne '2'}">
                                                            <option value="2">2</option>
                                                        </c:if>
                                                        <c:if test="${detail.rating eq '3'}">
                                                            <option value="3" selected="selected">3</option>
                                                        </c:if>
                                                        <c:if test="${detail.rating ne '3'}">
                                                            <option value="3">3</option>
                                                        </c:if>
                                                        <c:if test="${detail.rating eq '4'}">
                                                            <option value="4" selected="selected">4</option>
                                                        </c:if>
                                                        <c:if test="${detail.rating ne '4'}">
                                                            <option value="4">4</option>
                                                        </c:if>
                                                        <c:if test="${detail.rating eq '5'}">
                                                            <option value="5" selected="selected">5</option>
                                                        </c:if>
                                                        <c:if test="${detail.rating ne '5'}">
                                                            <option value="5">5</option>
                                                        </c:if>
                                                        <c:if test="${detail.rating eq '6'}">
                                                            <option value="6" selected="selected">6</option>
                                                        </c:if>
                                                        <c:if test="${detail.rating ne '6'}">
                                                            <option value="6">6</option>
                                                        </c:if>
                                                        <c:if test="${detail.rating eq '7'}">
                                                            <option value="7" selected="selected">7</option>
                                                        </c:if>
                                                        <c:if test="${detail.rating ne '7'}">
                                                            <option value="7">7</option>
                                                        </c:if>
                                                        <c:if test="${detail.rating eq '8'}">
                                                            <option value="8" selected="selected">8</option>
                                                        </c:if>
                                                        <c:if test="${detail.rating ne '8'}">
                                                            <option value="8">8</option>
                                                        </c:if>
                                                        <c:if test="${detail.rating eq '9'}">
                                                            <option value="9" selected="selected">9</option>
                                                        </c:if>
                                                        <c:if test="${detail.rating ne '9'}">
                                                            <option value="9">9</option>
                                                        </c:if>  
                                                        <c:if test="${detail.rating eq '10'}">
                                                            <option value="10" selected="selected">10</option>
                                                        </c:if>
                                                        <c:if test="${detail.rating ne '10'}">
                                                            <option value="10">10</option>
                                                        </c:if> 
                                                    </select>
                                                </td>
                                                <td>
                                                    <input type="text" name="txtFeedback" value="${detail.feedback}" />
                                                </td>
                                                <td>
                                                    <input type="submit" value="Update" />
                                                </td>
                                        </form>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>

                    </div>
                </c:forEach>
            </div>
        </c:if>
        <c:if test="${histories eq null || histories.size() == 0}">
            <h3 style="text-align: center; margin-top: 30px;">
                <font color="red">Not found any result</font>
            </h3>
        </c:if>

    </body>
</html>
