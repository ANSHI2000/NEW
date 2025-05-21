package AutomatedExamSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class AuthPage extends JFrame {

    CardLayout cardLayout;
    JPanel mainPanel;

    JTextField loginEmail;
    JPasswordField loginPassword;
    JButton loginBtn;

    JTextField signName, signEmail;
    JPasswordField signPassword;
    JButton signUpBtn;

    public AuthPage() {
        setTitle("Login / Sign Up - Big Data Portal");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header
        JLabel header = new JLabel("Welcome to Big Data Portal", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 32));
        header.setForeground(Color.BLACK);
        header.setBorder(BorderFactory.createEmptyBorder(30, 10, 30, 10));
        add(header, BorderLayout.NORTH);

        // Toggle buttons panel with transparent bg
        JPanel togglePanel = new JPanel(new FlowLayout());
        togglePanel.setOpaque(false);
        JButton loginToggle = createStyledButton("Login");
        JButton signUpToggle = createStyledButton("Sign Up");

        togglePanel.add(loginToggle);
        togglePanel.add(signUpToggle);
        add(togglePanel, BorderLayout.SOUTH);

        // Main card panel with background image
        mainPanel = new JPanel(cardLayout = new CardLayout()) {
            Image bg = new ImageIcon("auto1.jpg").getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int panelWidth = getWidth();
                int panelHeight = getHeight();

                int imgWidth = bg.getWidth(null);
                int imgHeight = bg.getHeight(null);

                // Maintain aspect ratio
                double panelRatio = (double) panelWidth / panelHeight;
                double imgRatio = (double) imgWidth / imgHeight;

                int drawWidth, drawHeight;
                if (panelRatio > imgRatio) {
                    drawHeight = panelHeight;
                    drawWidth = (int) (imgRatio * drawHeight);
                } else {
                    drawWidth = panelWidth;
                    drawHeight = (int) (drawWidth / imgRatio);
                }

                int x = (panelWidth - drawWidth) / 2;
                int y = (panelHeight - drawHeight) / 2;

                g.drawImage(bg, x, y, drawWidth, drawHeight, this);
            }
        };
        mainPanel.setOpaque(false);

        // Login panel
        JPanel loginPanel = createFormPanel();
        loginEmail = new JTextField();
        loginPassword = new JPasswordField();
        loginBtn = createStyledButton("Login");
        loginBtn.setBackground(new Color(76, 175, 80));

        addFormFields(loginPanel, new String[]{"Email:", "Password:"}, new JComponent[]{loginEmail, loginPassword}, loginBtn);

        // SignUp panel
        JPanel signUpPanel = createFormPanel();
        signName = new JTextField();
        signEmail = new JTextField();
        signPassword = new JPasswordField();
        signUpBtn = createStyledButton("Sign Up");
        signUpBtn.setBackground(new Color(33, 150, 243));

        addFormFields(signUpPanel,
                new String[]{"Name:", "Email:", "Password:"},
                new JComponent[]{signName, signEmail, signPassword},
                signUpBtn);

        mainPanel.add(loginPanel, "login");
        mainPanel.add(signUpPanel, "signup");

        add(mainPanel, BorderLayout.CENTER);

        // Show login form by default
        cardLayout.show(mainPanel, "login");

        // Toggle button actions
        loginToggle.addActionListener(e -> cardLayout.show(mainPanel, "login"));
        signUpToggle.addActionListener(e -> cardLayout.show(mainPanel, "signup"));

        loginBtn.addActionListener(e -> login());
        signUpBtn.addActionListener(e -> signUp());

        setVisible(true);
    }

    // Creates a panel with transparent background and padding for forms
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.white, 2),
                BorderFactory.createEmptyBorder(30, 50, 30, 50)
        ));
        return panel;
    }

    // Helper to add form labels, fields, and button to a panel with GridBagLayout
    private void addFormFields(JPanel panel, String[] labels, JComponent[] fields, JButton button) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        for (int i = 0; i < labels.length; i++) {
            JLabel lbl = new JLabel(labels[i]);
            lbl.setForeground(Color.black);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
            gbc.gridx = 0;
            gbc.weightx = 0.3;
            panel.add(lbl, gbc);

            gbc.gridx = 1;
            gbc.weightx = 0.7;
            fields[i].setFont(new Font("Segoe UI", Font.PLAIN, 16));
            panel.add(fields[i], gbc);

            gbc.gridy++;
        }

        // Empty label for spacing before button
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.gridy++;
        panel.add(new JLabel(" "), gbc);

        // Button
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.CENTER;
        button.setFont(new Font("Segoe UI", Font.BOLD, 18));
        button.setForeground(Color.black);
        panel.add(button, gbc);
    }

    // Styled button with hover effect and rounded corners
    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setBackground(new Color(0, 120, 215));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorderPainted(false);
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btn.setBackground(btn.getBackground().darker());
            }
            public void mouseExited(MouseEvent evt) {
                btn.setBackground(new Color(0, 120, 215));
            }
        });
        return btn;
    }

    void login() {
        String email = loginEmail.getText();
        String password = new String(loginPassword.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter email and password", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/BigData", "root", "password@123"
            );

            String query = "SELECT * FROM students WHERE email = ? AND password = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, email);
            pst.setString(2, password);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String name = rs.getString("name");
                JOptionPane.showMessageDialog(this, "Login successful! Welcome, " + name, "Success", JOptionPane.INFORMATION_MESSAGE);
                new Student(name,email);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid email or password", "Error", JOptionPane.ERROR_MESSAGE);
            }

            rs.close();
            pst.close();
            con.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    void signUp() {
        String name = signName.getText();
        String email = signEmail.getText();
        String password = new String(signPassword.getPassword());

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/BigData", "root", "password@123"
            );
            String checkQuery = "SELECT * FROM students WHERE email = ? OR name = ?";
            PreparedStatement checkStmt = con.prepareStatement(checkQuery);
            checkStmt.setString(1, email);
            checkStmt.setString(2, name);

            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                String existingEmail = rs.getString("email");
                String existingName = rs.getString("name");

                if (existingEmail.equalsIgnoreCase(email)) {
                    JOptionPane.showMessageDialog(this, "Email already registered. Please use a different email.", "Duplicate Error", JOptionPane.WARNING_MESSAGE);
                } else if (existingName.equalsIgnoreCase(name)) {
                    JOptionPane.showMessageDialog(this, "Name already taken. Please choose a different name.", "Duplicate Error", JOptionPane.WARNING_MESSAGE);
                }
                rs.close();
                checkStmt.close();
                con.close();
                return;
            }

            rs.close();
            checkStmt.close();

            String query = "INSERT INTO students (name, email, password) VALUES (?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, name);
            pst.setString(2, email);
            pst.setString(3, password);

            int rows = pst.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Sign-up successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                new Student(name,email);
                this.dispose();
            }

            pst.close();
            con.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "MySQL JDBC Driver not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        SwingUtilities.invokeLater(AuthPage::new);
    }
}
