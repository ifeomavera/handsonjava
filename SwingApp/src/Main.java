import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;

/**
 * Project: CGPA Analytics Dashboard
 * Architecture: Java Swing (Main Class)
 * Theme: Pink & Gray Professional
 */
public class Main extends JFrame {
    
    private static final long serialVersionUID = 1L;

    // UI Infrastructure - Pink & Gray Palette
    private final Color BG_GRAY = new Color(45, 45, 48);      // Deep charcoal gray
    private final Color CARD_GRAY = new Color(60, 60, 65);    // Lighter gray for cards
    private final Color PINK_ACCENT = new Color(255, 105, 180); // Hot Pink
    private final Color SOFT_PINK = new Color(255, 182, 193);  // Light Pink for text
    private final Color TEXT_WHITE = new Color(245, 245, 245);
    private final Color BORDER_PINK = new Color(255, 105, 180, 100); // Semi-transparent pink

    private JTextField courseField, unitField, scoreField;
    private JTextArea displayArea;
    private JLabel gpaVal, cgpaVal;

    private double totalPoints = 0;
    private int totalUnits = 0;
    private ArrayList<Double> semesterGPAs = new ArrayList<>();

    public Main() {
        setupWindow();
        initUI();
    }

    private void setupWindow() {
        setTitle("CGPA Calculator");
        setSize(550, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        getContentPane().setBackground(BG_GRAY);
        setLayout(new BorderLayout(15, 15));
    }

    private void initUI() {
        // --- Header ---
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_GRAY);
        header.setBorder(new EmptyBorder(25, 25, 15, 25));
        
        JLabel title = new JLabel("Academic Performance Tracker");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(PINK_ACCENT);
        
        JLabel subtitle = new JLabel("University 5.0 Grading Standard");
        subtitle.setForeground(SOFT_PINK);
        
        header.add(title, BorderLayout.NORTH);
        header.add(subtitle, BorderLayout.SOUTH);
        add(header, BorderLayout.NORTH);

        // --- Content ---
        JPanel centerPanel = new JPanel(new BorderLayout(0, 20));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(0, 25, 0, 25));

        // Input Architecture
        JPanel inputCard = new JPanel(new GridBagLayout());
        inputCard.setBackground(CARD_GRAY);
        inputCard.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_PINK, 1),
                new EmptyBorder(20, 20, 20, 20)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addStyledInput(inputCard, "Course Code", courseField = new JTextField(), 0, gbc);
        addStyledInput(inputCard, "Unit Load", unitField = new JTextField(), 1, gbc);
        addStyledInput(inputCard, "Score (0-100)", scoreField = new JTextField(), 2, gbc);

        // Actions
        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 12, 0));
        btnPanel.setOpaque(false);
        JButton addBtn = createStyledButton("Add to Queue", PINK_ACCENT);
        JButton calcBtn = createStyledButton("Compute Semester", Color.GRAY);
        btnPanel.add(addBtn);
        btnPanel.add(calcBtn);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        inputCard.add(btnPanel, gbc);
        centerPanel.add(inputCard, BorderLayout.NORTH);

        // Console Log
        displayArea = new JTextArea();
        displayArea.setBackground(new Color(30, 30, 33));
        displayArea.setForeground(SOFT_PINK); 
        displayArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        displayArea.setEditable(false);
        
        JScrollPane scroll = new JScrollPane(displayArea);
        scroll.setBorder(new LineBorder(BORDER_PINK, 1));
        centerPanel.add(scroll, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // --- Stats Footer ---
        JPanel bottomCard = new JPanel(new GridLayout(1, 2, 20, 0));
        bottomCard.setBackground(CARD_GRAY);
        bottomCard.setPreferredSize(new Dimension(0, 130));
        bottomCard.setBorder(new EmptyBorder(25, 30, 25, 30));

        gpaVal = createStatPanel(bottomCard, "CURRENT GPA", "0.00");
        cgpaVal = createStatPanel(bottomCard, "TOTAL CGPA", "0.00");

        add(bottomCard, BorderLayout.SOUTH);

        // --- Logic Hooks ---
        addBtn.addActionListener(e -> processEntry());
        calcBtn.addActionListener(e -> computeGPA());
    }

    private void addStyledInput(JPanel panel, String label, JTextField field, int row, GridBagConstraints gbc) {
        JLabel lbl = new JLabel(label);
        lbl.setForeground(SOFT_PINK);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 1;
        gbc.weightx = 0.3;
        panel.add(lbl, gbc);

        field.setBackground(new Color(80, 80, 85));
        field.setForeground(Color.WHITE);
        field.setCaretColor(PINK_ACCENT);
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_PINK, 1),
                new EmptyBorder(8, 10, 8, 10)));
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(field, gbc);
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBorder(new EmptyBorder(12, 15, 12, 15));
        return btn;
    }

    private JLabel createStatPanel(JPanel parent, String title, String val) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        JLabel t = new JLabel(title, SwingConstants.CENTER);
        t.setForeground(SOFT_PINK);
        JLabel v = new JLabel(val, SwingConstants.CENTER);
        v.setForeground(PINK_ACCENT);
        v.setFont(new Font("Segoe UI", Font.BOLD, 32));
        p.add(t, BorderLayout.NORTH);
        p.add(v, BorderLayout.CENTER);
        parent.add(p);
        return v;
    }

    private void processEntry() {
        try {
            String course = courseField.getText().trim().toUpperCase();
            int units = Integer.parseInt(unitField.getText().trim());
            int score = Integer.parseInt(scoreField.getText().trim());

            if (score < 0 || score > 100) throw new Exception();

            int gp = (score >= 80) ? 5 : (score >= 60) ? 4 : (score >= 50) ? 3 : 
                     (score >= 45) ? 2 : (score >= 40) ? 1 : 0;
            
            String grade = (gp == 5) ? "A" : (gp == 4) ? "B" : (gp == 3) ? "C" : 
                           (gp == 2) ? "D" : (gp == 1) ? "E" : "F";

            totalPoints += (units * gp);
            totalUnits += units;

            displayArea.append(String.format(" [+] %-10s | Grade: %s | Units: %d\n", course, grade, units));
            
            courseField.setText(""); unitField.setText(""); scoreField.setText("");
            courseField.requestFocus();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Validation Error: Please check your input fields.", "System Alert", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void computeGPA() {
        if (totalUnits == 0) return;
        double gpa = totalPoints / totalUnits;
        semesterGPAs.add(gpa);
        gpaVal.setText(String.format("%.2f", gpa));

        double sum = 0;
        for (double d : semesterGPAs) sum += d;
        cgpaVal.setText(String.format("%.2f", sum / semesterGPAs.size()));

        totalPoints = 0; totalUnits = 0;
        displayArea.append(">> LOG: Semester calculation finalized.\n");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Main().setVisible(true);
        });
    }
}