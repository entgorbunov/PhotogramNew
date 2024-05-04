<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<%@include file="header.jsp"%>
<h1>Список юзеров:</h1>
<ul>
    <c:forEach var="user" items="${requestScope.users}">

        <li><a href="${pageContext.request.contextPath}/posts?userId=${user.id}">${user.username}</a></li>

    </c:forEach>
</ul>
</body>
</html>
