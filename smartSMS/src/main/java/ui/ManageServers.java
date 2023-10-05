package main.java.ui;

import main.java.models.Server;

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

    public ManageServers(ArrayList<Server> servers) {
        this.servers = servers; // Set the servers list
        setLayout(new BorderLayout());

        // Create a new table model for ManageServers
        String[] columnNames = {"Server Name", "CPU Limit", "Memory Limit", "Network Limit"};
        this.tableModel = new DefaultTableModel(columnNames, 0);

        // Populate the table with existing servers
        for (Server server : this.servers) {
            Object[] rowData = {
                    server.getName(),
                    server.getCpuThreshold(),
                    server.getMemoryThreshold(),
                    server.getNetworkThreshold()
            };
            this.tableModel.addRow(rowData);
        }

        // Create the JTable with the new table model
        serverTable = new JTable(this.tableModel);

        // Create buttons for Add, Edit, and Remove
        JButton addButton = new JButton("Add");
        JButton editButton = new JButton("Edit");
        JButton removeButton = new JButton("Remove");

        // Add action listeners to buttons
        addButton.addActionListener(new AddServerAction());
        editButton.addActionListener(new EditServerAction());
        removeButton.addActionListener(new RemoveServerAction());

        // Layout components
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
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter numerical values for limits.");
            }
        }
    }


    private class EditServerAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = serverTable.getSelectedRow();
            if (selectedRow != -1) {
                Server serverToEdit = servers.get(selectedRow);

                String newName = JOptionPane.showInputDialog("Enter new Server Name:", serverToEdit.getName());
                if (newName == null || newName.isEmpty()) return; // User cancelled or entered blank, exit the method

                String newCpuLimitStr = JOptionPane.showInputDialog("Enter new CPU Limit:", serverToEdit.getCpuThreshold());
                if (newCpuLimitStr == null) return; // User cancelled, exit the method

                String newMemoryLimitStr = JOptionPane.showInputDialog("Enter new Memory Limit:", serverToEdit.getMemoryThreshold());
                if (newMemoryLimitStr == null) return; // User cancelled, exit the method

                String newNetworkLimitStr = JOptionPane.showInputDialog("Enter new Network Limit:", serverToEdit.getNetworkThreshold());
                if (newNetworkLimitStr == null) return; // User cancelled, exit the method

                try {
                    int newCpuLimit = Integer.parseInt(newCpuLimitStr);
                    int newMemoryLimit = Integer.parseInt(newMemoryLimitStr);
                    int newNetworkLimit = Integer.parseInt(newNetworkLimitStr);

                    serverToEdit.setName(newName);
                    serverToEdit.setCpuUsage(newCpuLimit);  // Assuming you have a setter for this
                    serverToEdit.setMemoryUsage(newMemoryLimit);  // Assuming you have a setter for this
                    serverToEdit.setNetworkLatency(newNetworkLimit);  // Assuming you have a setter for this

                    tableModel.setValueAt(newName, selectedRow, 0);
                    tableModel.setValueAt(newCpuLimit, selectedRow, 1);
                    tableModel.setValueAt(newMemoryLimit, selectedRow, 2);
                    tableModel.setValueAt(newNetworkLimit, selectedRow, 3);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid input. Please enter numerical values for limits.");
                }
            }
        }
    }

    private class RemoveServerAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = serverTable.getSelectedRow();
            if (selectedRow != -1) {
                servers.remove(selectedRow);
                tableModel.removeRow(selectedRow);
            }
        }
    }
}
