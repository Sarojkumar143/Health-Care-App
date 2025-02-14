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

@WebServlet("/delete-prescription")
public class DeletePrescriptionServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int prescriptionId = getPrescriptionIdFromRequest(request);

        if (prescriptionId <= 0) {
            sendErrorResponse(response, "Invalid prescription ID");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            if (deletePrescription(conn, prescriptionId)) {
                response.setStatus(HttpServletResponse.SC_OK);  // Indicate success
            } else {
                sendErrorResponse(response, "Prescription not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            sendErrorResponse(response, "Error deleting prescription");
        }
    }

    // Helper method to get the prescription ID from the request
    private int getPrescriptionIdFromRequest(HttpServletRequest request) {
        try {
            return Integer.parseInt(request.getParameter("prescriptionId"));
        } catch (NumberFormatException e) {
            return -1;  // Return -1 if the prescription ID is invalid
        }
    }

    // Helper method to delete the prescription from the database
    private boolean deletePrescription(Connection conn, int prescriptionId) throws SQLException {
        String sql = "DELETE FROM Prescriptions WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, prescriptionId);
            return stmt.executeUpdate() > 0;  // Return true if a row was deleted
        }
    }

    // Helper method to send an error response with a specific message
    private void sendErrorResponse(HttpServletResponse response, String errorMessage) throws IOException {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.getWriter().write(errorMessage);
    }
}
