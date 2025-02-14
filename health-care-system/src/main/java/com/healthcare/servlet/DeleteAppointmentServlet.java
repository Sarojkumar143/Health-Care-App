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
import java.util.Date;
import java.util.List;

@WebServlet("/delete-appointment")
public class DeleteAppointmentServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int appointmentId = getAppointmentId(request);
        String username = getUsernameFromSession(request);

        if (appointmentId <= 0 || username == null) {
            redirectWithError(response, username, "Invalid appointment or session data");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            if (deleteAppointment(conn, appointmentId)) {
                List<Appointment> appointments = getUpdatedAppointments(conn, username);
                request.setAttribute("appointments", appointments);
                request.getRequestDispatcher("appointment.jsp").forward(request, response);
            } else {
                redirectWithError(response, username, "Appointment not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            redirectWithError(response, username, "Error deleting appointment");
        }
    }

    // Helper method to retrieve the appointmentId from the request
    private int getAppointmentId(HttpServletRequest request) {
        try {
            return Integer.parseInt(request.getParameter("appointmentId"));
        } catch (NumberFormatException e) {
            return -1; // Return -1 if the ID is invalid
        }
    }

    // Helper method to retrieve the username from the session
    private String getUsernameFromSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return (String) session.getAttribute("username");
    }

    // Helper method to delete the appointment from the database
    private boolean deleteAppointment(Connection conn, int appointmentId) throws SQLException {
        String sql = "DELETE FROM Consultations WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, appointmentId);
            return stmt.executeUpdate() > 0;
        }
    }

    // Helper method to retrieve the updated list of appointments for the user
    private List<Appointment> getUpdatedAppointments(Connection conn, String username) throws SQLException {
        List<Appointment> appointments = new ArrayList<>();
        String query = "SELECT c.id, c.date, c.notes, u.username AS doctor_username, u.full_name AS doctor_name, " +
                "u2.full_name AS patient_name, c.status " +
                "FROM Consultations c " +
                "JOIN Users u ON c.doctor_name = u.username " +
                "JOIN Users u2 ON c.username = u2.username " +
                "WHERE c.username = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                appointments.add(mapResultSetToAppointment(rs));
            }
        }
        return appointments;
    }

    // Helper method to map ResultSet data to Appointment object
    private Appointment mapResultSetToAppointment(ResultSet rs) throws SQLException {
        Date dbDate = rs.getTimestamp("date");
        return new Appointment(
                rs.getInt("id"),
                rs.getString("doctor_name"),
                rs.getString("doctor_username"),
                dbDate,
                rs.getString("notes"),
                rs.getString("patient_name"),
                rs.getString("status")
        );
    }

    // Helper method to redirect to the appointments page with an error message
    private void redirectWithError(HttpServletResponse response, String username, String errorMessage) throws IOException {
        response.sendRedirect("appointment.jsp?username=" + username + "&error=" + errorMessage);
    }
}
