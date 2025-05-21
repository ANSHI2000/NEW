package AutomatedExamSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Test extends JFrame {
    private String studentName, studentEmail, subject;
    private int score = 0;
    private int currentQuestion = 0;

    private String[][] questions;
    private String[] answers;

    private JLabel questionLabel;
    private JRadioButton[] options;
    private ButtonGroup group;
    private JButton nextButton;

    private List<String> analysisList = new ArrayList<>();

    public Test(String name, String email, String subject) {
        this.studentName = name;
        this.studentEmail = email;
        this.subject = subject;

        setTitle("Take Test - " + subject);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        questions = getQuestionsBySubject(subject);
        answers = getAnswersBySubject(subject);

        questionLabel = new JLabel();
        questionLabel.setFont(new Font("Arial", Font.BOLD, 18));
        questionLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel optionsPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 50));
        options = new JRadioButton[4];
        group = new ButtonGroup();

        for (int i = 0; i < 4; i++) {
            options[i] = new JRadioButton();
            options[i].setFont(new Font("Arial", Font.PLAIN, 16));
            group.add(options[i]);
            optionsPanel.add(options[i]);
        }

        nextButton = new JButton("Next");
        nextButton.setFont(new Font("Arial", Font.BOLD, 16));
        nextButton.setBackground(new Color(33, 150, 243));
        nextButton.setForeground(Color.WHITE);
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkAnswer();
            }
        });

        add(questionLabel, BorderLayout.NORTH);
        add(optionsPanel, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(nextButton);
        add(buttonPanel, BorderLayout.SOUTH);

        loadQuestion();

        setVisible(true);
    }

    private void loadQuestion() {
        if (currentQuestion >= questions.length) {
            showResult();
            return;
        }

        questionLabel.setText("Q" + (currentQuestion + 1) + ": " + questions[currentQuestion][0]);
        for (int i = 0; i < 4; i++) {
            options[i].setText(questions[currentQuestion][i + 1]);
        }
        group.clearSelection();
    }

    private void checkAnswer() {
        boolean answered = false;
        for (int i = 0; i < 4; i++) {
            if (options[i].isSelected()) {
                answered = true;
                if (options[i].getText().equals(answers[currentQuestion])) {
                    score++;
                } else {
                    analysisList.add("Q" + (currentQuestion + 1) + ": " + questions[currentQuestion][0] + "\nYour Answer: " + options[i].getText() + "\nCorrect Answer: " + answers[currentQuestion]);
                }
                break;
            }
        }

        if (!answered) {
            JOptionPane.showMessageDialog(this, "Please select an answer.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        currentQuestion++;
        loadQuestion();
    }

    private void showResult() {
        StringBuilder result = new StringBuilder();
        result.append("Test Completed!\nScore: ").append(score).append("/").append(questions.length).append("\n\n");

        if (!analysisList.isEmpty()) {
            result.append("Analysis of Incorrect Answers:\n-------------------------------\n");
            for (String detail : analysisList) {
                result.append(detail).append("\n\n");
            }
        } else {
            result.append("Excellent! All answers correct.");
        }

        JOptionPane.showMessageDialog(this, result.toString(), "Test Analysis", JOptionPane.INFORMATION_MESSAGE);
        dispose();
        new Student(studentName, studentEmail);
    }

    private String[][] getQuestionsBySubject(String subject) {
        // Placeholder - You can load from DB
        if (subject.equals("Java")) {
            return new String[][]{
                    {"Which keyword is used to inherit a class in Java?", "extends", "super", "this", "implements"},
                    {"Which method is used to start a thread?", "start()", "run()", "init()", "go()"},
                    {"Which is not a Java primitive type?", "String", "int", "boolean", "char"}
            };
        } else if (subject.equals("SQL")) {
            return new String[][]{
                    {"Which SQL command is used to retrieve data?", "SELECT", "INSERT", "DELETE", "UPDATE"},
                    {"Which keyword is used to sort result set?", "ORDER BY", "GROUP BY", "HAVING", "SORT BY"},
                    {"Which function is used to count rows?", "COUNT()", "SUM()", "AVG()", "TOTAL()"}
            };
        }
        // Default fallback
        return new String[][]{
                {"Default Q?", "A", "B", "C", "D"}
        };
    }

    private String[] getAnswersBySubject(String subject) {
        if (subject.equals("Java")) {
            return new String[]{"extends", "start()", "String"};
        } else if (subject.equals("SQL")) {
            return new String[]{"SELECT", "ORDER BY", "COUNT()"};
        }
        return new String[]{"A"};
    }
}
