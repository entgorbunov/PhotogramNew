<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Account Settings</title>
    <style>
        body { font-family: Arial, sans-serif; }
        .container { width: 300px; margin: 50px auto; }
        label { display: block; margin-bottom: 5px; }
        input[type="text"], input[type="email"], input[type="date"], input[type="submit"] {
            width: 100%;
            padding: 8px;
            margin-bottom: 20px;
        }
        select, textarea {
            width: 100%;
            padding: 8px;
            margin-bottom: 20px;
        }
        .back-link, .extra-links {
            position: absolute;
            top: 10px;
            left: 10px;
        }
        .extra-links {
            top: 40px; /* Adjust spacing to avoid overlap */
        }
    </style>
</head>
<body>
<a href="${pageContext.request.contextPath}/mainPage" class="back-link">Back to Main Page</a>
<div class="extra-links">
    <a href="${pageContext.request.contextPath}/changeImage?${user.id}">Change Image</a><br>
    <a href="${pageContext.request.contextPath}/changePassword">Change Password</a>
</div>
<div class="container">
    <h1>Account Information</h1>
    <h3>You can change these fields</h3>
    <form action="${pageContext.request.contextPath}/settings" method="post">
        <div>
            <label for="email">Email:</label>
            <input type="email" id="email" name="email">
        </div>
        <div>
            <label for="username">Username:</label>
            <input type="text" id="username" name="username">
        </div>
        <div>
            <label for="name">Name and Surname:</label>
            <input type="text" id="name" name="name">
        </div>
        <div>
            <label for="birthday">Birthday:</label>
            <input type="date" id="birthday" name="birthday">
        </div>
        <div>
            <label for="gender">Gender:</label>
            <select id="gender" name="gender">
                <option value="MALE">Male</option>
                <option value="FEMALE">Female</option>
            </select>
        </div>
        <div>
            <label for="bio">Bio:</label>
            <textarea id="bio" name="bio"></textarea>
        </div>
        <input type="submit" value="Save">
    </form>
</div>
</body>
</html>
