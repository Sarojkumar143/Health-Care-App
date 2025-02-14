package com.healthcare.servlet;

import com.healthcare.db.DatabaseConnection;

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

@WebServlet("/add-user")
public class UserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("login".equals(action)) {
            String username = request.getParameter("username");
            String password = request.getParameter("password");

            try (Connection conn = DatabaseConnection.getConnection()) {
                String sql = "SELECT * FROM Users WHERE username=? AND password=?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, username);
                    stmt.setString(2, password);
                    ResultSet rs = stmt.executeQuery();

                    if (rs.next()) {
                        HttpSession session = request.getSession();
                        session.setAttribute("username", rs.getString("username"));
                        session.setAttribute("role", rs.getString("role"));
                        session.setAttribute("full_name", rs.getString("full_name")); // Store full name in session

                        String role = rs.getString("role");

                        // Redirect based on role
                        if ("admin".equalsIgnoreCase(role)) {
                            response.sendRedirect("admin-dashboard.jsp");
                        } else if ("doctor".equalsIgnoreCase(role)) {
                            response.sendRedirect("doctor-dashboard");
                        } else {
                            response.sendRedirect("patient-dashboard.jsp");
                        }
                    }

                }
            } catch (SQLException e) {
                e.printStackTrace();
                response.sendRedirect("index.jsp?error=Database error");
            }
        } else if ("register".equals(action)) {
            String fullName = request.getParameter("full_name");
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String role = request.getParameter("role");

            try (Connection conn = DatabaseConnection.getConnection()) {
                // Check if the username already exists
                String checkUserSql = "SELECT * FROM Users WHERE username=?";
                try (PreparedStatement checkStmt = conn.prepareStatement(checkUserSql)) {
                    checkStmt.setString(1, username);
                    ResultSet rs = checkStmt.executeQuery();
                    if (rs.next()) {
                        response.sendRedirect("register.jsp?error=Username already exists");
                        return;
                    }
                }

                // Insert new user with full name
                String insertSql = "INSERT INTO Users (full_name, username, password, role) VALUES (?, ?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(insertSql)) {
                    stmt.setString(1, fullName);
                    stmt.setString(2, username);
                    stmt.setString(3, password);  // Hashing passwords would be a better practice
                    stmt.setString(4, role);
                    int rowsAffected = stmt.executeUpdate();
                    if (rowsAffected > 0) {
                        response.sendRedirect("index.jsp?message=Registration successful");
                    } else {
                        response.sendRedirect("register.jsp?error=Registration failed");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                response.sendRedirect("register.jsp?error=Database error");
            }
        }
    }
}
