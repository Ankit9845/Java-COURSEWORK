package playerquiz;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class Reports extends Main {

	private static final long serialVersionUID = 1L;
	private String fullName;

	public Reports(String fullName) {
		this.fullName = fullName;
		contentPane.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(15, 20, 15, 20); // Increased padding for better spacing
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// Title Label
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2; // Span across two columns
		gbc.anchor = GridBagConstraints.CENTER;
		JLabel welcomeLabel = new JLabel("Reports");
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
		JButton playBtn = createButton("All Player Scores");
		playBtn.addActionListener(new ButtonAction());
		contentPane.add(playBtn, gbc);

		// Go Back Button
		gbc.gridy++;
		JButton playerScoresBtn = createButton("High Scores");
		playerScoresBtn.addActionListener(new ButtonAction());
		contentPane.add(playerScoresBtn, gbc);

		gbc.gridy++;
		JButton highScoresBtn = createButton("Quiz Statistics");
		highScoresBtn.addActionListener(new ButtonAction());
		contentPane.add(highScoresBtn, gbc);

		gbc.gridy++;
		JButton goBackBtn = createButton("Go Back");
	    goBackBtn.addActionListener(new ButtonAction());
	    contentPane.add(goBackBtn, gbc);
	}

	private class ButtonAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String button = e.getActionCommand();
			if (button.equals("All Player Scores")) {
				dispose();
				new DataList(fullName, "AllScore").setVisible(true);
			} else if (button.equals("High Scores")){
				dispose();
				new DataList(fullName, "HighScore").setVisible(true);
			}
			else if (button.equals("Quiz Stats")){
				dispose();
				new Home().setVisible(true);
			}
			else {
				dispose();
				new AdminPanel(fullName).setVisible(true);
			}
		}
	}
}
