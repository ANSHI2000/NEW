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
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 1, 10, 10));

        JLabel welcomeLabel = new JLabel("Welcome, " + name, SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JButton takeTestButton = new JButton("Take Test");
        JButton leaderboardButton = new JButton("Leaderboard");

        takeTestButton.setFont(new Font("Arial", Font.PLAIN, 16));
        leaderboardButton.setFont(new Font("Arial", Font.PLAIN, 16));

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

        leaderboardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(Student.this, "Leaderboard feature coming soon!");
                // You can later add: new Leaderboard();
            }
        });

        add(welcomeLabel);
        add(takeTestButton);
        add(leaderboardButton);

        setVisible(true);
    }
}
