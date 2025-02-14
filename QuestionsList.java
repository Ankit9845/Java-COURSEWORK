//package PlayerQuiz;
//
//import javax.swing.*;
//import java.awt.*;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//
//public class QuestionsList extends Main {
//
//    private static final long serialVersionUID = 1L;
//    private JPanel questionsPanel;
//    private String fullName;
//    
//    public QuestionsList(String fullName) {
//    	this.fullName = fullName;
//        setLayout(new BorderLayout());
//
//        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
//	    headerPanel.setBackground(new Color(45, 52, 54));
//
//	    String[] levels = {"All", "Beginner", "Intermediate", "Advanced"};
//	    JComboBox<String> levelDropdown = new JComboBox<>(levels);
//        levelDropdown.setFont(new Font("Arial", Font.PLAIN, 20));
//        levelDropdown.setPreferredSize(new Dimension(150, 40));
//
//	    DefaultListCellRenderer renderer = new DefaultListCellRenderer();
//	    renderer.setHorizontalAlignment(SwingConstants.CENTER); // Center text
//	    levelDropdown.setRenderer(renderer);
//	    headerPanel.add(levelDropdown);
//
//	    levelDropdown.addActionListener(e -> {
//            String selectedLevel = (String) levelDropdown.getSelectedItem();
//            fetchQuestions(selectedLevel); // Fetch filtered questions based on the selected level
//        });
//
//	    JButton addQuestionButton = createButton("Add Question");
//        // Add action listener for button (you can handle the logic in the method)
//        addQuestionButton.addActionListener(e -> formQuestion("Create", 0));
//        headerPanel.add(addQuestionButton);
//
//        JButton goBackButton = createButton("Go Back");
//        // Add action listener for button (you can handle the logic in the method)
//        goBackButton.addActionListener(e -> goBack());
//        headerPanel.add(goBackButton);
//        // Add the header panel above the questions panel
//        add(headerPanel, BorderLayout.NORTH);
//	    // === Content Panel (Wraps Profile Panel) ===
//	    JPanel contentPanel = new JPanel(new BorderLayout());
//
//	    // Profile Panel (Holds User Profiles)
//	    questionsPanel = new JPanel(new GridBagLayout());
//	    questionsPanel.setBackground(new Color(45, 52, 54));
//
//	    fetchQuestions("All"); // Fetch profiles and populate rows
//
//	    contentPanel.add(questionsPanel, BorderLayout.PAGE_START); // Ensures profiles align at the top
//
//	    // Scroll Pane for Profile Panel
//	    JScrollPane scrollPane = new JScrollPane(contentPanel);
//	    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
//	    scrollPane.getVerticalScrollBar().setUnitIncrement(16);
//	    scrollPane.setBorder(BorderFactory.createEmptyBorder());
//
//	    // Fix the white background issue
//	    scrollPane.getViewport().setBackground(new Color(45, 52, 54)); // Dark background for empty space
//	    contentPanel.setBackground(new Color(45, 52, 54)); // Ensure content panel has the same background
//
//	    add(scrollPane, BorderLayout.CENTER); // Add scrollPane to the main layout
//        
////        JPanel headerPanel = new JPanel();
////        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER)); // Align components to the left
////        headerPanel.setBackground(new Color(45, 52, 54));
////
////        String[] levels = {"All", "Beginner", "Intermediate", "Advanced"};
////        JComboBox<String> levelDropdown = new JComboBox<>(levels);
////        levelDropdown.setFont(new Font("Arial", Font.PLAIN, 20));
////        levelDropdown.setPreferredSize(new Dimension(150, 40));
////        DefaultListCellRenderer renderer = new DefaultListCellRenderer();
////        renderer.setHorizontalAlignment(SwingConstants.CENTER); // Center text
////        levelDropdown.setRenderer(renderer);
////        headerPanel.add(levelDropdown);
////
////        levelDropdown.addActionListener(e -> {
////            String selectedLevel = (String) levelDropdown.getSelectedItem();
////            fetchQuestions(selectedLevel); // Fetch filtered questions based on the selected level
////        });
////        // Add Question button
////        JButton addQuestionButton = createButton("Add Question");
////        // Add action listener for button (you can handle the logic in the method)
////        addQuestionButton.addActionListener(e -> formQuestion("Create", 0));
////        headerPanel.add(addQuestionButton);
////
////        JButton goBackButton = createButton("Go Back");
////        // Add action listener for button (you can handle the logic in the method)
////        goBackButton.addActionListener(e -> goBack());
////        headerPanel.add(goBackButton);
////        // Add the header panel above the questions panel
////        add(headerPanel, BorderLayout.NORTH);
////        questionsPanel = new JPanel();
////        questionsPanel.setLayout(new BoxLayout(questionsPanel, BoxLayout.Y_AXIS));
////        questionsPanel.setBackground(new Color(45, 52, 54));
////
////        fetchQuestions("All"); // Fetch questions from the database and populate rows
////
////        JScrollPane scrollPane = new JScrollPane(questionsPanel);
////        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Smooth scrolling
////        add(scrollPane, BorderLayout.CENTER);
//    }
//
////    private void addHeaderRow() {
////        JPanel headerPanel = new JPanel(new GridBagLayout());
////        headerPanel.setBackground(new Color(45, 52, 54)); // Set background color
////
////        GridBagConstraints gbc = new GridBagConstraints();
////        gbc.fill = GridBagConstraints.HORIZONTAL;
////        gbc.insets = new Insets(5, 5, 5, 5); // Add spacing between components
////        gbc.weightx = 1.0; // This allows columns to stretch horizontally
////
////        String[] headers = {"S.No", "Question", "Level", "Option1", "Option2", "Option3", "Option4", "Answer", "Edit", "Delete"};
////        for (int i = 0; i < headers.length; i++) {
////            gbc.gridx = i;
////            JLabel label = new JLabel(headers[i], SwingConstants.CENTER);
////            label.setFont(new Font("Arial", Font.BOLD, 12));
////            label.setForeground(Color.WHITE); // Set text color to white
////            label.setBorder(BorderFactory.createLineBorder(Color.WHITE));
////
////            // Dynamically adjust column widths
////            if (i == 1) {
////                label.setPreferredSize(new Dimension(250, 30)); // Larger for "Question" column
////            } else {
////                label.setPreferredSize(new Dimension(80, 30)); // Adjust width for other columns
////            }
////
////            headerPanel.add(label, gbc);
////        }
////
////        // Ensure the container stretches to fill the screen width
////        questionsPanel.add(headerPanel);
////        questionsPanel.revalidate();
////        questionsPanel.repaint();
////    }
////
////    private void addQuestionRow(int index, int id, String question, String level, String option1, String option2, String option3, String option4, String answer) {
////        JPanel rowPanel = new JPanel(new GridBagLayout());
////        rowPanel.setBackground(new Color(45, 52, 54)); // Set background color
////
////        GridBagConstraints gbc = new GridBagConstraints();
////        gbc.fill = GridBagConstraints.HORIZONTAL;
////        gbc.insets = new Insets(5, 5, 5, 5); // Add spacing between components
////        gbc.weightx = 1.0; // Allow the columns to stretch horizontally
////
////        // Add data fields (S.No, Question, Level, Options, Answer)
////        gbc.gridx = 0; 
////        rowPanel.add(createStyledLabel(String.valueOf(index), 60), gbc);
////        gbc.gridx = 1; 
////        rowPanel.add(createStyledLabel(question, 250), gbc); // Adjust width for "Question"
////        gbc.gridx = 2; 
////        rowPanel.add(createStyledLabel(level, 80), gbc);
////        gbc.gridx = 3; 
////        rowPanel.add(createStyledLabel(option1, 80), gbc);
////        gbc.gridx = 4; 
////        rowPanel.add(createStyledLabel(option2, 80), gbc);
////        gbc.gridx = 5; 
////        rowPanel.add(createStyledLabel(option3, 80), gbc);
////        gbc.gridx = 6; 
////        rowPanel.add(createStyledLabel(option4, 80), gbc);
////        gbc.gridx = 7; 
////        rowPanel.add(createStyledLabel(answer, 80), gbc);
////
////        // Add buttons (Edit, Delete)
////        gbc.gridx = 8; rowPanel.add(createStyledButton("Edit", id, true), gbc);
////        gbc.gridx = 9; rowPanel.add(createStyledButton("Delete", id, false), gbc);
////
////        questionsPanel.add(rowPanel);
////        questionsPanel.revalidate();
////        questionsPanel.repaint();
////    }
//    
//    private void addHeaderRow() {
//        JPanel headerRow = createRow(new String[]{"S.No", "Question", "Level", "Option1", "Option2", "Option3", "Option4", "Answer", "Edit", "Delete"}, true);
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.gridx = 0;
//        gbc.gridy = 0;
//        gbc.weightx = 1.0;
//        gbc.fill = GridBagConstraints.HORIZONTAL;
//        gbc.insets = new Insets(5, 5, 5, 5); // Add consistent spacing
//        questionsPanel.add(headerRow, gbc);
//    }
//
//    private void addQuestionRow(int rowIndex, int id, String question, String level, String option1, String option2, String option3, String option4, String answer) {
//        JPanel row = createRow(new String[]{String.valueOf(rowIndex), question, level, option1, option2, option3, option4, answer}, false);
//
//        // Add Edit and Delete buttons
//        row.add(createStyledButton("Edit", id, true));
//        row.add(createStyledButton("Delete", id, false));
//
//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.gridx = 0;
//        gbc.gridy = rowIndex;
//        gbc.weightx = 1.0;
//        gbc.fill = GridBagConstraints.HORIZONTAL;
//        gbc.insets = new Insets(5, 5, 5, 5); // Consistent spacing
//        questionsPanel.add(row, gbc);
//    }
//
//    private JPanel createRow(String[] rowData, boolean isHeader) {
//        JPanel rowPanel = new JPanel(new GridLayout(1, rowData.length));
//        rowPanel.setBackground(new Color(45, 52, 54));
//
//        for (String cell : rowData) {
//            JLabel label = new JLabel(cell, SwingConstants.CENTER);
//            label.setFont(new Font("Arial", isHeader ? Font.BOLD : Font.PLAIN, 12));
//            label.setForeground(Color.WHITE);
//            label.setBorder(BorderFactory.createLineBorder(Color.WHITE));
//            label.setPreferredSize(new Dimension(150, 30));
//            rowPanel.add(label);
//        }
//        return rowPanel;
//    }
//
////    // Helper method to create styled labels
////    private JLabel createStyledLabel(String text, int width) {
////        JLabel label = new JLabel(text, SwingConstants.CENTER);
////        label.setFont(new Font("Arial", Font.PLAIN, 12));
////        label.setForeground(Color.WHITE);
////        label.setPreferredSize(new Dimension(width, 10));
////        return label;
////    }
//
//    // Helper method to create styled buttons
//    private JButton createStyledButton(String text, int id, boolean isEdit) {
//        JButton button = new JButton(text);
//        button.setPreferredSize(new Dimension(80, 30));
//        button.setBackground(isEdit ? new Color(0, 184, 148) : new Color(214, 48, 49)); // Edit: Green, Delete: Red
//        button.setForeground(Color.WHITE);
//        button.setFocusPainted(false);
//        button.addActionListener(e -> {
//            if (isEdit) {
//            	formQuestion("Update", id);
//            } else {
//            	formQuestion("Delete", id);
//            }
//        });
//        return button;
//    }
//
//    private void fetchQuestions(String level) {
//        questionsPanel.removeAll();
//        addHeaderRow();
//
//        // Query to order by Level priority and latest question first within each level
//        String query = "SELECT * FROM Questions ";
//
//        if (!level.equals("All")) {
//            query += "WHERE Level = ? ORDER BY Id DESC";
//        }else {        	
//        	query += "ORDER BY CASE WHEN Level = 'Beginner' THEN 1 WHEN Level = 'Intermediate' THEN 2 WHEN Level = 'Advanced' THEN 3 ELSE 4 END, Id DESC"; // Sort by level priority and latest first
//        }
//
//        try (Connection conn = DBConnection.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(query)) {
//
//            if (!level.equals("All")) {
//                stmt.setString(1, level); // Set the selected level filter
//            }
//
//            ResultSet rs = stmt.executeQuery();
//
//            int index = 1;
//            while (rs.next()) {
//                int id = rs.getInt("id");
//                String question = rs.getString("Question");
//                String qLevel = rs.getString("Level");
//                String option1 = rs.getString("Option1");
//                String option2 = rs.getString("Option2");
//                String option3 = rs.getString("Option3");
//                String option4 = rs.getString("Option4");
//                String answer = rs.getString("Answer");
//
//                addQuestionRow(index++, id, question, qLevel, option1, option2, option3, option4, answer);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            JOptionPane.showMessageDialog(this, "Error fetching questions from the database!", "Error", JOptionPane.ERROR_MESSAGE);
//        }
//
//        questionsPanel.revalidate();
//        questionsPanel.repaint();
//    }
//
//
//    private void formQuestion(String action, int id) {
//    	dispose();
//    	new QuestionForm(action, id, fullName).setVisible(true);
//    }
//    
//    private void goBack() {
//    	dispose();
//        new AdminPanel(fullName).setVisible(true);
//    }
//}
