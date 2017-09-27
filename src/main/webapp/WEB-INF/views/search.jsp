<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta http-equiv="Content-Type" content="text/html; charset=windows-1251" />
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Searching</title>

    <link href="${contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
    <link href="<c:url value="//maxcdn.bootstrapcdn.com/font-awesome/4.1.0/css/font-awesome.min.css"/>" rel="stylesheet">

    <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
</head>
<body>
<div class="container">

    <form id="logoutForm" method="POST" action="${contextPath}/logout" accept-charset="UTF-8">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
    </form>

    <form action="/search_query" method="GET" class="form-search" accept-charset="UTF-8" >
        <div class="input-append">
            <input name="query" type="text" class="search-query input-xxlarge">
            <button type="submit" class="btn">Search</button>
        </div>
    </form>

    <form id="searchs" accept-charset="UTF-8" >
        <table id="keywords" class="table table-striped table-hover table-condenced" cellspacing="0" cellpadding="0">
            <c:if test="${components[0].description != null}">
            <thead>
                <tr>
                    <th>Description</th>
                    <th>Image</th>
                    <th>Price</th>
                </tr>
            </thead>
            </c:if>
            <tbody id = "tbodyid">
                <c:forEach var="component" items="${components}">
                   <tr>
                       <td style="vertical-align: center;">${component.description}</td>
                       <td style="height: 120px; width: 100px">${component.image}</td>
                       <td>${component.price}</td>
                   </tr>
                </c:forEach>
            </tbody>
        </table>
    </form>

    <script type="text/javascript">
        $(document).ready(function () {
            var contentLoadTriggered = false;
            var tbody = $("#tbodyid");
            window.onscroll = function () {
                if (document.body.scrollTop >= tbody.height() - window.innerHeight && !contentLoadTriggered) {
                   contentLoadTriggered = true;
                   $.get("more_results", function (data) {
                       var html = '';
                       for (var i = 0; i < data.length; ++i) {
                           var dataDescription = decodeURIComponent(data[i].description).replace(/\+/gi, ' ');
                           var dataprice = decodeURIComponent(data[i].price).replace(/\+/gi, ' ');
                           html += '<tr><td>' + dataDescription + '</td><td>' + data[i].image + '</td><td>' + dataprice;
                       }
                       tbody.innerHTML += html;
                       tbody.html(tbody.html() + html);
                       contentLoadTriggered = false;
                   });
                }
            };
        })
    </script>
</div>
</body>
</html>
