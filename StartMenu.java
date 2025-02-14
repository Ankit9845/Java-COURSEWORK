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
import javax.swing.SwingConstants;

public class StartMenu extends Main {

	private static final long serialVersionUID = 1L;
	private JComboBox<String> comboBox;
    private String lastPlayedLevel = "--Select Difficulty--";
    private int userId;
    private String fullName;

	public StartMenu(int userId, String fullName) {
		this.userId = userId;
		this.fullName = fullName;
		this.lastPlayedLevel = getLastPlayedLevel(userId);
		
		contentPane.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(15, 20, 15, 20); // Increased padding for better spacing
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// Title Label
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2; // Span across two columns
		gbc.anchor = GridBagConstraints.CENTER;
		JLabel welcomeLabel = new JLabel("Let's Start The Game!");
		welcomeLabel.setForeground(Color.WHITE);
		welcomeLabel.setFont(new Font("Arial", Font.BOLD, 30));
		welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(welcomeLabel, gbc);
		
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.LINE_END;
		
		gbc.gridy++;
		gbc.gridx = 0;
		JLabel levelLabel = new JLabel("Level:");
		levelLabel.setForeground(Color.WHITE);
		levelLabel.setFont(new Font("Arial", Font.BOLD, 20));
		levelLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		contentPane.add(levelLabel, gbc);

		// Full Name Field
		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.LINE_START;
		String[] levels = { "--Select Difficulty--", "Beginner", "Intermediate", "Advanced" };
        comboBox = new JComboBox<>(levels);
        comboBox.setPreferredSize(new Dimension(250, 40));
        comboBox.setBounds(171, 70, 140, 22);
        comboBox.setSelectedItem(lastPlayedLevel);
        comboBox.setFont(new Font("Arial", Font.PLAIN, 18));
		contentPane.add(comboBox, gbc);
		
		// Action Button
		gbc.gridx = 0;
		gbc.gridy++;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		JButton playBtn = createButton("Play");
		playBtn.addActionListener(new ButtonAction(comboBox));
		contentPane.add(playBtn, gbc);


		gbc.gridy++;
		JButton goBackBtn = createButton("Go Back");
		goBackBtn.addActionListener(new ButtonAction(null));
		contentPane.add(goBackBtn, gbc);
	}
	
	private class ButtonAction implements ActionListener {
        private JComboBox<String> comboBox;

        public ButtonAction(JComboBox<String> comboBox) {
            this.comboBox = comboBox;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String button = e.getActionCommand();
            if (button.equals("Go Back")) {
                dispose();
                PlayerHome homeFrame = new PlayerHome(userId, fullName);
                homeFrame.setVisible(true);
            } else if (comboBox != null) {
                String selectedLevel = (String) comboBox.getSelectedItem();
                if (selectedLevel.equals("--Select Difficulty--")) {
                    JOptionPane.showMessageDialog(contentPane, "Please select a difficulty level!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!lastPlayedLevel.equals("--Select Difficulty--") && !selectedLevel.equals(lastPlayedLevel)) {
                    JOptionPane.showMessageDialog(contentPane, "You must continue playing at " + lastPlayedLevel + " level!", "Error", JOptionPane.ERROR_MESSAGE);
                    comboBox.setSelectedItem(lastPlayedLevel);
                    return;
                }
                if (getLastPlayedLevel(userId).equals("--Select Difficulty--"))
                {                	
                	insertPlayerLevelAndId(selectedLevel);
                }
                dispose();
                Quiz quizFrame = new Quiz(userId, selectedLevel);
                quizFrame.setVisible(true);
            }
        }
    }

    private String getLastPlayedLevel(int userId) {
        String lastLevel = "";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement("SELECT level FROM scores WHERE userId = ? and (score1 is null or score2 is null or score3 is null or score4 is null or score5 is null) ORDER BY id DESC LIMIT 1;")) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                if (rs.getString("level") == null) {
                    lastLevel = "--Select Difficulty--";
                } else {
                    lastLevel = rs.getString("level");
                }
            }
            else {
            	lastLevel = "--Select Difficulty--";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lastLevel;
    }
    
    private void insertPlayerLevelAndId(String selectedLevel) {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "INSERT INTO scores (userId, level) VALUES (?, ?);";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, userId);
            stmt.setString(2, selectedLevel);
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
