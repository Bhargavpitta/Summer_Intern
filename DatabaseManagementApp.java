package jdbc;


import java.sql.*;
import java.util.Scanner;

// Interface for CRUD operations
interface UserDAO {
    void addUser(int id, String name, String email) throws SQLException;
    void getAllUsers() throws SQLException;
    void updateUser(int id, String name, String email) throws SQLException;
    void deleteUser(int id) throws SQLException;
}

// MySQL implementation of UserDAO
class MySQLUserDAO implements UserDAO {
    private Connection conn;

    public MySQLUserDAO() throws SQLException {
        // Load the MySQL JDBC driver
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bhargavdb", "root", "bhargav@143");
    }

    @Override
    public void addUser(int id, String name, String email) throws SQLException {
        String query = "INSERT INTO Users (id, name, email) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.setString(2, name);
            stmt.setString(3, email);
            stmt.executeUpdate();
        }
    }

    @Override
    public void getAllUsers() throws SQLException {
        String query = "SELECT * FROM Users";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + ", Name: " + rs.getString("name") + ", Email: " + rs.getString("email"));
            }
        }
    }

    @Override
    public void updateUser(int id, String name, String email) throws SQLException {
        String query = "UPDATE Users SET name = ?, email = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setInt(3, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteUser(int id) throws SQLException {
        String query = "DELETE FROM Users WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}

// SQLite implementation of UserDAO
class SQLiteUserDAO implements UserDAO {
    private Connection conn;

    public SQLiteUserDAO() throws SQLException {
        // Load the SQLite JDBC driver
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        conn = DriverManager.getConnection("jdbc:sqlite:bhargavdb.db");
    }

    @Override
    public void addUser(int id, String name, String email) throws SQLException {
        String query = "INSERT INTO Users (id, name, email) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.setString(2, name);
            stmt.setString(3, email);
            stmt.executeUpdate();
        }
    }

    @Override
    public void getAllUsers() throws SQLException {
        String query = "SELECT * FROM Users";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + ", Name: " + rs.getString("name") + ", Email: " + rs.getString("email"));
            }
        }
    }

    @Override
    public void updateUser(int id, String name, String email) throws SQLException {
        String query = "UPDATE Users SET name = ?, email = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setInt(3, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteUser(int id) throws SQLException {
        String query = "DELETE FROM Users WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}

// Main application class
public class DatabaseManagementApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UserDAO userDAO = null;

        System.out.println("Select Database: ");
        System.out.println("1. MySQL");
        System.out.println("2. SQLite");
        int choice = scanner.nextInt();

        try {
            switch (choice) {
                case 1:
                    userDAO = new MySQLUserDAO();
                    break;
                case 2:
                    userDAO = new SQLiteUserDAO();
                    break;
                default:
                    System.out.println("Invalid choice");
                    return;
            }

            boolean running = true;
            while (running) {
                System.out.println("1. Add User");
                System.out.println("2. Display All Users");
                System.out.println("3. Update User");
                System.out.println("4. Delete User");
                System.out.println("5. Exit");

                int action = scanner.nextInt();
                switch (action) {
                    case 1:
                        System.out.print("Enter ID: ");
                        int id = scanner.nextInt();
                        System.out.print("Enter Name: ");
                        String name = scanner.next();
                        System.out.print("Enter Email: ");
                        String email = scanner.next();
                        userDAO.addUser(id, name, email);
                        break;
                    case 2:
                        userDAO.getAllUsers();
                        break;
                    case 3:
                        System.out.print("Enter ID: ");
                        int updateId = scanner.nextInt();
                        System.out.print("Enter Name: ");
                        String updateName = scanner.next();
                        System.out.print("Enter Email: ");
                        String updateEmail = scanner.next();
                        userDAO.updateUser(updateId, updateName, updateEmail);
                        break;
                    case 4:
                        System.out.print("Enter ID: ");
                        int deleteId = scanner.nextInt();
                        userDAO.deleteUser(deleteId);
                        break;
                    case 5:
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid action");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
}
