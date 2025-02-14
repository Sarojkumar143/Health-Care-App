package com.healthcare.servlet;

import com.healthcare.db.DatabaseConnection;
import com.healthcare.model.Appointment;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/doctor-dashboard")
public class DoctorDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Retrieve the session and username
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");

        // Validate the session
        if (username == null) {
            redirectToLoginPage(response, "Session expired, please login again");
            return;
        }

        // Fetch appointments and handle potential errors
        List<Appointment> appointments = fetchAppointmentsForDoctor(username);
        if (appointments == null) {
            redirectToErrorPage(response, "Error retrieving appointments");
            return;
        }

        // Set appointments as a request attribute and forward to the dashboard page
        request.setAttribute("appointments", appointments);
        request.getRequestDispatcher("doctor-dashboard.jsp").forward(request, response);
    }

    // Helper method to fetch appointments for the doctor from the database
    private List<Appointment> fetchAppointmentsForDoctor(String doctorUsername) {
        List<Appointment> appointments = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT c.id, u.full_name AS patient_name, c.date, c.notes, c.status " +
                    "FROM consultations c " +
                    "JOIN users u ON c.username = u.username " +
                    "WHERE c.doctor_name = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, doctorUsername);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    appointments.add(new Appointment(
                            rs.getInt("id"),
                            rs.getString("patient_name"),
                            rs.getTimestamp("date"),
                            rs.getString("notes"),
                            rs.getString("status")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Log the error
            return null;  // Return null to indicate an error fetching data
        }

        return appointments;
    }

    // Helper method to redirect to the login page with an error message
    private void redirectToLoginPage(HttpServletResponse response, String message) throws IOException {
        response.sendRedirect("index.jsp?error=" + message);
    }

    // Helper method to redirect to an error page with a specific error message
    private void redirectToErrorPage(HttpServletResponse response, String message) throws IOException {
        response.sendRedirect("error.jsp?error=" + message);
    }
}
