package bankManagementSystem;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;

public class rg3 extends JFrame implements ActionListener {
    JRadioButton r1, r2, r3, r4;
    JCheckBox c1, c2, c3, c4, c5, c6, c7;
    JButton submit, cancel;
    String formno;

     rg3(String formno) {
        setTitle("Global Bank Management System");
        this.formno = formno;

        setLayout(null);

        JLabel l1 = new JLabel("Page 3 : Account Details");
        l1.setFont(new Font("Arial", Font.BOLD, 22));
        l1.setBounds(280, 40, 400, 40);
        add(l1);

        JLabel type = new JLabel("Account Type :");
        type.setFont(new Font("Arial", Font.BOLD, 20));
        type.setBounds(100, 140, 200, 30);
        add(type);

        r1 = new JRadioButton("Saving Account");
        r1.setFont(new Font("Arial", Font.BOLD, 16));
        r1.setBounds(100, 180, 150, 20);
        add(r1);

        r2 = new JRadioButton("Fixed Deposit Account");
        r2.setFont(new Font("Arial", Font.BOLD, 16));
        r2.setBounds(350, 180, 250, 20);
        add(r2);

        r3 = new JRadioButton("Current Account");
        r3.setFont(new Font("Arial", Font.BOLD, 16));
        r3.setBounds(100, 220, 250, 20);
        add(r3);

        r4 = new JRadioButton("Recurring Deposit Account");
        r4.setFont(new Font("Arial", Font.BOLD, 16));
        r4.setBounds(350, 220, 250, 20);
        add(r4);

        ButtonGroup bg = new ButtonGroup();
        bg.add(r1);
        bg.add(r2);
        bg.add(r3);
        bg.add(r4);

        JLabel card = new JLabel("Card Number :");
        card.setFont(new Font("Arial", Font.BOLD, 20));
        card.setBounds(100, 300, 200, 30);
        add(card);

        JLabel no = new JLabel("XXXX-XXXX-XXXX-4181");
        no.setFont(new Font("Arial", Font.BOLD, 20));
        no.setBounds(330, 300, 300, 30);
        add(no);

        JLabel cd = new JLabel("(Your 16 Digit Card Number)");
        cd.setFont(new Font("Arial", Font.BOLD, 12));
        cd.setBounds(100, 330, 300, 20);
        add(cd);

        JLabel pin = new JLabel("PIN Number :");
        pin.setFont(new Font("Arial", Font.BOLD, 20));
        pin.setBounds(100, 370, 200, 30);
        add(pin);

        JLabel pno = new JLabel("XXXX");
        pno.setFont(new Font("Arial", Font.BOLD, 20));
        pno.setBounds(330, 370, 300, 30);
        add(pno);

        JLabel pd = new JLabel("(Your 4 Digit PIN Number)");
        pd.setFont(new Font("Arial", Font.BOLD, 12));
        pd.setBounds(100, 400, 300, 20);
        add(pd);

        JLabel service = new JLabel("Services Required :");
        service.setFont(new Font("Arial", Font.BOLD, 20));
        service.setBounds(100, 450, 200, 30);
        add(service);

        c1 = new JCheckBox("ATM Card");
        c1.setFont(new Font("Arial", Font.BOLD, 16));
        c1.setBounds(100, 500, 200, 30);
        add(c1);

        c2 = new JCheckBox("Internet Banking");
        c2.setFont(new Font("Arial", Font.BOLD, 16));
        c2.setBounds(350, 500, 200, 30);
        add(c2);

        c3 = new JCheckBox("Mobile Banking");
        c3.setFont(new Font("Arial", Font.BOLD, 16));
        c3.setBounds(100, 550, 200, 30);
        add(c3);

        c4 = new JCheckBox("Email & SMS Alerts");
        c4.setFont(new Font("Arial", Font.BOLD, 16));
        c4.setBounds(350, 550, 200, 30);
        add(c4);

        c5 = new JCheckBox("Cheque Book");
        c5.setFont(new Font("Arial", Font.BOLD, 16));
        c5.setBounds(100, 600, 200, 30);
        add(c5);

        c6 = new JCheckBox("E-Statement");
        c6.setFont(new Font("Arial", Font.BOLD, 16));
        c6.setBounds(350, 600, 200, 30);
        add(c6);

        c7 = new JCheckBox("I hereby declare that the above entered details are correct to the best of my knowledge.", true);
        c7.setFont(new Font("Arial", Font.BOLD, 12));
        c7.setBounds(100, 650, 600, 30);
        add(c7);

        submit = new JButton("Submit");
        submit.setBackground(Color.BLACK);
        submit.setForeground(Color.WHITE);
        submit.setFont(new Font("Arial", Font.BOLD, 16));
        submit.setBounds(250, 690, 100, 30);
        submit.addActionListener(this);
        add(submit);

        cancel = new JButton("Cancel");
        cancel.setBackground(Color.BLACK);
        cancel.setForeground(Color.WHITE);
        cancel.setFont(new Font("Arial", Font.BOLD, 16));
        cancel.setBounds(400, 690, 100, 30);
        cancel.addActionListener(this);
        add(cancel);

        setSize(850, 770);
        setLocation(350, 0);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == submit) {
            String type = null;
            if (r1.isSelected()) {
                type = "Saving Account";
            } else if (r2.isSelected()) {
                type = "Fixed Deposit Account";
            } else if (r3.isSelected()) {
                type = "Current Account";
            } else if (r4.isSelected()) {
                type = "Recurring Deposit Account";
            }

            Random ran = new Random();
            String cardno = "" + Math.abs((ran.nextLong() % 90000000L) + 5040936000000000L);
            String pinnum = "" + Math.abs(ran.nextInt() % 9000 + 1000);

            String facility = "";
            if (c1.isSelected()) {
                facility += " ATM Card";
            }
            if (c2.isSelected()) {
                facility += " Internet Banking";
            }
            if (c3.isSelected()) {
                facility += " Mobile Banking";
            }
            if (c4.isSelected()) {
                facility += " Email Alerts";
            }
            if (c5.isSelected()) {
                facility += " Cheque Book";
            }
            if (c6.isSelected()) {
                facility += " E-Statement";
            }

            try {
                if (type == null) {
                    JOptionPane.showMessageDialog(null, "Account Type is Required!");
                } else if (facility.equals("")) {
                    JOptionPane.showMessageDialog(null, "Facility is Required!");
                } else if (!c7.isSelected()) {
                    JOptionPane.showMessageDialog(null, "Please Agree to our Terms & Conditions***");
                } else {
                    Conn c = new Conn();
                    String q1 = "INSERT INTO rg3 (formno, type, cardno, pinnum, facility ) VALUES (?, ?, ?, ?, ?)";
                    String q2 = "INSERT INTO login (formno, cardno, pinnum) VALUES (?, ?, ?)";

                    PreparedStatement ps1 = c.c.prepareStatement(q1);
                    ps1.setString(1, formno);
                    ps1.setString(2, type);
                    ps1.setString(3, cardno);
                    ps1.setString(4, pinnum);
                    ps1.setString(5, facility);
                    ps1.executeUpdate();

                    PreparedStatement ps2 = c.c.prepareStatement(q2);
                    ps2.setString(1, formno);
                    ps2.setString(2, cardno);
                    ps2.setString(3, pinnum);
                    ps2.executeUpdate();

                    JOptionPane.showMessageDialog(null, "Card Number: " + cardno + "\nPin: " + pinnum);
                    new Deposit().setVisible(true);
                    setVisible(false);
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        } else if (ae.getSource() == cancel) {
            System.exit(0);
        }
    }

//     public static void main(String[] args) {
//        new rg3("");
//    }
}
