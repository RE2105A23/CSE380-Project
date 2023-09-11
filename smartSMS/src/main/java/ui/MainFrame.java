package main.java.ui;

import javax.swing.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("SMS-based Remote Server Monitoring System");
        setSize(800, 600); // Setting the default size of the window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Ensures the application exits when the window is closed
        setLocationRelativeTo(null); // Centers the window on the screen

        // Adding the LoginPanel to the MainFrame
        LoginPanel loginPanel = new LoginPanel();
        add(loginPanel);
    }

    public static void main(String[] args) {
        MainFrame frame = new MainFrame();
        frame.setVisible(true); // This makes the window visible
    }
}
