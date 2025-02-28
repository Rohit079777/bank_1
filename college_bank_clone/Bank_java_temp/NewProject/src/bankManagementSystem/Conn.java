package bankManagementSystem;

import bankManagementSystem.Conn;
import java.sql.*;


public class Conn {
    public Connection c; // Connection object
    public Statement s;  // Statement object

    public Conn() {
        try {
            // Load the MySQL JDBC driver
            //Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish the connection
            c = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank", "root", "root");

            // Create a Statement object for executing queries
            s = c.createStatement();

            System.out.println("Database connected successfully!");
        } catch (Exception e) {
            System.out.println("Database connection failed: " + e.getMessage());
        }
    }
}
