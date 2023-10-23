package main.java.ui;

import main.java.models.Server;
import javax.swing.*;
import java.util.ArrayList;

public class AlertManagement {
    private ArrayList<Server> servers;
    private JComboBox<String> serverDropdown;
    private JComboBox<String> alertTypeDropdown;

    public AlertManagement(ArrayList<Server> servers) {
        if (servers == null) {
            throw new IllegalArgumentException("Servers list cannot be null");
        }
        this.servers = servers;
        this.serverDropdown = new JComboBox<>();
        this.alertTypeDropdown = new JComboBox<>();
        updateDropdowns();
    }


    public void subscribeToAlerts() {
        JPanel panel = createSubscriptionPanel();
        int result = showSubscriptionDialog(panel);

        if (result == JOptionPane.OK_OPTION) {
            handleSubscription();
        }
    }

    public JPanel createSubscriptionPanel() {
        JComboBox<String> serverDropdown = createServerDropdown();
        JComboBox<String> alertTypeDropdown = createAlertTypeDropdown();

        JPanel panel = new JPanel();
        panel.add(new JLabel("Select Server:"));
        panel.add(serverDropdown);
        panel.add(new JLabel("Select Alert Type:"));
        panel.add(alertTypeDropdown);

        return panel;
    }

    public JComboBox<String> createServerDropdown() {
        JComboBox<String> serverDropdown = new JComboBox<>();
        serverDropdown.addItem("All Servers");
        for (Server server : servers) {
            serverDropdown.addItem(server.getName());
        }
        return serverDropdown;
    }

    public JComboBox<String> createAlertTypeDropdown() {
        String[] alertTypes = {"All Alerts", "CPU Usage", "Memory Usage", "Network Latency"};
        JComboBox<String> dropdown = new JComboBox<>(alertTypes);
        return dropdown;
    }

    public int showSubscriptionDialog(JPanel panel) {
        return JOptionPane.showConfirmDialog(null, panel, "Subscribe to Alerts", JOptionPane.OK_CANCEL_OPTION);
    }

    public void handleSubscription() {
        if (serverDropdown == null || alertTypeDropdown == null) {
            // Log an error or show a message to the user
            System.err.println("Dropdowns are not initialized.");
            return;
        }

        String selectedServerName = (String) serverDropdown.getSelectedItem();
        String selectedAlertType = (String) alertTypeDropdown.getSelectedItem();

        if (selectedServerName == null || selectedAlertType == null) {
            // Log an error or show a message to the user
            System.err.println("No selection made in one or both dropdowns.");
            return;
        }

        if ("All Servers".equals(selectedServerName)) {
            if ("All Alerts".equals(selectedAlertType)) {
                subscribeAllServersToAllAlerts();
            } else {
                subscribeAllServers(selectedAlertType);
            }
        } else {
            if ("All Alerts".equals(selectedAlertType)) {
                subscribeSpecificServerToAllAlerts(selectedServerName);
            } else {
                subscribeSpecificServer(selectedServerName, selectedAlertType);
            }
        }
    }

    public void subscribeAllServersToAllAlerts() {
        for (Server server : servers) {
            server.subscribeToAlert("CPU Usage");
            server.subscribeToAlert("Memory Usage");
            server.subscribeToAlert("Network Latency");
        }
    }

    public void subscribeSpecificServerToAllAlerts(String serverName) {
        for (Server server : servers) {
            if (server.getName().equals(serverName)) {
                server.subscribeToAlert("CPU Usage");
                server.subscribeToAlert("Memory Usage");
                server.subscribeToAlert("Network Latency");
                break;
            }
        }
    }

    public void subscribeAllServers(String alertType) {
        for (Server server : servers) {
            server.subscribeToAlert(alertType);
        }
    }

    public void subscribeSpecificServer(String serverName, String alertType) {
        for (Server server : servers) {
            if (server.getName().equals(serverName)) {
                server.subscribeToAlert(alertType);
                break;
            }
        }
    }

    public void updateDropdowns() {
        // Null checks
        if (serverDropdown == null || alertTypeDropdown == null || servers == null) {
            System.err.println("Dropdowns or servers list are not initialized.");
            return;
        }

        // Clear existing items
        serverDropdown.removeAllItems();
        alertTypeDropdown.removeAllItems();

        // Add 'All Servers' and 'All Alerts' options
        serverDropdown.addItem("All Servers");
        alertTypeDropdown.addItem("All Alerts");

        // Populate serverDropdown with server names
        for (Server server : servers) {
            serverDropdown.addItem(server.getName());
        }

        // Populate alertTypeDropdown with alert types
        alertTypeDropdown.addItem("CPU Alert");
        alertTypeDropdown.addItem("Memory Alert");
        // Add more alert types as needed
    }

    public JComboBox<String> getServerDropdown() {
        return this.serverDropdown;
    }

    public JComboBox<String> getAlertTypeDropdown() {
        return this.alertTypeDropdown;
    }

}
