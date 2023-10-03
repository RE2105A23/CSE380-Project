package main.java.ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import main.java.models.AbstractUser;
import main.java.models.Admin;
import main.java.models.User;
import main.java.utils.UserDatabase;

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

        // Inside handleLogin() method
        AbstractUser currentUser = UserDatabase.authenticate(username, password);
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
