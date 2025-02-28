package bankManagementSystem;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import java.sql.*;
import com.toedter.calendar.JDateChooser;

public class rg1 extends JFrame implements ActionListener {
    int random;
    JTextField nameText, fnameText, eText, add1Text, cityText, stText, pcText;
    JButton next;
    JRadioButton male, female, other, married, unmarried, other1;
    JDateChooser dateChooser;

    rg1() {
        setTitle("Global Bank Management System");
        setLayout(null);

        // Random ran = new Random();
        // random = Math.abs(ran.nextInt(9999));
        Random ran = new Random();
        random = 1000 + ran.nextInt(9000);

        JLabel formno = new JLabel("REGISTRATION FORM No. " + random);
        formno.setFont(new Font("Arial", Font.BOLD, 38));
        formno.setBounds(140, 20, 600, 40);
        add(formno);

        JLabel perDetail = new JLabel("Page 1 : Personal Details");
        perDetail.setFont(new Font("Arial", Font.BOLD, 22));
        perDetail.setBounds(290, 80, 400, 30);
        add(perDetail);

        JLabel name = new JLabel("Name : ");
        name.setFont(new Font("Arial", Font.BOLD, 20));
        name.setBounds(100, 140, 100, 30);
        add(name);

        nameText = new JTextField();
        nameText.setFont(new Font("Arial", Font.BOLD, 15));
        nameText.setBounds(300, 140, 400, 30);
        add(nameText);

        // Validation using Regex when focus is lost
        nameText.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                String nameInput = nameText.getText().trim();
                if (!nameInput.matches("^[a-zA-Z ]{3,50}$")) {
                    JOptionPane.showMessageDialog(null,
                            "Invalid Name! Only letters and spaces allowed (3-50 characters).", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    nameText.setText(""); // Clear field if invalid
                }
            }
        });

        JLabel fname = new JLabel("Father's Name : ");
        fname.setFont(new Font("Arial", Font.BOLD, 20));
        fname.setBounds(100, 190, 200, 30);
        add(fname);

        fnameText = new JTextField();
        fnameText.setFont(new Font("Arial", Font.BOLD, 15));
        fnameText.setBounds(300, 190, 400, 30);
        add(fnameText);
        

        JLabel dob = new JLabel("Date of Birth : ");
        dob.setFont(new Font("Arial", Font.BOLD, 20));
        dob.setBounds(100, 240, 200, 30);
        add(dob);

        dateChooser = new JDateChooser();
        dateChooser.setBounds(300, 240, 400, 30);
        add(dateChooser);

        JLabel gen = new JLabel("Gender : ");
        gen.setFont(new Font("Arial", Font.BOLD, 20));
        gen.setBounds(100, 290, 200, 30);
        add(gen);

        male = new JRadioButton("Male");
        male.setBounds(300, 290, 60, 30);
        male.setBackground(new Color(152, 251, 152));
        add(male);

        female = new JRadioButton("Female");
        female.setBounds(400, 290, 120, 30);
        female.setBackground(new Color(152, 251, 152));
        add(female);

        other = new JRadioButton("Other");
        other.setBounds(520, 290, 120, 30);
        other.setBackground(new Color(152, 251, 152));
        add(other);

        ButtonGroup bg = new ButtonGroup();
        bg.add(male);
        bg.add(female);
        bg.add(other);

        JLabel email = new JLabel("E-mail Address : ");
        email.setFont(new Font("Arial", Font.BOLD, 20));
        email.setBounds(100, 340, 200, 30);
        add(email);

        eText = new JTextField();
        eText.setFont(new Font("Arial", Font.BOLD, 15));
        eText.setBounds(300, 340, 400, 30);
        add(eText);

        JLabel marital = new JLabel("Marital Status : ");
        marital.setFont(new Font("Arial", Font.BOLD, 20));
        marital.setBounds(100, 390, 200, 30);
        add(marital);

        married = new JRadioButton("Married");
        married.setBounds(300, 390, 100, 30);
        married.setBackground(new Color(152, 251, 152));
        add(married);

        unmarried = new JRadioButton("Unmarried");
        unmarried.setBounds(400, 390, 120, 30);
        unmarried.setBackground(new Color(152, 251, 152));
        add(unmarried);

        other1 = new JRadioButton("Other");
        other1.setBounds(520, 390, 100, 30);
        other1.setBackground(new Color(152, 251, 152));
        add(other1);

        ButtonGroup mg = new ButtonGroup();
        mg.add(married);
        mg.add(unmarried);
        mg.add(other1);

        JLabel add1 = new JLabel("Address : ");
        add1.setFont(new Font("Arial", Font.BOLD, 20));
        add1.setBounds(100, 440, 200, 30);
        add(add1);

        add1Text = new JTextField();
        add1Text.setFont(new Font("Arial", Font.BOLD, 15));
        add1Text.setBounds(300, 440, 400, 30);
        add(add1Text);

        JLabel city = new JLabel("City : ");
        city.setFont(new Font("Arial", Font.BOLD, 20));
        city.setBounds(100, 490, 200, 30);
        add(city);

        cityText = new JTextField();
        cityText.setFont(new Font("Arial", Font.BOLD, 15));
        cityText.setBounds(300, 490, 400, 30);
        add(cityText);

        JLabel st = new JLabel("State : ");
        st.setFont(new Font("Arial", Font.BOLD, 20));
        st.setBounds(100, 540, 200, 30);
        add(st);

        stText = new JTextField();
        stText.setFont(new Font("Arial", Font.BOLD, 15));
        stText.setBounds(300, 540, 400, 30);
        add(stText);

        JLabel pc = new JLabel("Pin Code : ");
        pc.setFont(new Font("Arial", Font.BOLD, 20));
        pc.setBounds(100, 590, 200, 30);
        add(pc);

        pcText = new JTextField();
        pcText.setFont(new Font("Arial", Font.BOLD, 15));
        pcText.setBounds(300, 590, 400, 30);
        add(pcText);

        next = new JButton("Next");
        next.setBackground(Color.BLACK);
        next.setForeground(Color.WHITE);
        next.setFont(new Font("Raleway", Font.BOLD, 15));
        next.setBounds(600, 650, 100, 30);
        next.addActionListener(this);
        add(next);

        getContentPane().setBackground(new Color(152, 251, 152)); // Light Blue
        setSize(850, 750);
        setLocation(330, 10);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
        String formno = "" + random;
        String name = nameText.getText();
        String fname = fnameText.getText();
        String dob = ((JTextField) dateChooser.getDateEditor().getUiComponent()).getText();
        String gender = null;
        if (male.isSelected()) {
            gender = "Male";
        } else if (female.isSelected()) {
            gender = "Female";
        } else if (other.isSelected()) {
            gender = "Other";
        }

        String email = eText.getText();

        String marital = null;
        if (married.isSelected()) {
            marital = "Married";
        } else if (unmarried.isSelected()) {
            marital = "Unmarried";
        } else if (other1.isSelected()) {
            marital = "Other";
        }

        String add1 = add1Text.getText();
        String city = cityText.getText();
        String state = stText.getText();
        String pincode = pcText.getText();

        try {
            if (name.equals("")) {
                JOptionPane.showMessageDialog(null, "Name is Required***");
            } else if (fname.equals("")) {
                JOptionPane.showMessageDialog(null, "Father's Name is Required***");
            } else if (dob.equals("")) {
                JOptionPane.showMessageDialog(null, "Date of Birth is Required***");
            } else if (pincode.equals("")) {
                JOptionPane.showMessageDialog(null, "Pincode is Required***");
            } else {
                Conn c = new Conn();
                String query = "INSERT INTO rg1 (formno, name, fname, dob, gender, email, marital, add1, city, state, pincode) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement pstmt = c.c.prepareStatement(query);
                pstmt.setString(1, formno);
                pstmt.setString(2, name);
                pstmt.setString(3, fname);
                pstmt.setString(4, dob);
                pstmt.setString(5, gender);
                pstmt.setString(6, email);
                pstmt.setString(7, marital);
                pstmt.setString(8, add1);
                pstmt.setString(9, city);
                pstmt.setString(10, state);
                pstmt.setString(11, pincode);

                pstmt.executeUpdate();
                pstmt.close();

                JOptionPane.showMessageDialog(null, "Details Submitted Successfully!");
                setVisible(false);
                new rg2(formno).setVisible(true);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    // public static void main(String[] args) {
    // new rg1();
    // }

}
