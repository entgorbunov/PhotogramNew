<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
  <head>
    <title>Title</title>
  </head>
  <body>
        <h1>
          Опубликованные посты:
        </h1>
  <ul>
    <c:if test="${not empty requestScope.posts}">
      <c:forEach var="post" items="${requestScope.posts}">
        <li>${fn:toUpperCase(post.caption)}</li>
      </c:forEach>
    </c:if>
  </ul>
  </body>
</html>
