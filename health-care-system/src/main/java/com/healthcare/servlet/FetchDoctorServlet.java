package com.healthcare.servlet;

import com.healthcare.db.DatabaseConnection;
import com.healthcare.model.Doctor;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/book-consultation")
public class FetchDoctorServlet extends HttpServlet {

    private static final String DOCTORS_QUERY = "SELECT username, full_name FROM Users WHERE role = 'doctor'";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Doctor> doctors = fetchDoctors();

        if (doctors == null) {
            redirectToErrorPage(response, "Unable to fetch doctors at this time.");
            return;
        }

        request.setAttribute("doctors", doctors);
        request.getRequestDispatcher("book-consultation.jsp").forward(request, response);
    }

    // Fetch doctors from the database
    private List<Doctor> fetchDoctors() {
        List<Doctor> doctors = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(DOCTORS_QUERY);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String username = resultSet.getString("username");
                String fullName = resultSet.getString("full_name");
                doctors.add(new Doctor(username, fullName));
            }

        } catch (SQLException e) {
            e.printStackTrace();  // Log error
            return null;  // Return null to indicate an error
        }

        return doctors;
    }

    // Redirect to an error page with the specified message
    private void redirectToErrorPage(HttpServletResponse response, String message) throws IOException {
        response.sendRedirect("error.jsp?error=" + message);
    }
}
