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

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

public class PlayerHome extends Main {

	private static final long serialVersionUID = 1L;
	private String fullName;
	private int userId;

	public PlayerHome(int userId, String fullName) {
		this.fullName = fullName;
		this.userId = userId;
		contentPane.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(15, 20, 15, 20); // Increased padding for better spacing
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// Title Label
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2; // Span across two columns
		gbc.anchor = GridBagConstraints.CENTER;
		JLabel welcomeLabel = new JLabel("Welcome " + fullName);
		welcomeLabel.setForeground(Color.WHITE);
		welcomeLabel.setFont(new Font("Arial", Font.BOLD, 30));
		welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(welcomeLabel, gbc);

		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.LINE_END;
		// Action Button
		gbc.gridx = 0;
		gbc.gridy++;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		JButton playBtn = createButton("Start Quiz");
		playBtn.addActionListener(new ButtonAction());
		contentPane.add(playBtn, gbc);

		// Go Back Button
		gbc.gridy++;
		JButton highScoresBtn = createButton("LeaderBoard");
		highScoresBtn.addActionListener(new ButtonAction());
		contentPane.add(highScoresBtn, gbc);

		gbc.gridy++;
		JButton playerDetailBtn = createButton("View Profile");
		playerDetailBtn.addActionListener(new ButtonAction());
		contentPane.add(playerDetailBtn, gbc);

		gbc.gridy++;
		JButton logoutBtn = createButton("Logout");
		logoutBtn.addActionListener(new ButtonAction());
		contentPane.add(logoutBtn, gbc);
	}

	private class ButtonAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String button = e.getActionCommand();
			if (button.equals("Start Quiz")) {
				startQuiz(fullName);
			}
			else if (button.equals("LeaderBoard")) {
				dispose();
				new DataList(fullName, "LeaderBoard").setVisible(true);
			}
			else if (button.equals("View Profile")) {
				dispose();
				new LoginSignUp("Update Profile", userId).setVisible(true);
			}
			else {
				dispose();
				new Home().setVisible(true);
			}
		}
	}
	
	private void startQuiz(String fullName) {
		String query = "SELECT Id FROM users WHERE FullName=?";
		int userId = 0;
		try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setString(1, fullName);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					userId = rs.getInt("Id");
				}
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(contentPane, "Database Error!", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		dispose();
		new StartMenu(userId, fullName).setVisible(true);
	}
}
