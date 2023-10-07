package main.java.ui;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import main.java.models.AbstractUser;
import main.java.models.Admin;
import main.java.models.User;
import main.java.utils.UserDatabase;
import main.java.utils.FileHandler;

public class LoginPanel extends JPanel {
    private JLabel welcomeLabel;
    private JLabel userLabel;
    private JTextField userText;
    private JLabel passwordLabel;
    private JPasswordField passwordText;
    private JButton loginButton;

    public LoginPanel() {
        setLayout(new GridBagLayout());  // Use GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();

        welcomeLabel = new JLabel("Welcome to the Smart-SMS System");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 10, 10);
        add(welcomeLabel, gbc);

        userLabel = new JLabel("Username");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(userLabel, gbc);

        userText = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(userText, gbc);

        passwordLabel = new JLabel("Password");
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(passwordLabel, gbc);

        passwordText = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(passwordText, gbc);

        loginButton = new JButton("Login");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(loginButton, gbc);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });

        // Add key listener to passwordText
        passwordText.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleLogin();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
    }

    private void handleLogin() {
        String username = userText.getText();
        String password = new String(passwordText.getPassword());

        // First, try to authenticate using UserDatabase
        AbstractUser currentUser = UserDatabase.authenticate(username, password);

        // If authentication fails, try to authenticate using users.txt
        if (currentUser == null) {
            //List<AbstractUser> usersFromFile = FileHandler.readUsersFromTextFile("users.txt");
            List<AbstractUser> usersFromFile = FileHandler.readUsersFromCSVFile("users.csv");
            for (AbstractUser user : usersFromFile) {
                if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                    currentUser = user;
                    break;
                }
            }
        }

        // Check if authentication was successful
        if (currentUser != null) {
            JOptionPane.showMessageDialog(this, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);

            // Switch to the dashboard
            JFrame mainFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            mainFrame.getContentPane().removeAll();
            mainFrame.add(new DashboardPanel(currentUser));  // Pass currentUser here
            mainFrame.revalidate();
            mainFrame.repaint();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
