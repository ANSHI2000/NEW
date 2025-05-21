package AutomatedExamSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Student extends JFrame {
    private String name, email;

    public Student(String name, String email) {
        this.name = name;
        this.email = email;

        setTitle("Student Dashboard");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // === Background Panel ===
        JLabel backgroundLabel = new JLabel(getScaledIcon("/AutomatedExamSystem/auto1.jpg", 1920, 1080));
        backgroundLabel.setLayout(new BorderLayout());
        setContentPane(backgroundLabel);

        // === Heading Panel (with logo + title) ===
        JPanel headingPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headingPanel.setOpaque(false); // Transparent
        headingPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel logoLabel = getImageLabel("/AutomatedExamSystem/logo.png", 60, 60);
        JLabel headingLabel = new JLabel("Automated Exam System");
        headingLabel.setForeground(Color.WHITE);
        headingLabel.setFont(new Font("Arial", Font.BOLD, 32));

        headingPanel.add(logoLabel);
        headingPanel.add(headingLabel);
        backgroundLabel.add(headingPanel, BorderLayout.NORTH);

        // === Center Panel (with welcome + buttons) ===
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(100, 300, 100, 300));

        JLabel welcomeLabel = new JLabel("Welcome, " + name, SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 28));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton takeTestButton = new JButton("Take Test");
        JButton analysisButton = new JButton("Analysis");

        takeTestButton.setFont(new Font("Arial", Font.PLAIN, 22));
        analysisButton.setFont(new Font("Arial", Font.PLAIN, 22));

        takeTestButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        analysisButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        takeTestButton.setMaximumSize(new Dimension(250, 50));
        analysisButton.setMaximumSize(new Dimension(250, 50));

        centerPanel.add(welcomeLabel);
        centerPanel.add(Box.createVerticalStrut(40));
        centerPanel.add(takeTestButton);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(analysisButton);

        backgroundLabel.add(centerPanel, BorderLayout.CENTER);

        // === Actions ===
        takeTestButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] subjects = {"Java", "SQL"};
                String selectedSubject = (String) JOptionPane.showInputDialog(
                        Student.this,
                        "Select a subject:",
                        "Take Test",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        subjects,
                        subjects[0]
                );
                if (selectedSubject != null) {
                    dispose();
                    new Test(name, email, selectedSubject);
                }
            }
        });
        analysisButton.addActionListener(e -> {
            try {
                new Analysis(name,email);  // Make sure we're passing the email
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(Student.this,
                        "Error showing analysis: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        setVisible(true);
    }

    // === Reusable: Scaled ImageIcon for BG ===
    private ImageIcon getScaledIcon(String path, int width, int height) {
        java.net.URL location = getClass().getResource(path);
        if (location != null) {
            ImageIcon icon = new ImageIcon(location);
            Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } else {
            System.err.println("Background not found at: " + path);
            return new ImageIcon(); // fallback
        }
    }

    // === Reusable: Scaled logo image ===
    private JLabel getImageLabel(String path, int width, int height) {
        java.net.URL location = getClass().getResource(path);
        if (location != null) {
            ImageIcon icon = new ImageIcon(location);
            Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new JLabel(new ImageIcon(scaledImage));
        } else {
            System.err.println("Logo not found at: " + path);
            return new JLabel(); // fallback
        }
    }
}
