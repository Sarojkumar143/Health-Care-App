<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, com.healthcare.model.Appointment" %>
<%@ page import="com.healthcare.model.Prescription" %>
<%@ page import="java.text.SimpleDateFormat" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Doctor Dashboard - Healthcare App</title>
    <link rel="stylesheet" href="styles.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" rel="stylesheet">
    <link href="doctor.css" rel="stylesheet">
</head>
<body>

<div class="container">
    <h2>Welcome to the Healthcare Dashboard</h2>
    <% String fullName = (String) session.getAttribute("full_name"); %>
    <h2>Dr. <%= fullName %>
    </h2>

    <form action="logout.jsp" method="POST" style="display: inline;">
        <button type="submit" class="small-btn logout-btn">Logout</button>
    </form>

    <%
        List<Appointment> appointments = (List<Appointment>) request.getAttribute("appointments");
        if (appointments != null && !appointments.isEmpty()) {
    %>
    <table>
        <tr>
            <th>Patient Name</th>
            <th>Appointment Date</th>
            <th>Notes</th>
            <th>Status</th>
            <th>Action</th>
        </tr>
        <%
            for (Appointment appt : appointments) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
                String formattedDate = dateFormat.format(appt.getDate());
        %>
        <tr>
            <td><%= appt.getPatientName() %>
            </td>
            <td><%= formattedDate %>
            </td>
            <td><%= appt.getNotes() %>
            </td>
            <td id="status-<%= appt.getId() %>"><%= appt.getStatus() %>
            </td>
            <td class="action-buttons">
                <button class="approve-btn" onclick="approveAppointment(<%= appt.getId() %>)">
                    <i class="fas fa-check"></i>
                </button>
                <button class="reject-btn" onclick="rejectAppointment(<%= appt.getId() %>)">
                    <i class="fas fa-times"></i>
                </button>
                <button class="prescription-btn" onclick="showPrescriptionForm(<%= appt.getId() %>)">
                    <i class="fas fa-file-medical"></i>
                </button>
            </td>
        </tr>
        <%
            }
        %>
    </table>
    <%
    } else {
    %>
    <div class="no-appointments">Appointments not available</div>
    <%
        }
    %>

    <!-- Prescription Modal -->
    <div id="prescription-modal">
        <div class="modal-content">
            <h2>Add Prescription</h2>
            <form id="prescription-form" action="add-prescription" method="POST">
                <input type="hidden" id="appointmentId" name="appointmentId" value="">
                <table id="prescription-table">
                    <thead>
                    <tr>
                        <th>Medication</th>
                        <th>Dosage</th>
                        <th>Duration</th>
                        <th>Action</th>
                    </tr>
                    </thead>
                    <tbody>
                    <%
                        List<Prescription> prescriptions = (List<Prescription>) request.getAttribute("prescriptions");
                        if (prescriptions != null && !prescriptions.isEmpty()) {
                            for (Prescription prescription : prescriptions) {
                    %>
                    <tr id="prescription-<%= prescription.getId() %>">
                        <td><%= prescription.getMedication() %>
                        </td>
                        <td><%= prescription.getDosage() %>
                        </td>
                        <td><%= prescription.getDuration() %>
                        </td>
                        <td>
                            <button type="button" onclick="deletePrescription(<%= prescription.getId() %>)">Delete
                            </button>
                        </td>
                    </tr>
                    <%
                            }
                        }
                    %>
                    </tbody>
                </table>

                <button type="button" onclick="addPrescriptionRow()">Add Row</button>
                <button type="submit">Submit</button>
            </form>
            <button onclick="closeModal()">Close</button>
        </div>
    </div>

</div>


<script>
    // Function to approve appointment
    function approveAppointment(appointmentId) {
        fetch('approve-appointment', {
            method: 'POST',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            body: 'appointmentId=' + appointmentId
        }).then(response => {
            if (response.ok) {
                document.getElementById("status-" + appointmentId).innerText = "Approved";
            }
        });
    }

    // Function to reject appointment
    function rejectAppointment(appointmentId) {
        fetch('reject-appointment', {
            method: 'POST',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            body: 'appointmentId=' + appointmentId
        }).then(response => {
            if (response.ok) {
                document.getElementById("status-" + appointmentId).innerText = "Rejected";
            }
        });
    }

    // Show the prescription form modal
    function showPrescriptionForm(appointmentId) {
        document.getElementById("appointmentId").value = appointmentId;

        fetch("add-prescription?appointmentId=" + appointmentId)
            .then(response => response.text()) // Get response as text (HTML)
            .then(data => {
                document.querySelector("#prescription-table tbody").innerHTML = data; // Inject HTML into table
                document.getElementById("prescription-modal").style.display = "flex"; // Show modal
            })
            .catch(error => console.error("Error fetching prescriptions:", error));
    }


    // Close the modal
    function closeModal() {
        const modal = document.getElementById("prescription-modal");
        modal.style.display = "none";  // Hide the modal
    }

    // Function to add a new row to the prescription table
    function addPrescriptionRow() {
        const table = document.getElementById("prescription-table").getElementsByTagName('tbody')[0];
        const newRow = table.insertRow();

        const medicationCell = newRow.insertCell(0);
        const dosageCell = newRow.insertCell(1);
        const durationCell = newRow.insertCell(2);
        const actionCell = newRow.insertCell(3);

        medicationCell.innerHTML = '<input type="text" name="medication[]" required>';
        dosageCell.innerHTML = '<input type="text" name="dosage[]" required>';
        durationCell.innerHTML = '<input type="text" name="duration[]" required>';
        actionCell.innerHTML = '<button type="button" onclick="removePrescriptionRow(this)">Remove</button>';
    }

    // Function to remove a row from the prescription table
    function removePrescriptionRow(button) {
        const row = button.closest('tr');
        row.remove();
    }

    // Function to delete prescription
    function deletePrescription(prescriptionId) {
        fetch('delete-prescription', {
            method: 'POST',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            body: 'prescriptionId=' + prescriptionId
        }).then(response => {
            if (response.ok) {
                document.getElementById("prescription-" + prescriptionId).remove();  // Remove the row from the table
            }
        });
    }

</script>

</body>
</html>
