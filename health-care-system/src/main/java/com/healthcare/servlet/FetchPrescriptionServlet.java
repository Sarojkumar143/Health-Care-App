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

@WebServlet("/fetch-prescription")
public class FetchPrescriptionServlet extends HttpServlet {

    private static final String PRESCRIPTION_QUERY = "SELECT p.* FROM prescriptions p " +
            "JOIN consultations c ON p.consultation_id = c.id " +
            "WHERE c.username = ? AND c.status = 'Approved' AND c.doctor_name = ? AND c.id = ?";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String patientUsername = (String) request.getSession().getAttribute("username"); // Get logged-in patient username
        String doctorId = request.getParameter("doctorId"); // Get doctor ID from request parameter
        String appointmentId = request.getParameter("appointmentId"); // Get appointment ID

        // Log the fetched parameters
        logRequestParameters(patientUsername, doctorId, appointmentId);

        List<Prescription> prescriptions = fetchPrescriptions(patientUsername, doctorId, appointmentId);

        // Set response content type and encoding
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        try (PrintWriter out = response.getWriter()) {
            if (prescriptions.isEmpty()) {
                out.println("<p>No prescriptions found for this patient.</p>");
            } else {
                renderPrescriptions(out, prescriptions);
            }
        }
    }

    // Log the request parameters for debugging purposes
    private void logRequestParameters(String patientUsername, String doctorId, String appointmentId) {
        System.out.println("Fetching prescriptions for patientUsername: " + patientUsername + ", doctorId: " + doctorId + ", appointmentId: " + appointmentId);
    }

    // Fetch prescriptions from the database
    private List<Prescription> fetchPrescriptions(String patientUsername, String doctorId, String appointmentId) {
        List<Prescription> prescriptions = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(PRESCRIPTION_QUERY)) {

            ps.setString(1, patientUsername);
            ps.setString(2, doctorId);
            ps.setString(3, appointmentId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    prescriptions.add(mapResultSetToPrescription(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Log the error
        }

        return prescriptions;
    }

    // Map a result set to a Prescription object
    private Prescription mapResultSetToPrescription(ResultSet rs) throws SQLException {
        Prescription prescription = new Prescription();
        prescription.setId(rs.getInt("id"));
        prescription.setMedication(rs.getString("medication"));
        prescription.setDosage(rs.getString("dosage"));
        prescription.setDuration(rs.getString("duration"));
        return prescription;
    }

    // Render prescriptions in a table format
    private void renderPrescriptions(PrintWriter out, List<Prescription> prescriptions) {
        out.println("<table>");
        out.println("<thead>");
        out.println("<tr>");
        out.println("<th>Medication</th>");
        out.println("<th>Dosage</th>");
        out.println("<th>Duration</th>");
        out.println("</tr>");
        out.println("</thead>");
        out.println("<tbody>");

        for (Prescription prescription : prescriptions) {
            out.println("<tr id='prescription-" + prescription.getId() + "'>");
            out.println("<td>" + prescription.getMedication() + "</td>");
            out.println("<td>" + prescription.getDosage() + "</td>");
            out.println("<td>" + prescription.getDuration() + "</td>");
            out.println("</tr>");
        }

        out.println("</tbody>");
        out.println("</table>");
    }
}
