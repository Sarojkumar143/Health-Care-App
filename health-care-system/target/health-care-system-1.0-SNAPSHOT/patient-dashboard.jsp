<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - Healthcare App</title>
    <link rel="stylesheet" href="styles.css">
</head>
<body>
<div class="dashboard-container">
    <h2>Welcome to the Healthcare Dashboard</h2>
    <% String fullName = (String) session.getAttribute("full_name"); %>
    <h2><%= fullName %></h2>
    <p>Choose an option:</p>
    <div class="dashboard-options">
        <a href="book-consultation" class="option">Book Consultation</a>
        <a href="my-appointments" class="option">View Consultations</a>
    </div>
    <a href="logout.jsp" class="logout">Logout</a>
</div>
</body>
</html>
