package main.java.ui;

import main.java.main.Main;
import main.java.models.AbstractUser;
import main.java.models.Server;
import main.java.ui.AdminDashboard;
import main.java.ui.UserDashboard;
import main.java.utils.FileHandler;
import main.java.utils.SMSHandler;
import main.java.utils.UserDatabase;
import main.java.ui.ServerManagement;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class DashboardPanel extends JPanel {
    private JLabel welcomeLabel;
    private JTable serverTable;
    private JPanel parentPanel;
    private DefaultTableModel tableModel;
    private ArrayList<Server> servers;
    private AbstractUser currentUser;
    private ArrayList<AbstractUser> users;
    private GridBagConstraints gbc = new GridBagConstraints();
    private ServerManagement serverManagement;

    public DashboardPanel(AbstractUser currentUser, JPanel parentPanel) {
        setLayout(new GridBagLayout());
        this.currentUser = currentUser;
        this.parentPanel = parentPanel;

        // Initialize tableModel
        String[] columnNames;
        if ("admin".equals(currentUser.getRole())) {
            columnNames = new String[]{"Server Name", "CPU Usage", "Memory Usage", "Network Latency", "Actions"};
        } else {
            columnNames = new String[]{"Server Name", "CPU Usage", "Memory Usage", "Network Latency"};
        }

        // Initialize tableModel and servers
        this.tableModel = new DefaultTableModel(columnNames, 0);
        this.servers = new ArrayList<>();

        // Initialize ServerManagement with the tableModel and servers
        if (this.tableModel != null) {
            this.serverManagement = new ServerManagement(this.servers, this.tableModel);  // Make sure to initialize here
            if (this.serverManagement != null) {
                this.servers = serverManagement.initializeServers();  // Assuming initializeServers is a method in ServerManagement
            } else {
                System.err.println("serverManagement is null. Aborting.");
                return;
            }
        } else {
            System.err.println("tableModel is null. Cannot initialize serverManagement.");
            return;
        }

        //this.servers = initializeServers();
        if (this.servers == null) {
            System.err.println("servers is null. Aborting.");
            return;  // Exit if servers is null
        }

        // Initialize UI Components
        initializeUI();

        // Initialize ServerManagement with the tableModel and servers
        this.serverManagement = new ServerManagement(this.servers, this.tableModel);
        //System.out.println("Dashboard Server object: "+servers.size());
    }



    private void initializeUI() {
        // Welcome Label
        welcomeLabel = new JLabel("Welcome to the SMS based Remote Server Monitoring System!");
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 0.1;
        gbc.insets = new Insets(10, 10, 10, 10);
        add(welcomeLabel, gbc);

        // Initialize Table
        initializeTable();

        // Initialize Timer
        initializeTimer();

        // Initialize Dashboard
        initializeDashboard();

        // Logout Button
        initializeLogoutButton();

        // Populate the table initially
        serverManagement.simulateServerMonitoring(servers);
    }

    private void initializeTable() {
        serverTable = new JTable(tableModel);
        serverTable.setPreferredScrollableViewportSize(new Dimension(500, 70));  // Set the preferred size
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 0.9;
        gbc.fill = GridBagConstraints.BOTH;

        if ("admin".equals(currentUser.getRole())) {
            serverTable.getColumn("Actions").setCellEditor(serverManagement.new SimpleButtonEditor(serverManagement));
            serverTable.getColumn("Actions").setCellRenderer(new ServerManagement.SimpleButtonRenderer());
        }

        JScrollPane scrollPane = new JScrollPane(serverTable);
        scrollPane.setPreferredSize(new Dimension(500, 200));  // Optional: set JScrollPane size
        add(scrollPane, gbc);
    }

    private void initializeTimer() {
        Timer timer = new Timer(5000, e -> {
            //System.out.println("Timer is called");  // Debug print
            tableModel.setRowCount(0);
            serverManagement.simulateServerMonitoring(servers);
        });
        timer.start();
    }

    private void initializeDashboard() {
        if (currentUser != null) {
            if ("admin".equals(currentUser.getRole())) {
                DefaultTableModel tableModel = new DefaultTableModel();  // Initialize this as needed
                AdminDashboard adminDashboard = new AdminDashboard(servers, users, gbc, this, this,tableModel);
                adminDashboard.initializeAdminDashboard(this);
            } else {
                UserDashboard userDashboard = new UserDashboard(servers, users, gbc, this, this);
                userDashboard.initializeUserDashboard(this);
            }
        }
    }

    private void initializeLogoutButton() {
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            for (Frame frame : Frame.getFrames()) {
                frame.dispose();
            }
            Main.createAndShowGUI();
        });
        add(logoutButton);
    }

    public void refreshServers() {
        this.servers = serverManagement.initializeServers(); // Re-initialize servers
        tableModel.setRowCount(0); // Clear the table
        serverManagement.simulateServerMonitoring(servers);
    }

}
