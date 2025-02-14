package playerquiz;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class QuestionForm extends Main {

	private static final long serialVersionUID = 1L;
	private String fullName;
	private JTextField questionField;
	private JComboBox<String> comboBox;
	private JTextField option1Field;
	private JTextField option2Field;
	private JTextField option3Field;
	private JTextField option4Field;
	private JTextField answerField;
	
	private int id;

	public QuestionForm(String action, int id, String fullName) {
		this.fullName = fullName;
	    this.id = id;
	    contentPane.setLayout(new GridBagLayout());
	    GridBagConstraints gbc = new GridBagConstraints();
	    gbc.insets = new Insets(15, 20, 15, 20);
	    gbc.fill = GridBagConstraints.HORIZONTAL;

	    // Title Label
	    gbc.gridx = 0;
	    gbc.gridy = 0;
	    gbc.gridwidth = 2;
	    gbc.anchor = GridBagConstraints.CENTER;
	    JLabel welcomeLabel = new JLabel(action + " Form");
	    welcomeLabel.setForeground(Color.WHITE);
	    welcomeLabel.setFont(new Font("Arial", Font.BOLD, 30));
	    welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
	    contentPane.add(welcomeLabel, gbc);

	    gbc.gridwidth = 1;
	    gbc.anchor = GridBagConstraints.LINE_END;

	    // Question Field
	    gbc.gridy++;
	    gbc.gridx = 0;
	    JLabel questionLabel = new JLabel("Question:");
	    questionLabel.setForeground(Color.WHITE);
	    questionLabel.setFont(new Font("Arial", Font.BOLD, 20));
	    questionLabel.setHorizontalAlignment(SwingConstants.RIGHT);
	    contentPane.add(questionLabel, gbc);

	    gbc.gridx = 1;
	    gbc.anchor = GridBagConstraints.LINE_START;
	    questionField = new JTextField();
	    questionField.setPreferredSize(new Dimension(600, 40));
	    questionField.setFont(new Font("Arial", Font.PLAIN, 18));
	    contentPane.add(questionField, gbc);

	    // Difficulty Level DropDown
	    gbc.gridy++;
	    gbc.gridx = 0;
	    JLabel levelLabel = new JLabel("Level:");
	    levelLabel.setForeground(Color.WHITE);
	    levelLabel.setFont(new Font("Arial", Font.BOLD, 20));
	    levelLabel.setHorizontalAlignment(SwingConstants.RIGHT);
	    contentPane.add(levelLabel, gbc);

	    gbc.gridx = 1;
	    gbc.anchor = GridBagConstraints.LINE_START;
	    String[] levels = { "--Select Difficulty--", "Beginner", "Intermediate", "Advanced" };
	    comboBox = new JComboBox<>(levels);
	    comboBox.setPreferredSize(new Dimension(250, 40));
	    comboBox.setFont(new Font("Arial", Font.PLAIN, 18));
	    contentPane.add(comboBox, gbc);

	    // Options & Answer Fields
	    createFormRow("Option 1:", option1Field = createTextField(), gbc);
	    createFormRow("Option 2:", option2Field = createTextField(), gbc);
	    createFormRow("Option 3:", option3Field = createTextField(), gbc);
	    createFormRow("Option 4:", option4Field = createTextField(), gbc);
	    createFormRow("Answer:", answerField = createTextField(), gbc);

	    // Fetch Data if Editing or Deleting
	    if (!action.equals("Create")) {
	        loadQuestionData(id);
	    }

	    // Action Button
	    gbc.gridy++;
	    gbc.gridx = 0;
	    gbc.gridwidth = 2;
	    gbc.anchor = GridBagConstraints.CENTER;
	    JButton actionBtn = createButton(action);
	    actionBtn.addActionListener(new ButtonAction());
	    addEnterKeyListener(actionBtn);
	    contentPane.add(actionBtn, gbc);

	    // Go Back Button
	    gbc.gridy++;
	    JButton goBackBtn = createButton("Go Back");
	    goBackBtn.addActionListener(new ButtonAction());
	    contentPane.add(goBackBtn, gbc);

	    // Disable Fields for Delete
	    if (action.equals("Delete")) {
	        disableFields();
	    }
	}

	
	private class ButtonAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String button = e.getActionCommand();
            if (button.equals("Go Back")) {
                dispose();
                new DataList(fullName, "Questions").setVisible(true);
            }
            else if (button.equals("Create")) {
            	createQuestion();
            }
            else if (button.equals("Update")) {
            	editQuestion(id);
            }
            else if (button.equals("Delete")) {
            	deleteQuestion(id);
            }
            else if (comboBox != null) {
                String selectedLevel = (String) comboBox.getSelectedItem();
                if (selectedLevel.equals("--Select Difficulty--")) {
                    JOptionPane.showMessageDialog(contentPane, "Please select a difficulty level!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        }
    }
	
	private void loadQuestionData(int id) {
	    try (Connection conn = DBConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement("SELECT * FROM questions WHERE Id = ?")) {

	        stmt.setInt(1, id);
	        ResultSet rs = stmt.executeQuery();

	        if (rs.next()) {
	            questionField.setText(rs.getString("Question"));
	            comboBox.setSelectedItem(rs.getString("Level"));
	            option1Field.setText(rs.getString("Option1"));
	            option2Field.setText(rs.getString("Option2"));
	            option3Field.setText(rs.getString("Option3"));
	            option4Field.setText(rs.getString("Option4"));
	            answerField.setText(rs.getString("Answer"));
	        } else {
	            JOptionPane.showMessageDialog(this, "No question found!", "Error", JOptionPane.ERROR_MESSAGE);
	            dispose();
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        JOptionPane.showMessageDialog(this, "Error loading question!", "Error", JOptionPane.ERROR_MESSAGE);
	    }
	}

	private void disableFields() {
	    questionField.setEnabled(false);
	    comboBox.setEnabled(false);
	    option1Field.setEnabled(false);
	    option2Field.setEnabled(false);
	    option3Field.setEnabled(false);
	    option4Field.setEnabled(false);
	    answerField.setEnabled(false);
	}
	
	private void createFormRow(String labelText, JTextField field, GridBagConstraints gbc) {
	    gbc.gridy++;
	    gbc.gridx = 0;
	    JLabel label = new JLabel(labelText);
	    label.setForeground(Color.WHITE);
	    label.setFont(new Font("Arial", Font.BOLD, 20));
	    label.setHorizontalAlignment(SwingConstants.RIGHT);
	    contentPane.add(label, gbc);

	    gbc.gridx = 1;
	    gbc.anchor = GridBagConstraints.LINE_START;
	    field.setPreferredSize(new Dimension(250, 40));
	    field.setFont(new Font("Arial", Font.PLAIN, 18));
	    contentPane.add(field, gbc);
	}	
	
	private void createQuestion() {
	    String question = questionField.getText().trim();
	    String level = (String) comboBox.getSelectedItem();
	    String option1 = option1Field.getText().trim();
	    String option2 = option2Field.getText().trim();
	    String option3 = option3Field.getText().trim();
	    String option4 = option4Field.getText().trim();
	    String answer = answerField.getText().trim();

	    // Validate required fields
	    if (question.isEmpty() || level.equals("--Select Difficulty--") || option1.isEmpty() || 
	        option2.isEmpty() || option3.isEmpty() || option4.isEmpty() || answer.isEmpty()) {
	        JOptionPane.showMessageDialog(this, "Please fill in all fields!", "Error", JOptionPane.ERROR_MESSAGE);
	        return;
	    }
	    
	    if (!answer.equals(option1) && !answer.equals(option2) && !answer.equals(option3) && !answer.equals(option4)) {
	        JOptionPane.showMessageDialog(this, "Answer should be among the 4 options provided!", "Error", JOptionPane.ERROR_MESSAGE);
	        return;
	    }

	    try (Connection conn = DBConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement("INSERT INTO questions(Question, Level, Option1, Option2, Option3, Option4, Answer) VALUES (?, ?, ?, ?, ?, ?, ?)")) {

	        stmt.setString(1, question);
	        stmt.setString(2, level);
	        stmt.setString(3, option1);
	        stmt.setString(4, option2);
	        stmt.setString(5, option3);
	        stmt.setString(6, option4);
	        stmt.setString(7, answer);

	        stmt.executeUpdate();
	        JOptionPane.showMessageDialog(this, "Question created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
	        dispose();
	        new DataList(fullName, "Questions").setVisible(true);
	    } catch (SQLException e) {
	        e.printStackTrace();
	        JOptionPane.showMessageDialog(this, "Error creating question!", "Error", JOptionPane.ERROR_MESSAGE);
	    }
	}

	private void editQuestion(int id) {
	    String question = questionField.getText().trim();
	    String level = (String) comboBox.getSelectedItem();
	    String option1 = option1Field.getText().trim();
	    String option2 = option2Field.getText().trim();
	    String option3 = option3Field.getText().trim();
	    String option4 = option4Field.getText().trim();
	    String answer = answerField.getText().trim();

	    // Validate required fields
	    if (question.isEmpty() || level.equals("--Select Difficulty--") || option1.isEmpty() || 
	        option2.isEmpty() || option3.isEmpty() || option4.isEmpty() || answer.isEmpty()) {
	        JOptionPane.showMessageDialog(this, "Please fill in all fields!", "Error", JOptionPane.ERROR_MESSAGE);
	        return;
	    }

	    try (Connection conn = DBConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement("UPDATE questions SET Question=?, Level=?, Option1=?, Option2=?, Option3=?, Option4=?, Answer=? WHERE Id=?")) {

	        stmt.setString(1, question);
	        stmt.setString(2, level);
	        stmt.setString(3, option1);
	        stmt.setString(4, option2);
	        stmt.setString(5, option3);
	        stmt.setString(6, option4);
	        stmt.setString(7, answer);
	        stmt.setInt(8, id);

	        stmt.executeUpdate();
	        JOptionPane.showMessageDialog(this, "Question updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
	        dispose();
	        new DataList(fullName, "Questions").setVisible(true);
	    } catch (SQLException e) {
	        e.printStackTrace();
	        JOptionPane.showMessageDialog(this, "Error updating question!", "Error", JOptionPane.ERROR_MESSAGE);
	    }
	}

	private void deleteQuestion(int id) {
	    int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this question?", "Confirm", JOptionPane.YES_NO_OPTION);
	    if (choice == JOptionPane.YES_OPTION) {
	        try (Connection conn = DBConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement("DELETE FROM questions WHERE Id = ?")) {

	            stmt.setInt(1, id);
	            stmt.executeUpdate();
	            JOptionPane.showMessageDialog(this, "Question deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
	            dispose();
	            new DataList(fullName, "Questions").setVisible(true);
	        } catch (SQLException e) {
	            e.printStackTrace();
	            JOptionPane.showMessageDialog(this, "Error deleting question!", "Error", JOptionPane.ERROR_MESSAGE);
	        }
	    }
	}

}
