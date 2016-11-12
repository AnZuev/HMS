<%@ page contentType="application/json;charset=UTF-8" language="java" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page isELIgnored="false" %>
${response.sendError(404)}
{
    "commonErrors": [
        "<spring:message code="page.not.found"/>"
    ]
}