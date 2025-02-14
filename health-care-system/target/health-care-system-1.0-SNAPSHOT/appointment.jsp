<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.healthcare.model.Appointment" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<html>
<head>
    <title>My Appointments</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css" rel="stylesheet"/>
    <link href="appointment.css" rel="stylesheet"/>
</head>
<body>
<div class="appointments-container">
    <h2>My Appointments</h2>

    <%
        // Retrieve the appointments attribute set by the servlet
        List<Appointment> appointments = (List<Appointment>) request.getAttribute("appointments");

        // Display any error message if present
        String errorMessage = request.getParameter("error");
        if (errorMessage != null && !errorMessage.isEmpty()) {
    %>
    <div class="error-message" style="color: red; text-align: center;">
        <%= errorMessage %>
    </div>
    <%
        }

        // Define the SimpleDateFormat object for formatting the date
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy hh:mm a");

        // Check if there are any appointments
        if (appointments != null && !appointments.isEmpty()) {
    %>
    <table class="appointments-table">
        <thead>
        <tr>
            <th>Doctor</th>
            <th>Date</th>
            <th>Notes</th>
            <th>Status</th> <!-- Added Status Column -->
            <th>Action</th>
        </tr>
        </thead>
        <tbody>
        <%
            for (Appointment appt : appointments) {
        %>
        <tr>
            <td><%= appt.getDoctorName() %>
            </td>
            <td><%= dateFormat.format(appt.getDate()) %>
            </td>
            <td><%= appt.getNotes() %>
            </td>
            <td><%= appt.getStatus() %>
            </td> <!-- Displaying Status -->
            <td>
                <!-- Delete Icon -->
                <!-- Hidden Doctor ID Field -->
                <form action="delete-appointment" method="POST" style="display: inline;">
                    <input type="hidden" name="appointmentId" value="<%= appt.getId() %>">
                    <input type="hidden" name="username" value="<%= request.getParameter("username") %>">
                    <!-- Updated Square Delete Icon Button -->
                    <button type="submit" class="delete-btn">
                        <i class="fas fa-trash-alt delete-icon"></i>
                    </button>
                </form>

                <!-- Prescription Button -->
                <button class="prescription-btn"
                        onclick="showPrescriptionForm(<%= appt.getId() %>, '<%= request.getParameter("username") %>', '<%= appt.getDoctorUserName() %>')">
                    <i class="fas fa-file-medical"></i>
                </button>

            </td>
        </tr>
        <%
            }
        %>
        </tbody>
    </table>
    <%
    } else {
    %>
    <div class="no-appointments">No appointments found.</div>
    <%
        }
    %>

    <!-- Button to go back to Dashboard -->
    <a href="patient-dashboard.jsp" class="dashboard-btn">Go to Dashboard</a>
    <!-- Prescription Modal -->
    <div id="prescriptionModal" class="modal">
        <div class="modal-content">
            <span class="close-btn" onclick="closeModal()">&times;</span>
            <h3>Prescription Details</h3>
            <div id="prescriptionDetails"></div>
        </div>
    </div>

</div>

<script>
    // Show prescription modal
    function showPrescriptionForm(appointmentId, username, doctorUserName) {
        fetch("fetch-prescription?appointmentId=" + appointmentId + "&username=" + username + "&doctorId=" + encodeURIComponent(doctorUserName))
            .then(response => response.text())
            .then(data => {
                document.getElementById("prescriptionDetails").innerHTML = data;
                document.getElementById("prescriptionModal").style.display = "flex"; // Show modal
            })
            .catch(error => console.error("Error fetching prescriptions:", error));
    }


    // Close the prescription modal
    function closeModal() {
        const modal = document.getElementById("prescriptionModal");
        modal.style.display = "none";  // Hide modal
    }

</script>
</body>
</html>
