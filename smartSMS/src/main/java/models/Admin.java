package main.java.models;

import java.util.List;

public class Admin extends AbstractUser {
    private List<Server> managedServers;

    public Admin(String username, String password, String role, List<Server> managedServers) {
        super(username, password, role);
        this.managedServers = managedServers;
    }

    // Getters and Setters for managedServers
    public List<Server> getManagedServers() {
        return managedServers;
    }

    public void setManagedServers(List<Server> managedServers) {
        this.managedServers = managedServers;
    }

    @Override
    public void login() {
        // Implement login logic for admin
    }

    @Override
    public void logout() {
        // Implement logout logic for admin
    }

    public void addServer(Server server) {
        // Logic to add a server
    }

    public void removeServer(Server server) {
        // Logic to remove a server
    }

    public void viewAllServerStatus() {
        // Logic to view status of all servers
    }

    public void approveRestartRequest(Server server) {
        // Logic to approve restart request
    }
}