<%@ page import="com.healthcare.model.Doctor" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Book Consultation - Healthcare App</title>
    <link rel="stylesheet" href="styles.css">
    <!-- Font Awesome for icons -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" rel="stylesheet">
    <style>
        .back-btn {
            font-size: 15px;
            margin-top: 20px;
            color: #007bff;
            cursor: pointer;
            text-decoration: none;
        }

        .back-btn i {
            margin-right: 10px;
        }
    </style>
</head>
<body>
<div class="consultation-container">
    <!-- Back Arrow Button -->
    <a href="patient-dashboard.jsp" class="back-btn"><i class="fas fa-arrow-left"></i>Back</a>

    <h2>Book a Consultation</h2>

    <!-- Form for booking consultation -->
    <form action="my-appointments" method="POST">
        <div class="input-field">
            <label for="doctorId">Select Doctor</label>
            <select id="doctorId" name="doctorId" required>
                <option value="" disabled selected>Select Doctor</option>

                <%-- Fetching doctors from request and populating the dropdown --%>
                <%
                    List<Doctor> doctors = (List<Doctor>) request.getAttribute("doctors");
                    if (doctors != null && !doctors.isEmpty()) {
                        for (Doctor doctor : doctors) {
                %>
                <option value="<%= doctor.getUsername() %>"><%= doctor.getFullName() %>
                </option>
                <%
                    }
                } else {
                %>
                <option disabled>No doctors available</option>
                <%
                    }
                %>
            </select>
        </div>
        <div class="input-field">
            <label for="date">Consultation Date</label>
            <input type="datetime-local" id="date" name="date" required>
        </div>
        <div class="input-field">
            <label for="notes">Notes</label>
            <textarea id="notes" name="notes"></textarea>
        </div>
        <button type="submit">Book Consultation</button>
    </form>
</div>
</body>
</html>
