<%-- 
    Document   : cart
    Created on : Mar 2, 2021, 4:02:58 PM
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
        </style>

    </head>
    <body>
        <Font color="red">Wellcome,${sessionScope.WELLCOME_NAME}</font>
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
        <h3 style="text-align: center">
            <c:if test="${param.Status ne null && param.Status ne ''}">
                <font color="red">${param.Status}</font>
            </c:if>
        </h3>
        <h3 style="text-align: center">
            <c:if test="${requestScope.TOTAL ne null && requestScope.TOTAL ne ''}">
                Total price: ${requestScope.TOTAL}$
            </c:if>
        </h3>

        <c:if test="${requestScope.CHECK_OUT_SUCCESS ne null}">
            <h3 style="text-align: center">
                <font color="red">${requestScope.CHECK_OUT_SUCCESS}</font>
            </h3>
        </c:if>

        <c:set var="items" value="${requestScope.ITEMS}"/>

        <c:if test="${items ne null && items.size() ne 0}">

            <div style="text-align: center; height: 120px">
                <form action="addDiscountCode">
                    Discount code: <input type="text" name="txtDiscount" value="${requestScope.CODE}" />
                    <input type="submit" value="Apply" />
                    <a href="deleteDiscountCode">Delete code</a>
                </form>
                <c:if test="${requestScope.NOT_EXIST_DISCOUNT ne null && requestScope.NOT_EXIST_DISCOUNT ne ''}">
                    <br/><font color="red">${requestScope.NOT_EXIST_DISCOUNT}</font>
                </c:if>
                <c:if test="${requestScope.TIME_OUT ne null && requestScope.TIME_OUT ne ''}">
                    <br/><font color="red">${requestScope.TIME_OUT}</font>
                </c:if>   
                <c:if test="${requestScope.DISCOUNT_PERCENT ne null && requestScope.DISCOUNT_PERCENT ne ''}">
                    <h3>Discount: ${requestScope.DISCOUNT_PERCENT}%</h3>
                </c:if>
                <c:if test="${requestScope.TOTAL_AFTER_DISCOUNT ne null && requestScope.TOTAL_AFTER_DISCOUNT ne ''}">
                    <h3><br/>Total after apply discount: ${requestScope.TOTAL_AFTER_DISCOUNT}$</h3>
                    </c:if>    
                    <form action="checkout" onsubmit="return confirmDCheckout()">
                    <br/><input type="submit" value="Checkout" />
                </form>

            </div>


            <table style="margin: 0 auto; margin-top: 70px;" border="1">
                <thead>
                    <tr>
                        <th>Car name</th>
                        <th>Car type</th>
                        <th>Quantity</th>
                        <th>Renting date</th>
                        <th>Return date</th>
                        <th>Price per day</th>
                        <th>Total price</th>
                        <th>Action</th>
                        <th>Action</th>
                        <th>Status</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="item" varStatus="counter" items="${items}">
                        <tr>
                    <form action="updateQuantityOfCar">
                        <td>${item.carId}</td>
                        <td>${item.category}</td>
                        <td>
                            <input type="text" name="txtQuantity" value="${item.quantity}" />
                        </td>
                        <td>${item.dateFromText}</td>
                        <td>${item.dateToText}</td>
                        <td>${item.price}$</td>
                        <td>${item.totalPrice}$</td>
                        <td>
                            <input type="hidden" name="txtName" value="${item.carId}" />
                            <input type="hidden" name="txtDateFrom" value="${item.dateFromText}" />
                            <input type="hidden" name="txtDateTo" value="${item.dateToText}" />
                            <input type="submit" value="Update" />
                        </td>
                        
                    </form>
                    <td>
                        <form action="deleteFromCart" onsubmit="return confirmDelete()">
                            <input type="hidden" name="txtName" value="${item.carId}" />
                            <input type="hidden" name="txtDateFrom" value="${item.dateFromText}" />
                            <input type="hidden" name="txtDateTo" value="${item.dateToText}" />
                            <input type="submit" value="Delete" />
                        </form>
                    </td>
                    <td>
                        <font color="red">${item.statusRemain}</font>
                    </td>

                </tr>
            </c:forEach>
        </tbody>
    </table>

</c:if>
<c:if test="${items eq null || items.size() eq 0}">
    <h3 style="text-align: center; margin-top: 80px;">Your cart is empty</h3>
</c:if>
<script>
    function confirmDelete() {
        return confirm("Are you sure to delete this car");
    }
    
    function confirmDCheckout() {
        return confirm("Are you sure to check out this car");
    }
    
</script>
</body>

</html>
