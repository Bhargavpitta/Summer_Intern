package jdbc;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;

public class ViewUpdateEmployeeFrame extends JFrame implements ActionListener {
    JTextField idField, nameField, emailField, positionField, departmentField, salaryField, dateHiredField;
    JButton viewButton, updateButton, printButton;
    JTable employeeTable;
    DefaultTableModel tableModel;

    public ViewUpdateEmployeeFrame() {
        // Frame properties
        setTitle("View/Update Employee");
        setSize(800, 600);
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // ID label and text field
        JLabel idLabel = new JLabel("Employee ID:");
        idLabel.setBounds(20, 20, 100, 25);
        add(idLabel);

        idField = new JTextField();
        idField.setBounds(130, 20, 165, 25);
        add(idField);

        // Name label and text field
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(20, 60, 100, 25);
        add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(130, 60, 165, 25);
        add(nameField);

        // Email label and text field
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(20, 100, 100, 25);
        add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(130, 100, 165, 25);
        add(emailField);

        // Position label and text field
        JLabel positionLabel = new JLabel("Position:");
        positionLabel.setBounds(20, 140, 100, 25);
        add(positionLabel);

        positionField = new JTextField();
        positionField.setBounds(130, 140, 165, 25);
        add(positionField);

        // Department label and text field
        JLabel departmentLabel = new JLabel("Department:");
        departmentLabel.setBounds(20, 180, 100, 25);
        add(departmentLabel);

        departmentField = new JTextField();
        departmentField.setBounds(130, 180, 165, 25);
        add(departmentField);

        // Salary label and text field
        JLabel salaryLabel = new JLabel("Salary:");
        salaryLabel.setBounds(20, 220, 100, 25);
        add(salaryLabel);

        salaryField = new JTextField();
        salaryField.setBounds(130, 220, 165, 25);
        add(salaryField);

        // Date Hired label and text field
        JLabel dateHiredLabel = new JLabel("Date Hired:");
        dateHiredLabel.setBounds(20, 260, 100, 25);
        add(dateHiredLabel);

        dateHiredField = new JTextField();
        dateHiredField.setBounds(130, 260, 165, 25);
        add(dateHiredField);

        // View button
        viewButton = new JButton("View");
        viewButton.setBounds(310, 20, 80, 25);
        viewButton.addActionListener(this);
        add(viewButton);

        // Update button
        updateButton = new JButton("Update");
        updateButton.setBounds(310, 260, 80, 25);
        updateButton.addActionListener(this);
        add(updateButton);

        // Print button
        printButton = new JButton("Print");
        printButton.setBounds(400, 20, 80, 25);
        printButton.addActionListener(this);
        add(printButton);

        // Table to display all employees
        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Email", "Position", "Department", "Salary", "Date Hired"}, 0);
        employeeTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(employeeTable);
        scrollPane.setBounds(20, 300, 750, 250);
        add(scrollPane);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == viewButton) {
            viewEmployees();
        } else if (e.getSource() == updateButton) {
            updateEmployee();
        } else if (e.getSource() == printButton) {
            printEmployees();
        }
    }

    private void viewEmployees() {
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ems", "root", "bhargav@143");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM employees");

            tableModel.setRowCount(0); // Clear previous data
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String position = rs.getString("position");
                String department = rs.getString("department");
                double salary = rs.getDouble("salary");
                String dateHired = rs.getString("date_hired");
                tableModel.addRow(new Object[]{id, name, email, position, department, salary, dateHired});
            }
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void updateEmployee() {
        int id = Integer.parseInt(idField.getText());
        String name = nameField.getText();
        String email = emailField.getText();
        String position = positionField.getText();
        String department = departmentField.getText();
        String salary = salaryField.getText();
        String dateHired = dateHiredField.getText();

        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ems", "root", "bhargav@143");
            PreparedStatement pst = con.prepareStatement("UPDATE employees SET name=?, email=?, position=?, department=?, salary=?, date_hired=? WHERE id=?");
            pst.setString(1, name);
            pst.setString(2, email);
            pst.setString(3, position);
            pst.setString(4, department);
            pst.setString(5, salary);
            pst.setString(6, dateHired);
            pst.setInt(7, id);
            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "Employee updated successfully");
            } else {
                JOptionPane.showMessageDialog(this, "Employee with ID " + id + " not found");
            }
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void printEmployees() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Employees List");

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try {
                FileWriter fw = new FileWriter(fileToSave);
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    for (int j = 0; j < tableModel.getColumnCount(); j++) {
                        fw.write(tableModel.getValueAt(i, j) + "\t");
                    }
                    fw.write("\n");
                }
                fw.close();
                JOptionPane.showMessageDialog(this, "Employees printed to " + fileToSave.getAbsolutePath());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new ViewUpdateEmployeeFrame();
    }
}
