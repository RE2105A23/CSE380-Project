package main.java.ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

        // For now, we'll use a simple check. Later, this can be replaced with actual user/admin checks.
        if ("admin".equals(username) && "pass".equals(password)) {
            JOptionPane.showMessageDialog(this, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);

            // Switch to the dashboard
            JFrame mainFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            mainFrame.getContentPane().removeAll();
            mainFrame.add(new DashboardPanel());
            mainFrame.revalidate();
            mainFrame.repaint();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
