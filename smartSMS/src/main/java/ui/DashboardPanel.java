package main.java.ui;

import main.java.models.Server;
import main.java.models.ServerRestarter;
import main.java.utils.FileHandler;
import main.java.utils.SMSHandler;
import main.java.models.AbstractUser;
import main.java.ui.ManageServers;

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
    private AbstractUser currentUser;
    //private String role = currentUser.getRole();  // Initialize role from currentUser
    private JTextField nameField;  // Assuming you have a JTextField for the name
    private JTextField cpuLimitField;  // And so on for other fields
    private JTextField memoryLimitField;
    private JTextField networkLimitField;


    public DashboardPanel(AbstractUser currentUser) {
        this.currentUser = currentUser;
        System.out.println("Current User Role: " + currentUser.getRole());
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

        if (currentUser != null) {
            if ("admin".equals(currentUser.getRole())) {
                initializeAdminDashboard();
            } else {
                initializeUserDashboard();
            }
        } else {
            System.out.println("Error: currentUser is null");
        }
        // Initialize button for managing servers
        JButton manageServersButton = new JButton("Manage Servers");
        manageServersButton.addActionListener(e -> openManageServersPanel());

        /*
        if ("admin".equals(this.role)) {
            JButton adminButton = new JButton("Admin Action");
            adminButton.addActionListener(e -> {
                // Implement your admin-specific logic here
                System.out.println("Admin button clicked");
            });
            add(adminButton, BorderLayout.SOUTH);
        }
        */
    }

    private void initializeAdminDashboard() {
        System.out.println("Initializing Admin Dashboard");  // Debugging line
        JPanel adminPanel = new JPanel(new FlowLayout());  // Create a new JPanel with FlowLayout
        JButton manageServersButton = new JButton("Manage Servers");

        manageServersButton.addActionListener(e -> openManageServersPanel());
        /*
        manageServersButton.addActionListener(e -> {
            // Open server management panel
        });
        */

        JButton manageUsersButton = new JButton("Manage Users");
        manageUsersButton.addActionListener(e -> {
            // Open user management panel
        });

        JButton setThresholdsButton = new JButton("Set Thresholds");
        setThresholdsButton.addActionListener(e -> {
            // Open threshold settings panel
        });

        adminPanel.add(manageServersButton);  // Add the button to the JPanel
        adminPanel.add(manageUsersButton);    // Add the button to the JPanel
        adminPanel.add(setThresholdsButton);  // Add the button to the JPanel

        add(adminPanel, BorderLayout.SOUTH);  // Add the JPanel to the SOUTH region
    }

    private void initializeUserDashboard() {
        System.out.println("Initializing User Dashboard");  // Debugging line
        JPanel userPanel = new JPanel(new FlowLayout());  // Create a new JPanel with FlowLayout
        JButton requestRestartButton = new JButton("Request Server Restart");
        requestRestartButton.addActionListener(e -> {
            // Send request to admin
        });

        JButton subscribeAlertsButton = new JButton("Subscribe to Alerts");
        subscribeAlertsButton.addActionListener(e -> {
            // Subscribe to server alerts
        });

        userPanel.add(requestRestartButton);    // Add the button to the JPanel
        userPanel.add(subscribeAlertsButton);  // Add the button to the JPanel

        add(userPanel, BorderLayout.SOUTH);  // Add the JPanel to the SOUTH region
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

    public void addServer() {
        String newName = nameField.getText();
        int newCpuLimit = Integer.parseInt(cpuLimitField.getText());
        int newMemoryLimit = Integer.parseInt(memoryLimitField.getText());
        int newNetworkLimit = Integer.parseInt(networkLimitField.getText());

        Server newServer = new Server(newName, newCpuLimit, newMemoryLimit, newNetworkLimit);
        servers.add(newServer);
        Object[] rowData = {newName, newCpuLimit, newMemoryLimit, newNetworkLimit};
        tableModel.addRow(rowData);
        tableModel.fireTableDataChanged(); // Refresh table
    }

    public void editServer(int selectedRow) {
        if (selectedRow >= 0 && selectedRow < servers.size()) {
            String newName = nameField.getText();
            int newCpuLimit = Integer.parseInt(cpuLimitField.getText());
            int newMemoryLimit = Integer.parseInt(memoryLimitField.getText());
            int newNetworkLimit = Integer.parseInt(networkLimitField.getText());

            Server server = servers.get(selectedRow);
            server.setName(newName);
            // Update other server details here

            tableModel.setValueAt(newName, selectedRow, 0); // Assuming name is in column 0
            tableModel.fireTableDataChanged(); // Refresh table
        } else {
            JOptionPane.showMessageDialog(null, "Invalid row selected for editing.");
        }
    }

    public void removeServer(int selectedRow) {
        if (selectedRow >= 0 && selectedRow < servers.size()) {
            servers.remove(selectedRow);
            tableModel.removeRow(selectedRow);
            tableModel.fireTableDataChanged(); // Refresh table
        } else {
            JOptionPane.showMessageDialog(null, "Invalid row selected for removal.");
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
    private void openManageServersPanel() {
        JFrame manageServersFrame = new JFrame("Manage Servers");
        ManageServers manageServersPanel = new ManageServers(this.servers); // Pass the servers list
        manageServersFrame.add(manageServersPanel);
        manageServersFrame.setSize(1000, 400);
        manageServersFrame.setVisible(true);
        manageServersFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}