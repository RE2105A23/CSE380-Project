package main.java.ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import main.java.models.AbstractUser;
import main.java.models.Admin;
import main.java.models.User;
import main.java.utils.UserDatabase;
import main.java.utils.FileHandler;

public class LoginPanel extends JPanel {
    private JLabel userLabel;
    private JTextField userText;
    private JLabel passwordLabel;
    private JPasswordField passwordText;
    private JButton loginButton;

    public LoginPanel() {
        userLabel = new JLabel("Username");
        userText = new JTextField(20); // 20 columns wide

        passwordLabel = new JLabel("Password");
        passwordText = new JPasswordField(20);

        loginButton = new JButton("Login");

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });

        // Adding components to the panel
        add(userLabel);
        add(userText);
        add(passwordLabel);
        add(passwordText);
        add(loginButton);
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
