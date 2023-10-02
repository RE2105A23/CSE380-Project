package main.java.ui;

import main.java.models.Server;
import main.java.models.ServerRestarter;
import main.java.utils.FileHandler;
import main.java.utils.SMSHandler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.event.TableModelEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class DashboardPanel extends JPanel implements ServerRestarter {
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

        // Add custom renderer and editor for the "Actions" column
        serverTable.getColumn("Actions").setCellRenderer(new SimpleButtonRenderer());
        serverTable.getColumnModel().getColumn(4).setCellEditor(new SimpleButtonEditor());

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
        //System.out.println("Simulating server monitoring. Metrics updated.");
        // Clear the existing table rows
        tableModel.setRowCount(0);

        for (Server server : servers) {
            server.simulateMonitoring();

            // Create a new JButton for each row
            JButton restartButton = new JButton("Restart");
            restartButton.setActionCommand("Restart_" + server.getName());  // Set action command

            Object[] rowData = {
                    server.getName(),
                    String.format("%.2f", server.getCpuUsage()),
                    String.format("%.2f", server.getMemoryUsage()),
                    String.format("%.2f", server.getNetworkLatency()),
                    restartButton  // Add the button here
            };

            tableModel.addRow(rowData);

            // Log server metrics
            FileHandler.writeLog("server_metrics.txt",
                    String.format("%s: CPU=%.2f, Memory=%.2f, Latency=%.2f",
                            server.getName(),
                            server.getCpuUsage(),
                            server.getMemoryUsage(),
                            server.getNetworkLatency())
            );
        }
        checkServerThresholds();
        // Force the table to refresh
        tableModel.fireTableDataChanged();
    }

    public void restartServer(Server server) {
        System.out.println("Attempting to restart server: " + server.getName());  // Debugging line
        server.restart();
        //System.out.println("Server " + server.getName() + " metrics after calling restart: CPU=" + server.getCpuUsage() + ", Memory=" + server.getMemoryUsage() + ", Latency=" + server.getNetworkLatency());
        // Log the restart action
        FileHandler.writeLog("server_actions.txt", "Restarted " + server.getName());
        // Refresh the table to reflect the changes
        //simulateServerMonitoring();
        //tableModel.fireTableDataChanged();  // Explicitly notify that the table data has changed

        // Find the index of the server in the ArrayList
        int rowIndex = servers.indexOf(server);

        // Update only the specific row in the table model
        tableModel.setValueAt(String.format("%.2f", server.getCpuUsage()), rowIndex, 1);
        tableModel.setValueAt(String.format("%.2f", server.getMemoryUsage()), rowIndex, 2);
        tableModel.setValueAt(String.format("%.2f", server.getNetworkLatency()), rowIndex, 3);
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

    // Simple Button Renderer
    public class SimpleButtonRenderer extends JButton implements TableCellRenderer {
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText("Restart");
            return this;
        }
    }

    // Simple Button Editor
    public class SimpleButtonEditor extends DefaultCellEditor implements ActionListener {
        private JButton button;
        private int row;
        private JTable table;

        public SimpleButtonEditor() {
            super(new JCheckBox());
            button = new JButton("Restart");
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.row = row;
            this.table = table;

            // Remove all existing action listeners
            for (ActionListener al : button.getActionListeners()) {
                button.removeActionListener(al);
            }

            // Add the action listener
            button.addActionListener(this);

            return button;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Restart button clicked for row: " + row);
            Server server = servers.get(row);
            restartServer(server);
            // Stop cell editing to release the button
            fireEditingStopped();
        }
    }

}