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

@WebServlet("/my-appointments")
public class ConsultationServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = getUsernameFromSession(request);
        String doctorName = request.getParameter("doctorId");
        String date = request.getParameter("date");
        String notes = request.getParameter("notes");

        if (username == null || doctorName == null || date == null) {
            response.sendRedirect("error.jsp"); // Handle invalid input
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            // Insert consultation details
            insertConsultation(conn, username, doctorName, date, notes);

            // Fetch updated appointments and forward to JSP
            List<Appointment> appointments = getAppointmentsForUser(conn, username);
            request.setAttribute("appointments", appointments);
            request.getRequestDispatcher("appointment.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp"); // Handle error
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = getUsernameFromSession(request);

        if (username == null || username.isEmpty()) {
            response.sendRedirect("error.jsp"); // Handle error if no username is found
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            List<Appointment> appointments = getAppointmentsForUser(conn, username);
            request.setAttribute("appointments", appointments);
            request.getRequestDispatcher("appointment.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp"); // Handle error
        }
    }

    // Helper method to retrieve the username from the session
    private String getUsernameFromSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return (String) session.getAttribute("username");
    }

    // Helper method to insert consultation details into the database
    private void insertConsultation(Connection conn, String username, String doctorName, String date, String notes) throws SQLException {
        String sql = "INSERT INTO Consultations (username, doctor_name, date, notes) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, doctorName);
            stmt.setString(3, date);
            stmt.setString(4, notes);
            stmt.executeUpdate(); // Execute the insert query
        }
    }

    // Helper method to fetch appointments for the logged-in user
    private List<Appointment> getAppointmentsForUser(Connection conn, String username) throws SQLException {
        List<Appointment> appointments = new ArrayList<>();
        String query = "SELECT c.id, c.date, c.notes, u.username AS doctor_username, u.full_name AS doctor_name, u2.full_name AS patient_name, c.status " +
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

    // Helper method to map ResultSet to Appointment object
    private Appointment mapResultSetToAppointment(ResultSet rs) throws SQLException {
        Date dbDate = rs.getTimestamp("date");
        String status = rs.getString("status");

        return new Appointment(
                rs.getInt("id"),
                rs.getString("doctor_name"),
                rs.getString("doctor_username"),
                dbDate,
                rs.getString("notes"),
                rs.getString("patient_name"),
                status
        );
    }
}
