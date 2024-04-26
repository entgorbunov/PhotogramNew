<%@ page import="com.photogram.service.UserService" %>
<%@ page import="com.photogram.service.PostService" %>
<%@ page import="com.photogram.dto.PostDto" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: ent
  Date: 4/25/24
  Time: 9:27 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h1>Опубликованные посты:</h1>
<ul>
<%
    Long userId = Long.valueOf(request.getParameter("userId"));
    List<PostDto> posts = PostService.getInstance().findAllByUserId(userId);
    for (PostDto post : posts) {
        out.write(String.format("<li>%s</li>", post.getCaption()));
    }

%>
    </ul>
</body>
</html>
