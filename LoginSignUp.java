package playerquiz;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;

public class LoginSignUp extends Main {

    private static final long serialVersionUID = 1L;
    private JTextField emailField;
    private JTextField fullNameField;
    private JPasswordField passwordField;
    private JTextField countryField;
    private JComboBox<String> comboBox;
    private JLabel messageLabel;

    private int id;
    private String action;
    private String email;
    private String fullName;
    private String password;
    private String country;
    private String role;

    public LoginSignUp(String action, int id) {
        this.id = id;
        this.action = action;
        contentPane.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 20, 15, 20); // Increased padding for better spacing
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title Label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Span across two columns
        gbc.anchor = GridBagConstraints.CENTER;
        JLabel welcomeLabel = new JLabel(action);
        welcomeLabel.setForeground(new Color(255, 255, 255));
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 30));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(welcomeLabel, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_END;

        // Full Name Label
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel fullNameLabel = new JLabel("Full Name:");
        fullNameLabel.setForeground(new Color(255, 255, 255));
        fullNameLabel.setFont(new Font("Arial", Font.BOLD, 20));
        fullNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        contentPane.add(fullNameLabel, gbc);

        // Full Name Field
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        fullNameField = createTextField();
        fullNameField.setPreferredSize(new Dimension(250, 40));
        fullNameField.setFont(new Font("Arial", Font.PLAIN, 18));
        fullNameField.setBackground(new Color(45, 45, 45));
        fullNameField.setForeground(new Color(255, 255, 255));
        contentPane.add(fullNameField, gbc);

        if (!action.equals("Login")) {
            // Email Label
            gbc.gridy++;
            gbc.gridx = 0;
            JLabel emailLabel = new JLabel("Email:");
            emailLabel.setForeground(new Color(255, 255, 255));
            emailLabel.setFont(new Font("Arial", Font.BOLD, 20));
            emailLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            contentPane.add(emailLabel, gbc);

            // Email Field
            gbc.gridx = 1;
            gbc.anchor = GridBagConstraints.LINE_START;
            emailField = createTextField();
            emailField.setPreferredSize(new Dimension(250, 40)); // Increased height
            emailField.setFont(new Font("Arial", Font.PLAIN, 18));
            emailField.setBackground(new Color(45, 45, 45));
            emailField.setForeground(new Color(255, 255, 255));
            contentPane.add(emailField, gbc);
        }

        if (action.equals("Login") || action.equals("SignUp")) {
            // Password Label
            gbc.gridy++;
            gbc.gridx = 0;
            JLabel passwordLabel = new JLabel("Password:");
            passwordLabel.setForeground(new Color(255, 255, 255));
            passwordLabel.setFont(new Font("Arial", Font.BOLD, 20));
            passwordLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            contentPane.add(passwordLabel, gbc);

            // Password Field
            gbc.gridx = 1;
            gbc.anchor = GridBagConstraints.LINE_START;
            passwordField = createPasswordField();
            passwordField.setPreferredSize(new Dimension(250, 40));
            passwordField.setFont(new Font("Arial", Font.PLAIN, 18));
            passwordField.setBackground(new Color(45, 45, 45));
            passwordField.setForeground(new Color(255, 255, 255));
            contentPane.add(passwordField, gbc);
        }

        if (!action.equals("Login")) {
            // Country Label
            gbc.gridy++;
            gbc.gridx = 0;
            JLabel countryLabel = new JLabel("Country:");
            countryLabel.setForeground(new Color(255, 255, 255));
            countryLabel.setFont(new Font("Arial", Font.BOLD, 20));
            countryLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            contentPane.add(countryLabel, gbc);

			// Email Field
			gbc.gridx = 1;
			gbc.anchor = GridBagConstraints.LINE_START;
			countryField = createTextField();
			countryField.setPreferredSize(new Dimension(250, 40)); // Increased height
			countryField.setFont(new Font("Arial", Font.PLAIN, 18));
			contentPane.add(countryField, gbc);
		}

		if (action.equals("Update") || action.equals("Delete")) {
			gbc.gridy++;
			gbc.gridx = 0;
			JLabel roleLabel = new JLabel("Role:");
			roleLabel.setForeground(Color.WHITE);
			roleLabel.setFont(new Font("Arial", Font.BOLD, 20));
			roleLabel.setHorizontalAlignment(SwingConstants.RIGHT);
			contentPane.add(roleLabel, gbc);

			gbc.gridx = 1;
			gbc.anchor = GridBagConstraints.LINE_START;
			String[] roles = { "All", "Admin", "Player" };
			comboBox = new JComboBox<>(roles);
			comboBox.setPreferredSize(new Dimension(250, 40));
			comboBox.setBounds(171, 70, 140, 22);
			comboBox.setFont(new Font("Arial", Font.PLAIN, 18));
			contentPane.add(comboBox, gbc);
		}
		
		if (action.equals("Update Profile")) {
			gbc.gridx = 0;
			gbc.gridy++;
			gbc.gridwidth = 2;
			gbc.anchor = GridBagConstraints.CENTER;
			messageLabel = new JLabel("");
			messageLabel.setForeground(Color.WHITE);
			messageLabel.setFont(new Font("Arial", Font.BOLD, 20));
			messageLabel.setHorizontalAlignment(SwingConstants.RIGHT);
			contentPane.add(messageLabel, gbc);
			
			String stats = fetchPlayerStats(id);
			messageLabel.setText("<html><div style='text-align:center;'>" + stats.replace("\n", "<br>") + "</div></html>");
		}

		// Action Button
		gbc.gridx = 0;
		gbc.gridy++;
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

		if (action.equals("Update") || action.equals("Delete") || action.equals("Update Profile")) {
			loadProfileData(id, action);
		}

		if (action.equals("Delete")) {
			disableFields();
		}
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	private class ButtonAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String button = e.getActionCommand();
			if (button.equals("Login") || button.equals("SignUp")) {
				// Access the encapsulated fields via getters
				setFullName(fullNameField.getText().trim());
				setPassword(new String(passwordField.getPassword()).trim());
				String enteredPassword = getPassword();
				String enteredFullName = getFullName();

				if (button.equals("Login")) {
					if (enteredFullName.isEmpty() || enteredPassword.isEmpty()) {
						JOptionPane.showMessageDialog(contentPane, "Full Name and Password are required!", "Error",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					if (usersAuthenticateUser(enteredFullName, enteredPassword)) {
						dispose();
						if (checkUserRole(enteredFullName).equals("Admin")) {
							new AdminPanel(enteredFullName).setVisible(true);
						} else {
							openQuiz(enteredFullName);
						}
					} else {
						JOptionPane.showMessageDialog(contentPane, "Invalid username or password!", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				} else {
					setEmail(emailField.getText().trim());
					String enteredEmail = getEmail();
					setCountry(countryField.getText().trim());
					String enteredCountry = getCountry();

					// Validation Checks
					if (enteredEmail.isEmpty() || enteredPassword.isEmpty() || enteredFullName.isEmpty()
							|| enteredCountry.isEmpty()) {
						JOptionPane.showMessageDialog(contentPane, "All fields are required!", "Error",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					if (doesFullNameOrEmailExist("Name", enteredFullName, "Create")) {
						JOptionPane.showMessageDialog(contentPane, "Full Name already exists! Please choose another.",
								"Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
					if (!isValidEmail(enteredEmail)) {
						JOptionPane.showMessageDialog(contentPane, "Invalid email format!", "Error",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					if (doesFullNameOrEmailExist("Email", enteredEmail, "Create")) {
						JOptionPane.showMessageDialog(contentPane, "Email already exists! Please choose another.",
								"Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
					

					// Add user via encapsulated fields
					addUser(enteredEmail, enteredPassword, enteredFullName, enteredCountry);
					JOptionPane.showMessageDialog(contentPane, "User Added Successfully!", "Success",
							JOptionPane.INFORMATION_MESSAGE);
					dispose();
					new Home().setVisible(true);
				}
			} else if (button.equals("Update") || button.equals("Update Profile")) {
				editProfile(id, action);
			} else if (button.equals("Delete")) {
				deleteProfile(id);
			} else {
				dispose();
				if (action.equals("Update") || action.equals("Delete")) {
					new PlayerProfile(fullName).setVisible(true);
				} else if (action.equals("Update Profile")) {
					setFullName(fullNameField.getText().trim());
					String enteredFullName = getFullName();
					new PlayerHome(id, enteredFullName).setVisible(true);
				} else {
					new Home().setVisible(true);
				}
			}
		}
	}

	private boolean isValidEmail(String email) {
		String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
		return email.matches(emailRegex);
	}

	// Password validation: Minimum 8 characters, at least one UpperCase, one
	// LowerCase, and one digit
	private boolean isValidPassword(String password) {
		String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$";
		return password.matches(passwordRegex);
	}

	// Check if the Full Name already exists in the database
	private boolean doesFullNameOrEmailExist(String type, String fullName, String action) {
		String query = "SELECT COUNT(*) FROM users";
		if (type.equals("Name")) {
			query += " WHERE FullName = ?";
		} else {
			query += " WHERE Email = ?";
		}
		if (action.equals("Edit")) {
			query += " AND Id !=" + id;
		}
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {

			preparedStatement.setString(1, fullName);
			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				return resultSet.getInt(1) > 0; // If count > 0, the user exists
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(contentPane, "Database error. Please try again later.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
		return false;
	}

	private String checkUserRole(String fullName) {
		String role = "";
		String query = "SELECT Role FROM users WHERE FullName = ?";
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(query)) {

			preparedStatement.setString(1, fullName);
			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				role = resultSet.getString("Role"); // If count > 0, the user exists
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(contentPane, "Database error. Please try again later.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
		return role;
	}

	private void openQuiz(String enteredFullName) {
		String query = "SELECT Id FROM users WHERE FullName=?";
		int userId = 0;
		try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
			pstmt.setString(1, enteredFullName);
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
		new PlayerHome(userId, enteredFullName).setVisible(true);
	}

	private String hashPassword(String password) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hashBytes = digest.digest(password.getBytes());
			StringBuilder hexString = new StringBuilder();
			for (byte b : hashBytes) {
				hexString.append(String.format("%02x", b)); // Convert to hex
			}
			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Hashing error: " + e.getMessage());
		}
	}

	private boolean usersAuthenticateUser(String fullname, String password) {
		String hashedPassword = hashPassword(password);
		String query = "SELECT * FROM users WHERE FullName=? AND Password=?";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {

			pstmt.setString(1, fullname);
			pstmt.setString(2, hashedPassword);

			try (ResultSet rs = pstmt.executeQuery()) {
				return rs.next();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(contentPane, "Database Error!", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}

	private void addUser(String email, String password, String fullname, String country) {
		String query = "INSERT INTO users(FullName, Email, Password, Country, Role) values(?,?,?,?,?)";
		String hashedPassword = hashPassword(password);
		try (Connection conn = DBConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {

			pstmt.setString(1, fullname);
			pstmt.setString(2, email);
			pstmt.setString(3, hashedPassword);
			pstmt.setString(4, country);
			pstmt.setString(5, "Player");

			pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(contentPane, "Database Error!", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void loadProfileData(int id, String action) {
		try (Connection conn = DBConnection.getConnection();
				PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE Id = ?")) {

			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				fullNameField.setText(rs.getString("FullName"));
				emailField.setText(rs.getString("Email"));
				countryField.setText(rs.getString("Country"));
				if (!action.equals("Update Profile")) {					
					comboBox.setSelectedItem(rs.getString("Role"));
				}
			} else {
				JOptionPane.showMessageDialog(this, "No user profile found!", "Error", JOptionPane.ERROR_MESSAGE);
				dispose();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error loading user profile!", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public String fetchPlayerStats(int userId) {
        String query = "SELECT level, COUNT(*) AS games_played, AVG((a.score1 + a.score2 + a.score3 + a.score4 + a.score5) / 5) AS overall_score " +
                       "FROM scores a, users b WHERE a.userId=b.Id and a.userId=? GROUP BY a.level";

        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            String statsText = "Games Played Per Level:\n";
            while (rs.next()) {
                String level = rs.getString("level");
                int gamesPlayed = rs.getInt("games_played");
                int score = rs.getInt("overall_score");

                statsText += " Level: " + level + ", Games Played: " + gamesPlayed + ", Overall Score: " + score + "\n";
            }
            return statsText;

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

	private void disableFields() {
		fullNameField.setEnabled(false);
		emailField.setEnabled(false);
		countryField.setEnabled(false);
		comboBox.setEnabled(false);
	}

	private void editProfile(int id, String action) {
		String fullName = fullNameField.getText().trim();
		String email = emailField.getText().trim();
		String country = countryField.getText().trim();
		String role = "";
		if (action.equals("Update")) {
			role = (String) comboBox.getSelectedItem();
		}

		// Validate required fields
		if (fullName.isEmpty() || (role.equals("All") && action.equals("Update")) || email.isEmpty() || country.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Please fill in all fields!", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (doesFullNameOrEmailExist("Name", fullName, "Edit")) {
			JOptionPane.showMessageDialog(contentPane, "Full Name already exists! Please choose another.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (!isValidEmail(email)) {
			JOptionPane.showMessageDialog(contentPane, "Invalid email format!", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if (doesFullNameOrEmailExist("Email", email, "Edit")) {
			JOptionPane.showMessageDialog(contentPane, "Email already exists! Please choose another.", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		String query = "UPDATE users SET FullName=?, Email=?, Country=?";
		if (action.equals("Update")) {
			query += ", Role=?";
		}
		query += " WHERE Id=?";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
			int paramIndex = 1;
			stmt.setString(paramIndex++, fullName);
			stmt.setString(paramIndex++, email);
			stmt.setString(paramIndex++, country);
			if (action.equals("Update")) {
				stmt.setString(paramIndex++, role);
			}
			stmt.setInt(paramIndex++, id);

			stmt.executeUpdate();
			JOptionPane.showMessageDialog(this, fullName + " User Profile updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
			dispose();
			if (action.equals("Update Profile")) {
				new PlayerHome(getPlayerId(fullName), fullName).setVisible(true);
			}
			else {				
				new PlayerProfile("Admin").setVisible(true);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error updating user profile for" + fullName + "!", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void deleteProfile(int id) {
		int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this user profile?",
				"Confirm", JOptionPane.YES_NO_OPTION);
		if (choice == JOptionPane.YES_OPTION) {
			try (Connection conn = DBConnection.getConnection();
					PreparedStatement stmt = conn.prepareStatement("DELETE FROM users WHERE Id = ?")) {

				stmt.setInt(1, id);
				stmt.executeUpdate();
				JOptionPane.showMessageDialog(this, "User profile deleted successfully!", "Success",
						JOptionPane.INFORMATION_MESSAGE);
				dispose();
				new PlayerProfile("Admin").setVisible(true);
			} catch (SQLException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(this, "Error deleting user profile!", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

}
