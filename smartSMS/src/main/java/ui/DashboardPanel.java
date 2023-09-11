package main.java.ui;

import javax.swing.*;

public class DashboardPanel extends JPanel {
    private JLabel welcomeLabel;

    public DashboardPanel() {
        welcomeLabel = new JLabel("Welcome to the SMS-based Remote Server Monitoring System!");
        add(welcomeLabel);

        // Placeholder for server statuses or other components
        JLabel serverStatusLabel = new JLabel("Server statuses will be displayed here.");
        add(serverStatusLabel);
    }
}
