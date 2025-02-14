package playerquiz;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class DataList extends Main {

	private static final long serialVersionUID = 1L;
	private JPanel mainPanel;
	private String fullName;

	public DataList(String fullName, String reportType) {
		this.fullName = fullName;
		setLayout(new BorderLayout());

		JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		headerPanel.setBackground(new Color(70, 130, 180)); // Steel Blue

		String[] levels;
		if (reportType.equals("LeaderBoard")) {
			levels = new String[] { "Beginner", "Intermediate", "Advanced" };
		} else {
			levels = new String[] { "All", "Beginner", "Intermediate", "Advanced" };
		}
		JComboBox<String> levelDropdown = new JComboBox<>(levels);
		levelDropdown.setFont(new Font("Arial", Font.PLAIN, 20));
		levelDropdown.setPreferredSize(new Dimension(150, 40));

		DefaultListCellRenderer renderer = new DefaultListCellRenderer();
		renderer.setHorizontalAlignment(SwingConstants.CENTER); // Center text
		levelDropdown.setRenderer(renderer);
		headerPanel.add(levelDropdown);

		// Create search field
		JTextField searchField = createTextField();
		headerPanel.add(searchField);

		// Create search button
		JButton searchButton = createButton("Search");
		headerPanel.add(searchButton);

		searchButton.addActionListener(e -> {
			// Ensure values are fetched dynamically inside the listeners
			String selectedLevel = (String) levelDropdown.getSelectedItem();
			String searchKey = searchField.getText().trim();
			fetchData(selectedLevel, reportType, searchKey);
		});

		levelDropdown.addActionListener(e -> {
			// Ensure values are fetched dynamically inside the listeners
			String selectedLevel = (String) levelDropdown.getSelectedItem();
			String searchKey = searchField.getText().trim();
			fetchData(selectedLevel, reportType, searchKey);
		});

		// Add button for Questions if needed
		if (reportType.equals("Questions")) {
			JButton addQuestionButton = createButton("Add Question");
			addQuestionButton.addActionListener(e -> formQuestion("Create", 0));
			headerPanel.add(addQuestionButton);
		}

		JButton goBackButton = createButton("Go Back");
		goBackButton.addActionListener(e -> goBack(reportType));
		headerPanel.add(goBackButton);

		add(headerPanel, BorderLayout.NORTH); // Ensures header stays at the top

		// === Content Panel (Wraps Profile Panel) ===
		JPanel contentPanel = new JPanel(new BorderLayout());

		// Profile Panel (Holds User Profiles)
		mainPanel = new JPanel(new GridBagLayout());
		mainPanel.setBackground(new Color(45, 52, 54));

		if (reportType.equals("LeaderBoard")) {
			fetchData("Beginner", reportType, null);
		} else {
			fetchData("All", reportType, null);
		}

		contentPanel.add(mainPanel, BorderLayout.PAGE_START); // Ensures profiles align at the top

		// Scroll Pane for Profile Panel
		JScrollPane scrollPane = new JScrollPane(contentPanel);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);
		scrollPane.setBorder(BorderFactory.createEmptyBorder());

		// Fix the white background issue
		scrollPane.getViewport().setBackground(new Color(45, 52, 54)); // Dark background for empty space
		contentPanel.setBackground(new Color(45, 52, 54)); // Ensure content panel has the same background

		add(scrollPane, BorderLayout.CENTER); // Add scrollPane to the main layout
	}

	private void addHeaderRow(String reportType) {
		JPanel headerRow;
		if (reportType.equals("Questions")) {
			headerRow = createRow(new String[] { "S.No", "Question", "Level", "Option1", "Option2", "Option3",
					"Option4", "Answer", "Edit", "Delete" }, true);
		} else {
			headerRow = createRow(new String[] { "S.No", "FullName", "Level", "Score1", "Score2", "Score3", "Score4",
					"Score5", "OverallScore" }, true);
		}
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 5, 5, 5); // Add consistent spacing
		mainPanel.add(headerRow, gbc);
	}

	private void addRow(String reportType, int rowIndex, int id, String fullName, String level, String score1,
			String score2, String score3, String score4, String score5, String overallScore, String question,
			String option1, String option2, String option3, String option4, String answer) {
		JPanel row;
		if (reportType.equals("Questions")) {
			row = createRow(new String[] { String.valueOf(rowIndex), question, level, option1, option2, option3,
					option4, answer }, false);
			// Add Edit and Delete buttons
			row.add(createStyledButton("Edit", id, true));
			row.add(createStyledButton("Delete", id, false));
		} else {
			row = createRow(new String[] { String.valueOf(rowIndex), fullName, level, score1, score2, score3, score4,
					score5, overallScore }, false);
		}

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = rowIndex;
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(5, 5, 5, 5); // Consistent spacing
		mainPanel.add(row, gbc);
	}

	private JPanel createRow(String[] rowData, boolean isHeader) {
		JPanel rowPanel = new JPanel(new GridLayout(1, rowData.length));
		rowPanel.setBackground(new Color(45, 52, 54));

		for (String cell : rowData) {
			JLabel label = new JLabel(cell, SwingConstants.CENTER);
			label.setFont(new Font("Arial", isHeader ? Font.BOLD : Font.PLAIN, 12));
			label.setForeground(Color.WHITE);
			label.setBorder(BorderFactory.createLineBorder(Color.WHITE));
			label.setPreferredSize(new Dimension(100, 30));
			rowPanel.add(label);
		}
		return rowPanel;
	}

	private JButton createStyledButton(String text, int id, boolean isEdit) {
		JButton button = new JButton(text);
		button.setPreferredSize(new Dimension(80, 30));
		button.setBackground(isEdit ? new Color(0, 184, 148) : new Color(214, 48, 49)); // Edit: Green, Delete: Red
		button.setForeground(Color.WHITE);
		button.setFocusPainted(false);
		button.addActionListener(e -> {
			if (isEdit) {
				formQuestion("Update", id);
			} else {
				formQuestion("Delete", id);
			}
		});
		return button;
	}

	private void fetchData(String level, String reportType, String searchBy) {
		mainPanel.removeAll(); // Clear previous rows
		addHeaderRow(reportType); // Re-add header row

		String query = "";

		boolean hasLevelFilter = !level.equals("All");
		boolean hasSearchFilter = searchBy != null && !searchBy.isEmpty();

		if (reportType.equals("Questions")) {
			query = "SELECT a.* FROM Questions a ";
		} else if (reportType.equals("AllScore")) {
			query = "SELECT a.Id, b.FullName, a.Level, a.Score1, a.Score2, a.Score3, a.Score4, a.Score5 "
					+ "FROM scores a, users b WHERE a.userId=b.Id ";
		}
		// Add filter
		if ((hasLevelFilter || hasSearchFilter)
				&& !(reportType.equals("HighScore") || reportType.equals("LeaderBoard"))) {
			if (reportType.equals("Questions")) {
				query += "WHERE ";
			} else {
				query += "AND ";
			}
			if (hasLevelFilter) {
				query += "a.Level = ? ";
			}
			if (hasSearchFilter) {
				query += (hasLevelFilter ? "AND " : "")
						+ (reportType.equals("Questions") ? "a.Question LIKE ? " : "b.FullName LIKE ? ");
			}
		}

		if (reportType.equals("AllScore")) {
			query += "ORDER BY CASE WHEN Level = 'Beginner' THEN 1 WHEN Level = 'Intermediate' THEN 2 "
					+ "WHEN Level = 'Advanced' THEN 3 ELSE 4 END, a.Id DESC";
		}

		if (reportType.equals("HighScore")) {
			query = "WITH Score_Averages AS ("
					+ "SELECT a.userId, b.FullName, a.Level, a.Score1, a.Score2, a.Score3, a.Score4, a.Score5, "
					+ "(a.Score1 + a.Score2 + a.Score3 + a.Score4 + a.Score5) / 5.0 AS AvgScore "
					+ "FROM scores a JOIN users b ON a.userId = b.Id) "
					+ "SELECT sa.userId, sa.FullName, sa.Level, sa.Score1, sa.Score2, sa.Score3, sa.Score4, sa.Score5, "
					+ "MAX(sa.AvgScore) OVER (PARTITION BY sa.userId, sa.Level) AS MaxAvgScore "
					+ "FROM Score_Averages sa ";

			if (hasLevelFilter || hasSearchFilter) {
				query += "WHERE ";
				if (hasLevelFilter) {
					query += "sa.Level = ? ";
				}
				if (hasSearchFilter) {
					query += (hasLevelFilter ? "AND " : "") + "sa.FullName LIKE ? ";
				}
			}
			query += "ORDER BY sa.FullName ASC, CASE WHEN sa.Level = 'Beginner' THEN 1 "
					+ "WHEN sa.Level = 'Intermediate' THEN 2 " + "WHEN sa.Level = 'Advanced' THEN 3 " + "ELSE 4 END;";
		} else if (reportType.equals("LeaderBoard")) {
			query = "WITH Score_Averages AS ("
					+ "SELECT a.userId, b.FullName, a.Level, a.Score1, a.Score2, a.Score3, a.Score4, a.Score5, "
					+ "(a.Score1 + a.Score2 + a.Score3 + a.Score4 + a.Score5) / 5.0 AS AvgScore "
					+ "FROM scores a JOIN users b ON a.userId = b.Id ";

			if (hasLevelFilter || hasSearchFilter) {
				query += "WHERE ";
				if (hasLevelFilter) {
					query += "a.Level = ? ";
				}
				if (hasSearchFilter) {
					query += (hasLevelFilter ? "AND " : "") + "b.FullName LIKE ? ";
				}
				query += ")";
			}
			query += "SELECT sa.userId, sa.FullName, sa.Level, sa.Score1, sa.Score2, sa.Score3, sa.Score4, sa.Score5, "
					+ "sa.AvgScore FROM Score_Averages sa WHERE sa.AvgScore = ("
					+ "SELECT MAX(AvgScore) FROM Score_Averages sa_inner WHERE sa_inner.userId = sa.userId) "
					+ "ORDER BY sa.AvgScore DESC LIMIT 10;";
		}

		System.out.println(query);
		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
			int paramIndex = 1;
			if (hasLevelFilter) {
				stmt.setString(paramIndex++, level);
			}

			if (hasSearchFilter) {
				stmt.setString(paramIndex++, "%" + searchBy + "%");
			}
			ResultSet rs = stmt.executeQuery();

			int index = 1;
			boolean hasResults = false;
			if (reportType.equals("Questions")) {
				while (rs.next()) {
					hasResults = true;
					int id = rs.getInt("id");
					String question = rs.getString("Question");
					String qLevel = rs.getString("Level");
					String option1 = rs.getString("Option1");
					String option2 = rs.getString("Option2");
					String option3 = rs.getString("Option3");
					String option4 = rs.getString("Option4");
					String answer = rs.getString("Answer");
					addRow(reportType, index++, id, null, qLevel, null, null, null, null, null, null, question, option1,
							option2, option3, option4, answer);
				}
			} else {
				while (rs.next()) {
					hasResults = true;
					String fullName = rs.getString("FullName");
					String quizLevel = rs.getString("Level");
					int score1 = rs.getInt("Score1");
					int score2 = rs.getInt("Score2");
					int score3 = rs.getInt("Score3");
					int score4 = rs.getInt("Score4");
					int score5 = rs.getInt("Score5");
					int overallScore = (score1 + score2 + score3 + score4 + score5) / 5;
					addRow(reportType, index++, 0, fullName, quizLevel, String.valueOf(score1), String.valueOf(score2),
							String.valueOf(score3), String.valueOf(score4), String.valueOf(score5),
							String.valueOf(overallScore), null, null, null, null, null, null);
				}
			}

			if (!hasResults) {
				JOptionPane.showMessageDialog(this, "No data found!", "Info", JOptionPane.INFORMATION_MESSAGE);
			}

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error fetching data from the database!", "Error",
					JOptionPane.ERROR_MESSAGE);
		}

		mainPanel.revalidate();
		mainPanel.repaint();
	}

	private void formQuestion(String action, int id) {
		dispose();
		new QuestionForm(action, id, fullName).setVisible(true);
	}

	private void goBack(String reportType) {
		dispose();
		if (reportType.equals("Questions")) {
			dispose();
			new AdminPanel(fullName).setVisible(true);
		} else if (reportType.equals("LeaderBoard")) {
			dispose();
			new PlayerHome(getPlayerId(fullName), fullName).setVisible(true);
		} else {
			dispose();
			new Reports(fullName).setVisible(true);
		}
	}
}
