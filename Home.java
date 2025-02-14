package playerquiz;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class Home extends Main {

    private static final long serialVersionUID = 1L;

    public Home() {
        super();
        initializeUI();
    }

    private void initializeUI() {
        contentPane.setLayout(new BorderLayout());
        contentPane.setBackground(new Color(30, 30, 30)); // Slightly modern dark theme

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(45, 45, 45));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel welcomeLabel = new JLabel("Welcome to the Quiz zone");
        welcomeLabel.setForeground(new Color(255, 215, 0)); // Gold for a premium look
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 28)); // Elegant, slightly smaller font
        titlePanel.add(welcomeLabel);
        contentPane.add(titlePanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1, 8, 8)); // Balanced spacing
        buttonPanel.setBackground(new Color(30, 30, 30));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 35, 15, 35)); // Compact margins
        
        JButton loginBtn = createButton("Login");
        loginBtn.addActionListener(new ButtonAction());
        loginBtn.setBackground(new Color(50, 205, 50)); // Soft green
        loginBtn.setForeground(Color.BLACK);
        loginBtn.setFont(new Font("Arial", Font.BOLD, 15)); // Elegant font size
        loginBtn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 215, 0), 2),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        addEnterKeyListener(loginBtn);
        buttonPanel.add(loginBtn);
        
        JButton signUpBtn = createButton("SignUp");
        signUpBtn.addActionListener(new ButtonAction());
        signUpBtn.setBackground(new Color(70, 130, 180)); // Steel blue
        signUpBtn.setForeground(Color.BLACK);
        signUpBtn.setFont(new Font("Arial", Font.BOLD, 15));
        signUpBtn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 215, 0), 2),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        buttonPanel.add(signUpBtn);
        
        contentPane.add(buttonPanel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            Home home = new Home();
            home.setVisible(true);
        });
    }
    
    private class ButtonAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String button = e.getActionCommand();
            
            if (button.equals("Login") || button.equals("SignUp")) {
                dispose();
                new LoginSignUp(button, 0).setVisible(true);
            } else if(button.equals("Go Back")) {
                dispose();
                new Home().setVisible(true);
            }
        }
    }
}