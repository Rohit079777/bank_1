package bankManagementSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AccountServices extends JFrame {

    String pinnum;

    public AccountServices(String pinnum) {

        this.pinnum = pinnum;
        setTitle("World Bank Management System");
        setLayout(null);

        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("bankManagementSystem/bg_7.png"));
        Image i2 = i1.getImage().getScaledInstance(300, 250, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel image = new JLabel(i3);

        // Image ko upar shift karne ke liye y-axis ki value kam karein
        image.setBounds(260, 200, 350, 250); // -20 se aur upar chala jayega
        add(image);

        // Background blue set karne ke liye
        getContentPane().setBackground(new Color(135, 206, 235)); 
        // JLabel ke andar bhi elements properly align honge

        // Title
        JLabel title = new JLabel("World Bank Services");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 36));
        title.setBounds(250, 50, 400, 40);
        add(title);

        // Main Button to Trigger Popup Menu for Account Services
        JButton accountServicesButton = new JButton("Account Services");
        accountServicesButton.setBounds(20, 120, 220, 40);
        accountServicesButton.setFont(new Font("Arial", Font.BOLD, 18));
        add(accountServicesButton);

        // Popup Menu for Account Services
        JPopupMenu accountPopupMenu = new JPopupMenu();

        JMenuItem viewProfile = new JMenuItem("View Profile");
        JMenuItem updateProfile = new JMenuItem("Change PIN");
        JMenuItem checkBalance = new JMenuItem("Check Balance");
        JMenuItem viewMiniStatement = new JMenuItem("View Mini Statement");

        accountPopupMenu.add(viewProfile);
        accountPopupMenu.add(updateProfile);
        accountPopupMenu.add(checkBalance);
        accountPopupMenu.add(viewMiniStatement);

        // Action listeners for Account Services menu items
        viewProfile.addActionListener(e -> {

            String enteredPin = JOptionPane.showInputDialog(this, "Enter your PIN number:");

            if (enteredPin == null || enteredPin.isEmpty()) {
                JOptionPane.showMessageDialog(this, "PIN number is required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                Conn c = new Conn(); // Make sure Conn class is configured for DB connection

                // SQL query to join login, rg1, and rg2 tables
                String query = "SELECT * FROM rg1 r1 INNER JOIN rg2 r2 ON r1.formno = r2.formno " +
                        "INNER JOIN login l ON r1.formno = l.formno WHERE l.pinnum = ?";
                PreparedStatement ps = c.c.prepareStatement(query);
                ps.setString(1, enteredPin); // Use entered PIN for authentication

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    // Extract data from rg1 and rg2 tables
                    String name = rs.getString("name");
                    String fname = rs.getString("fname");
                    String dob = rs.getString("dob");
                    String gender = rs.getString("gender");
                    String email = rs.getString("email");
                    String marital = rs.getString("marital");
                    String add1 = rs.getString("add1");
                    String city = rs.getString("city");
                    String state = rs.getString("state");
                    String pincode = rs.getString("pincode");
                    String religion = rs.getString("religion");
                    String category = rs.getString("category");
                    String income = rs.getString("income");
                    String education = rs.getString("education");
                    String occupation = rs.getString("occupation");
                    String pan = rs.getString("pan");
                    String adhar = rs.getString("adhar");
                    String seniorCitizen = rs.getString("seniorCitizen");
                    String existingAccount = rs.getString("existingAccount");

                    // Display profile details
                    JOptionPane.showMessageDialog(this,
                            "Profile Details:\n" +
                                    "Name: " + name + "\n" +
                                    "Father's Name: " + fname + "\n" +
                                    "Date of Birth: " + dob + "\n" +
                                    "Gender: " + gender + "\n" +
                                    "Email: " + email + "\n" +
                                    "Marital Status: " + marital + "\n" +
                                    "Address: " + add1 + "\n" +
                                    "City: " + city + "\n" +
                                    "State: " + state + "\n" +
                                    "Pincode: " + pincode + "\n" +
                                    "Religion: " + religion + "\n" +
                                    "Category: " + category + "\n" +
                                    "Income: " + income + "\n" +
                                    "Education: " + education + "\n" +
                                    "Occupation: " + occupation + "\n" +
                                    "PAN: " + pan + "\n" +
                                    "Aadhar: " + adhar + "\n" +
                                    "Senior Citizen: " + seniorCitizen + "\n" +
                                    "Existing Account: " + existingAccount,
                            "View Profile", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "No profile found for the entered PIN number!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error retrieving profile: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        // change current pin
        updateProfile.addActionListener(e -> {
            // Prompt the user to enter current PIN
            String currentPin = JOptionPane.showInputDialog(this, "Enter your current PIN:");

            // Check if the entered current PIN is valid
            if (currentPin == null || currentPin.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Current PIN is required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                // Connect to the database
                Conn c = new Conn();

                // Verify if the entered current PIN exists in the database
                String verifyQuery = "SELECT * FROM login WHERE pinnum = ?";
                PreparedStatement psVerify = c.c.prepareStatement(verifyQuery);
                psVerify.setString(1, currentPin);
                ResultSet rsVerify = psVerify.executeQuery();

                if (rsVerify.next()) {
                    // If current PIN is valid, prompt for new PIN
                    String newPin = JOptionPane.showInputDialog(this, "Enter your new 4-digit PIN:");

                    // Validate new PIN (Must be 4 digits and numeric)
                    if (newPin == null || newPin.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "New PIN is required!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Check if the new PIN is 4 digits and numeric . (\\d+ is a only for digits)
                    if (newPin.length() != 4 || !newPin.matches("\\d+")) {
                        JOptionPane.showMessageDialog(this, "PIN must be 4 digits and numeric!", "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Check if the new PIN already exists in the database
                    String checkPinQuery = "SELECT * FROM login WHERE pinnum = ?";
                    PreparedStatement psCheckPin = c.c.prepareStatement(checkPinQuery);
                    psCheckPin.setString(1, newPin);
                    ResultSet rsCheckPin = psCheckPin.executeQuery();

                    if (rsCheckPin.next()) {
                        JOptionPane.showMessageDialog(this, "This PIN is already in use. Please choose another PIN.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Update the new PIN in the login table
                    String updateQuery = "UPDATE login SET pinnum = ? WHERE pinnum = ?";
                    PreparedStatement psUpdate = c.c.prepareStatement(updateQuery);
                    psUpdate.setString(1, newPin);
                    psUpdate.setString(2, currentPin);

                    int rowsUpdated = psUpdate.executeUpdate();

                    if (rowsUpdated > 0) {
                        JOptionPane.showMessageDialog(this, "PIN updated successfully!", "Success",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to update PIN. Please try again.", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Current PIN is incorrect!", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error updating PIN: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        checkBalance.addActionListener(e -> {
            // Prompt for PIN number
            String enteredPin = JOptionPane.showInputDialog(this, "Enter your PIN number:");

            // If user cancels the input or enters an empty PIN, return
            if (enteredPin == null || enteredPin.isEmpty()) {
                JOptionPane.showMessageDialog(this, "PIN number is required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                // Connect to the database
                Conn c = new Conn();

                // Query to fetch the balance from login table using the PIN number
                String query = "SELECT account_balance FROM login WHERE pinnum = ?";
                PreparedStatement ps = c.c.prepareStatement(query);
                ps.setString(1, enteredPin); // Use entered PIN for fetching balance

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    // Extract account balance
                    double balance = rs.getDouble("account_balance");

                    // Display balance
                    JOptionPane.showMessageDialog(this, "Your Account Balance is: " + balance,
                            "Check Balance", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "No account found for the entered PIN number!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error retrieving balance: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        viewMiniStatement.addActionListener(e -> {
            // Prompt for PIN number
            String enteredPin = JOptionPane.showInputDialog(this, "Enter your PIN number:");

            // If user cancels the input or enters an empty PIN, return
            if (enteredPin == null || enteredPin.isEmpty()) {
                JOptionPane.showMessageDialog(this, "PIN number is required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                // Connect to the database
                Conn c = new Conn();

                // Query to fetch the formno using the PIN number
                String query = "SELECT formno FROM login WHERE pinnum = ?";
                PreparedStatement ps = c.c.prepareStatement(query);
                ps.setString(1, enteredPin);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    String formno = rs.getString("formno");

                    // Query to fetch the mini statement for the formno
                    String transactionQuery = "SELECT * FROM transactions WHERE formno = ? ORDER BY transaction_date DESC LIMIT 5";
                    ps = c.c.prepareStatement(transactionQuery);
                    ps.setString(1, formno);
                    rs = ps.executeQuery();

                    StringBuilder miniStatement = new StringBuilder("Mini Statement:\n");
                    boolean hasTransactions = false;

                    while (rs.next()) {
                        hasTransactions = true;
                        String transactionType = rs.getString("transaction_type");
                        double amount = rs.getDouble("transaction_amount");
                        String date = rs.getString("transaction_date");

                        miniStatement.append("Type: ").append(transactionType)
                                .append(", Amount: ").append(amount)
                                .append(", Date: ").append(date).append("\n");
                    }

                    if (!hasTransactions) {
                        miniStatement.append("No transactions found.");
                    }

                    // Display the mini statement
                    JOptionPane.showMessageDialog(this, miniStatement.toString(), "Mini Statement",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid PIN number!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error retrieving mini statement: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // Show Popup Menu on Button Click for Account Services
        accountServicesButton.addActionListener(
                e -> accountPopupMenu.show(accountServicesButton, 0, accountServicesButton.getHeight()));

        // Main Button to Trigger Popup Menu for Transaction Services
        JButton transactionServicesButton = new JButton("Transaction Services");
        transactionServicesButton.setBounds(630, 120, 230, 40);
        transactionServicesButton.setFont(new Font("Arial", Font.BOLD, 18));
        add(transactionServicesButton);

        // Popup Menu for Transaction Services
        JPopupMenu transactionPopupMenu = new JPopupMenu();

        JMenuItem depositMoney = new JMenuItem("Deposit Money");
        JMenuItem withdrawMoney = new JMenuItem("Withdraw Money");
        JMenuItem fundTransfer = new JMenuItem("Fund Transfer");
        JMenuItem viewTransactionHistory = new JMenuItem("View Transaction History");

        transactionPopupMenu.add(depositMoney);
        transactionPopupMenu.add(withdrawMoney);
        transactionPopupMenu.add(fundTransfer);
        transactionPopupMenu.add(viewTransactionHistory);

        // Action listeners for Transaction Services menu items
        depositMoney.addActionListener(e -> {
            // Prompt for PIN number
            String enteredPin = JOptionPane.showInputDialog(this, "Enter your PIN number:");

            // If user cancels the input or enters an empty PIN, return
            if (enteredPin == null || enteredPin.isEmpty()) {
                JOptionPane.showMessageDialog(this, "PIN number is required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                // Connect to the database
                Conn c = new Conn();

                // Query to fetch the formno and current balance using the PIN number
                String query = "SELECT formno, account_balance FROM login WHERE pinnum = ?";
                PreparedStatement ps = c.c.prepareStatement(query);
                ps.setString(1, enteredPin);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    String formno = rs.getString("formno");
                    double currentBalance = rs.getDouble("account_balance");

                    // Ask for deposit amount
                    String depositAmountStr = JOptionPane.showInputDialog(this, "Enter deposit amount:");
                    if (depositAmountStr == null || depositAmountStr.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Deposit amount is required!", "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    double depositAmount = Double.parseDouble(depositAmountStr);

                    // Insert the deposit transaction into the transactions table
                    String insertTransactionQuery = "INSERT INTO transactions (formno, transaction_type, transaction_amount) VALUES (?, 'Deposit', ?)";
                    ps = c.c.prepareStatement(insertTransactionQuery);
                    ps.setString(1, formno);
                    ps.setDouble(2, depositAmount);
                    ps.executeUpdate();

                    // Update the account balance in the login table
                    double newBalance = currentBalance + depositAmount;
                    String updateBalanceQuery = "UPDATE login SET account_balance = ? WHERE pinnum = ?";
                    ps = c.c.prepareStatement(updateBalanceQuery);
                    ps.setDouble(1, newBalance);
                    ps.setString(2, enteredPin);
                    ps.executeUpdate();

                    JOptionPane.showMessageDialog(this, "Deposit successful! New balance: " + newBalance);
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid PIN number!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error processing deposit: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        withdrawMoney.addActionListener(e -> {
            // Prompt for PIN number
            String enteredPin = JOptionPane.showInputDialog(this, "Enter your PIN number:");

            // If user cancels the input or enters an empty PIN, return
            if (enteredPin == null || enteredPin.isEmpty()) {
                JOptionPane.showMessageDialog(this, "PIN number is required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                // Connect to the database
                Conn c = new Conn();

                // Query to fetch formno and current balance using the PIN number
                String query = "SELECT formno, account_balance FROM login WHERE pinnum = ?";
                PreparedStatement ps = c.c.prepareStatement(query);
                ps.setString(1, enteredPin);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    String formno = rs.getString("formno");
                    double currentBalance = rs.getDouble("account_balance");

                    // Prompt for withdrawal amount
                    String withdrawAmountStr = JOptionPane.showInputDialog(this, "Enter amount to withdraw:");
                    if (withdrawAmountStr == null || withdrawAmountStr.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Withdrawal amount is required!", "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    double withdrawAmount = Double.parseDouble(withdrawAmountStr);

                    // Check if there is enough balance
                    if (withdrawAmount > currentBalance) {
                        JOptionPane.showMessageDialog(this, "Insufficient balance!", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    } else {
                        // Deduct amount from balance
                        double newBalance = currentBalance - withdrawAmount;

                        // Update the account balance in the login table
                        String updateBalanceQuery = "UPDATE login SET account_balance = ? WHERE pinnum = ?";
                        ps = c.c.prepareStatement(updateBalanceQuery);
                        ps.setDouble(1, newBalance);
                        ps.setString(2, enteredPin);
                        ps.executeUpdate();

                        // Insert withdrawal transaction into the transactions table
                        String insertTransactionQuery = "INSERT INTO transactions (formno, transaction_type, transaction_amount) VALUES (?, 'Withdrawal', ?)";
                        ps = c.c.prepareStatement(insertTransactionQuery);
                        ps.setString(1, formno);
                        ps.setDouble(2, withdrawAmount);
                        ps.executeUpdate();

                        JOptionPane.showMessageDialog(this, "Withdrawal successful! New balance: " + newBalance);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid PIN number!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error processing withdrawal: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        fundTransfer.addActionListener(e -> {
            // Prompt for sender's PIN number
            String senderPin = JOptionPane.showInputDialog(this, "Enter your PIN number:");

            // If user cancels the input or enters an empty PIN, return
            if (senderPin == null || senderPin.isEmpty()) {
                JOptionPane.showMessageDialog(this, "PIN number is required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                // Connect to the database
                Conn c = new Conn();

                // Query to fetch sender's account balance using the PIN
                String senderQuery = "SELECT formno, account_balance FROM login WHERE pinnum = ?";
                PreparedStatement ps = c.c.prepareStatement(senderQuery);
                ps.setString(1, senderPin);
                ResultSet senderRs = ps.executeQuery();

                if (senderRs.next()) {
                    String senderFormno = senderRs.getString("formno");
                    double senderBalance = senderRs.getDouble("account_balance");

                    // Prompt for recipient's formno
                    String recipientFormno = JOptionPane.showInputDialog(this, "Enter the recipient's form number:");
                    if (recipientFormno == null || recipientFormno.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Recipient's form number is required!", "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Prompt for transfer amount
                    String transferAmountStr = JOptionPane.showInputDialog(this, "Enter transfer amount:");
                    if (transferAmountStr == null || transferAmountStr.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Transfer amount is required!", "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    double transferAmount = Double.parseDouble(transferAmountStr);

                    // Check if sender has enough balance
                    if (transferAmount > senderBalance) {
                        JOptionPane.showMessageDialog(this, "Insufficient balance!", "Error",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Query to check if recipient's formno exists
                    String recipientQuery = "SELECT formno FROM login WHERE formno = ?";
                    ps = c.c.prepareStatement(recipientQuery);
                    ps.setString(1, recipientFormno);
                    ResultSet recipientRs = ps.executeQuery();

                    if (recipientRs.next()) {
                        // Deduct amount from sender's account
                        double newSenderBalance = senderBalance - transferAmount;
                        String updateSenderBalanceQuery = "UPDATE login SET account_balance = ? WHERE formno = ?";
                        ps = c.c.prepareStatement(updateSenderBalanceQuery);
                        ps.setDouble(1, newSenderBalance);
                        ps.setString(2, senderFormno);
                        ps.executeUpdate();

                        // Add amount to recipient's account
                        String updateRecipientBalanceQuery = "UPDATE login SET account_balance = account_balance + ? WHERE formno = ?";
                        ps = c.c.prepareStatement(updateRecipientBalanceQuery);
                        ps.setDouble(1, transferAmount);
                        ps.setString(2, recipientFormno);
                        ps.executeUpdate();

                        // Insert transaction for sender
                        String insertSenderTransactionQuery = "INSERT INTO transactions (formno, transaction_type, transaction_amount) VALUES (?, 'Transfer (Debit)', ?)";
                        ps = c.c.prepareStatement(insertSenderTransactionQuery);
                        ps.setString(1, senderFormno);
                        ps.setDouble(2, transferAmount);
                        ps.executeUpdate();

                        // Insert transaction for recipient
                        String insertRecipientTransactionQuery = "INSERT INTO transactions (formno, transaction_type, transaction_amount) VALUES (?, 'Transfer (Credit)', ?)";
                        ps = c.c.prepareStatement(insertRecipientTransactionQuery);
                        ps.setString(1, recipientFormno);
                        ps.setDouble(2, transferAmount);
                        ps.executeUpdate();

                        JOptionPane.showMessageDialog(this,
                                "Fund transfer successful! Remaining balance: " + newSenderBalance);
                    } else {
                        JOptionPane.showMessageDialog(this, "Recipient's account not found!", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid PIN number!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error processing fund transfer: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        viewTransactionHistory.addActionListener(e -> {
            String userPin = JOptionPane.showInputDialog(this, "Enter your PIN number:");

            // If the user cancels or leaves the PIN empty
            if (userPin == null || userPin.isEmpty()) {
                JOptionPane.showMessageDialog(this, "PIN number is required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                // Establish database connection
                Conn c = new Conn();

                // Query to fetch transaction history based on the user's PIN
                String query = "SELECT * FROM transactions WHERE formno = (SELECT formno FROM login WHERE pinnum = ?)";
                PreparedStatement ps = c.c.prepareStatement(query);
                ps.setString(1, userPin);
                ResultSet rs = ps.executeQuery();

                // Build a string to display transactions
                StringBuilder transactionHistory = new StringBuilder("Transaction History:\n\n");
                boolean hasTransactions = false;

                while (rs.next()) {
                    hasTransactions = true;
                    String transactionType = rs.getString("transaction_type");
                    double transactionAmount = rs.getDouble("transaction_amount");
                    String date = rs.getString("transaction_date"); // Assuming you have a date column
                    transactionHistory.append("Date: ").append(date)
                            .append("\nType: ").append(transactionType)
                            .append("\nAmount: ").append(transactionAmount)
                            .append("\n---------------------------\n");
                }

                if (hasTransactions) {
                    // Create a JTextArea for displaying transaction history
                    JTextArea textArea = new JTextArea(transactionHistory.toString());
                    textArea.setEditable(false); // Make it read-only
                    textArea.setFont(new Font("Arial", Font.PLAIN, 14));

                    // Add the JTextArea to a JScrollPane
                    JScrollPane scrollPane = new JScrollPane(textArea);
                    scrollPane.setPreferredSize(new Dimension(500, 400)); // Set scrollable area size

                    // Display the scrollable dialog
                    JOptionPane.showMessageDialog(this, scrollPane, "Transaction History",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "No transactions found!", "Info",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error retrieving transaction history: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // Show Popup Menu on Button Click for Transaction Services
        transactionServicesButton.addActionListener(
                e -> transactionPopupMenu.show(transactionServicesButton, 0, transactionServicesButton.getHeight()));

        // Additional Button for Loan Services (Optional)
        JButton loanServicesButton = new JButton("Loan Services");
        loanServicesButton.setBounds(630, 300, 230, 40);
        loanServicesButton.setFont(new Font("Arial", Font.BOLD, 18));
        add(loanServicesButton);

        loanServicesButton.addActionListener(e -> {
            // Create an instance of LoanService and call the processLoan method
            LoanService loanService = new LoanService();
            loanService.processLoan(this); // 'this' refers to the JFrame (parent)
        });

        JButton investmentServicesButton = new JButton("Investment Services");
        investmentServicesButton.setBounds(20, 300, 220, 40);
        investmentServicesButton.setFont(new Font("Arial", Font.BOLD, 18));
        add(investmentServicesButton);

        investmentServicesButton.addActionListener(e -> {
            // Directly call the static method without creating an instance
            InvestmentService.processInvestment(this); // Direct call to the static method
        });

        JButton paymentServicesButton = new JButton("Payments Services");
        paymentServicesButton.setBounds(20, 480, 220, 40);
        paymentServicesButton.setFont(new Font("Arial", Font.BOLD, 18));
        add(paymentServicesButton);
        paymentServicesButton.addActionListener(e -> {
            // Directly call the static method without creating an instance
            PaymentService.processPayment(this); // Direct call to the static method
        });

        JButton customerServicesButton = new JButton("Customer Services");
        customerServicesButton.setBounds(630, 480, 230, 40);
        customerServicesButton.setFont(new Font("Arial", Font.BOLD, 18));
        add(customerServicesButton);
        customerServicesButton.addActionListener(e -> {
            // Directly call the static method without creating an instance
            CustomerSupportService.submitSupportRequest(this); // Correct method call
        });

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBounds(250, 600, 400, 40);
        logoutButton.setFont(new Font("Arial", Font.BOLD, 18));
        add(logoutButton);

        logoutButton.addActionListener(e -> {
            // Close the current frame
            dispose();

            // Redirect to the Login Page
            new Login().setVisible(true);
        });

        JButton exit = new JButton("EXIT");
        exit.setBounds(350, 660, 200, 30);
        exit.setBackground(Color.BLACK);
        exit.setForeground(Color.WHITE);
        exit.addActionListener(e -> System.exit(0));
        add(exit);

        // Frame Settings
        setSize(900, 770);
        setLocation(300, 0);
       // getContentPane().setBackground(Color.YELLOW);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        new AccountServices("");
    }
}
