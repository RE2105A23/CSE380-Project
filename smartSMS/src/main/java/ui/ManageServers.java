package main.java.ui;

import main.java.models.Server;
import main.java.ui.ServerManagement;
import main.java.utils.FileHandler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ManageServers extends JPanel {
    private JTable serverTable;
    private DefaultTableModel tableModel;
    private ArrayList<Server> servers;
    private ServerManagement serverManagement;  // Added this line
    private AdminDashboard adminDashboard;  // Declare the field

    public ManageServers(ArrayList<Server> servers, ServerManagement serverManagement, AdminDashboard adminDashboard) {  // Added ServerManagement parameter
    this.adminDashboard = adminDashboard;  // Initialize the AdminDashboard instance
        this.servers = servers;
        this.serverManagement = serverManagement;  // Initialize the ServerManagement instance

        setLayout(new BorderLayout());

        String[] columnNames = {"Server Name", "CPU Limit", "Memory Limit", "Network Limit"};
        this.tableModel = new DefaultTableModel(columnNames, 0);

        for (Server server : this.servers) {
            Object[] rowData = {
                    server.getName(),
                    server.getCpuThreshold(),
                    server.getMemoryThreshold(),
                    server.getNetworkThreshold()
            };
            this.tableModel.addRow(rowData);
        }

        serverTable = new JTable(this.tableModel);

        JButton addButton = new JButton("Add");
        JButton editButton = new JButton("Edit");
        JButton removeButton = new JButton("Remove");

        addButton.addActionListener(new AddServerAction());
        editButton.addActionListener(new EditServerAction());
        removeButton.addActionListener(new RemoveServerAction());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(removeButton);

        add(new JScrollPane(serverTable), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private class AddServerAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String name = JOptionPane.showInputDialog("Enter Server Name:");
            if (name == null) return; // User cancelled, exit the method

            String cpuLimitStr = JOptionPane.showInputDialog("Enter CPU Limit:");
            if (cpuLimitStr == null) return; // User cancelled, exit the method

            String memoryLimitStr = JOptionPane.showInputDialog("Enter Memory Limit:");
            if (memoryLimitStr == null) return; // User cancelled, exit the method

            String networkLimitStr = JOptionPane.showInputDialog("Enter Network Limit:");
            if (networkLimitStr == null) return; // User cancelled, exit the method

            try {
                int cpuLimit = Integer.parseInt(cpuLimitStr);
                int memoryLimit = Integer.parseInt(memoryLimitStr);
                int networkLimit = Integer.parseInt(networkLimitStr);

                Server newServer = new Server(name, cpuLimit, memoryLimit, networkLimit);
                servers.add(newServer);

                Object[] rowData = {name, cpuLimit, memoryLimit, networkLimit};
                tableModel.addRow(rowData);
                // Use ServerManagement's addServer method
                serverManagement.addServer(name, cpuLimit, memoryLimit, networkLimit);
                FileHandler.writeServersToCSVFile("servers.csv",servers);  // Save to CSV
                populateServerTable();  // <-- Add this line
                // Refresh the AdminDashboard table
                adminDashboard.refreshTable();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter numerical values for limits.");
            }
        }
    }


    private class EditServerAction implements ActionListener {
        private String validateInput(String prompt, String defaultValue) {
            String input = JOptionPane.showInputDialog(prompt, defaultValue);
            if (input == null || input.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Input cannot be empty. Please try again.");
                return null; // or throw an exception, or use a default value
            }
            return input;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int selectedRow = serverTable.getSelectedRow();
                if (selectedRow >= 0 && selectedRow < servers.size()) {
                    Server serverToEdit = servers.get(selectedRow);

                    String newName = validateInput("Enter new Server Name:", serverToEdit.getName());
                    String newCpuLimitStr = validateInput("Enter new CPU Limit:", String.valueOf(serverToEdit.getCpuThreshold()));
                    String newMemoryLimitStr = validateInput("Enter new Memory Limit:", String.valueOf(serverToEdit.getMemoryThreshold()));
                    String newNetworkLimitStr = validateInput("Enter new Network Limit:", String.valueOf(serverToEdit.getNetworkThreshold()));

                    int newCpuLimit = Integer.parseInt(newCpuLimitStr);
                    int newMemoryLimit = Integer.parseInt(newMemoryLimitStr);
                    int newNetworkLimit = Integer.parseInt(newNetworkLimitStr);

                    serverToEdit.setName(newName);
                    serverToEdit.setCpuThreshold(newCpuLimit);  // Assuming you have a setter for this
                    serverToEdit.setMemoryThreshold(newMemoryLimit);  // Assuming you have a setter for this
                    serverToEdit.setNetworkThreshold(newNetworkLimit);  // Assuming you have a setter for this

                    FileHandler.writeServersToCSVFile("servers.csv", servers);  // Save to CSV
                    populateServerTable();  // Refresh the table
                    adminDashboard.refreshTable();

                } else {
                    JOptionPane.showMessageDialog(null, "Please select a server to edit.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter numerical values for limits.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "An unexpected error occurred. Please try again.");
            }
        }
    }


    // New method to populate the server table
    private void populateServerTable() {
        tableModel.setRowCount(0);
        for (Server server : this.servers) {
            Object[] rowData = {
                    server.getName(),
                    server.getCpuThreshold(),
                    server.getMemoryThreshold(),
                    server.getNetworkThreshold()
            };
            tableModel.addRow(rowData);
        }
    }

    private class RemoveServerAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = serverTable.getSelectedRow();
            if (selectedRow >= 0 && selectedRow < servers.size()) {
                servers.remove(selectedRow);

                // Update the CSV file
                FileHandler.writeServersToCSVFile("servers.csv", servers);  // Save to CSV
                populateServerTable();  // <-- Add this line
                adminDashboard.refreshTable();

            } else {
                JOptionPane.showMessageDialog(null, "Please select a server to remove.");
            }
        }
    }
}
