<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error - Healthcare App</title>
    <link rel="stylesheet" href="styles.css">
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f8d7da;
            color: #721c24;
            text-align: center;
            padding: 50px;
        }

        .error-container {
            background-color: #f8d7da;
            border: 1px solid #f5c6cb;
            padding: 30px;
            border-radius: 10px;
            display: inline-block;
            box-shadow: 2px 2px 10px rgba(0, 0, 0, 0.1);
        }

        h1 {
            color: #721c24;
            font-size: 24px;
        }

        p {
            font-size: 18px;
        }

        .error-code {
            font-size: 20px;
            font-weight: bold;
        }

        .button-container {
            margin-top: 20px;
        }

        button {
            background-color: #d9534f;
            color: white;
            padding: 10px 20px;
            border: none;
            cursor: pointer;
            border-radius: 5px;
            font-size: 16px;
            margin-right: 10px;
        }

        button:hover {
            background-color: #c9302c;
        }
    </style>
</head>
<body>
<div class="error-container">
    <h1>Oops! Something Went Wrong</h1>
    <p class="error-code">Error Code: <%= request.getAttribute("errorCode") != null ? request.getAttribute("errorCode") : "Unknown" %></p>
    <p><%= request.getAttribute("errorMessage") != null ? request.getAttribute("errorMessage") : "An unexpected error occurred. Please try again later." %></p>

    <div class="button-container">
        <button onclick="window.history.back();">Go Back</button>
        <button onclick="window.location.href='patient-dashboard.jsp';">Go to Dashboard</button>
    </div>
</div>
</body>
</html>

