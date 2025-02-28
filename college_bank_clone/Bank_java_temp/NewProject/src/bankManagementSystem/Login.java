package bankManagementSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Login extends JFrame implements ActionListener {
    JButton login, clear, rg, adminLogin, close;
    JTextField card;
    JPasswordField Ipin;

    Login() {
        setTitle("Global Bank Management System");
        setLayout(null);

        ImageIcon bankIcon = new ImageIcon(getClass().getResource("logo.jpg"));
        Image img = bankIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(img));
        logoLabel.setBounds(90, 25, 100, 86);
        add(logoLabel);
        

        JLabel text = new JLabel(" Welcome To Global Bank ");
        text.setFont(new Font("Arial", Font.BOLD, 38));
        text.setBounds(200, 10, 600, 100);
        add(text);

        JLabel text1 = new JLabel(" (USER LOGIN PAGE) ");
        text1.setFont(new Font("Arial", Font.BOLD, 15));
        text1.setBounds(330, 15, 500, 180);
        add(text1);

        JLabel cardno = new JLabel("Enter a Card Number : ");
        cardno.setFont(new Font("Arial", Font.BOLD, 28));
        cardno.setBounds(120, 120, 600, 100);
        add(cardno);

        card = new JTextField();
        card.setBounds(430, 157, 300, 30);
        card.setFont(new Font("Arial", Font.BOLD, 15));
        add(card);

        JLabel pin = new JLabel("Enter a Pin Number : ");
        pin.setFont(new Font("Arial", Font.BOLD, 28));
        pin.setBounds(120, 190, 600, 100);
        add(pin);

        Ipin = new JPasswordField();
        Ipin.setBounds(430, 225, 300, 30);
        Ipin.setFont(new Font("Arial", Font.BOLD, 15));
        add(Ipin);

        login = new JButton("SIGN IN");
        login.setBounds(200, 300, 150, 30);
        login.setBackground(Color.BLACK);
        login.setForeground(Color.WHITE);
        login.addActionListener(this);
        add(login);

        clear = new JButton("CLEAR ALL!");
        clear.setBounds(420, 300, 150, 30);
        clear.setBackground(Color.BLACK);
        clear.setForeground(Color.WHITE);
        clear.addActionListener(this);
        add(clear);

        rg = new JButton("! REGISTRATION !");
        rg.setBounds(240, 360, 280, 35);
        rg.setBackground(Color.BLACK);
        rg.setForeground(Color.WHITE);
        rg.addActionListener(this);
        add(rg);

        // **Close Button (Admin Login)**
        close = new JButton("CLOSE");
        close.setBounds(280, 420, 200, 30);
        close.setBackground(Color.DARK_GRAY);
        close.setForeground(Color.WHITE);
        close.addActionListener(this);
        add(close);

        // **Only Bank Employee Login Label**
        JLabel adminLabel = new JLabel("(Only Bank Employee Login)***");
        adminLabel.setFont(new Font("Arial", Font.BOLD, 16));
        adminLabel.setForeground(Color.BLACK);
        adminLabel.setBounds(180, 460, 260, 30);
        add(adminLabel);

        // **Admin Login Button**
        adminLogin = new JButton("ADMIN LOGIN");
        adminLogin.setBounds(430, 460, 244, 30);
        adminLogin.setBackground(Color.RED);
        adminLogin.setForeground(Color.WHITE);
        adminLogin.addActionListener(this);
        add(adminLogin);

        getContentPane().setBackground(Color.ORANGE);
        setSize(800, 550);
        setVisible(true);
        setLocation(350, 150);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == clear) {
            card.setText("");
            Ipin.setText("");
        } else if (ae.getSource() == login) {
            // User Login Logic
            Conn c = new Conn();
            String cardno = card.getText();
            String pinnum = Ipin.getText();

            if (cardno.isEmpty() || pinnum.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter both card number and pin.");
                return;
            }

            String query = "SELECT * FROM login WHERE cardno = ? AND pinnum = ?";
            try (PreparedStatement ps = c.c.prepareStatement(query)) {
                ps.setString(1, cardno);
                ps.setString(2, pinnum);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    setVisible(false);
                    new AccountServices(pinnum).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid Card Number or Pin");
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        } else if (ae.getSource() == rg) {
            setVisible(false);
            new rg1().setVisible(true);
        } else if (ae.getSource() == adminLogin) {
            // Admin Login Logic
            String adminPass = JOptionPane.showInputDialog(this, "Enter 4-digit Admin Password:");

            if (adminPass != null && adminPass.equals("1234")) { // Set your admin password
                setVisible(false);
                new AdminDashboard().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Admin Password!");
            }
        } else if (ae.getSource() == close) {
            System.exit(0); // Application Close Logic
        }
    }

    public static void main(String[] args) {
        new Login();
    }
}
