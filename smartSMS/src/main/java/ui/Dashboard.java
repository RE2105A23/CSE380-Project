package main.java.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Dashboard extends JFrame {
    private JButton viewServerStatusButton;
    private JButton requestServerRestartButton;
    private JButton logoutButton;

    public Dashboard(String role) {
        setTitle("Dashboard");
        setSize(400, 200);
        setLayout(new FlowLayout());

        viewServerStatusButton = new JButton("View Server Status");
        requestServerRestartButton = new JButton("Request Server Restart");
        logoutButton = new JButton("Logout");

        add(viewServerStatusButton);
        add(requestServerRestartButton);
        add(logoutButton);

        // Show/Hide buttons based on role
        if ("normal".equals(role)) {
            requestServerRestartButton.setVisible(false);
        }

        // Add Action Listeners
        viewServerStatusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewServerStatus();
            }
        });

        requestServerRestartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                requestServerRestart();
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });
    }

    private void viewServerStatus() {
        // TODO: Add logic to view server status
        System.out.println("Viewing server status...");
    }

    private void requestServerRestart() {
        // TODO: Add logic to request server restart
        System.out.println("Requesting server restart...");
    }

    private void logout() {
        // TODO: Add logic to logout
        System.out.println("Logging out...");
    }

    public static void main(String[] args) {
        // For testing, you can pass "admin" or "normal" to see the changes
        new Dashboard("admin").setVisible(true);
    }
}
