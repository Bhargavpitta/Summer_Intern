package jdbc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*; 

public class LoginFrame extends JFrame implements ActionListener {
    JTextField usernameField;
    JPasswordField passwordField;
    JButton loginButton;

    public LoginFrame() {
        // Frame properties
        setTitle("Login");
        setSize(400, 300);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Background Image
        setContentPane(new JLabel(new ImageIcon("")));
        setLayout(null);

        // Username label and text field
        JLabel usernameLabel = new JLabel("UsernameBHA:");
        usernameLabel.setBounds(50, 50, 80, 25);
        usernameLabel.setForeground(Color.BLACK); // Text color
        add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(150, 50, 165, 25);
        add(usernameField);

        // Password label and text field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 100, 80, 25);
        passwordLabel.setForeground(Color.BLACK);
        add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(150, 100, 165, 25);
        add(passwordField);

        // Login button
        loginButton = new JButton("Login");
        loginButton.setBounds(150, 150, 100, 25);
        loginButton.addActionListener(this);
        add(loginButton);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            login();
        }
    }

    private void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ems", "root", "bhargav@143");
            PreparedStatement pst = con.prepareStatement("SELECT * FROM users WHERE username=? AND password=?");
            pst.setString(1, username);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Login successful");
                new MainDashboard(); // Open the main dashboard
                dispose(); // Close the login frame
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password");
            }
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new LoginFrame();
    }
} 
