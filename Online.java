package jdbc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class Online extends JFrame {

    private JTextField usernameField, emailField, transportationIdField, journeyDetailsField, priceField, cardNumberField, cardHolderNameField, expiryDateField, cvvField;
    private JPasswordField passwordField;
    private JTextArea outputArea;
    private JButton bookButton;

    public Online() {
        setTitle("Booking Application");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(12, 2));

        // User information
        add(new JLabel("Username:"));
        usernameField = new JTextField();
        add(usernameField);

        add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        add(passwordField);

        add(new JLabel("Email:"));
        emailField = new JTextField();
        add(emailField);

        // Booking details
        add(new JLabel("Transportation ID:"));
        transportationIdField = new JTextField();
        add(transportationIdField);

        add(new JLabel("Journey Details:"));
        journeyDetailsField = new JTextField();
        add(journeyDetailsField);

        add(new JLabel("Price:"));
        priceField = new JTextField();
        add(priceField);

        // Payment details
        add(new JLabel("Card Number:"));
        cardNumberField = new JTextField();
        add(cardNumberField);

        add(new JLabel("Card Holder Name:"));
        cardHolderNameField = new JTextField();
        add(cardHolderNameField);

        add(new JLabel("Expiry Date (MM/YY):"));
        expiryDateField = new JTextField();
        add(expiryDateField);

        add(new JLabel("CVV:"));
        cvvField = new JTextField();
        add(cvvField);

        // Output Area
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        add(outputArea);

        // Book button
        bookButton = new JButton("Book");
        add(bookButton);
        bookButton.addActionListener(new BookButtonListener());

        setVisible(true);
    }

    private class BookButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String email = emailField.getText();
            int transportationId = Integer.parseInt(transportationIdField.getText());
            String journeyDetails = journeyDetailsField.getText();
            double price = Double.parseDouble(priceField.getText());
            String cardNumber = cardNumberField.getText();
            String cardHolderName = cardHolderNameField.getText();
            String expiryDate = expiryDateField.getText();
            String cvv = cvvField.getText();

            User user = new User(1, username, password, email, "user");
            PaymentDetails paymentDetails = new PaymentDetails(cardNumber, cardHolderName, expiryDate, cvv);
            BookingRequest bookingRequest = new BookingRequest(username, password, transportationId, journeyDetails, price, paymentDetails);

            try {
                // Store user information in the database
                storeUserInDatabase(user);

                // Check availability
                TransportationService transportationService = new TransportationService();
                transportationService.addTransportation(101, "Flight"); // Adding a sample transportation for demo

                if (transportationService.checkAvailability(bookingRequest.getTransportationId(), bookingRequest.getJourneyDetails())) {
                    PaymentGateway paymentGateway = new PaymentGateway();
                    if (paymentGateway.processPayment(bookingRequest.getPaymentDetails())) {
                        Ticket ticket = new Ticket(1, user.getId(), bookingRequest.getTransportationId(), bookingRequest.getJourneyDetails(), bookingRequest.getPrice());

                        // Store booking information in the database
                        storeBookingInDatabase(ticket);

                        outputArea.setText("Booking Successful!\n" + ticket);
                    } else {
                        outputArea.setText("Payment Failed!");
                    }
                } else {
                    outputArea.setText("Transportation Not Available!");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                outputArea.setText("Error: " + ex.getMessage());
            }
        }
    }

    private void storeUserInDatabase(User user) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/booking_system", "root", "password");
        String query = "INSERT INTO users (username, password, email, role) VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, user.getUsername());
        preparedStatement.setString(2, user.getPassword());
        preparedStatement.setString(3, user.getEmail());
        preparedStatement.setString(4, user.getRole());
        preparedStatement.executeUpdate();
        preparedStatement.close();
        connection.close();
    }

    private void storeBookingInDatabase(Ticket ticket) throws SQLException {
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/booking_system", "root", "password");
        String query = "INSERT INTO bookings (user_id, transportation_id, journey_details, price) VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, ticket.getUserId());
        preparedStatement.setInt(2, ticket.getTransportationId());
        preparedStatement.setString(3, ticket.getJourneyDetails());
        preparedStatement.setDouble(4, ticket.getPrice());
        preparedStatement.executeUpdate();
        preparedStatement.close();
        connection.close();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Online());
    }

    // All other classes go here
    public static class BookingRequest {
        private String username;
        private String password;
        private int transportationId;
        private String journeyDetails;
        private double price;
        private PaymentDetails paymentDetails;

        public BookingRequest(String username, String password, int transportationId, String journeyDetails, double price, PaymentDetails paymentDetails) {
            this.username = username;
            this.password = password;
            this.transportationId = transportationId;
            this.journeyDetails = journeyDetails;
            this.price = price;
            this.paymentDetails = paymentDetails;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public int getTransportationId() {
            return transportationId;
        }

        public String getJourneyDetails() {
            return journeyDetails;
        }

        public double getPrice() {
            return price;
        }

        public PaymentDetails getPaymentDetails() {
            return paymentDetails;
        }
    }

    public static class PaymentDetails {
        private String cardNumber;
        private String cardHolderName;
        private String expiryDate;
        private String cvv;

        public PaymentDetails(String cardNumber, String cardHolderName, String expiryDate, String cvv) {
            this.cardNumber = cardNumber;
            this.cardHolderName = cardHolderName;
            this.expiryDate = expiryDate;
            this.cvv = cvv;
        }

        public boolean isValid() {
            // Simulate payment details validation
            return cardNumber != null && !cardNumber.isEmpty() &&
                    cardHolderName != null && !cardHolderName.isEmpty() &&
                    expiryDate != null && !expiryDate.isEmpty() &&
                    cvv != null && !cvv.isEmpty();
        }
    }

    public static class PaymentGateway {
        public boolean processPayment(PaymentDetails paymentDetails) {
            // Simulate payment processing
            return paymentDetails == null || paymentDetails.isValid();
        }
    }

    public static class Ticket {
        private int id;
        private int userId;
        private int transportationId;
        private String journeyDetails;
        private double price;

        public Ticket(int id, int userId, int transportationId, String journeyDetails, double price) {
            this.id = id;
            this.userId = userId;
            this.transportationId = transportationId;
            this.journeyDetails = journeyDetails;
            this.price = price;
        }

        public int getId() {
            return id;
        }

        public int getUserId() {
            return userId;
        }

        public int getTransportationId() {
            return transportationId;
        }

        public String getJourneyDetails() {
            return journeyDetails;
        }

        public double getPrice() {
            return price;
        }

        @Override
        public String toString() {
            return "Ticket{" +
                    "id=" + id +
                    ", userId=" + userId +
                    ", transportationId=" + transportationId +
                    ", journeyDetails='" + journeyDetails + '\'' +
                    ", price=" + price +
                    '}';
        }
    }

    public static class TransportationService {
        private Map<Integer, String> transportations;

        public TransportationService() {
            transportations = new HashMap<>();
        }

        public void addTransportation(int id, String type) {
            transportations.put(id, type);
        }

        public boolean checkAvailability(int transportationId, String journeyDetails) {
            // Simulate checking availability
            return transportations.containsKey(transportationId);
        }
    }

    public static class User {
        private int id;
        private String username;
        private String password;
        private String email;
        private String role;

        public User(int id, String username, String password, String email, String role) {
            this.id = id;
            this.username = username;
            this.password = password;
            this.email = email;
            this.role = role;
        }

        public int getId() {
            return id;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public String getEmail() {
            return email;
        }

        public String getRole() {
            return role;
        }

        @Override
        public String toString() {
            return "User{" +
                    "id=" + id +
                    ", username='" + username + '\'' +
                    ", email='" + email + '\'' +
                    ", role='" + role + '\'' +
                    '}';
        }
    }
}
