<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Change Profile Image</title>
    <style>
        body {
            font-family: Arial, sans-serif;
        }

        .container {
            width: 300px;
            margin: 50px auto;
        }

        label {
            display: block;
            margin-bottom: 5px;
        }

        input[type="text"], input[type="email"], input[type="password"], input[type="date"], input[type="submit"], input[type="file"] {
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
    <h1>Change Profile Image</h1>
    <form action="${pageContext.request.contextPath}/changeImage" method="post" enctype="multipart/form-data">
        <div>
            <label for="image">New Profile Image:</label>
            <input type="file" id="image" name="image" required>
        </div>
        <input type="submit" value="Upload">
    </form>
</div>
</body>
</html>
