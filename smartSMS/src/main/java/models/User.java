package main.java.models;

import java.util.List;
import java.io.Serializable;

public class User extends AbstractUser implements Serializable {
    private List<Server> serverList;

    public User(String username, String password, String role, List<Server> servers) {
        super(username, password, role);
        this.serverList = servers;
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
        System.out.println("User " + getUsername() + " logged in.");
    }

    @Override
    public void logout() {
        System.out.println("User " + getUsername() + " logged out.");
    }

    public void viewServerStatus() {
        if (hasPrivilege("view")) {
            for (Server server : serverList) {
                System.out.println("Server: " + server.getName() + ", CPU: " + server.getCpuUsage() + ", Memory: " + server.getMemoryUsage() + ", Latency: " + server.getNetworkLatency());
            }
        } else {
            System.out.println("Insufficient privileges to view server status.");
        }
    }

    public void requestServerRestart(Server server) {
        if (hasPrivilege("restart")) {
            System.out.println("Restart request sent for server: " + server.getName());
        } else {
            System.out.println("Insufficient privileges to request server restart.");
        }
    }

    private boolean hasPrivilege(String action) {
        // You can extend this method to include more complex role-based logic
        switch (action) {
            case "view":
                return "user".equals(getRole()) || "admin".equals(getRole());
            case "restart":
                return "admin".equals(getRole());
            default:
                return false;
        }
    }
}
