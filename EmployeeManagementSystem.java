package jdbc;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

class Employee {
    private int id;
    private String name;
    private String department;
    private String designation;
    private double salary;
    private LocalDate hireDate;
    private String employmentStatus;

    public Employee(int id, String name, String department, String designation, double salary, LocalDate hireDate, String employmentStatus) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.designation = designation;
        this.salary = salary;
        this.hireDate = hireDate;
        this.employmentStatus = employmentStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public String getDesignation() {
        return designation;
    }

    public double getSalary() {
        return salary;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public String getEmploymentStatus() {
        return employmentStatus;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public void setEmploymentStatus(String employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", department='" + department + '\'' +
                ", designation='" + designation + '\'' +
                ", salary=" + salary +
                ", hireDate=" + hireDate +
                ", employmentStatus='" + employmentStatus + '\'' +
                '}';
    }
}

class Admin {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/ems2";
    private static final String USER = "root";
    private static final String PASS = "bhargav@143";
    private Connection conn;
    private List<Employee> employees;
    private Lock lock;

    public Admin() {
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("DataBase Connected Successfully");
            System.out.println();
            System.out.println();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        employees = Collections.synchronizedList(new ArrayList<>());
        lock = new ReentrantLock();
    }

    public void addEmployee(Employee employee) {
        lock.lock();
        try {
            employees.add(employee);
            String query = "INSERT INTO employees (id, name, department, designation, salary, hireDate, employmentStatus) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, employee.getId());
                pstmt.setString(2, employee.getName());
                pstmt.setString(3, employee.getDepartment());
                pstmt.setString(4, employee.getDesignation());
                pstmt.setDouble(5, employee.getSalary());
                pstmt.setDate(6, Date.valueOf(employee.getHireDate()));
                pstmt.setString(7, employee.getEmploymentStatus());
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } finally {
            lock.unlock();
        }
    }

    public Employee searchEmployee(int id) {
        lock.lock();
        try {
            return employees.stream().filter(emp -> emp.getId() == id).findFirst().orElse(null);
        } finally {
            lock.unlock();
        }
    }

    public void removeEmployee(int id) {
        lock.lock();
        try {
            employees.removeIf(emp -> emp.getId() == id);
            String query = "DELETE FROM employees WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, id);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } finally {
            lock.unlock();
        }
    }

    public void generateReport() {
        lock.lock();
        try {
            employees.forEach(System.out::println);
        } finally {
            lock.unlock();
        }
    }

    public List<Employee> getEmployeesByDepartment(String department) {
        lock.lock();
        try {
            return employees.stream().filter(emp -> emp.getDepartment().equalsIgnoreCase(department)).collect(Collectors.toList());
        } finally {
            lock.unlock();
        }
    }

    public List<Employee> getAllEmployees() {
        lock.lock();
        try {
            return new ArrayList<>(employees);
        } finally {
            lock.unlock();
        }
    }

    public void loadEmployeesFromDatabase() {
        String query = "SELECT * FROM employees";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Employee emp = new Employee(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("department"),
                        rs.getString("designation"),
                        rs.getDouble("salary"),
                        rs.getDate("hireDate").toLocalDate(),
                        rs.getString("employmentStatus")
                );
                employees.add(emp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

public class EmployeeManagementSystem {
    private static Admin admin = new Admin();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        admin.loadEmployeesFromDatabase();
        while (true) {
            showMenu();
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            switch (choice) {
                case 1:
                    addEmployee();
                    break;
                case 2:
                    viewEmployee();
                    break;
                case 3:
                    removeEmployee();
                    break;
                case 4:
                    generateReport();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void showMenu() {
        System.out.println("1. Add Employee");
        System.out.println("2. View Employee");
        System.out.println("3. Remove Employee");
        System.out.println("4. Generate Report");
        System.out.println("5. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void addEmployee() {
        try {
            System.out.print("Enter ID: ");
            int id = scanner.nextInt();
            scanner.nextLine(); // consume newline
            System.out.print("Enter Name: ");
            String name = scanner.nextLine();
            System.out.print("Enter Department: ");
            String department = scanner.nextLine();
            System.out.print("Enter Designation: ");
            String designation = scanner.nextLine();
            System.out.print("Enter Salary: ");
            double salary = scanner.nextDouble();
            System.out.print("Enter Hire Date (YYYY-MM-DD): ");
            LocalDate hireDate = LocalDate.parse(scanner.next());
            System.out.print("Enter Employment Status: ");
            String employmentStatus = scanner.next();

            Employee employee = new Employee(id, name, department, designation, salary, hireDate, employmentStatus);
            admin.addEmployee(employee);
            System.out.println("Employee added successfully!");
        } catch (Exception e) {
            System.out.println("Error adding employee. Please try again.");
            e.printStackTrace();
            scanner.nextLine(); // consume remaining input
        }
    }

    private static void viewEmployee() {
        System.out.print("Enter Employee ID to View: ");
        int id = scanner.nextInt();
        Employee employee = admin.searchEmployee(id);
        System.out.println(employee != null ? employee : "Employee not found.");
    }

    private static void removeEmployee() {
        System.out.print("Enter Employee ID to Remove: ");
        int id = scanner.nextInt();
        admin.removeEmployee(id);
        System.out.println("Employee removed successfully!");
    }

    private static void generateReport() {
        admin.generateReport();
    }
}
