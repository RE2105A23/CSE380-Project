package main.java.ui;

import main.java.models.AbstractUser;
import main.java.models.Server;
import main.java.models.ServerRestarter;
import main.java.models.User;
import main.java.utils.FileHandler;
import main.java.utils.SMSHandler;
import main.java.utils.UserDatabase;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ServerManagement {
    private ArrayList<Server> servers;
    private DefaultTableModel tableModel;

    public ServerManagement(DefaultTableModel tableModel) {
        this.tableModel = tableModel;
        if (tableModel == null) {
            System.err.println("tableModel is null. Aborting simulation.");
            return;
        }
        initializeServers();
    }

    public void initializeServers() {
        servers = new ArrayList<>();
        servers.add(new Server("Server1", 20, 50, 60));
        servers.add(new Server("Server2", 20, 50, 70));
        servers.add(new Server("Server3", 20, 50, 80));
        servers.add(new Server("Server4", 20, 50, 88));
        servers.add(new Server("Server5", 20, 50, 100));
    }

    public void simulateServerMonitoring() {
        tableModel.setRowCount(0);
        if (tableModel == null) {
            System.err.println("tableModel is null. Aborting simulation.");
            return;
        }

        for (Server server : servers) {
            server.simulateMonitoring();

            // Create a new JButton for each row
            //JButton restartButton = new JButton("Restart");
            //restartButton.setActionCommand("Restart_" + server.getName());  // Set action command

            Object[] rowData = {
                    server.getName(),
                    String.format("%.2f", server.getCpuUsage()),
                    String.format("%.2f", server.getMemoryUsage()),
                    String.format("%.2f", server.getNetworkLatency()),
                    "Restart"  // Add a "Restart" action here
            };
            tableModel.addRow(rowData);
            FileHandler.writeLog("server_metrics.txt",
                    String.format("%s: CPU=%.2f, Memory=%.2f, Latency=%.2f",
                            server.getName(),
                            server.getCpuUsage(),
                            server.getMemoryUsage(),
                            server.getNetworkLatency())
            );
        }
        checkServerThresholds();
        tableModel.fireTableDataChanged();
    }


    public void restartServer(Server server) {
        server.restart();
    }

    public void checkServerThresholds() {
        for (Server server : servers) {
            String message = server.checkThresholds();
            if (!message.isEmpty()) {
                SMSHandler.sendSMS("123-456-7890", message);
                FileHandler.writeLog("server_alerts.txt", server.getName() + ": " + message);
            }
        }
    }

    public void addServer(String name, int cpuLimit, int memoryLimit, int networkLimit) {
        Server newServer = new Server(name, cpuLimit, memoryLimit, networkLimit);
        servers.add(newServer);
        Object[] rowData = {name, cpuLimit, memoryLimit, networkLimit};
        tableModel.addRow(rowData);
        tableModel.fireTableDataChanged();
    }

    public void editServer(int selectedRow, String name, int cpuLimit, int memoryLimit, int networkLimit) {
        if (selectedRow >= 0 && selectedRow < servers.size()) {
            Server server = servers.get(selectedRow);
            server.setName(name);
            tableModel.setValueAt(name, selectedRow, 0);
            tableModel.fireTableDataChanged();
        } else {
            JOptionPane.showMessageDialog(null, "Invalid row selected for editing.");
        }
    }

    public void removeServer(int selectedRow) {
        if (selectedRow >= 0 && selectedRow < servers.size()) {
            servers.remove(selectedRow);
            tableModel.removeRow(selectedRow);
            tableModel.fireTableDataChanged();
        } else {
            JOptionPane.showMessageDialog(null, "Invalid row selected for removal.");
        }
    }

    public static class SimpleButtonRenderer extends JButton implements TableCellRenderer {
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText("Restart");
            return this;
        }
    }

    // Updated inner class to non-static and added a reference to the ServerManagement instance
    public class SimpleButtonEditor extends DefaultCellEditor implements ActionListener {
        private JButton button;
        private int row;
        private JTable table;
        private ServerManagement serverManagementInstance;  // Add this line

        public SimpleButtonEditor(ServerManagement serverManagementInstance) {
            super(new JCheckBox());
            this.serverManagementInstance = serverManagementInstance;  // Initialize it
            button = new JButton("Restart");
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.row = row;
            this.table = table;
            for (ActionListener al : button.getActionListeners()) {
                button.removeActionListener(al);
            }
            button.addActionListener(this);
            return button;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            //System.out.println("Restart button clicked for row: " + row);
            Server server = serverManagementInstance.servers.get(row);  // Access it via instance
            serverManagementInstance.restartServer(server);  // Access it via instance

            // Update the table row with the new server metrics
            tableModel.setValueAt(String.format("%.2f", server.getCpuUsage()), row, 1);
            tableModel.setValueAt(String.format("%.2f", server.getMemoryUsage()), row, 2);
            tableModel.setValueAt(String.format("%.2f", server.getNetworkLatency()), row, 3);

            tableModel.fireTableRowsUpdated(row, row);  // Notify that the data for the specific row has changed

            fireEditingStopped();
        }
    }
}
