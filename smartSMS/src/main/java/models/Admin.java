package main.java.models;

import java.util.List;
import java.io.Serializable;

public class Admin extends AbstractUser implements Serializable {
    private List<Server> managedServers;

    public Admin(String username, String password, String role, String phoneNumber, List<Server> managedServers) {
        super(username, password, role, phoneNumber);
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
        System.out.println("Admin " + getUsername() + " logged in.");
    }

    @Override
    public void logout() {
        System.out.println("Admin " + getUsername() + " logged out.");
    }

    public void viewAllServerStatus() {
        for (Server server : managedServers) {
            System.out.println("Server: " + server.getName() + ", CPU: " + server.getCpuUsage() + ", Memory: " + server.getMemoryUsage() + ", Latency: " + server.getNetworkLatency());
        }
    }

    public void approveRestartRequest(Server server) {
        server.restart();
        System.out.println("Restart request approved for server: " + server.getName());
    }
    public void addServer(Server server) {
        if ("admin".equals(getRole())) {
            // Logic to add a server
            managedServers.add(server);
            System.out.println("Server added: " + server.getName());
        } else {
            System.out.println("Insufficient privileges to add server.");
        }
    }

    public void removeServer(Server server) {
        if ("admin".equals(getRole())) {
            // Logic to remove a server
            managedServers.remove(server);
            System.out.println("Server removed: " + server.getName());
        } else {
            System.out.println("Insufficient privileges to remove server.");
        }
    }
}
