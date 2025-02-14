package playerquiz;

import java.awt.Color;
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
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class Quiz extends Main {

	private static final long serialVersionUID = 1L;
    private JRadioButton option1, option2, option3, option4;
    private JButton btnNext;
    private JLabel lblQuestion;
    private int questionIndex = 0, score = 0;
    private ArrayList<Question> questions;
    private int userId;
    private ButtonGroup group;
    private int activeScoreColumn = -1;
    private String level;
	
	public Quiz(int userId, String level) {
		this.userId = userId;
		this.level = level;

        if (!checkPlayerAttempts(level)) {
            return;
        }
		
     // Use GridBagLayout for proper alignment
        contentPane.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding for elements

        // Question Label (Spans across 2 columns)
        lblQuestion = new JLabel("Loading question...");
        lblQuestion.setForeground(Color.WHITE);
        lblQuestion.setFont(new Font("Arial", Font.BOLD, 25));
        lblQuestion.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Make the label span two columns
        gbc.fill = GridBagConstraints.HORIZONTAL;
        contentPane.add(lblQuestion, gbc);

        // Reset grid width for options
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;

        // Option 1 (Left Column)
        gbc.gridx = 0;
        gbc.gridy = 1;
        option1 = new JRadioButton();
        option1.setBackground(new Color(45, 52, 54));
        option1.setForeground(Color.WHITE);
        option1.setFont(new Font("Arial", Font.BOLD, 20));
        contentPane.add(option1, gbc);

        // Option 2 (Right Column)
        gbc.gridx = 1;
        option2 = new JRadioButton();
        option2.setBackground(new Color(45, 52, 54));
        option2.setForeground(Color.WHITE);
        option2.setFont(new Font("Arial", Font.BOLD, 20));
        contentPane.add(option2, gbc);

        // Option 3 (Left Column, Next Row)
        gbc.gridx = 0;
        gbc.gridy = 2;
        option3 = new JRadioButton();
        option3.setBackground(new Color(45, 52, 54));
        option3.setForeground(Color.WHITE);
        option3.setFont(new Font("Arial", Font.BOLD, 20));
        contentPane.add(option3, gbc);

        // Option 4 (Right Column, Same Row)
        gbc.gridx = 1;
        option4 = new JRadioButton();
        option4.setBackground(new Color(45, 52, 54));
        option4.setForeground(Color.WHITE);
        option4.setFont(new Font("Arial", Font.BOLD, 20));
        contentPane.add(option4, gbc);

        // Group the radio buttons so only one can be selected
        group = new ButtonGroup();
        group.add(option1);
        group.add(option2);
        group.add(option3);
        group.add(option4);

        // Next Button (Centered in the last row)
        btnNext = createButton("Next");
        btnNext.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleNextQuestion();
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2; // Make the button span across two columns
        gbc.anchor = GridBagConstraints.CENTER;
        contentPane.add(btnNext, gbc);

        // Fetch and show questions
        questions = fetchQuestions(level);
        if (!questions.isEmpty()) {
            showQuestion(questionIndex);
        } else {
            lblQuestion.setText("No questions found for this level.");
        }
	}
	
	private boolean checkPlayerAttempts(String level) {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT * FROM scores WHERE userId = ? and (score1 is null or score2 is null or score3 is null or score4 is null or score5 is null) ORDER BY id DESC LIMIT 1;";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                for (int i = 1; i <= 5; i++) {
                    if (rs.getObject("score" + i) == null) {
                        activeScoreColumn = i;
                        return true;
                    }
                }
            }

            int choice = JOptionPane.showConfirmDialog(this, 
                    "You have played 5 times in " + level + " level. Would you like to start another game?", 
                    "New Game", 
                    JOptionPane.YES_NO_OPTION);

            if (choice == JOptionPane.YES_OPTION) {
//                resetScores();
                SwingUtilities.invokeLater(() -> {
                	dispose();
                	new StartMenu(userId, getPlayerName(userId)).setVisible(true);
                });
            } else {
            	SwingUtilities.invokeLater(() -> {
                    dispose();
                    new PlayerHome(userId, getPlayerName(userId)).setVisible(true);
                });
            }
            return false;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private ArrayList<Question> fetchQuestions(String level) {
        ArrayList<Question> questionsList = new ArrayList<>();
        try {
        	Connection conn = DBConnection.getConnection();
            String query = "SELECT Id, Question, Option1, Option2, Option3, Option4, Answer, Level FROM questions WHERE level = ? LIMIT 10";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, level);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                questionsList.add(new Question(
                		rs.getInt("Id"),
                        rs.getString("Question"),
                        rs.getString("Option1"),
                        rs.getString("Option2"),
                        rs.getString("Option3"),
                        rs.getString("Option4"),
                        rs.getString("Answer"),
                        rs.getString("Level")
                ));
            }
            conn.close();
            Collections.shuffle(questionsList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return questionsList;
    }

    private void showQuestion(int index) {
        Question q = questions.get(index);
        lblQuestion.setText(q.getQuestion());
        option1.setText(q.getOption1());
        option2.setText(q.getOption2());
        option3.setText(q.getOption3());
        option4.setText(q.getOption4());
        group.clearSelection();
    }

    private void handleNextQuestion() {
        String selectedAnswer = getSelectedOption();
        if (selectedAnswer == null) {
            JOptionPane.showMessageDialog(this, "Please select an answer before proceeding!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Question currentQuestion = questions.get(questionIndex);
        if (selectedAnswer.equals(currentQuestion.getAnswer())) {
            score += 1;
        }

        updateScoreInDatabase();

        questionIndex++;
        if (questionIndex < questions.size()) {
            showQuestion(questionIndex);
        } else {
        	int choice = JOptionPane.showOptionDialog(
                    this,
                    "Quiz Completed! Your Score: " + score + "\nDo you want to play again?",
                    "Quiz Completed",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new String[]{"Play Again", "Exit"},
                    "Play Again"
                );

                if (choice == JOptionPane.YES_OPTION) {
                	if (!checkPlayerAttempts(level)) {
                        return;
                    }
                    questionIndex = 0;
                    score = 0;
                    showQuestion(questionIndex);
                } else {
                	SwingUtilities.invokeLater(() -> {
                        dispose();
                        new PlayerHome(userId, getPlayerName(userId)).setVisible(true);
                    });
                }
        }
    }

    private void updateScoreInDatabase() {
        try {
        	Connection conn = DBConnection.getConnection();
            String query = "UPDATE scores SET score" + activeScoreColumn + " = ? WHERE userId=?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, score);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String getSelectedOption() {
        if (option1.isSelected()) return option1.getText();
        if (option2.isSelected()) return option2.getText();
        if (option3.isSelected()) return option3.getText();
        if (option4.isSelected()) return option4.getText();
        return null;
    }
}
