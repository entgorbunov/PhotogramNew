<%--
  Created by IntelliJ IDEA.
  User: ent
  Date: 4/26/24
  Time: 11:18 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title>Registration Page</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap-theme.min.css">
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>

</head>
<body>
<div class="container mt-5">
    <h2>Registration page</h2>
    <form action="${pageContext.request.contextPath}/registration" method="post" enctype="multipart/form-data" class="needs-validation" novalidate>
        <div class="form-group">
            <label for="username">Username:</label>
            <input type="text" class="form-control" name="username" id="username" required>
        </div>
        <div class="form-group">
            <label for="name">Name:</label>
            <input type="text" class="form-control" name="name" id="name" required>
        </div>
        <div class="form-group">
            <label for="birthday">Birthday:</label>
            <input type="date" class="form-control" name="birthday" id="birthday" required>
        </div>
        <div class="form-group">
            <label for="email">Email:</label>
            <input type="email" class="form-control" name="email" id="email" required>
        </div>
        <div class="form-group">
            <label for="image">Image:</label>
            <input type="file" class="form-control-file" name="image" id="image">
        </div>
        <div class="form-group">
            <label for="password">Password:</label>
            <input type="password" class="form-control" name="password" id="password" required>
        </div>
        <div>
            <label>Gender:</label>
            <c:forEach var="gender" items="${genders}">
                <div class="form-check">
                    <input class="form-check-input" type="radio" name="gender" id="gender${gender}" value="${gender}">
                    <label class="form-check-label" for="gender${gender}">
                            ${gender}
                    </label>
                </div>
            </c:forEach>
        </div>
        <button type="submit" class="btn btn-primary">Send</button>
    </form>
    <a href="${pageContext.request.contextPath}/login" class="btn btn-secondary mt-3">Login</a>
    <c:if test="${not empty errors}">
        <div class="alert alert-danger mt-4">
            <c:forEach var="error" items="${errors}">
                <p>${error}</p>
            </c:forEach>
        </div>
    </c:if>
</div>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.bundle.min.js"></script>
</body>

</html>
