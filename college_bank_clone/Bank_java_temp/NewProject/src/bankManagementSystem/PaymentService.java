package bankManagementSystem;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PaymentService {

    public static void processPayment(JFrame parent) {
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

            // Check if user has an active loan
            String loanQuery = "SELECT id, total_repayment FROM loans WHERE formno = ? AND status = 'Active'";
            ps = c.c.prepareStatement(loanQuery);
            ps.setString(1, formno);
            rs = ps.executeQuery();

            boolean hasLoan = rs.next();
            int id = hasLoan ? rs.getInt("id") : -1;
            double total_repayment = hasLoan ? rs.getDouble("total_repayment") : 0;

            // Select Payment Type (Include Loan EMI if user has an active loan)
            String[] paymentTypes;
            if (hasLoan) {
                paymentTypes = new String[]{"Loan EMI Payment", "Utility Bill Payment", "Mobile/DTH Recharge", "Credit Card Payment"};
            } else {
                paymentTypes = new String[]{"Utility Bill Payment", "Mobile/DTH Recharge", "Credit Card Payment"};
            }

            String paymentType = (String) JOptionPane.showInputDialog(parent, "Select Payment Type:",
                    "Payment Services", JOptionPane.QUESTION_MESSAGE, null, paymentTypes, paymentTypes[0]);

            if (paymentType == null) return;

            // Enter Payment Amount
            double amount;
            if (paymentType.equals("Loan EMI Payment")) {
                String amountStr = JOptionPane.showInputDialog(parent, "Enter Loan EMI Amount (Max: ₹" + total_repayment + "):");
                if (amountStr == null || amountStr.isEmpty()) return;
                amount = Double.parseDouble(amountStr);

                if (amount > total_repayment) {
                    JOptionPane.showMessageDialog(parent, "Amount cannot be greater than total repayment!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else {
                String amountStr = JOptionPane.showInputDialog(parent, "Enter Payment Amount:");
                if (amountStr == null || amountStr.isEmpty()) return;
                amount = Double.parseDouble(amountStr);
            }

            // Check if User has sufficient balance (for example, 1000 is the minimum balance)
            String balanceQuery = "SELECT income FROM rg2 WHERE formno = ?";
            ps = c.c.prepareStatement(balanceQuery);
            ps.setString(1, formno);
            rs = ps.executeQuery();

            if (rs.next()) {
                double balance = Double.parseDouble(rs.getString("income").replaceAll("[^0-9]", "")); // Extract balance from income field
                if (balance < amount) {
                    JOptionPane.showMessageDialog(parent, "Insufficient balance!", "Payment Failed", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Insert Payment Details into Database
                String insertPayment = "INSERT INTO payments (formno, payment_type, amount) VALUES (?, ?, ?)";
                ps = c.c.prepareStatement(insertPayment);
                ps.setString(1, formno);
                ps.setString(2, paymentType);
                ps.setDouble(3, amount);
                ps.executeUpdate();

                // If it's a Loan EMI Payment, update the loan repayment
                if (paymentType.equals("Loan EMI Payment")) {
                    String updateLoanRepayment = "UPDATE loans SET total_repayment = total_repayment - ? WHERE id = ?";
                    ps = c.c.prepareStatement(updateLoanRepayment);
                    ps.setDouble(1, amount);
                    ps.setInt(2, id);
                    ps.executeUpdate();
                    
                    // Check if loan is fully repaid
                    String checkLoanStatus = "SELECT total_repayment FROM loans WHERE id = ?";
                    ps = c.c.prepareStatement(checkLoanStatus);
                    ps.setInt(1, id);
                    rs = ps.executeQuery();
                    
                    if (rs.next() && rs.getDouble("total_repayment") <= 0) {
                        String completeLoan = "UPDATE loans SET status = 'Completed' WHERE id = ?";
                        ps = c.c.prepareStatement(completeLoan);
                        ps.setInt(1, id);
                        ps.executeUpdate();
                    }
                }

                // Get the current date and format it to show only the date (without time)
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate = dateFormat.format(new Date());

                // Show Confirmation Message
                JOptionPane.showMessageDialog(parent, "Payment Successful!\n" +
                        "Payment Type: " + paymentType + "\n" +
                        "Amount: ₹" + amount + "\n" +
                        "Date: " + formattedDate,
                        "Payment Successful", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(parent, "User not found! Invalid Form Number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(parent, "Error processing payment: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
