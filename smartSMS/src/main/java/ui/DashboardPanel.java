package main.java.ui;

import main.java.models.Server;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

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
        servers.add(new Server("Server1"));
        servers.add(new Server("Server2"));
        servers.add(new Server("Server3"));

        // Table to display server statuses
        String[] columnNames = {"Server Name", "CPU Usage", "Memory Usage", "Network Latency"};
        tableModel = new DefaultTableModel(columnNames, 0);
        serverTable = new JTable(tableModel);
        add(new JScrollPane(serverTable), BorderLayout.CENTER);

        // Simulate server monitoring
        simulateServerMonitoring();
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
        }
        checkServerThresholds();
        // Log server metrics
        FileHandler.writeLog("server_metrics.txt", server.getName() + ": CPU=" + server.getCpuUsage() + ", Memory=" + server.getMemoryUsage() + ", Latency=" + server.getNetworkLatency());
    }

    private void checkServerThresholds() {
        for (Server server : servers) {
            String message = server.checkThresholds();
            if (!message.isEmpty()) {
                // Simulate sending SMS to users
                JOptionPane.showMessageDialog(this, message, "Server Alert", JOptionPane.WARNING_MESSAGE);
            }
        }
        // Log alerts
        if (!message.isEmpty()) {
            FileHandler.writeLog("server_alerts.txt", server.getName() + ": " + message);
        }
    }

}
