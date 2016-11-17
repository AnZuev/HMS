<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="path" value="${pageContext.servletContext.contextPath}"/>

<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>Management</title>
     <script   src="${path}/resources/static/libs/jquery-3.1.1.js"></script>

        <script src="${path}/resources/static/bower_components/react/react.js"></script>
        <script src="${path}/resources/static/bower_components/react/react-dom.js"></script>
        <!-- Latest compiled and minified CSS -->
        <link rel="stylesheet" href="${path}/resources/static/libs/bootstrap.min.css">
        <script src="${path}/resources/static/libs/bootstrap.min.js" ></script>

       <link rel="stylesheet" href="${path}/resources/static/css/main.css">

        <link rel="stylesheet" href="${path}/resources/static/bower_components/bootstrap-offcanvas/dist/css/bootstrap.offcanvas.css">
        <script src="${path}/resources/static/bower_components/bootstrap-offcanvas/dist/js/bootstrap.offcanvas.min.js"></script>
        <link rel="stylesheet" href="${path}/resources/static/libs/font-awesome.min.css">
        <link rel="stylesheet" href="${path}/resources/static/libs/jasny-bootstrap.min.css">

        <!-- Latest compiled and minified JavaScript -->
        <script src="${path}/resources/static/libs/jasny-bootstrap.min.js"></script>
        <script src="${path}/resources/static/libs/fetch.min.js"></script>
        <script src="${path}/resources/static/libs/bootstrap-datepicker.min.js"></script>

</head>
<body>
<script>
    window.host = "${path}";
    window.tmpData = {};
</script>

<div class='container'>
    <img id="background" src="${path}/resources/static/img/background2.jpg"/>
    <div id="app"></div>
</div>

<script src="${path}/resources/static/js/bundle.js" type="text/javascript"></script>
 <!-- <script src="http://localhost:8125/production/js/bundle.js" type="text/javascript"> -->

<!-- Script for fixing background size changing during scrolling -->
<script>
    $(document).ready(function(){
        var width = $(window).width();
        document.getElementById('background').style.maxHeight = $(window).height() + 200 + "px";
        $('body').css('max-width', width).css('overflow-x', 'hidden');
    });

    $( window ).resize(function() {
        var width = $(window).width();
        document.getElementById('background').style.maxHeight = $(window).height() + 200 + "px";
        $('body').css('max-width', width).css('overflow-x', 'hidden');
    });
</script>
</body>
</html>