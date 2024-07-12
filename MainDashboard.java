package jdbc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainDashboard extends JFrame implements ActionListener {
    JButton addEmployeeButton, removeEmployeeButton, viewUpdateEmployeeButton, reportButton;

    public MainDashboard() {
        // Frame properties
        setTitle("Main Dashboard");
        setSize(400, 300);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Background Image
        setContentPane(new JLabel(new ImageIcon("path_to_dashboard_background_image.jpg")));
        setLayout(null);

        // Add Employee button
        addEmployeeButton = new JButton("Add Employee");
        addEmployeeButton.setBounds(100, 50, 200, 30);
        addEmployeeButton.addActionListener(this);
        add(addEmployeeButton);

        // Remove Employee button
        removeEmployeeButton = new JButton("Remove Employee");
        removeEmployeeButton.setBounds(100, 100, 200, 30);
        removeEmployeeButton.addActionListener(this);
        add(removeEmployeeButton);

        // View/Update Employee button
        viewUpdateEmployeeButton = new JButton("View/Update Employee");
        viewUpdateEmployeeButton.setBounds(100, 150, 200, 30);
        viewUpdateEmployeeButton.addActionListener(this);
        add(viewUpdateEmployeeButton);

        // Report button
        reportButton = new JButton("Generate Reports");
        reportButton.setBounds(100, 200, 200, 30);
        reportButton.addActionListener(this);
        add(reportButton);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addEmployeeButton) {
            new AddEmployeeFrame();
        } else if (e.getSource() == removeEmployeeButton) {
            new RemoveEmployeeFrame();
        } else if (e.getSource() == viewUpdateEmployeeButton) {
            new ViewUpdateEmployeeFrame();
        } else if (e.getSource() == reportButton) {
            new ReportFrame();
        }
    }

    public static void main(String[] args) {
        new MainDashboard();
    }
}
