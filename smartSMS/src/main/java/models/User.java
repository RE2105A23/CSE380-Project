package main.java.models;

import java.util.List;

public class User extends AbstractUser {
    private List<Server> serverList;
    private String name;
    private String password;
    private String role;

    public User(String name, String password, String role, List<Server> servers) {
        super();
        this.name = name;
        this.password = password;
        System.out.println("Constructor password: " + this.password);  // Debug print
        // Initialize other fields
        this.serverList = servers;  // Make sure this line exists
    }

    public boolean authenticate(String inputPassword) {
        return this.password.equals(inputPassword);
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

    public String getName() {
        return this.name;
    }

    public String getPassword() {
        return this.password;
    }

}
