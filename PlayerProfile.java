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
import javax.swing.SwingConstants;

public class PlayerProfile extends Main {

	private static final long serialVersionUID = 1L;
	private JPanel profilePanel;
	private String fullName;

	public PlayerProfile(String fullName) {
		this.fullName = fullName;
	    setLayout(new BorderLayout());

	    JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	    headerPanel.setBackground(new Color(45, 52, 54));

	    String[] role = {"All", "Admin", "Player"};
	    JComboBox<String> roleDropdown = new JComboBox<>(role);
	    roleDropdown.setFont(new Font("Arial", Font.PLAIN, 20));
	    roleDropdown.setPreferredSize(new Dimension(120, 40));

	    DefaultListCellRenderer renderer = new DefaultListCellRenderer();
	    renderer.setHorizontalAlignment(SwingConstants.CENTER); // Center text
	    roleDropdown.setRenderer(renderer);
	    headerPanel.add(roleDropdown);

	    roleDropdown.addActionListener(e -> {
	        String selectedRole = (String) roleDropdown.getSelectedItem();
	        fetchUserProfiles(selectedRole); // Fetch filtered profiles
	    });

	    JButton goBackButton = createButton("Go Back");
	    goBackButton.addActionListener(e -> goBack());
	    headerPanel.add(goBackButton);

	    add(headerPanel, BorderLayout.NORTH); // Ensures header stays at the top

	    // === Content Panel (Wraps Profile Panel) ===
	    JPanel contentPanel = new JPanel(new BorderLayout());

	    // Profile Panel (Holds User Profiles)
	    profilePanel = new JPanel(new GridBagLayout());
	    profilePanel.setBackground(new Color(45, 52, 54));

	    fetchUserProfiles("All"); // Fetch profiles and populate rows

	    contentPanel.add(profilePanel, BorderLayout.PAGE_START); // Ensures profiles align at the top

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

    private void addHeaderRow() {
        JPanel headerRow = createRow(new String[]{"S.No", "FullName", "Email", "Country", "Role", "Edit", "Delete"}, true);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5); // Add consistent spacing
        profilePanel.add(headerRow, gbc);
    }

    private void addProfileRow(int rowIndex, int id, String fullName, String email, String country, String role) {
        JPanel row = createRow(new String[]{
                String.valueOf(rowIndex), fullName, email, country, role}, false);

        // Add Edit and Delete buttons
        row.add(createStyledButton("Edit", id, true));
        row.add(createStyledButton("Delete", id, false));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = rowIndex;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5); // Consistent spacing
        profilePanel.add(row, gbc);
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
        button.setPreferredSize(new Dimension(100, 30));
        button.setBackground(isEdit ? new Color(0, 184, 148) : new Color(214, 48, 49)); // Edit: Green, Delete: Red
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.addActionListener(e -> {
            if (isEdit) {
                formProfile("Update", id);
            } else {
                formProfile("Delete", id);
            }
        });
        return button;
    }


	private void fetchUserProfiles(String role) {
	    profilePanel.removeAll(); // Clear previous rows
	    addHeaderRow(); // Re-add header row

	    String query = "SELECT * FROM users ";

	    if (!role.equals("All")) {
	        query += "WHERE Role = ? ORDER BY Id DESC";
	    }
	    else {
	    	query += "ORDER BY CASE WHEN Role = 'Admin' THEN 1 WHEN Role = 'Player' THEN 2 ELSE 3 END, Id DESC";
	    }

	    try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

	        if (!role.equals("All")) {
	            stmt.setString(1, role);
	        }

	        ResultSet rs = stmt.executeQuery();

	        int index = 1;
	        boolean hasResults = false;
	        while (rs.next()) {
	            hasResults = true;
	            int id = rs.getInt("id");
	            String fullName = rs.getString("FullName");
	            String email = rs.getString("Email");
	            String country = rs.getString("country");
	            String userRole = rs.getString("Role");

	            addProfileRow(index++, id, fullName, email, country, userRole);
	        }

	        if (!hasResults) {
	            JOptionPane.showMessageDialog(this, "No profiles found!", "Info", JOptionPane.INFORMATION_MESSAGE);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	        JOptionPane.showMessageDialog(this, "Error fetching questions from the database!", "Error",
	                JOptionPane.ERROR_MESSAGE);
	    }

	    profilePanel.revalidate();
	    profilePanel.repaint();
	}

	
	private void formProfile(String action, int id) {
    	dispose();
    	new LoginSignUp(action, id).setVisible(true);
    }
    
    private void goBack() {
    	dispose();
        new AdminPanel(fullName).setVisible(true);
    }
}
