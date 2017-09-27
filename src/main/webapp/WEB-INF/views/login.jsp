<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">
    <META HTTP-EQUIV="REFRESH" CONTENT="1; URL=http://localhost:4200/succesRegistration ">
    <title>Log in with your account</title>

    <link href="${contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
    <link href="${contextPath}/resources/css/common.css" rel="stylesheet">

    <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.10.1/jquery.min.js"></script>

</head>

<body>

<script type="text/javascript">
    $(document).ready(function(){
        $('.spoiler-body').hide();
        $('.spoiler-title').click(function(){
            $(this).next().toggle()});
    });
</script>
<div class="container" align="center">

    <form method="POST" action="${contextPath}/login" class="form-signin">
        <h2 class="form-heading text-center">Log in</h2>
        <a class=" text-center spoiler-title">Authorization through social networks</a>
        <div class="center-block glyphicon-align-center spoiler-body">
        <script src="//ulogin.ru/js/ulogin.js"></script>
        <div id="uLogin" data-ulogin="display=panel;theme=flat;
        fields=first_name,last_name;
        providers=vkontakte,twitter,facebook;
        hidden=;redirect_uri=;callback=preview;mobilebuttons=0;"></div>
        <script>
            function preview(token){
                $.getJSON("//ulogin.ru/token.php?host=" + encodeURIComponent(location.toString()) + "&token=" + token + "&callback=?", function(data){
                    data = $.parseJSON(data.toString());
                    if(!data.error){
                        window.location.assign("/welcome?first_name=" + data.first_name
                            + "&last_name=" + data.last_name);
                    }
                });
            }
        </script>
        </div>
            <form style="align-self: center;" method="POST" action="${contextPath}/login" class="form-actions btn-group-vertical">

                <div align="center" class="${error != null ? 'has-error' : ''}">
                    <span>${message}</span>
                    <p>
                        <input name="username" type="text" class="form-control" placeholder="Username"
                               autofocus="true"/>
                    </p>
                    <p>
                        <input name="password" type="password" class="form-control" placeholder="Password"/>
                    </p>
                    <span>${error}</span>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <p>
                        <button class="btn btn-success" style="min-width: 220px;" type="submit">Log In</button>
                    </p>
                    <h4 class="text-center"><a href="${contextPath}/registration">Create an account</a></h4>
                </div>
    </form>

</div>
<!-- /container -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script src="${contextPath}/resources/js/bootstrap.min.js"></script>
</body>
</html>