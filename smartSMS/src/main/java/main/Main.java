package main.java.main;

import main.java.ui.LoginPanel;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    public static void createAndShowGUI() {
        JFrame frame = new JFrame("Smart Server Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 600);

        LoginPanel loginPanel = new LoginPanel();
        frame.add(loginPanel);

        frame.setVisible(true);
    }
}

