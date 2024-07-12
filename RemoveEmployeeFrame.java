package jdbc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class RemoveEmployeeFrame extends JFrame implements ActionListener {
    JTextField idField;
    JButton removeButton;

    public RemoveEmployeeFrame() {
        // Frame properties
        setTitle("Remove Employee");
        setSize(400, 200);
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Background Image
        setContentPane(new JLabel(new ImageIcon("path_to_remove_employee_background_image.jpg")));
        setLayout(null);

        // ID label and text field
        JLabel idLabel = new JLabel("Employee ID:");
        idLabel.setBounds(50, 50, 80, 25);
        idLabel.setForeground(Color.BLACK); // Text color
        add(idLabel);

        idField = new JTextField();
        idField.setBounds(150, 50, 165, 25);
        add(idField);

        // Remove button
        removeButton = new JButton("Remove");
        removeButton.setBounds(150, 100, 100, 25);
        removeButton.addActionListener(this);
        add(removeButton);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == removeButton) {
            removeEmployee();
        }
    }

    private void removeEmployee() {
        int id = Integer.parseInt(idField.getText());

        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ems", "root", "bhargav@143");
            PreparedStatement pst = con.prepareStatement("DELETE FROM employees WHERE id=?");
            pst.setInt(1, id);
            int rowsDeleted = pst.executeUpdate();
            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(this, "Employee removed successfully");
            } else {
                JOptionPane.showMessageDialog(this, "Employee with ID " + id + " not found");
            }
            con.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new RemoveEmployeeFrame();
    }
}
