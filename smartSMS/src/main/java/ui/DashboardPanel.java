package main.java.ui;

import com.sun.org.apache.bcel.internal.classfile.ModuleMainClass;
import main.java.main.Main;
import main.java.models.Server;
import main.java.models.ServerRestarter;
import main.java.utils.FileHandler;
import main.java.utils.SMSHandler;
import main.java.models.AbstractUser;
import java.util.ArrayList;
import main.java.models.User;  // Import User model
import main.java.utils.UserDatabase;  // Import UserDatabase
import main.java.ui.ManageServers;
import main.java.utils.FileHandler;
import main.java.models.AbstractUser;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.event.TableModelEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.io.IOException;


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
    //private ArrayList<User> users;  // Add this field for User list
    private List<AbstractUser> users;  // Change this field to List<AbstractUser>

    private GridBagConstraints gbc = new GridBagConstraints();  // Declare it here

    public DashboardPanel(AbstractUser currentUser) {
        setLayout(new GridBagLayout());  // Use GridBagLayout
        //GridBagConstraints gbc = new GridBagConstraints();

        this.currentUser = currentUser;
        System.out.println("Current User Role: " + currentUser.getRole());

        // Welcome Label
        welcomeLabel = new JLabel("Welcome to the SMS-based Remote Server Monitoring System!");
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 0.1;
//        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 10, 10, 10);
        add(welcomeLabel, gbc);

        // Initialize Server List
        servers = new ArrayList<>();
        servers.add(new Server("Server1", 20, 50, 100));
        servers.add(new Server("Server2", 20, 50, 100));
        servers.add(new Server("Server3", 20, 50, 100));

        // Initialize User List from UserDatabase
        this.users = UserDatabase.users;  // Directly reference the users list from UserDatabase

        // Table to display server statuses
        String[] columnNames;
        if ("admin".equals(currentUser.getRole())) {
            columnNames = new String[]{"Server Name", "CPU Usage", "Memory Usage", "Network Latency", "Actions"};
        } else {
            columnNames = new String[]{"Server Name", "CPU Usage", "Memory Usage", "Network Latency"};
        }
        tableModel = new DefaultTableModel(columnNames, 0);
        serverTable = new JTable(tableModel);

        if ("admin".equals(currentUser.getRole())) {
            try {
                // Add custom renderer and editor for the "Actions" column only for admin
                serverTable.getColumn("Actions").setCellRenderer(new SimpleButtonRenderer());
                serverTable.getColumnModel().getColumn(4).setCellEditor(new SimpleButtonEditor());
            } catch (IllegalArgumentException e) {
                // Log the exception
                System.err.println("Column not found: " + e.getMessage());
            }
        }
        //add(new JScrollPane(serverTable), gbc);

        // Add custom renderer and editor for the "Actions" column
        //serverTable.getColumn("Actions").setCellRenderer(new SimpleButtonRenderer());
        //serverTable.getColumnModel().getColumn(4).setCellEditor(new SimpleButtonEditor());

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 0.9;
        gbc.fill = GridBagConstraints.BOTH;
        add(new JScrollPane(serverTable), gbc);

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
                initializeAdminDashboard(gbc);  // Pass gbc
            } else {
                initializeUserDashboard(gbc);  // Pass gbc
            }
        } else {
            System.out.println("Error: currentUser is null");
        }

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBounds(50, 50, 100, 30);  // Example coordinates
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Close the current window
                JFrame dashboardFrame = (JFrame) SwingUtilities.getWindowAncestor(logoutButton);
                dashboardFrame.dispose();

                // Open the login screen
                main.java.main.Main.createAndShowGUI();
            }
        });

        add(logoutButton);

    }

    private void initializeAdminDashboard(GridBagConstraints gbc) {
        System.out.println("Initializing Admin Dashboard");  // Debugging line
        JPanel adminPanel = new JPanel(new FlowLayout());  // Create a new JPanel with FlowLayout


        JButton manageServersButton = new JButton("Manage Servers");
        manageServersButton.addActionListener(e -> openManageServersPanel());

        JButton manageUsersButton = new JButton("Manage Users");
        manageUsersButton.addActionListener(e -> openManageUsersPanel());

        JButton setThresholdsButton = new JButton("Set Thresholds");
        setThresholdsButton.addActionListener(e -> {
            setThresholdsForServers();
        });

        adminPanel.add(manageServersButton);  // Add the button to the JPanel
        adminPanel.add(manageUsersButton);    // Add the button to the JPanel
        adminPanel.add(setThresholdsButton);  // Add the button to the JPanel

        gbc.gridx = 0;
        gbc.gridy = 4;  // Adjust as needed
        gbc.weighty = 0.1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(adminPanel, gbc);  // Use GridBagConstraints
    }

    private void initializeUserDashboard(GridBagConstraints gbc) {
        System.out.println("Initializing User Dashboard");  // Debugging line

        JPanel userPanel = new JPanel(new FlowLayout());  // Create a new JPanel with FlowLayout
        JButton requestRestartButton = new JButton("Request Server Restart");
        requestRestartButton.addActionListener(e -> {
            int selectedRow = serverTable.getSelectedRow();
            if (selectedRow != -1) {
                Server selectedServer = servers.get(selectedRow);
                requestServerRestart(selectedServer);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a server to restart.");
            }
        });

        // Initialize serverDropdown and alertTypeDropdown
        serverDropdown = new JComboBox();
        alertTypeDropdown = new JComboBox();

        // Update the dropdowns
        updateDropdowns();

        // Add them to the panel using GridBagConstraints
        // Set gbc properties as needed
        add(serverDropdown, gbc);
        add(alertTypeDropdown, gbc);

        JButton subscribeAlertsButton = new JButton("Subscribe to Alerts");
        subscribeAlertsButton.addActionListener(e -> {
            subscribeToAlerts();
        });

        userPanel.add(requestRestartButton);    // Add the button to the JPanel
        userPanel.add(subscribeAlertsButton);  // Add the button to the JPanel

        gbc.gridx = 0;
        gbc.gridy = 4;  // Adjust as needed
        gbc.weighty = 0.1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(userPanel, gbc);  // Use GridBagConstraints
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

    private void openManageUsersPanel() {
        List<AbstractUser> allUsers = new ArrayList<>();
        // Load users from UserDatabase
        if (UserDatabase.users != null) {
            allUsers.addAll(UserDatabase.users);
        }

        // Load users from users.txt
        //List<AbstractUser> fileUsers = FileHandler.readUsersFromTextFile("users.txt"); // Assuming you have a method that reads users from a text file
        List<AbstractUser> fileUsers = FileHandler.readUsersFromCSVFile("users.csv"); // Assuming you have a method that reads users from a text file
        if (fileUsers != null) {
            allUsers.addAll(fileUsers);
        }

        JFrame manageUsersFrame = new JFrame("Manage Users");
        ManageUsers manageUsersPanel = new ManageUsers(allUsers);  // No need for explicit cast now
        manageUsersFrame.add(manageUsersPanel);
        manageUsersFrame.setSize(1000, 400);
        manageUsersFrame.setVisible(true);
        manageUsersFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void setThresholdsForServers() {
        // Create a dialog box with input fields
        JComboBox<String> serverDropdown = new JComboBox<>();
        for (Server server : servers) {
            serverDropdown.addItem(server.getName());
        }
        JTextField cpuField = new JTextField(5);
        JTextField memoryField = new JTextField(5);
        JTextField networkField = new JTextField(5);
        JCheckBox applyToAllCheckbox = new JCheckBox("Apply to All Servers");

        JPanel panel = new JPanel();
        panel.add(new JLabel("Select Server:"));
        panel.add(serverDropdown);
        panel.add(new JLabel("CPU Limit:"));
        panel.add(cpuField);
        panel.add(new JLabel("Memory Limit:"));
        panel.add(memoryField);
        panel.add(new JLabel("Network Limit:"));
        panel.add(networkField);
        panel.add(applyToAllCheckbox);

        int result = JOptionPane.showConfirmDialog(null, panel, "Set Thresholds", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            // Validate and set the thresholds
            String selectedServerName = (String) serverDropdown.getSelectedItem();
            int cpuThreshold = Integer.parseInt(cpuField.getText());
            int memoryThreshold = Integer.parseInt(memoryField.getText());
            int networkThreshold = Integer.parseInt(networkField.getText());

            if (applyToAllCheckbox.isSelected()) {
                for (Server server : servers) {
                    server.setCpuThreshold(cpuThreshold);
                    server.setMemoryThreshold(memoryThreshold);
                    server.setNetworkThreshold(networkThreshold);
                }
            } else {
                for (Server server : servers) {
                    if (server.getName().equals(selectedServerName)) {
                        server.setCpuThreshold(cpuThreshold);
                        server.setMemoryThreshold(memoryThreshold);
                        server.setNetworkThreshold(networkThreshold);
                        break;
                    }
                }
            }
        }
    }

    // Overloaded Method to handle server restart request without a Server object
    private void requestServerRestart() {
        // Logic to pick a server to restart
        // For demonstration, let's say we pick the first server from the list
        Server serverToRestart = servers.get(0);
        requestServerRestart(serverToRestart);
    }

    // Method to handle server restart request with a Server object
    private void requestServerRestart(Server serverToRestart) {
        // Confirm with the user
        int dialogResult = JOptionPane.showConfirmDialog(this,
                "Do you want to restart " + serverToRestart.getName() + "?",
                "Confirm Restart",
                JOptionPane.YES_NO_OPTION);

        if (dialogResult == JOptionPane.YES_OPTION) {
            // Send the restart request to the admin
            sendRestartRequestToAdmin(serverToRestart);
        }
    }

    // Method to send restart request to admin
    private void sendRestartRequestToAdmin(Server server) {
        // Logic to send a restart request to the admin
        // This could be an API call, database update, etc.
        System.out.println("Restart request for " + server.getName() + " sent to admin.");
    }

    // Method to handle subscription to alerts
    private JComboBox<String> serverDropdown;
    private JComboBox<String> alertTypeDropdown;
    private void subscribeToAlerts() {
        JPanel panel = createSubscriptionPanel();
        int result = showSubscriptionDialog(panel);

        if (result == JOptionPane.OK_OPTION) {
            handleSubscription();
        }
    }

    private JPanel createSubscriptionPanel() {
        JComboBox<String> serverDropdown = createServerDropdown();
        JComboBox<String> alertTypeDropdown = createAlertTypeDropdown();

        JPanel panel = new JPanel();
        panel.add(new JLabel("Select Server:"));
        panel.add(serverDropdown);
        panel.add(new JLabel("Select Alert Type:"));
        panel.add(alertTypeDropdown);

        return panel;
    }

    private JComboBox<String> createServerDropdown() {
        JComboBox<String> serverDropdown = new JComboBox<>();
        serverDropdown.addItem("All Servers");
        for (Server server : servers) {
            serverDropdown.addItem(server.getName());
        }
        return serverDropdown;
    }

    private JComboBox<String> createAlertTypeDropdown() {
        String[] alertTypes = {"All Alerts", "CPU Usage", "Memory Usage", "Network Latency"};
        JComboBox<String> dropdown = new JComboBox<>(alertTypes);
        return dropdown;
    }

    private int showSubscriptionDialog(JPanel panel) {
        return JOptionPane.showConfirmDialog(null, panel, "Subscribe to Alerts", JOptionPane.OK_CANCEL_OPTION);
    }

    private void handleSubscription() {
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

    private void subscribeAllServersToAllAlerts() {
        for (Server server : servers) {
            server.subscribeToAlert("CPU Usage");
            server.subscribeToAlert("Memory Usage");
            server.subscribeToAlert("Network Latency");
        }
    }

    private void subscribeSpecificServerToAllAlerts(String serverName) {
        for (Server server : servers) {
            if (server.getName().equals(serverName)) {
                server.subscribeToAlert("CPU Usage");
                server.subscribeToAlert("Memory Usage");
                server.subscribeToAlert("Network Latency");
                break;
            }
        }
    }

    private void subscribeAllServers(String alertType) {
        for (Server server : servers) {
            server.subscribeToAlert(alertType);
        }
    }

    private void subscribeSpecificServer(String serverName, String alertType) {
        for (Server server : servers) {
            if (server.getName().equals(serverName)) {
                server.subscribeToAlert(alertType);
                break;
            }
        }
    }

    private void updateDropdowns() {
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

        // Populate alertTypeDropdown with alert types (replace with your actual alert types)
        alertTypeDropdown.addItem("CPU Alert");
        alertTypeDropdown.addItem("Memory Alert");
        // Add more alert types as needed
    }



}