package main.java.ui;

import main.java.models.Server;
import main.java.models.AbstractUser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusAdapter;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserDashboard extends JPanel {
    private JLabel welcomeLabel;
    private JTable serverTable;
    private DefaultTableModel tableModel;
    private AbstractUser currentUser;
    private GridBagConstraints gbc = new GridBagConstraints();
    private AlertManagement alertManagement;

    private ArrayList<Server> servers;
    private ArrayList<AbstractUser> users;
    private JPanel parentPanel;
    private DashboardPanel dashboardPanel;
    private Server selectedServer = null;

    public UserDashboard(ArrayList<Server> servers, ArrayList<AbstractUser> users, GridBagConstraints gbc, JPanel parentPanel, DashboardPanel dashboardPanel) {
        if(servers == null) {
            throw new IllegalArgumentException("Servers list cannot be null");
        }
        setLayout(new GridBagLayout());
        this.servers = servers;
        this.users = users;
        this.gbc = gbc;
        this.parentPanel = parentPanel;
        this.dashboardPanel = dashboardPanel;

        String[] columnNames = {"Server Name", "CPU Usage", "Memory Usage", "Network Latency"};
        tableModel = new DefaultTableModel(columnNames, 0);
        serverTable = new JTable(tableModel);
        serverTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        serverTable.setFocusable(true);
        serverTable.setRowSelectionAllowed(true);

        for (Server server : servers) {
            Object[] rowData = {server.getName(), server.getCpuUsage(), server.getMemoryUsage(), server.getNetworkLatency()};
            tableModel.addRow(rowData);
        }

        JScrollPane scrollPane = new JScrollPane(serverTable);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 0.1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        this.add(scrollPane, gbc);

        // Debug: Check if table model is populated correctly
        //System.out.println("Debug: Table model row count: " + tableModel.getRowCount());

        // Focus Debugging
        serverTable.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                System.out.println("Table gained focus");
            }
            public void focusLost(FocusEvent e) {
                System.out.println("Table lost focus");
            }
        });
    }

    public void initializeUserDashboard(DashboardPanel dashboardPanel) {
        System.out.println("Initializing User Dashboard");

        // Debug: Check if table model is populated correctly now
        //System.out.println("Debug: Table model row count: " + tableModel.getRowCount());

        this.dashboardPanel = dashboardPanel;

        JPanel userPanel = new JPanel(new FlowLayout());

        this.alertManagement = new AlertManagement(servers);
        alertManagement.updateDropdowns();

        // Initialize the Request Restart Button with the new ActionListener
        initializeRequestRestartButton(userPanel);
        initializeSubscribeAlertsButton(userPanel);

        addDropdownsToPanel(gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weighty = 0.1;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        parentPanel.add(userPanel, gbc);
        //System.out.println("Table Row Count: " + serverTable.getRowCount());
    }

    // Inside initializeRequestRestartButton method

    public void initializeRequestRestartButton(JPanel userPanel) {
        JButton requestRestartButton = new JButton("Request Server Restart");
        userPanel.add(requestRestartButton);

        requestRestartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JDialog serverSelectDialog = new JDialog();
                serverSelectDialog.setTitle("Select Server to Restart");
                serverSelectDialog.setLayout(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();

                JComboBox<Object> serverDropdown = new JComboBox<>();
                serverDropdown.addItem("All Servers");
                for (Server server : servers) {
                    serverDropdown.addItem(server);
                }

                JButton submitButton = new JButton("Submit");
                submitButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Object selected = serverDropdown.getSelectedItem();
                        if ("All Servers".equals(selected)) {
                            for (Server server : servers) {
                                requestServerRestart(server);
                            }
                        } else {
                            Server selectedServer = (Server) selected;
                            if (selectedServer != null) {
                                requestServerRestart(selectedServer);
                            }
                        }
                        serverSelectDialog.dispose();
                    }
                });

                gbc.insets = new Insets(10, 10, 10, 10);  // Padding

                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.anchor = GridBagConstraints.WEST;
                serverSelectDialog.add(new JLabel("Select Server:"), gbc);

                gbc.gridx = 1;
                serverSelectDialog.add(serverDropdown, gbc);

                gbc.gridx = 2;
                gbc.gridy = 0;  // Changed from 1 to 2 to add more space
                gbc.gridwidth = 1;
                gbc.anchor = GridBagConstraints.EAST;
                serverSelectDialog.add(submitButton, gbc);

                serverSelectDialog.setPreferredSize(new Dimension(400, 150));  // Set the preferred size
                serverSelectDialog.pack();
                serverSelectDialog.setLocationRelativeTo(null);  // Center the dialog on the screen
                serverSelectDialog.setVisible(true);
            }
        });
    }

    public void initializeSubscribeAlertsButton(JPanel userPanel) {
        JButton subscribeAlertsButton = new JButton("Subscribe to Alerts");
        subscribeAlertsButton.addActionListener(e -> alertManagement.subscribeToAlerts());
        userPanel.add(subscribeAlertsButton);
    }

    private void addDropdownsToPanel(GridBagConstraints gbc) {
        add(alertManagement.getServerDropdown(), gbc);
        add(alertManagement.getAlertTypeDropdown(), gbc);
    }

    private void requestServerRestart() {
        Server serverToRestart = servers.get(0);
        requestServerRestart(serverToRestart);
    }

    private void requestServerRestart(Server serverToRestart) {
        int dialogResult = JOptionPane.showConfirmDialog(this,
                "Do you want to submit restart for " + serverToRestart.getName() + "?",
                "Confirm Request",
                JOptionPane.YES_NO_OPTION);

        if (dialogResult == JOptionPane.YES_OPTION) {
            //serverToRestart.restart();
            sendRestartRequestToAdmin(serverToRestart);
        }
    }

    private void sendRestartRequestToAdmin(Server server) {
        System.out.println("Restart request for " + server.getName() + " sent to admin.");
    }
}