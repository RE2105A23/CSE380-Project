package main.java.ui;

import main.java.models.Server;
import main.java.utils.FileHandler;
import main.java.utils.SMSHandler;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
//import javax.swing.Timer;


public class DashboardPanel extends JPanel {
    private JLabel welcomeLabel;
    private JTable serverTable;
    private DefaultTableModel tableModel;
    private ArrayList<Server> servers;

    public DashboardPanel() {
        setLayout(new BorderLayout());

        // Welcome Label
        welcomeLabel = new JLabel("Welcome to the SMS-based Remote Server Monitoring System!");
        add(welcomeLabel, BorderLayout.NORTH);

        // Initialize Server List
        servers = new ArrayList<>();
        servers.add(new Server("Server1", 20, 50, 100));
        servers.add(new Server("Server2", 20, 50, 100));
        servers.add(new Server("Server3", 20, 50, 100));

        // Table to display server statuses
        String[] columnNames = {"Server Name", "CPU Usage", "Memory Usage", "Network Latency", "Actions"};
        tableModel = new DefaultTableModel(columnNames, 0);
        serverTable = new JTable(tableModel);
        add(new JScrollPane(serverTable), BorderLayout.CENTER);

        // Simulate server monitoring
        simulateServerMonitoring();

        // Set up a timer to refresh server metrics every 5 seconds
        Timer timer = new Timer(5000, e -> {
            // Clear the existing table rows
            tableModel.setRowCount(0);

            // Update server metrics and check for alerts
            simulateServerMonitoring();
        });
        timer.start();
    }

    private void simulateServerMonitoring() {
        for (Server server : servers) {
            server.simulateMonitoring();
            Object[] rowData = {
                    server.getName(),
                    server.getCpuUsage(),
                    server.getMemoryUsage(),
                    server.getNetworkLatency()
            };
            tableModel.addRow(rowData);

            // Log server metrics
            FileHandler.writeLog("server_metrics.txt", server.getName() + ": CPU=" + server.getCpuUsage() + ", Memory=" + server.getMemoryUsage() + ", Latency=" + server.getNetworkLatency());

            JButton restartButton = new JButton("Restart");
            restartButton.addActionListener(e -> restartServer(server));
            tableModel.addRow(new Object[]{server.getName(), server.getCpuUsage(), server.getMemoryUsage(), server.getNetworkLatency(), restartButton});
        }
        checkServerThresholds();
    }

    private void restartServer(Server server) {
        server.restart();  // We'll implement this method in the Server class next
        // Log the restart action
        FileHandler.writeLog("server_actions.txt", "Restarted " + server.getName());
    }

    private void checkServerThresholds() {
        for (Server server : servers) {
            String message = server.checkThresholds();
            if (!message.isEmpty()) {
                // Simulate sending SMS to users
                SMSHandler.sendSMS("123-456-7890", message);
                // Log the alert
                FileHandler.writeLog("server_alerts.txt", server.getName() + ": " + message);
            }
        }
    }
}
