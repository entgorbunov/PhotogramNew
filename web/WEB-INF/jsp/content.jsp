    <%@ page contentType="text/html;charset=UTF-8" %>
    <html>
    <head>
        <title>Title</title>
    </head>
    <body>
    <%@include file="header.jsp" %>
    <div>
        <span>Content. Русский</span>
        <p>Size: ${requestScope.users.size()}</p>
        <p>Size: ${requestScope.}</p>
        <p>Size: ${requestScope}</p>
        <p>Id: ${requestScope.users.get(0).id}</p>
        <p>Id 2: ${requestScope.users[1].id}</p>
        <p>Map Id 2: ${sessionScope.usersMap[1]}</p>
        <p>JSESSION id: ${cookie["JSESSIONID"].value}, unique identifier</p>
        <p>Header: ${header["Cookie"]}</p>
        <p>Param id: ${param.id}</p>
        <p>Param test: ${param.test}</p>
        <p>Empty list: ${not empty users}</p>

    </div>
    <%@include file="footer.jsp" %>
    </body>
    </html>
