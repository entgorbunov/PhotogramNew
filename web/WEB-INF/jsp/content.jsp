
<%--
  Created by IntelliJ IDEA.
  User: entgorbunov
  Date: 15.04.2024
  Time: 22:02
  To change this template use File | Settings | File Templates.
--%>
@
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title>Title</title>
</head>
<body>
<%@include file="header.jsp" %>
<div>
    <span>Content. Русский</span>
    <p>Size: ${userList.size()}</p>  // Display size of the user list
    <ul>
        <c:forEach items="${userList}" var="user">  // Loop over userList
            <li>${user.username}</li>  // Display each username
        </c:forEach>
    </ul>
</div>
<%@include file="footer.jsp" %>
</body>
</html>
