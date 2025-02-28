package bankManagementSystem;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class rg2 extends JFrame implements ActionListener {
   // int random = (int) (Math.random() * 10000); // Generate random form number
   
    JTextField panno, adno;
    JButton next;
    JRadioButton syes, sno, eyes, eno;
    JComboBox<String> religion, category, income, education, occupation;
    String formno;

    rg2(String formno) {
        setTitle("Global Bank Management System");
        this.formno = formno;
       // setTitle("New Account Form - Page 2");
        setLayout(null);

        JLabel addDetail = new JLabel("Page 2 : Additional Details");
        addDetail.setFont(new Font("Arial", Font.BOLD, 22));
        addDetail.setBounds(290, 80, 400, 30);
        add(addDetail);

        JLabel rel = new JLabel("Religion : ");
        rel.setFont(new Font("Arial", Font.BOLD, 20));
        rel.setBounds(100, 140, 200, 30);
        add(rel);

        String valReligion[] = {"Hindu", "Muslim", "Sikh", "Christian", "Other"};
        religion = new JComboBox<>(valReligion);
        religion.setBounds(300, 140, 400, 30);
        add(religion);

        JLabel cat = new JLabel("Category : ");
        cat.setFont(new Font("Arial", Font.BOLD, 20));
        cat.setBounds(100, 190, 200, 30);
        add(cat);

        String valCategory[] = {"General", "OBC", "SC", "ST", "Other"};
        category = new JComboBox<>(valCategory);
        category.setBounds(300, 190, 400, 30);
        add(category);

        JLabel in1 = new JLabel("Income : ");
        in1.setFont(new Font("Arial", Font.BOLD, 20));
        in1.setBounds(100, 240, 200, 30);
        add(in1);

        String inCategory[] = {"Null", "< 1,50,000", "< 2,50,000", "< 5,00,000", "Up to 10,00,000"};
        income = new JComboBox<>(inCategory);
        income.setBounds(300, 240, 400, 30);
        add(income);

        JLabel edu = new JLabel("Educational  Qualification : ");
        edu.setFont(new Font("Arial", Font.BOLD, 20));
        edu.setBounds(100, 290, 300, 30);
        add(edu);

        String valEducation[] = {"Non-Graduate", "Graduate", "Post-Graduate", "Doctorate", "Other"};
        education = new JComboBox<>(valEducation);
        education.setBounds(300, 330, 400, 30);
        add(education);

        JLabel occu = new JLabel("Occupation : ");
        occu.setFont(new Font("Arial", Font.BOLD, 20));
        occu.setBounds(100, 380, 200, 30);
        add(occu);

        String valOccupation[] = {"Salaried", "Self-Employed", "Business", "Student", "Retired", "Other"};
        occupation = new JComboBox<>(valOccupation);
        occupation.setBounds(300, 380, 400, 30);
        add(occupation);

        JLabel pan = new JLabel("PAN Number : ");
        pan.setFont(new Font("Arial", Font.BOLD, 20));
        pan.setBounds(100, 440, 200, 30);
        add(pan);

        panno = new JTextField();
        panno.setFont(new Font("Arial", Font.BOLD, 15));
        panno.setBounds(300, 440, 400, 30);
        add(panno);

        JLabel ad = new JLabel("Aadhar Number : ");
        ad.setFont(new Font("Arial", Font.BOLD, 20));
        ad.setBounds(100, 490, 200, 30);
        add(ad);

        adno = new JTextField();
        adno.setFont(new Font("Arial", Font.BOLD, 15));
        adno.setBounds(300, 490, 400, 30);
        add(adno);

        JLabel scLabel = new JLabel("Senior Citizen : ");
        scLabel.setFont(new Font("Arial", Font.BOLD, 20));
        scLabel.setBounds(100, 540, 200, 30);
        add(scLabel);

        syes = new JRadioButton("YES");
        syes.setBounds(300, 540, 100, 30);
        syes.setBackground(new Color(255, 223, 186));
        add(syes);

        sno = new JRadioButton("NO");
        sno.setBounds(400, 540, 100, 30);
        sno.setBackground(new Color(255, 223, 186));
        add(sno);

        ButtonGroup scGroup = new ButtonGroup();
        scGroup.add(syes);
        scGroup.add(sno);

        JLabel ea = new JLabel("Existing Account : ");
        ea.setFont(new Font("Arial", Font.BOLD, 20));
        ea.setBounds(100, 590, 200, 30);
        add(ea);

        eyes = new JRadioButton("YES");
        eyes.setBounds(300, 590, 100, 30);
        eyes.setBackground(new Color(255, 223, 186));
        add(eyes);

        eno = new JRadioButton("NO");
        eno.setBounds(400, 590, 100, 30);
        eno.setBackground(new Color(255, 223, 186));
        add(eno);

        ButtonGroup eaGroup = new ButtonGroup();
        eaGroup.add(eyes);
        eaGroup.add(eno);

        next = new JButton("Next");
        next.setBackground(Color.BLACK);
        next.setForeground(Color.WHITE);
        next.setFont(new Font("Raleway", Font.BOLD, 15));
        next.setBounds(600, 650, 100, 30);
        next.addActionListener(this);
        add(next);

        getContentPane().setBackground(new Color(255, 223, 186));

        setSize(850, 750);
        setLocation(330, 10);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent ae) {
       // String formno = String.valueOf(random);
        String rel = (String) religion.getSelectedItem();
        String cat = (String) category.getSelectedItem();
        String inc = (String) income.getSelectedItem();
        String edu = (String) education.getSelectedItem();
        String occ = (String) occupation.getSelectedItem();
        String pan = panno.getText();
        String ad = adno.getText();
        String seniorCitizen = syes.isSelected() ? "YES" : "NO";
        String existingAccount = eyes.isSelected() ? "YES" : "NO";

        try {
            if (pan.equals("")) {
                JOptionPane.showMessageDialog(null, "PAN Number is Required");
            } else if (ad.equals("")) {
                JOptionPane.showMessageDialog(null, "Aadhar Number is Required");
            } else {
                Conn c = new Conn();
                String query = "INSERT INTO rg2 (formno, religion, category, income, education, occupation, pan, adhar, seniorCitizen, existingAccount) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement pstmt = c.c.prepareStatement(query);
                pstmt.setString(1, formno);
                pstmt.setString(2, rel);
                pstmt.setString(3, cat);
                pstmt.setString(4, inc);
                pstmt.setString(5, edu);
                pstmt.setString(6, occ);
                pstmt.setString(7, pan);
                pstmt.setString(8, ad);
                pstmt.setString(9, seniorCitizen);
                pstmt.setString(10, existingAccount);

                pstmt.executeUpdate();
                pstmt.close();

                JOptionPane.showMessageDialog(null, "Details Submitted Successfully!");

                setVisible(false);
                new rg3(formno).setVisible(true);
                
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    //  public static void main(String[] args) {
    //     new rg2("");
    // }
}
