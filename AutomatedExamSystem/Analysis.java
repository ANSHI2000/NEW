package AutomatedExamSystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;

public class Analysis extends JFrame {
    private String email;
    private String name;
    private JTable table;
    private DefaultTableModel model;

    public Analysis(String name, String email) {
        this.name = name;
        this.email = email;

        setTitle("📊 Test History & Analysis");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // 🧑‍🎓 Student Info Panel
        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Student Info"));
        infoPanel.add(new JLabel("👤 Name: " + name));
        infoPanel.add(new JLabel("📧 Email: " + email));
        infoPanel.setPreferredSize(new Dimension(800, 60));
        infoPanel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        add(infoPanel, BorderLayout.NORTH);

        // 📋 Table for test history
        String[] columns = {"Subject", "Score", "Total", "Date Taken"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(table);

        add(scrollPane, BorderLayout.CENTER);

        loadTestHistory();
        setVisible(true);
    }

    private void loadTestHistory() {
        try (Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/BigData", "root", "password@123")) {

            String sql = "SELECT subject, score, total, date_taken FROM scores WHERE email = ? ORDER BY date_taken DESC";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, email);

            ResultSet rs = pst.executeQuery();
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, HH:mm");

            while (rs.next()) {
                String subject = rs.getString("subject");
                int score = rs.getInt("score");
                int total = rs.getInt("total");
                String date = sdf.format(rs.getTimestamp("date_taken"));

                model.addRow(new Object[]{subject, score, total, date});
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "⚠ Error loading test history:\n" + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
