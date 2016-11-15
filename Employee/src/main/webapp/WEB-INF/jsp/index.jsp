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
    <script   src="https://code.jquery.com/jquery-3.1.1.js"   integrity="sha256-16cdPddA6VdVInumRGo6IbivbERE8p7CQR3HzTBuELA="   crossorigin="anonymous"></script>

    <script src="${path}/resources/static/bower_components/react/react.js"></script>
    <script src="${path}/resources/static/bower_components/react/react-dom.js"></script>

    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.4/css/bootstrap.min.css" integrity="sha384-2hfp1SzUoho7/TsGGGDaFdsuuDL0LX2hnUp6VkX3CUQ2K4K+xjboZdsXyp4oUHZj" crossorigin="anonymous">
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>

   <link rel="stylesheet" href="${path}/resources/static/css/main.css">


    <link rel="stylesheet" href="${path}/resources/static/bower_components/bootstrap-offcanvas/dist/css/bootstrap.offcanvas.css">
    <script src="${path}/resources/static/bower_components/bootstrap-offcanvas/dist/js/bootstrap.offcanvas.min.js"></script>

    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.6.3/css/font-awesome.min.css">
    <link rel="stylesheet" href="//cdnjs.cloudflare.com/ajax/libs/jasny-bootstrap/3.1.3/css/jasny-bootstrap.min.css">

    <!-- Latest compiled and minified JavaScript -->
    <script src="//cdnjs.cloudflare.com/ajax/libs/jasny-bootstrap/3.1.3/js/jasny-bootstrap.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/fetch/1.0.0/fetch.min.js"></script>
    <script src="//cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.3.0/js/bootstrap-datepicker.min.js"></script>

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