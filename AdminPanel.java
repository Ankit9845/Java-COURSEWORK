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

public class AdminPanel extends Main {

	private static final long serialVersionUID = 1L;
	private String fullName;

	public AdminPanel(String fullName) {
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
		JLabel welcomeLabel = new JLabel("Welcome " + fullName);
		welcomeLabel.setForeground(Color.orange);
		welcomeLabel.setFont(new Font("Arial", Font.BOLD, 40));
		welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(welcomeLabel, gbc);

		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.LINE_END;

		gbc.gridx = 0;
		gbc.gridy++;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		JButton questionsBtn = createButton("Questions");
		questionsBtn.addActionListener(new ButtonAction());
		contentPane.add(questionsBtn, gbc);

		gbc.gridy++;
		JButton viewProfileBtn = createButton("View Player Profiles");
		viewProfileBtn.addActionListener(new ButtonAction());
		contentPane.add(viewProfileBtn, gbc);

		gbc.gridy++;
		JButton viewReportsBtn = createButton("View Reports");
		viewReportsBtn.addActionListener(new ButtonAction());
		contentPane.add(viewReportsBtn, gbc);

		gbc.gridy++;
		JButton logoutBtn = createButton("Logout");
		logoutBtn.addActionListener(new ButtonAction());
		contentPane.add(logoutBtn, gbc);
	}

	private class ButtonAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String button = e.getActionCommand();
			if (button.equals("Questions")) {
				dispose();
				new DataList(fullName, "Questions").setVisible(true);
			} else if (button.equals("View Player Profiles")) {
				dispose(); 
				new PlayerProfile(fullName).setVisible(true);
			} else if (button.equals("View Reports")) {
				dispose();
				new Reports(fullName).setVisible(true);
			} else {
				dispose();
				new Home().setVisible(true);
			}
		}
	}
}
