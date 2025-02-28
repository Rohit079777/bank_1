package bankManagementSystem;

import javax.swing.*;
import java.sql.*;

public class LoanService {
    public static void processLoan(JFrame parent) {
        try {
            Conn c = new Conn();

            // User se PIN Number Input Lena
            String pinnum = JOptionPane.showInputDialog(parent, "Enter PIN Number:");
            if (pinnum == null || pinnum.isEmpty()) {
                JOptionPane.showMessageDialog(parent, "Error: PIN Number cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Select Loan Type or Loan Status
            String[] loanTypes = {"Personal Loan", "Home Loan", "Car Loan", "Loan Status"};
            String loanType = (String) JOptionPane.showInputDialog(parent, "Select Loan Type:", "Loan Services", JOptionPane.QUESTION_MESSAGE, null, loanTypes, loanTypes[0]);

            if (loanType == null) return;

            if (loanType.equals("Loan Status")) {
                viewLoanStatus(parent, pinnum);
                return;
            }

            // Enter Loan Amount
            String amountStr = JOptionPane.showInputDialog(parent, "Enter Loan Amount:");
            if (amountStr == null || amountStr.isEmpty()) return;
            double amount = Double.parseDouble(amountStr);

            // Enter Tenure (Years)
            String tenureStr = JOptionPane.showInputDialog(parent, "Enter Loan Tenure (Years):");
            if (tenureStr == null || tenureStr.isEmpty()) return;
            int tenure = Integer.parseInt(tenureStr);

            // Interest Rate Calculation
            double interestRate = loanType.equals("Personal Loan") ? 12.0 : loanType.equals("Home Loan") ? 8.0 : 10.0;
            double monthlyInterest = interestRate / (12 * 100);
            int months = tenure * 12;
            double emi = (amount * monthlyInterest * Math.pow(1 + monthlyInterest, months)) / (Math.pow(1 + monthlyInterest, months) - 1);
            double totalRepayment = emi * months;

            // Check Account Balance
            String balanceQuery = "SELECT account_balance FROM login WHERE pinnum = ?";
            PreparedStatement ps = c.c.prepareStatement(balanceQuery);
            ps.setString(1, pinnum);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                JOptionPane.showMessageDialog(parent, "Error: No account found with PIN Number " + pinnum, "Loan Services", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double balance = rs.getDouble("account_balance");
            JOptionPane.showMessageDialog(parent, "Your Balance: ₹" + balance);

            if (balance < 50000) {
                JOptionPane.showMessageDialog(parent, "Loan Denied! Minimum balance required: ₹50,000", "Loan Services", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Insert Loan Request
            String insertLoan = "INSERT INTO loans (pinnum, loan_type, amount, tenure, interest_rate, emi, total_repayment, status) VALUES (?, ?, ?, ?, ?, ?, ?, 'Pending')";
            ps = c.c.prepareStatement(insertLoan);
            ps.setString(1, pinnum);
            ps.setString(2, loanType);
            ps.setDouble(3, amount);
            ps.setInt(4, tenure);
            ps.setDouble(5, interestRate);
            ps.setDouble(6, emi);
            ps.setDouble(7, totalRepayment);
            ps.executeUpdate();

            // Show Loan Summary
            JOptionPane.showMessageDialog(parent, "Loan Request Sent to Admin for Approval.\n\n" +
                    "Loan Details:\n" +
                    "-----------------------------\n" +
                    "Loan Type: " + loanType + "\n" +
                    "Loan Amount: ₹" + amount + "\n" +
                    "Tenure: " + tenure + " years\n" +
                    "Interest Rate: " + interestRate + "%\n" +
                    "Monthly EMI: ₹" + String.format("%.2f", emi) + "\n" +
                    "Total Repayment: ₹" + String.format("%.2f", totalRepayment),
                    "Loan Details", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            JOptionPane.showMessageDialog(parent, "SQL Error: " + sqlEx.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException nfEx) {
            JOptionPane.showMessageDialog(parent, "Invalid Number Format!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(parent, "Unexpected Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void viewLoanStatus(JFrame parent, String pinnum) {
        try {
            Conn c = new Conn();
            String statusQuery = "SELECT status FROM loans WHERE pinnum = ?";
            PreparedStatement ps = c.c.prepareStatement(statusQuery);
            ps.setString(1, pinnum);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String status = rs.getString("status");
                JOptionPane.showMessageDialog(parent, "Your Loan Status: " + status, "Loan Status", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(parent, "Error: No loan found for this PIN Number.", "Loan Status", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            JOptionPane.showMessageDialog(parent, "SQL Error: " + sqlEx.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(parent, "Unexpected Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
