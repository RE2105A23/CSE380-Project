package main.java.models;

import java.util.List;

public class User extends AbstractUser {
    private List<Server> serverList;

    public User(String username, String password, String role, List<Server> serverList) {
        super(username, password, role);
        this.serverList = serverList;
    }

    // Getters and Setters for serverList
    public List<Server> getServerList() {
        return serverList;
    }

    public void setServerList(List<Server> serverList) {
        this.serverList = serverList;
    }

    @Override
    public void login() {
        // Implement login logic for user
    }

    @Override
    public void logout() {
        // Implement logout logic for user
    }

    public void viewServerStatus() {
        // Logic to view server status
    }

    public void requestServerRestart(Server server) {
        // Logic to request server restart
    }
}
