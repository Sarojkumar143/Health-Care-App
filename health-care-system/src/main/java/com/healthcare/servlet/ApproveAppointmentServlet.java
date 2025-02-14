package com.healthcare.servlet;

import com.healthcare.db.DatabaseConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/approve-appointment")
public class ApproveAppointmentServlet extends HttpServlet {

    // Handles the POST request to approve an appointment
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int appointmentId = getAppointmentId(request);

        try {
            approveAppointment(appointmentId);
            response.setStatus(HttpServletResponse.SC_OK); // Return 200 status
        } catch (SQLException e) {
            handleError(response, e);
        }
    }

    // Retrieves the appointment ID from the request
    private int getAppointmentId(HttpServletRequest request) {
        return Integer.parseInt(request.getParameter("appointmentId"));
    }

    // Updates the appointment status to 'Approved' in the database
    private void approveAppointment(int appointmentId) throws SQLException {
        String sql = "UPDATE Consultations SET status = 'Approved' WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, appointmentId);
            stmt.executeUpdate();
        }
    }

    // Handles errors by logging and setting the response status to 500 (Internal Server Error)
    private void handleError(HttpServletResponse response, SQLException e) {
        e.printStackTrace();  // Log the exception
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // Return 500 status
    }
}
