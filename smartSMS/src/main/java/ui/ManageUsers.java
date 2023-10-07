package main.java.ui;

import main.java.models.AbstractUser;
import main.java.models.Admin;
import main.java.models.User;
import main.java.utils.FileHandler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ManageUsers extends JPanel {
    private JTable userTable;
    private DefaultTableModel tableModel;
    private ArrayList<AbstractUser> users;

    public ManageUsers(List<? extends AbstractUser> users) {
        //this.users = new ArrayList<>(users);
        this.users = new ArrayList<>(FileHandler.readUsersFromCSVFile("users.csv"));
        setLayout(new BorderLayout());

        String[] columnNames = {"Username", "Role"};
        tableModel = new DefaultTableModel(columnNames, 0);

        populateTable();

        userTable = new JTable(tableModel);

        JButton addButton = new JButton("Add");
        JButton editButton = new JButton("Edit");
        JButton removeButton = new JButton("Remove");

        addButton.addActionListener(new AddUserAction());
        editButton.addActionListener(new EditUserAction());
        removeButton.addActionListener(new RemoveUserAction());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(removeButton);

        add(new JScrollPane(userTable), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void populateTable() {
        tableModel.setRowCount(0); // Clear existing rows
        for (AbstractUser user : this.users) {
            Object[] rowData = {user.getUsername(), user.getRole()};
            tableModel.addRow(rowData);
        }
    }

    private class AddUserAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = JOptionPane.showInputDialog("Enter Username:");

            // Check for duplicate username
            boolean isDuplicate = false;
            for (AbstractUser user : users) {
                if (user.getUsername().equals(username)) {
                    isDuplicate = true;
                    break;
                }
            }

            if (isDuplicate) {
                JOptionPane.showMessageDialog(null, "Username already exists.");
                return; // Exit the method if username is duplicate
            }

            String password = JOptionPane.showInputDialog("Enter Password:");
            String role = JOptionPane.showInputDialog("Enter Role (admin/user):");

            AbstractUser newUser;
            if ("admin".equals(role)) {
                newUser = new Admin(username, password, role, null);
            } else {
                newUser = new User(username, password, role, null);
            }

            addUser(newUser);
            populateTable();
        }
    }

    private class EditUserAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow >= 0 && selectedRow < users.size()) {
                AbstractUser user = users.get(selectedRow);

                String newUsername = JOptionPane.showInputDialog("Enter new Username:", user.getUsername());
                String newPassword = JOptionPane.showInputDialog("Enter new Password:", user.getPassword());
                String newRole = JOptionPane.showInputDialog("Enter new Role (admin/user):", user.getRole());

                user.setUsername(newUsername);
                user.setPassword(newPassword);
                user.setRole(newRole);

                populateTable();

                // Update the CSV file

                FileHandler.writeUsersToCSVFile("users.csv", users);

            } else {
                JOptionPane.showMessageDialog(null, "Please select a user to edit.");
            }
        }
    }


    private class RemoveUserAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow >= 0 && selectedRow < users.size()) {
                users.remove(selectedRow);
                populateTable();

                // Update the CSV file
                FileHandler.writeUsersToCSVFile("users.csv", users);

            } else {
                JOptionPane.showMessageDialog(null, "Please select a user to remove.");
            }
        }
    }

    private void addUser(AbstractUser newUser) {
        boolean isDuplicate = false;
        for (AbstractUser user : users) {
            if (user.getUsername().equals(newUser.getUsername())) {
                isDuplicate = true;
                break;
            }
        }

        if (!isDuplicate) {
            users.add(newUser);
            Object[] rowData = {newUser.getUsername(), newUser.getRole()};
            tableModel.addRow(rowData);

            FileHandler.appendUserToCSVFile("users.csv", newUser);

        } else {
            JOptionPane.showMessageDialog(null, "Username already exists.");
        }
    }


}
