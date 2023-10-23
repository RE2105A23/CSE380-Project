package main.java.ui;

import main.java.models.Server;
import main.java.models.AbstractUser;
import main.java.ui.AlertManagement;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

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

        serverTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                System.out.println("Row changed to: " + serverTable.getSelectedRow());
            }
        });


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
    }

    public void initializeUserDashboard(DashboardPanel dashboardPanel) {
        System.out.println("Initializing User Dashboard");
        this.dashboardPanel = dashboardPanel;

        JPanel userPanel = new JPanel(new FlowLayout());

        this.alertManagement = new AlertManagement(servers);
        alertManagement.updateDropdowns();

        initializeRequestRestartButton(userPanel);
        initializeSubscribeAlertsButton(userPanel);

        addDropdownsToPanel(gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weighty = 0.1;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        parentPanel.add(userPanel, gbc);
        System.out.println("Table Row Count: " + serverTable.getRowCount());
    }

    public void initializeRequestRestartButton(JPanel userPanel) {
        JButton requestRestartButton = new JButton("Request Server Restart");
        requestRestartButton.addActionListener(e -> {
            SwingUtilities.invokeLater(() -> {
                serverTable.requestFocus();  // Explicitly request focus for the table
                serverTable.repaint();  // Repaint the table
                int selectedRow = serverTable.getSelectedRow();
                System.out.println("Selected Row: " + selectedRow);

                // Debugging: Print the state of the table's selection model
                ListSelectionModel selectionModel = serverTable.getSelectionModel();
                System.out.println("Is selection empty: " + selectionModel.isSelectionEmpty());

                if (selectedRow != -1) {
                    Server selectedServer = servers.get(selectedRow);
                    if (selectedServer != null) {
                        requestServerRestart(selectedServer);
                    } else {
                        JOptionPane.showMessageDialog(this, "Invalid server selected.");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Please select a server to restart.");
                }
            });
        });
        userPanel.add(requestRestartButton);
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
                "Do you want to restart " + serverToRestart.getName() + "?",
                "Confirm Restart",
                JOptionPane.YES_NO_OPTION);

        if (dialogResult == JOptionPane.YES_OPTION) {
            serverToRestart.restart();
            sendRestartRequestToAdmin(serverToRestart);
        }
    }

    private void sendRestartRequestToAdmin(Server server) {
        System.out.println("Restart request for " + server.getName() + " sent to admin.");
    }
}
