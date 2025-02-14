<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register - Healthcare App</title>
    <link rel="stylesheet" href="styles.css">
</head>
<body>
<div class="registration-container">
    <!-- Display error or success messages -->
    <c:if test="${param.error != null}">
        <div class="message error">${param.error}</div>
    </c:if>

    <h2>Create an Account</h2>
    <form action="add-user" method="POST">
        <input type="hidden" name="action" value="register">

        <div class="input-field">
            <label for="name">Full Name</label>
            <input type="text" id="name" name="full_name" required>
        </div>

        <div class="input-field">
            <label for="username">Username</label>
            <input type="text" id="username" name="username" required>
        </div>

        <div class="input-field">
            <label for="password">Password</label>
            <input type="password" id="password" name="password" required>
        </div>

        <div class="input-field">
            <label for="role">Role</label>
            <select id="role" name="role" required>
                <option value="patient">Patient</option>
                <option value="doctor">Doctor</option>
            </select>
        </div>

        <button type="submit">Register</button>
    </form>

    <p>Already have an account? <a href="index.jsp">Login here</a></p>
</div>
</body>
</html>
