package training.DataFlair;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.sqlite.SQLiteDataSource;

public class LoginScreen {
    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private Connection conn; // Connection field

    public LoginScreen(Connection conn) {
        this.conn = conn; // Initialize the Connection field

        frame = new JFrame("Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 150);
        frame.setLayout(null);

        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setBounds(20, 20, 80, 20);
        frame.add(lblUsername);

        usernameField = new JTextField();
        usernameField.setBounds(110, 20, 150, 20);
        frame.add(usernameField);

        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setBounds(20, 50, 80, 20);
        frame.add(lblPassword);

        passwordField = new JPasswordField();
        passwordField.setBounds(110, 50, 150, 20);
        frame.add(passwordField);

        JButton btnLogin = new JButton("Login");
        btnLogin.setBounds(110, 80, 80, 20);
        frame.add(btnLogin);

        JButton btnRegister = new JButton("Register");
        btnRegister.setBounds(200, 80, 80, 20);
        frame.add(btnRegister);

        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (validateUser(username, password)) {
                    frame.dispose();
                    ExpenseTracker expenseTracker = new ExpenseTracker();
                    expenseTracker.getFrame().setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid username or password");
                }
            }
        });

        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please enter a username and password.");
                } else {
                    registerUser(username, password);
                }
            }
        });

        frame.setVisible(true);
    }

    private boolean validateUser(String username, String password) {
        try {
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet resultSet = stmt.executeQuery();
            boolean userExists = resultSet.next();
            resultSet.close();
            stmt.close();
            return userExists;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error authenticating user: " + e.getMessage());
            return false;
        }
    }

    private void registerUser(String username, String password) {
        try {
            String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.executeUpdate();
            stmt.close();
            JOptionPane.showMessageDialog(frame, "User registered successfully");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Error registering user: " + e.getMessage());
        }
    }
}
