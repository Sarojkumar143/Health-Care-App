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

@WebServlet("/reject-appointment")
public class RejectAppointmentServlet extends HttpServlet {

    private static final String REJECT_APPOINTMENT_QUERY = "UPDATE Consultations SET status='Rejected' WHERE id=?";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int appointmentId = getAppointmentId(request);

        if (appointmentId != -1) {
            processRejection(appointmentId, response);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);  // Bad request due to invalid appointment ID
        }
    }

    // Extract appointment ID from request, handling invalid input
    private int getAppointmentId(HttpServletRequest request) {
        try {
            return Integer.parseInt(request.getParameter("appointmentId"));
        } catch (NumberFormatException e) {
            return -1;  // Return -1 if parsing fails
        }
    }

    // Process the appointment rejection
    private void processRejection(int appointmentId, HttpServletResponse response) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(REJECT_APPOINTMENT_QUERY)) {
                stmt.setInt(1, appointmentId);
                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    response.setStatus(HttpServletResponse.SC_OK);  // Successfully rejected appointment
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);  // Appointment not found
                }
            } catch (SQLException e) {
                handleSQLException(e, response);
            }
        } catch (SQLException e) {
            handleSQLException(e, response);
        }
    }

    // Handle SQLException and send appropriate error status
    private void handleSQLException(SQLException e, HttpServletResponse response) {
        e.printStackTrace();
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);  // Internal server error
    }
}
