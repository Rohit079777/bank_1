package bankManagementSystem;

import javax.swing.*;
import net.proteanit.sql.DbUtils;
import java.sql.*;

public class CustomerSupportService {

    public static void submitSupportRequest(JFrame parent) {
        try {
            Conn c = new Conn();

            // Enter Form Number (User Identification)
            String formno = JOptionPane.showInputDialog(parent, "Enter your Form Number:");
            if (formno == null || formno.isEmpty()) return;

            // Check if the Form Number exists in rg1 table
            String formnoQuery = "SELECT * FROM rg1 WHERE formno = ?";
            PreparedStatement ps = c.c.prepareStatement(formnoQuery);
            ps.setString(1, formno);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                JOptionPane.showMessageDialog(parent, "User not found! Invalid Form Number.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Get status from support_requests table
            String statusQuery = "SELECT status FROM support_requests WHERE formno = ?";
            ps = c.c.prepareStatement(statusQuery);
            ps.setString(1, formno);
            rs = ps.executeQuery();

            String currentStatus = "No Previous Requests";
            if (rs.next()) {
                currentStatus = rs.getString("status");
            }

            // Show Popup with Status
            String[] options = {"Submit New Request", "Cancel"};
            int choice = JOptionPane.showOptionDialog(parent,
                    "Current Request Status: " + currentStatus,
                    "Support Request",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]);

            if (choice != 0) return; // If user selects "Cancel", exit function

            // Enter the Issue or Query
            String issue = JOptionPane.showInputDialog(parent, "Enter your issue or query:");
            if (issue == null || issue.isEmpty()) return;

            // Insert Support Request into Database
            String insertRequest = "INSERT INTO support_requests (formno, issue, status) VALUES (?, ?, 'Pending')";
            ps = c.c.prepareStatement(insertRequest);
            ps.setString(1, formno);
            ps.setString(2, issue);
            ps.executeUpdate();

            // Show Confirmation Message
            JOptionPane.showMessageDialog(parent, "Your support request has been submitted successfully.\n" +
                    "Our customer support team will get back to you soon.", 
                    "Support Request Submitted", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(parent, "Error submitting support request: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void viewComplaints(JFrame parent) {
        try {
            Conn c = new Conn();
            String query = "SELECT * FROM support_requests";
            ResultSet rs = c.s.executeQuery(query);

            JTable table = new JTable(DbUtils.resultSetToTableModel(rs));
            JScrollPane sp = new JScrollPane(table);
            sp.setBounds(20, 100, 700, 250);

            JFrame frame = new JFrame("View Complaints");
            frame.setLayout(null);
            frame.setSize(800, 400);
            frame.add(sp);
            frame.setVisible(true);
            frame.setLocationRelativeTo(parent);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(parent, "Error fetching complaints: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void markAsResolved(JFrame parent) {
        try {
            Conn c = new Conn();
            String formno = JOptionPane.showInputDialog(parent, "Enter Form Number of the complaint to mark as resolved:");
            if (formno == null || formno.isEmpty()) return;

            String updateQuery = "UPDATE support_requests SET status = 'Resolved' WHERE formno = ?";
            PreparedStatement ps = c.c.prepareStatement(updateQuery);
            ps.setString(1, formno);
            int rowsUpdated = ps.executeUpdate();

            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(parent, "Complaint marked as resolved successfully.",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(parent, "No complaint found with the given Form Number.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(parent, "Error marking complaint as resolved: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void deleteComplaint(JFrame parent) {
        try {
            Conn c = new Conn();
            String formno = JOptionPane.showInputDialog(parent, "Enter Form Number of the complaint to delete:");
            if (formno == null || formno.isEmpty()) return;

            String deleteQuery = "DELETE FROM support_requests WHERE formno = ?";
            PreparedStatement ps = c.c.prepareStatement(deleteQuery);
            ps.setString(1, formno);
            int rowsDeleted = ps.executeUpdate();

            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(parent, "Complaint deleted successfully.",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(parent, "No complaint found with the given Form Number.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(parent, "Error deleting complaint: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void checkComplaintStatus(JFrame parent) {
        try {
            Conn c = new Conn();
            String formno = JOptionPane.showInputDialog(parent, "Enter your Form Number to check complaint status:");
            if (formno == null || formno.isEmpty()) return;

            String query = "SELECT issue, status FROM support_requests WHERE formno = ?";
            PreparedStatement ps = c.c.prepareStatement(query);
            ps.setString(1, formno);
            ResultSet rs = ps.executeQuery();

            StringBuilder statusMessage = new StringBuilder("📢 Your Complaint Status:\n\n");

            boolean found = false;
            while (rs.next()) {
                found = true;
                statusMessage.append("📌 Issue: ").append(rs.getString("issue")).append("\n")
                             .append("🔔 Status: ").append(rs.getString("status")).append("\n\n");
            }

            if (!found) {
                statusMessage.append("No complaints found.");
            }

            JOptionPane.showMessageDialog(parent, statusMessage.toString(), "Complaint Status", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(parent, "Error checking complaint status: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void processPayment(AccountServices accountServices) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'processPayment'");
    }

    public static void processCustomer(AccountServices accountServices) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'processCustomer'");
    }
}
