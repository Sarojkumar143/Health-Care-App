package com.healthcare.servlet;

import com.healthcare.db.DatabaseConnection;
import com.healthcare.model.Prescription;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/add-prescription")
public class AddPrescriptionServlet extends HttpServlet {

    // Handles the POST request to add prescriptions
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int consultationId = getConsultationId(request);
            String doctorName = getDoctorName(request);
            String[] medications = getPrescriptionData(request, "medication[]");
            String[] dosages = getPrescriptionData(request, "dosage[]");
            String[] durations = getPrescriptionData(request, "duration[]");

            if (medications == null || dosages == null || durations == null) {
                redirectToDoctorDashboard(response);
                return;
            }

            addPrescriptionsToDatabase(consultationId, doctorName, medications, dosages, durations);

            redirectToDoctorDashboard(response);  // Redirect after successful insertion
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");  // Handle database error
        }
    }

    // Handles the GET request to fetch existing prescriptions
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int consultationId = Integer.parseInt(request.getParameter("appointmentId"));
        List<Prescription> prescriptions = getExistingPrescriptions(consultationId);

        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        try (PrintWriter out = response.getWriter()) {
            renderPrescriptionsTable(out, prescriptions);
        }
    }

    // Retrieves the consultation ID from the request
    private int getConsultationId(HttpServletRequest request) {
        return Integer.parseInt(request.getParameter("appointmentId"));
    }

    // Retrieves the logged-in doctor's username
    private String getDoctorName(HttpServletRequest request) {
        return (String) request.getSession().getAttribute("username");
    }

    // Retrieves the prescription data (medications, dosages, durations) from the request
    private String[] getPrescriptionData(HttpServletRequest request, String parameterName) {
        return request.getParameterValues(parameterName);
    }

    // Redirects the user to the DoctorDashboardServlet
    private void redirectToDoctorDashboard(HttpServletResponse response) throws IOException {
        response.sendRedirect("doctor-dashboard");
    }

    // Adds prescriptions to the database using a batch insert
    private void addPrescriptionsToDatabase(int consultationId, String doctorName, String[] medications, String[] dosages, String[] durations) throws SQLException {
        String sql = "INSERT INTO prescriptions (consultation_id, doctor_name, medication, dosage, duration, notes) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < medications.length; i++) {
                stmt.setInt(1, consultationId);
                stmt.setString(2, doctorName);
                stmt.setString(3, medications[i]);
                stmt.setString(4, dosages[i]);
                stmt.setString(5, durations[i]);
                stmt.setString(6, ""); // Optional notes
                stmt.addBatch();
            }
            stmt.executeBatch();  // Execute batch insert
        }
    }

    // Fetches existing prescriptions for a given consultation ID
    private List<Prescription> getExistingPrescriptions(int consultationId) {
        List<Prescription> prescriptions = new ArrayList<>();
        String sql = "SELECT * FROM prescriptions WHERE consultation_id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, consultationId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Prescription prescription = new Prescription();
                prescription.setId(rs.getInt("id"));
                prescription.setMedication(rs.getString("medication"));
                prescription.setDosage(rs.getString("dosage"));
                prescription.setDuration(rs.getString("duration"));
                prescriptions.add(prescription);
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Handle database error
        }
        return prescriptions;
    }

    // Renders the prescriptions in a table format
    private void renderPrescriptionsTable(PrintWriter out, List<Prescription> prescriptions) {
        for (Prescription prescription : prescriptions) {
            out.println("<tr id='prescription-" + prescription.getId() + "'>");
            out.println("<td>" + prescription.getMedication() + "</td>");
            out.println("<td>" + prescription.getDosage() + "</td>");
            out.println("<td>" + prescription.getDuration() + "</td>");
            out.println("<td><button type='button' onclick='deletePrescription(" + prescription.getId() + ")'>Delete</button></td>");
            out.println("</tr>");
        }
    }
}
