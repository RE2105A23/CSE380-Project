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
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ManageUsers extends JPanel {
    private JTable userTable;
    private DefaultTableModel tableModel;
    private ArrayList<AbstractUser> users;

    public ManageUsers(List<? extends AbstractUser> users) {
        this.users = new ArrayList<>(FileHandler.readUsersFromCSVFile("users.csv"));
        setLayout(new BorderLayout());

        String[] columnNames = {"Username", "Role", "Phone Number"};
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
        tableModel.setRowCount(0);
        for (AbstractUser user : this.users) {
            Object[] rowData = {user.getUsername(), user.getRole(), user.getPhoneNumber()};
            tableModel.addRow(rowData);
        }
    }

    private String validateInput(String prompt, String defaultValue) {
        String input;
        do {
            input = JOptionPane.showInputDialog(null, prompt, defaultValue);
            if (input == null) {
                return null;  // User clicked "X" or "Close"
            }
            if (!Pattern.matches("\\s*", input)) {
                return input;
            } else {
                JOptionPane.showMessageDialog(null, "Should not be blank or contain only whitespace.");
            }
        } while (true);
    }


    private class AddUserAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = validateInput("Enter Username:", "");
            String password = validateInput("Enter Password:", "");
            String role = validateInput("Enter Role (admin/user):", "");
            String phoneNumber = validateInput("Enter Phone Number:", "");

            // Check for null values
            if (username == null || password == null || role == null || phoneNumber == null) {
                JOptionPane.showMessageDialog(null, "Operation cancelled or incomplete data.");
                return;
            }

            // Check for duplicate username
            boolean isDuplicate = users.stream().anyMatch(user -> user.getUsername().equals(username));
            if (isDuplicate) {
                JOptionPane.showMessageDialog(null, "Username already exists.");
                return; // Exit the method if username is duplicate
            }

            AbstractUser newUser;
            if ("admin".equals(role)) {
                newUser = new Admin(username, password, role, phoneNumber, null);
            } else {
                newUser = new User(username, password, role, phoneNumber, null);
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

                String newUsername = validateInput("Enter new Username:",user.getUsername());

                String newPassword = validateInput("Enter new Password:", user.getPassword());

                String newRole = validateInput("Enter new Role (admin/user):", user.getRole());

                String newPhoneNumber = validateInput("Enter new Phone Number:", user.getPhoneNumber());

                user.setUsername(newUsername);
                user.setPassword(newPassword);
                user.setRole(newRole);
                user.setPhoneNumber(newPhoneNumber);

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
            Object[] rowData = {newUser.getUsername(), newUser.getRole(), newUser.getPhoneNumber()};
            tableModel.addRow(rowData);

            FileHandler.appendUserToCSVFile("users.csv", newUser);

        } else {
            JOptionPane.showMessageDialog(null, "Username already exists.");
        }
    }


}
