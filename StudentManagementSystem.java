package jdbc;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

// Student Class
class Student {
    private String name;
    private String id;
    private String major;

    public Student(String name, String id, String major) {
        this.name = name;
        this.id = id;
        this.major = major;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }
}

// Student Table Model Class
class StudentTableModel extends AbstractTableModel {
    private ArrayList<Student> students;
    private final String[] columnNames = {"Name", "ID", "Major"};

    public StudentTableModel() {
        this.students = new ArrayList<>();
    }

    public void setStudents(ArrayList<Student> students) {
        this.students = students;
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return students.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Student student = students.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return student.getName();
            case 1:
                return student.getId();
            case 2:
                return student.getMajor();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
}

// Student Management System Class
public class StudentManagementSystem extends JFrame {
    private JTextField nameField;
    private JTextField idField;
    private JTextField majorField;
    private ArrayList<Student> students;
    private JTable studentTable;
    private StudentTableModel tableModel;

    public StudentManagementSystem() {
        students = new ArrayList<>();
        setTitle("Student Management System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Input Panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(4, 2));

        inputPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("ID:"));
        idField = new JTextField();
        inputPanel.add(idField);

        inputPanel.add(new JLabel("Major:"));
        majorField = new JTextField();
        inputPanel.add(majorField);

        JButton addButton = new JButton("Add Student");
        inputPanel.add(addButton);

        add(inputPanel, BorderLayout.NORTH);

        // Table Panel
        tableModel = new StudentTableModel();
        studentTable = new JTable(tableModel);
        add(new JScrollPane(studentTable), BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton viewButton = new JButton("View Students");
        buttonPanel.add(viewButton);

        JButton deleteButton = new JButton("Delete Student");
        buttonPanel.add(deleteButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Event Handling
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String id = idField.getText();
                String major = majorField.getText();
                students.add(new Student(name, id, major));
                tableModel.fireTableDataChanged();
                clearFields();
            }
        });

        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tableModel.setStudents(students);
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = JOptionPane.showInputDialog("Enter ID of student to delete:");
                students.removeIf(student -> student.getId().equals(id));
                tableModel.fireTableDataChanged();
            }
        });
    }

    private void clearFields() {
        nameField.setText("");
        idField.setText("");
        majorField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StudentManagementSystem sms = new StudentManagementSystem();
            sms.setVisible(true);
        });
    }
}
