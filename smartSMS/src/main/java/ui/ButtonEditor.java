package main.java.ui;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;  // For the ArrayList<Server>
import main.java.models.Server;
import main.java.models.ServerRestarter;

public class ButtonEditor extends DefaultCellEditor {
    protected JButton button;
    private ServerRestarter serverRestarter;
    private ArrayList<Server> servers;
    private Server currentServer;
    private String label;  // Declare label
    private boolean isPushed;  // Declare isPushed

    public ButtonEditor(JCheckBox checkBox, ServerRestarter serverRestarter, ArrayList<Server> servers) {
        super(checkBox);
        this.serverRestarter = serverRestarter;
        this.servers = servers;
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (serverRestarter != null && currentServer != null) {
                    serverRestarter.restartServer(currentServer);
                }
                fireEditingStopped();
            }
        });
    }

    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        if (isSelected) {
            button.setForeground(table.getSelectionForeground());
            button.setBackground(table.getSelectionBackground());
        } else {
            button.setForeground(table.getForeground());
            button.setBackground(table.getBackground());
        }
        label = (value == null) ? "" : value.toString();
        button.setText(label);
        isPushed = true;
        currentServer = servers.get(row);  // Set the currentServer based on the row
        return button;
    }

    public Object getCellEditorValue() {
        if (isPushed) {
            // Implement your button click logic here
        }
        isPushed = false;
        return new String(label);
    }

    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }

    protected void fireEditingStopped() {
        super.fireEditingStopped();
    }
}
