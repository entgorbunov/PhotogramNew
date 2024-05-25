<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Photogram</title>
    <style>
        header {
            background-color: #f8f9fa;
            padding: 10px 20px;
            text-align: center;
        }
        .header-link {
            margin-right: 20px;
            text-decoration: none;
            color: black;
        }
        .right-align {
            float: right;
        }
        img.profile-pic, img.post-image {
            width: 100px; /* Adjusted size */
            height: 100px; /* Maintain aspect ratio */
            border-radius: 50%;
        }
        .recommendation-section, .posts-section {
            display: flex;
            flex-wrap: wrap;
            justify-content: space-around;
            margin-top: 20px;
        }
        .recommendation, .post {
            text-align: center;
            margin: 10px;
        }
        .posts-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        .add-post-link {
            margin-left: auto;
        }
        .error-message {
            color: red;
            font-size: 14px;
        }
    </style>
</head>
<body>
<header>
    <a href="${pageContext.request.contextPath}/mainPage" class="header-link">Photogram</a>
    <div class="right-align">
        <a href="${pageContext.request.contextPath}/settings?id=${user.id}" class="header-link">Settings</a>
        <a href="${pageContext.request.contextPath}/logout" class="header-link">Logout</a>
        <c:if test="${user.role == 'ADMIN' || user.role.roleId == 1}">
            <a href="${pageContext.request.contextPath}/admin" class="header-link">Admin</a>
        </c:if>
    </div>
</header>

<div id="userImage">
    <h2>${user.username}</h2>
    <img src="${pageContext.request.contextPath}/downloadUserImage?userId=${user.id}" alt="${user.username}'s profile image" class="profile-pic">
    <h2>${user.name}</h2>
    <p>Bio: ${user.bio}</p>
    <p>Birthday: ${user.birthday}</p>

    <c:if test="${user.id == activeUserId}">
        <h2>We recommend subscribing</h2>
        <div class="recommendation-section">
            <c:forEach items="${recommendedUsers}" var="user">
                <div class="recommendation">
                    <img src="${pageContext.request.contextPath}/downloadUserImage?userId=${user.id}" alt="${user.username}'s profile image" class="profile-pic">
                    <p>
                        <a href="${pageContext.request.contextPath}/mainPage?userId=${user.id}" class="header-link">${user.username}</a>
                    </p>
                    <form action="${pageContext.request.contextPath}/addSubscription" method="post" style="display:inline;">
                        <input type="hidden" name="userId" value="${user.id}">
                        <button type="submit" class="header-link" style="background:none; border:none; color:blue; cursor:pointer; text-decoration:underline;">Subscribe</button>
                    </form>
                </div>
            </c:forEach>
        </div>
    </c:if>

    <h2>Subscriptions</h2>
    <div class="subscriptions-section">
        <c:forEach items="${subscriptionUsers}" var="user">
            <div class="subscription">
                <img src="${pageContext.request.contextPath}/downloadUserImage?userId=${user.id}" alt="${user.username}'s profile image" class="profile-pic">
                <p>
                    <a href="${pageContext.request.contextPath}/mainPage?userId=${user.id}" class="header-link">${user.username}</a>
                </p>
                <form action="${pageContext.request.contextPath}/removeSubscription" method="post" style="display:inline;">
                    <input type="hidden" name="userId" value="${user.id}">
                    <button type="submit" class="header-link" style="background:none; border:none; color:red; cursor:pointer; text-decoration:underline;">Unsubscribe</button>
                </form>
            </div>
        </c:forEach>
    </div>

    <c:if test="${user.id == activeUserId}">
        <h2>Add a New Post</h2>
        <form action="${pageContext.request.contextPath}/addPost" method="post" enctype="multipart/form-data">
            <input type="hidden" name="userId" value="${param.userId}" />
            <label for="content">Post Content:</label>
            <textarea name="content" id="content" rows="5" cols="30"></textarea>
            <br />
            <label for="image">Select image (optional):</label>
            <input type="file" name="image" id="image" accept="image/*">
            <br />
            <input type="submit" value="Submit Post" />
            <c:if test="${not empty errorMessage}">
                <div class="error-message">${errorMessage}</div>
            </c:if>
        </form>
    </c:if>

    <div class="posts-header">
        <h2>My posts</h2>
    </div>

    <div class="posts-section">
        <c:forEach items="${posts}" var="post">
            <div class="post">
                <img src="${pageContext.request.contextPath}/downloadPostImage?postId=${post.id}" alt="Post image" class="post-image">
                <p>${post.text}</p>
                <form action="${pageContext.request.contextPath}/deletePost" method="post" style="display:inline;">
                    <input type="hidden" name="postId" value="${post.id}">
                    <button type="submit">Delete</button>
                </form>
            </div>
        </c:forEach>
    </div>

</div>

</body>
</html>