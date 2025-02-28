package bankManagementSystem;

import javax.swing.*;
import java.sql.*;

public class InvestmentService {
    public static void processInvestment(JFrame parent) {
        try {
            Conn c = new Conn();

            // 🔑 User se PIN Number Input Lena
            String pinnum = JOptionPane.showInputDialog(parent, "Enter your PIN Number:");
            if (pinnum == null || pinnum.trim().isEmpty()) {
                JOptionPane.showMessageDialog(parent, "❌ Error: PIN Number cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 🏦 PIN Number se Form Number Fetch Karna
            String formno = null;
            String getFormnoQuery = "SELECT formno FROM login WHERE pinnum = ?";
            try (PreparedStatement ps = c.c.prepareStatement(getFormnoQuery)) {
                ps.setString(1, pinnum);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    formno = rs.getString("formno");
                }
            }

            if (formno == null) {
                JOptionPane.showMessageDialog(parent, "❌ Invalid PIN Number!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 📊 Select Investment Type
            String[] options = {"Fixed Deposit", "Mutual Funds", "Stocks", "Investment Status"};
            String selectedOption = (String) JOptionPane.showInputDialog(parent, "Select Investment Option:", "Investment Services", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

            if (selectedOption == null) return;

            if (selectedOption.equals("Investment Status")) {
                viewInvestmentStatus(parent, pinnum);
                return;
            }

            // 💰 Investment Amount Input
            String amountStr = JOptionPane.showInputDialog(parent, "Enter Investment Amount:");
            if (amountStr == null || amountStr.trim().isEmpty()) return;

            double amount;
            try {
                amount = Double.parseDouble(amountStr);
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(parent, "❌ Investment amount must be greater than 0!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(parent, "❌ Invalid amount format!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // ⏳ Investment Tenure Input
            String tenureStr = JOptionPane.showInputDialog(parent, "Enter Investment Tenure (Years):");
            if (tenureStr == null || tenureStr.trim().isEmpty()) return;

            int tenure;
            try {
                tenure = Integer.parseInt(tenureStr);
                if (tenure <= 0) {
                    JOptionPane.showMessageDialog(parent, "❌ Tenure must be greater than 0!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(parent, "❌ Invalid tenure format!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 📈 Interest Rate Calculation
            double interestRate;
            switch (selectedOption) {
                case "Fixed Deposit":
                    interestRate = 6.0;
                    break;
                case "Mutual Funds":
                    interestRate = 12.0;
                    break;
                case "Stocks":
                    interestRate = 15.0;
                    break;
                default:
                    interestRate = 0.0;
            }

            // 📊 Expected Returns Calculation: A = P * (1 + R/100)^N
            double expectedReturn = amount * Math.pow(1 + (interestRate / 100), tenure);

            // 📝 Insert Investment Data
            String insertInvestment = "INSERT INTO investments (formno, investment_type, amount, tenure, expected_return, interest_rate, status) VALUES (?, ?, ?, ?, ?, ?, 'Pending')";
            try (PreparedStatement ps = c.c.prepareStatement(insertInvestment)) {
                ps.setString(1, formno);
                ps.setString(2, selectedOption);
                ps.setDouble(3, amount);
                ps.setInt(4, tenure);
                ps.setDouble(5, expectedReturn);
                ps.setDouble(6, interestRate);
                ps.executeUpdate();
            }

            // 📜 Investment Summary
            JOptionPane.showMessageDialog(parent, "✅ Investment Request Submitted!\n\n" +
                            "📌 Investment Type: " + selectedOption + "\n" +
                            "💰 Investment Amount: ₹" + amount + "\n" +
                            "⏳ Tenure: " + tenure + " years\n" +
                            "📊 Interest Rate: " + interestRate + "%\n" +
                            "💵 Expected Return: ₹" + String.format("%.2f", expectedReturn) + "\n\n" +
                            "🔔 Status: Pending Admin Approval",
                    "Investment Confirmation", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            JOptionPane.showMessageDialog(parent, "❌ SQL Error: " + sqlEx.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(parent, "❌ Unexpected Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void viewInvestmentStatus(JFrame parent, String pinnum) {
        try {
            Conn c = new Conn();
            
            // PIN Number se Form Number Fetch Karna
            String formno = null;
            String getFormnoQuery = "SELECT formno FROM login WHERE pinnum = ?";
            try (PreparedStatement ps = c.c.prepareStatement(getFormnoQuery)) {
                ps.setString(1, pinnum);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    formno = rs.getString("formno");
                }
            }

            if (formno == null) {
                JOptionPane.showMessageDialog(parent, "❌ Invalid PIN Number!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Investment Fetch Karna
            String statusQuery = "SELECT investment_type, amount, tenure, expected_return, interest_rate, status FROM investments WHERE formno = ?";
            try (PreparedStatement ps = c.c.prepareStatement(statusQuery)) {
                ps.setString(1, formno);
                ResultSet rs = ps.executeQuery();

                StringBuilder statusMessage = new StringBuilder("📢 Your Investment Status:\n\n");

                boolean found = false;
                while (rs.next()) {
                    found = true;
                    statusMessage.append("📌 Investment Type: ").append(rs.getString("investment_type")).append("\n")
                                 .append("💰 Amount: ₹").append(rs.getDouble("amount")).append("\n")
                                 .append("⏳ Tenure: ").append(rs.getInt("tenure")).append(" years\n")
                                 .append("📊 Expected Return: ₹").append(rs.getDouble("expected_return")).append("\n")
                                 .append("📈 Interest Rate: ").append(rs.getDouble("interest_rate")).append("%\n")
                                 .append("🔔 Status: ").append(rs.getString("status")).append("\n\n");
                }

                if (!found) {
                    statusMessage.append("No investments found.");
                }

                JOptionPane.showMessageDialog(parent, statusMessage.toString(), "Investment Status", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            JOptionPane.showMessageDialog(parent, "❌ SQL Error: " + sqlEx.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}