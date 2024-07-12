package jdbc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.regex.Pattern;

public class AddEmployeeFrame extends JFrame implements ActionListener {
    JTextField idField, nameField, emailField, positionField, departmentField, salaryField, dateHiredField;
    JButton addButton;

    public AddEmployeeFrame() {
        // Frame properties
        setTitle("Add Employee");
        setSize(400, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(null);

        // Employee ID label and text field
        JLabel idLabel = new JLabel("Employee ID:");
        idLabel.setBounds(50, 50, 100, 25);
        idLabel.setForeground(Color.BLACK); // Text color
        add(idLabel);

        idField = new JTextField();
        idField.setBounds(150, 50, 165, 25);
        add(idField);

        // Name label and text field
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(50, 100, 80, 25);
        nameLabel.setForeground(Color.BLACK); // Text color
        add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(150, 100, 165, 25);
        add(nameField);

        // Email label and text field
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(50, 150, 80, 25);
        emailLabel.setForeground(Color.BLACK);
        add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(150, 150, 165, 25);
        add(emailField);

        // Position label and text field
        JLabel positionLabel = new JLabel("Position:");
        positionLabel.setBounds(50, 200, 80, 25);
        positionLabel.setForeground(Color.BLACK);
        add(positionLabel);

        positionField = new JTextField();
        positionField.setBounds(150, 200, 165, 25);
        add(positionField);

        // Department label and text field
        JLabel departmentLabel = new JLabel("Department:");
        departmentLabel.setBounds(50, 250, 80, 25);
        departmentLabel.setForeground(Color.BLACK);
        add(departmentLabel);

        departmentField = new JTextField();
        departmentField.setBounds(150, 250, 165, 25);
        add(departmentField);

        // Salary label and text field
        JLabel salaryLabel = new JLabel("Salary:");
        salaryLabel.setBounds(50, 300, 80, 25);
        salaryLabel.setForeground(Color.BLACK);
        add(salaryLabel);

        salaryField = new JTextField();
        salaryField.setBounds(150, 300, 165, 25);
        add(salaryField);

        // Date Hired label and text field
        JLabel dateHiredLabel = new JLabel("Date Hired:");
        dateHiredLabel.setBounds(50, 350, 80, 25);
        dateHiredLabel.setForeground(Color.BLACK);
        add(dateHiredLabel);

        dateHiredField = new JTextField();
        dateHiredField.setBounds(150, 350, 165, 25);
        add(dateHiredField);

        // Add button
        addButton = new JButton("Add");
        addButton.setBounds(150, 400, 100, 25);
        addButton.addActionListener(this);
        add(addButton);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            addEmployee();
        }
    }

    private void addEmployee() {
        String idText = idField.getText();
        String name = nameField.getText();
        String email = emailField.getText();
        String position = positionField.getText();
        String department = departmentField.getText();
        String salaryText = salaryField.getText();
        String dateHired = dateHiredField.getText();

        // Validate input fields
        if (idText.isEmpty() || name.isEmpty() || email.isEmpty() || position.isEmpty() || department.isEmpty() || salaryText.isEmpty() || dateHired.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid Employee ID format", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, "Invalid email format", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double salary;
        try {
            salary = Double.parseDouble(salaryText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid salary format", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ems", "root", "bhargav@143");
            PreparedStatement pst = con.prepareStatement("INSERT INTO employees (id, name, email, position, department, salary, date_hired) VALUES (?, ?, ?, ?, ?, ?, ?)");
            pst.setInt(1, id);
            pst.setString(2, name);
            pst.setString(3, email);
            pst.setString(4, position);
            pst.setString(5, department);
            pst.setDouble(6, salary);
            pst.setString(7, dateHired);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Employee added successfully");
            con.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding employee", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isValidEmail(String email) {
        // Simple email validation
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return Pattern.matches(emailRegex, email);
    }

    public static void main(String[] args) {
        new AddEmployeeFrame();
    }
}
