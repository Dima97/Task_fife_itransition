<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Welcome</title>

    <style>
        .table {
            font-family: Tahoma;
            font-size: 30px;
            font-weight: 500;
        }
        .holder{
            position: inherit;
        }
        .block{
            display:none;
        }
        /*.holder:hover .block{*/
            /*display: block;*/
        /*}*/
        .holder:hover .block{
            display: block;
        }
    </style>

    <link href="${contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>

<div class="container">

    <c:if test="${pageContext.request.userPrincipal.name != null}">
        <form id="logoutForm" method="POST" action="${contextPath}/logout">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form>

        <h2>${pageContext.request.userPrincipal.name}|<a onclick="document.forms['logoutForm'].submit()">Logout</a></h2>
        <div class="btn-toolbar">
            <form>
                <button class="btn" style="margin-left: 5px;"
                        formaction="${contextPath}/search_page" formmethod="GET" type="submit">
                    <%--<i class="icon-search"></i>--%>Searche
                </button>
            </form>
        </div>
        <c:if test="${accessed == null || accessed}">
                <table class="table">
                    <c:forEach var="u" items="${users}">
                        <tr class="holder">
                            <td><h12>${u.username}</h12></td>
                            <td height="50">
                                    <div class="dropdown block">
                                        <button class="btn btn-primary dropdown-toggle" type="button" data-toggle="dropdown">Choose <span class="caret"></span>
                                        </button>
                                        <ul class="dropdown-menu">
                                                <c:if test="${!u.blocked}">
                                                <li><a href="/blocking?id=${u.id}&user=${pageContext.request.userPrincipal.name}&do=block"
                                                >block</a></li>
                                                </c:if>
                                                <c:if test="${u.blocked}">
                                                <li><a href="/blocking?id=${u.id}&user=${pageContext.request.userPrincipal.name}&do=unblock"
                                                >unblock</a></li>
                                                </c:if>
                                            <li><a href="/deleting?id=${u.id}&user=${pageContext.request.userPrincipal.name}"
                                                   >delete</a></li>
                                        </ul>
                                    </div>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
        </c:if>
        <c:if test="${accessed != null && !accessed}">
            <h2>Your account was blocked.</h2>
        </c:if>
    </c:if>

</div>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script src="${contextPath}/resources/js/bootstrap.min.js"></script>
</body>
</html>