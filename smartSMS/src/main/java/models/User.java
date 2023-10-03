package main.java.models;

import java.util.List;

public class User extends AbstractUser {
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

    /*
    public void viewServerStatus() {
        for (Server server : serverList) {
            System.out.println("Server: " + server.getName() + ", CPU: " + server.getCpuUsage() + ", Memory: " + server.getMemoryUsage() + ", Latency: " + server.getNetworkLatency());
        }
    }

    public void requestServerRestart(Server server) {
        System.out.println("Restart request sent for server: " + server.getName());
    }
    */

    public void viewServerStatus() {
        if ("user".equals(getRole())) {
            // Logic to view server status
            for (Server server : serverList) {
                System.out.println("Server: " + server.getName() + ", CPU: " + server.getCpuUsage() + ", Memory: " + server.getMemoryUsage() + ", Latency: " + server.getNetworkLatency());
            }
        } else {
            System.out.println("Insufficient privileges to view server status.");
        }
    }

    public void requestServerRestart(Server server) {
        if ("user".equals(getRole())) {
            // Logic to request server restart
            System.out.println("Restart request sent for server: " + server.getName());
        } else {
            System.out.println("Insufficient privileges to request server restart.");
        }
    }

}
