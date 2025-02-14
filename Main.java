package playerquiz;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class Main extends JFrame {

    private static final long serialVersionUID = 1L;
    protected JPanel contentPane;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Main frame = new Main();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Main() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setBounds(100, 100, 1372, 806);
        contentPane = new JPanel();	
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setBackground(new Color(60, 63, 65)); // Dark Gray
        setContentPane(contentPane);
        contentPane.setLayout(null);
    }

    protected JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setBackground(new Color(142, 68, 173)); // Purple

        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(220, 40));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setActionCommand(text);

        // Adding mouse listener for hover effects and press effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(255, 140, 0)); // Dark Orange when hovered
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(30, 144, 255)); // Dodger Blue as original color
            }

            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(220, 20, 60)); // Crimson when clicked
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(255, 140, 0)); // Dark Orange again on release
            }
        });

        return button;
    }

    protected JTextField createTextField() {
        JTextField textField = new JTextField(20);
        textField.setPreferredSize(new Dimension(220, 40));
        textField.setFont(new Font("Arial", Font.PLAIN, 16));
        textField.setBackground(Color.gray);
        return textField;
    }

    protected JPasswordField createPasswordField() {
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setPreferredSize(new Dimension(220, 40));
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16));
        passwordField.setBackground(Color.gray);
        return passwordField;
    }
    
    public static void addEnterKeyListener(JButton button) {
        button.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    button.doClick(); // Simulate button click when Enter is pressed
                }
            }
        });
    }
    
    protected String getPlayerName(int userId) {
    	String playerName = "";
        try {
        	Connection conn = DBConnection.getConnection();
            String query = "SELECT FullName FROM users WHERE Id=?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                playerName = rs.getString("FullName");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return playerName;
    }
    
    protected int getPlayerId(String fullName) {
    	int userId = 0;
        try {
        	Connection conn = DBConnection.getConnection();
            String query = "SELECT Id FROM users WHERE FullName=?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, fullName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                userId = rs.getInt("Id");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userId;
    }
}