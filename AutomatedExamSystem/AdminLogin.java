package AutomatedExamSystem;
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminLogin extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public AdminLogin() {
        setTitle("Admin Login");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 2, 10, 10));

        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");

        loginButton.addActionListener(e -> attemptLogin());

        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(new JLabel()); // Empty cell
        add(loginButton);

        setVisible(true);
    }

    private void attemptLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (validateAdmin(username, password)) {
            new AdminDashboard();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validateAdmin(String username, String password) {
        // Hardcoded admin credentials
        final String ADMIN_USERNAME = "admin";
        final String ADMIN_PASSWORD_HASH = hashPassword("admin123"); // password = admin123

        return username.equals(ADMIN_USERNAME) && hashPassword(password).equals(ADMIN_PASSWORD_HASH);
    }

    private String hashPassword(String password) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return password; // fallback (not secure)
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminLogin());
    }
}

class AdminDashboard extends JFrame {
    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();

        // Results Panel
        JPanel resultsPanel = new JPanel(new BorderLayout());
        JTable resultsTable = new JTable(fetchResults(), getColumnNames());
        JScrollPane scrollPane = new JScrollPane(resultsTable);
        resultsPanel.add(scrollPane, BorderLayout.CENTER);

        tabbedPane.addTab("Exam Results", resultsPanel);
        add(tabbedPane, BorderLayout.CENTER);

        setVisible(true);
    }

    private String[] getColumnNames() {
        return new String[] { "Name", "Email", "Subject", "Score", "Total", "Date Taken" };
    }

    private Object[][] fetchResults() {
        List<Object[]> data = new ArrayList<>();
        final String DB_URL = "jdbc:mysql://localhost:3306/your_database";
        final String USER = "your_username";
        final String PASS = "your_password";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                String sql = "SELECT name, email, subject, score, total, date_taken FROM exam_results";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    Object[] row = {
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("subject"),
                            rs.getInt("score"),
                            rs.getInt("total"),
                            rs.getTimestamp("date_taken")
                    };
                    data.add(row);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data.toArray(new Object[0][]);
    }
}
