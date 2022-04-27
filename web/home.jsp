<%-- 
    Document   : home
    Created on : Feb 26, 2021, 4:12:03 PM
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
                width: 70%;
                margin: 0 auto;
                border: groove;
                margin-top: 30px;
                padding-left: 150px;
                padding-bottom:  20px;
                padding-top: 20px;
                font-size: 110%;
                font-family: cursive;
            }
            #search {
                text-align: center;
            }
            .searchComponent {
                margin-top: 10px;
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
        <div id="search">
            <form action="searchCar">
                <div class="searchComponent">
                Search by: <select id="typeSearch" name="searchType" onchange="typeChanged()" >
                        <c:if test="${param.searchType eq 'Category' && param.searchType ne 'Name'}">
                            <option value="Category" selected="true">Category</option>
                            <option value="Name">Name</option>
                        </c:if>
                        <c:if test="${param.searchType ne 'Category' && param.searchType eq 'Name'}">
                            <option value="Category">Category</option>
                            <option value="Name" selected="true">Name</option>
                        </c:if>
                        <c:if test="${param.searchType ne 'Category' && param.searchType ne 'Name'}">
                            <option value="Category" selected="true">Category</option>
                            <option value="Name">Name</option>
                        </c:if>
                    </select>
                </div>
                <div class="searchComponent">
                    <c:if test="${param.searchType ne 'Category' && param.searchType ne 'Name'}">
                        Category:<select name="cbbCategory" id="Category">
                            <option value="">Choose an category</option>
                            <c:forEach items="${requestScope.CATEGORY}" var="category">
                                <c:if test="${category eq param.cbbCategory}">
                                    <option value="${category}" selected = "true">${category}</option>
                                </c:if>
                                <c:if test="${category ne param.cbbCategory}">
                                    <option value="${category}">${category}</option>
                                </c:if>
                            </c:forEach>
                        </select>
                    </c:if>
                    <c:if test="${param.searchType eq 'Category'}">
                        Category:<select name="cbbCategory" id="Category">
                            <option value="">Choose an category</option>
                            <c:forEach items="${requestScope.CATEGORY}" var="category">
                                <c:if test="${category eq param.cbbCategory}">
                                    <option value="${category}" selected = "true">${category}</option>
                                </c:if>
                                <c:if test="${category ne param.cbbCategory}">
                                    <option value="${category}">${category}</option>
                                </c:if>
                            </c:forEach>
                        </select>
                    </c:if>
                    <c:if test="${param.searchType ne 'Category' && param.searchType eq 'Name'}">
                        Category:<select name="cbbCategory" disabled="true" id="Category">
                            <option value="">Choose an category</option>
                            <c:forEach items="${requestScope.CATEGORY}" var="category">
                                <c:if test="${category eq param.cbbCategory}">
                                    <option value="${category}" selected = "true">${category}</option>
                                </c:if>
                                <c:if test="${category ne param.cbbCategory}">
                                    <option value="${category}">${category}</option>
                                </c:if>
                            </c:forEach>
                        </select>        
                    </c:if>
                </div>
                <div class="searchComponent" >
                    Name: 
                    <c:if test="${param.searchType ne 'Name'}">
                        <input id="NameSearch" type="text" disabled="true" name="txtName" value="${param.txtName}" />
                    </c:if>
                    <c:if test="${param.searchType eq 'Name'}">
                        <input id="NameSearch" type="text" name="txtName" value="${param.txtName}" />
                    </c:if>
                </div>
                <div class="searchComponent">
                    Renting Date: <input type="date" name="txtDateFrom" required="" value="${param.txtDateFrom}"/>*
                </div>
                <div class="searchComponent">
                    Return Date: <input type="date" name="txtDateTo" required="" value="${param.txtDateTo}"/>*
                </div>
                <div class="searchComponent">
                    Amount of car: <input type="text" name="txtAmount" required="" value="${param.txtAmount}"/>*
                </div>
                <div class="searchComponent">
                    <input type="submit" value="Search" name="btnAction" />
                </div>
            </form>
        </div>
        <div style="text-align: center">
            <font color="red">${requestScope.NOT_SEARCH}</font>
        </div>
        <div style="text-align: center">
            <font color="red">${requestScope.INVALID_RENT_RETURN}</font>
        </div>
        <div style="text-align: center">
            <font color="red">${requestScope.INVALID_RENT}</font>
        </div>
        <div style="text-align: center">
            <font color="red">${requestScope.INVALID_QUANTITY}</font>
        </div>
        <c:set var="curPage" value="${param.curPage}"/>
        <c:set value="${requestScope.PAGING_LIST}" var="cars"/>
        <c:if test="${cars ne null && cars.size() > 0}"> 
            <c:forEach items="${cars}" var="car">
                <div id="content" style="display: block; width: 50%">
                    <c:if test="${car.statusRemain ne null && car.statusRemain ne ''}">
                        <font color="red">${car.statusRemain}</font><br/>
                    </c:if>
                    Name: ${car.name}<br/>
                    Color: ${car.color}<br/>
                    Year: ${car.year}<br/>
                    Category: ${car.category}<br/>
                    Price: ${car.price}$/day<br/>
                    Quantity: ${car.quantity}<br/>
                    Average Rating: ${car.averageRating}<br/>
                    <c:if test="${car.statusRemain eq null || car.statusRemain eq ''}">
                        <form action="addToCart" >
                            <input type="hidden" name="txtNameAdd" value="${car.name}" />
                            <c:if test="${param.txtName ne null}">
                                <input type="hidden" name="txtName" value="${param.txtName}"/>
                            </c:if>
                            <input type="hidden" name="txtAmount" value="${param.txtAmount}"/>
                            <input type="hidden" name="txtDateTo" value="${param.txtDateTo}"/>
                            <input type="hidden" name="txtDateFrom" value="${param.txtDateFrom}"/>
                            <c:if test="${param.cbbCategory ne null}">
                                <input type="hidden" name="cbbCategory" value="${param.cbbCategory}"/>
                            </c:if>
                            <input type="hidden" name="searchType" value="${param.searchType}"/>
                            <input type="submit" name="btnAction" value="Add to cart" />
                        </form>
                    </c:if>
                </div>
            </c:forEach>
            <div class="paging" style="width: 50%; margin-left:  45%; margin-bottom:  200px;  margin-top: 50px; ">   
                <c:set var="totalPage" value="${requestScope.TOTAL_PAGE}"/>
                <c:if test="${totalPage gt 1}">
                    <c:forEach begin="${1}" end="${totalPage}" var="page" step="${1}">
                        <form action="searchCar">
                                <c:if test="${page eq curPage}">
                                    <div>
                                        <input style="background:  yellow; float: left; margin-left: 5px; margin-right: 5px;" type="submit" value="${page}" />
                                    </div>
                                </c:if>
                                <c:if test="${page ne curPage}">
                                    <div>
                                        <input style="float: left; margin-left: 5px;  margin-right: 5px;" type="submit" value="${page}" />
                                    </div>
                                </c:if>
                            <input type="hidden" name="txtName" value="${param.txtName}" />
                            <input type="hidden" name="cbbCategory" value="${param.cbbCategory}" />
                            <input type="hidden" name="txtDateFrom" value="${param.txtDateFrom}" />
                            <input type="hidden" name="txtDateTo" value="${param.txtDateTo}" />
                            <input type="hidden" name="txtAmount" value="${param.txtAmount}" />
                            <input type="hidden" name="curPage" value="${page}" />
                        </form>
                    </c:forEach>
                </c:if>
            </div>
        </c:if>
        <c:if test="${cars eq null || cars.size() == 0}">
            <h3 style="text-align: center; margin-top: 70px; font-size: 130%;">Can not found any result</h3>
        </c:if> 
            
        <script language="javascript">
            
            function typeChanged()
            {
                var category = document.getElementById("Category");
                var name = document.getElementById("NameSearch");
                
                var typeObject = document.getElementById("typeSearch");
                var typeString =  typeObject.options[typeObject.selectedIndex].value;
                if(typeString === "Name") {
                    category.disabled = true;
                    name.disabled = false;
                }
                else {
                    category.disabled = false;
                    name.disabled = true;
                }
            }
            
            
        </script>

    </body>
</html>
