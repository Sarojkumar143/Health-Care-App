<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Healthcare App</title>
    <link rel="stylesheet" href="styles.css">
</head>
<body>
<div class="login-container">
    <!-- Display error or success messages -->
    <c:if test="${param.error != null}">
        <div class="message error">${param.error}</div>
    </c:if>
    <c:if test="${param.message != null}">
        <div class="message success">${param.message}</div>
    </c:if>

    <h2>Login</h2>
    <form action="add-user" method="POST">
        <input type="hidden" name="action" value="login">
        <div class="input-field">
            <label for="username">Username</label>
            <input type="text" id="username" name="username" required>
        </div>
        <div class="input-field">
            <label for="password">Password</label>
            <input type="password" id="password" name="password" required>
        </div>
        <button type="submit">Login</button>
    </form>
    <p>If you don't have an account, <a href="register.jsp">Register here</a></p>
</div>
</body>
</html>
