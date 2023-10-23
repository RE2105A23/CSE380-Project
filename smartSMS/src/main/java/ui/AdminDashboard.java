package main.java.ui;

import main.java.models.Server;
import main.java.utils.FileHandler;
import main.java.models.AbstractUser;
import main.java.utils.UserDatabase;
import main.java.ui.ManageServers;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AdminDashboard {
    private List<Server> servers;
    private List<AbstractUser> users;
    private GridBagConstraints gbc;
    private JPanel parentPanel;  // Add this line
    private DashboardPanel dashboardPanel;  // Add this line to declare the field

    public AdminDashboard(List<Server> servers, List<AbstractUser> users, GridBagConstraints gbc, JPanel parentPanel, DashboardPanel dashboardPanel) {
        this.servers = servers;
        this.users = users;
        this.gbc = gbc;
        this.parentPanel = parentPanel;  // Initialize the JPanel
        this.dashboardPanel = dashboardPanel;  // Initialize the DashboardPanel field

    }

    public void initializeAdminDashboard(JPanel panel) {
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
        parentPanel.add(adminPanel, gbc);  // Use parentPanel to add components

    }

    public void openManageServersPanel() {
        JFrame manageServersFrame = new JFrame("Manage Servers");
        ManageServers manageServersPanel = new ManageServers(new ArrayList<>(this.servers));  // Explicitly cast to ArrayList
        manageServersFrame.add(manageServersPanel);
        manageServersFrame.setSize(1000, 400);
        manageServersFrame.setVisible(true);
        manageServersFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public void openManageUsersPanel() {
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

    public void setThresholdsForServers() {
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
}
