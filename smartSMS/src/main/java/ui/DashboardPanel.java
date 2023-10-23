package main.java.ui;

import main.java.main.Main;
import main.java.models.AbstractUser;
import main.java.models.Server;
import main.java.models.ServerRestarter;
import main.java.ui.AdminDashboard;
import main.java.ui.UserDashboard;
import main.java.utils.FileHandler;
import main.java.utils.SMSHandler;
import main.java.utils.UserDatabase;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class DashboardPanel extends JPanel implements ServerRestarter {
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
        this.tableModel = new DefaultTableModel(columnNames, 0);

        // Initialize ServerManagement with the tableModel
        this.serverManagement = new ServerManagement(this.tableModel);  // <-- This line should be here

        // Initialize UI Components
        initializeUI();
    }


    private void initializeUI() {
        // Welcome Label
        welcomeLabel = new JLabel("Welcome to the SMS-based Remote Server Monitoring System!");
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 0.1;
        gbc.insets = new Insets(10, 10, 10, 10);
        add(welcomeLabel, gbc);

        servers = new ArrayList<>();

        servers.add(new Server("Server1", 20, 50, 60));
        servers.add(new Server("Server2", 20, 50, 70));
        servers.add(new Server("Server3", 20, 50, 80));
        servers.add(new Server("Server4", 20, 50, 88));
        servers.add(new Server("Server5", 20, 50, 100));

        if(servers == null || servers.isEmpty()) {
            System.out.println("Servers list is null or empty. Cannot proceed.");
            return; // Or handle this case as you see fit
        }

        // Initialize Table
        initializeTable();

        // Initialize Timer
        initializeTimer();

        // Initialize Dashboard
        initializeDashboard();

        // Logout Button
        initializeLogoutButton();

        // Populate the table initially
        serverManagement.simulateServerMonitoring();
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


    @Override
    public void restartServer(Server server) {
        server.restart();
    }

    private void initializeTimer() {
        Timer timer = new Timer(5000, e -> {
            //System.out.println("Timer is called");  // Debug print
            tableModel.setRowCount(0);
            serverManagement.simulateServerMonitoring();
        });
        timer.start();
    }

    private void initializeDashboard() {
        if (currentUser != null) {
            if ("admin".equals(currentUser.getRole())) {
                AdminDashboard adminDashboard = new AdminDashboard(servers, users, gbc, this, this);
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
}
