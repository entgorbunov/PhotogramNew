<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Change Password</title>
    <style>
        body { font-family: Arial, sans-serif; }
        .container { width: 300px; margin: 50px auto; }
        label { display: block; margin-bottom: 5px; }
        input[type="password"], input[type="submit"] {
            width: 100%;
            padding: 8px;
            margin-bottom: 20px;
        }
        .back-link {
            position: absolute;
            top: 10px;
            left: 10px;
        }
        .settings-link {
            position: absolute;
            top: 35px;
            left: 10px;
        }
    </style>
</head>
<body>
<a href="${pageContext.request.contextPath}/mainPage?id=${user.id}" class="back-link">Back to Main Page</a>
<a href="${pageContext.request.contextPath}/settings?id=${user.id}" class="settings-link">Back to Settings</a>

<div class="container">
    <h1>Change Password</h1>
    <form action="${pageContext.request.contextPath}/changePassword" method="post">
        <div>
            <label for="oldPassword">Old Password:</label>
            <input type="password" id="oldPassword" name="oldPassword" required>
        </div>
        <div>
            <label for="newPassword">New Password:</label>
            <input type="password" id="newPassword" name="newPassword" required>
        </div>
        <div>
            <label for="confirmNewPassword">Confirm New Password:</label>
            <input type="password" id="confirmNewPassword" name="confirmNewPassword" required>
        </div>
        <input type="submit" value="Change Password">
    </form>
</div>
</body>
</html>
