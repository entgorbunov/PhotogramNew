<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Admin Page</title>
</head>
<body>
<h1>Welcome, Admin!</h1>
<p>This is the admin page.</p>

<h2>Delete User</h2>
<form action="${pageContext.request.contextPath}/admin" method="post">
    <input type="hidden" name="action" value="delete">
    <label for="delete-id">ID:</label>
    <input type="text" id="delete-id" name="id">
    <button type="submit">Delete</button>
</form>

<h2>Find All Users</h2>
<form action="${pageContext.request.contextPath}/admin" method="post">
    <input type="hidden" name="action" value="findAll">
    <button type="submit">Find All</button>
</form>

<h2>Assign Role</h2>
<form action="${pageContext.request.contextPath}/admin" method="post">
    <input type="hidden" name="action" value="assignRole">
    <label for="assign-role-id">User ID:</label>
    <input type="text" id="assign-role-id" name="id">
    <label for="assign-role">Role:</label>
    <select id="assign-role" name="role">
        <option value="USER">USER</option>
        <option value="ADMIN">ADMIN</option>
        <!-- Add other roles as needed -->
    </select>
    <button type="submit">Assign Role</button>
</form>

<h2>Restore User</h2>
<form action="${pageContext.request.contextPath}/admin" method="post">
    <input type="hidden" name="action" value="restore">
    <label for="restore-id">ID:</label>
    <input type="text" id="restore-id" name="id">
    <button type="submit">Restore</button>
</form>

</body>
</html>