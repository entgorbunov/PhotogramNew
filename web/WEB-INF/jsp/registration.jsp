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
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <!-- Optional theme -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap-theme.min.css">
    <!-- Latest compiled and minified JavaScript -->
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>

</head>
<%--<img src="${pageContext.request.contextPath}/images/users/42.jpg" alt="User image">--%>
<%--<img height="600" width="400" src="https://images.unsplash.com/photo-1494790108377-be9c29b29330?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D">--%>
<body>
<div class="container mt-5">
    <h2>Register</h2>
    <form action="/registration" method="post" enctype="multipart/form-data" class="needs-validation" novalidate>
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
        <div class="form-group">
            <label for="role">Role:</label>
            <select class="form-control" name="role" id="role">
                <c:forEach var="role" items="${roles}">
                    <option value="${role}">${role}</option>
                </c:forEach>
            </select>
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
    <a href="/login" class="btn btn-secondary mt-3">Login</a>
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
