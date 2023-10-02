package main.java.models;

import java.util.List;

public class User extends AbstractUser {
    private List<Server> serverList;
    private String name;
    private String password;
    private String role;

    public User(String name, String password, String role, List<Server> servers) {
        super();
        if (name == null || password == null || role == null || servers == null) {
            throw new IllegalArgumentException("Constructor arguments cannot be null");
        }
        this.name = name;
        this.password = password;
        this.role = role;
        this.serverList = servers;
        System.out.println("Constructor password: " + this.password);  // Debug print
    }

    public boolean authenticate(String inputPassword) {
        if (inputPassword == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }
        return this.password.equals(inputPassword);
    }

    // Getters and Setters for serverList
    public List<Server> getServerList() {
        if (serverList == null) {
            throw new IllegalStateException("Server list is not initialized");
        }
        return serverList;
    }

    public void setServerList(List<Server> serverList) {
        if (serverList == null) {
            throw new IllegalArgumentException("Server list cannot be null");
        }
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
        if (server == null) {
            throw new IllegalArgumentException("Server cannot be null");
        }
        // Logic to request server restart
    }

    public String getName() {
        return this.name;
    }

    public String getPassword() {
        return this.password;
    }
}
