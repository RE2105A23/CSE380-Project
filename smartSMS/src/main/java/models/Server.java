package main.java.models;

import main.java.exceptions.ServerException;
import main.java.utils.FileHandler;

public class Server {
    private String name;
    private double cpuUsage;
    private double memoryUsage;
    private double networkLatency;

    public Server(String name, int cpuUsage, int memoryUsage, int networkLatency) {
        this.name = name;
        this.cpuUsage = cpuUsage;
        this.memoryUsage = memoryUsage;
        this.networkLatency = networkLatency;
    }

    public void simulateMonitoring() {
        try {
            // existing code
            // Simulate CPU usage
            this.cpuUsage = Math.random() * 100;

            // Simulate Memory usage
            this.memoryUsage = Math.random() * 100;

            // Simulate Network latency
            this.networkLatency = Math.random() * 100;
        } catch(RuntimeException e) {
            throw new ServerException("Error during server monitoring: " + e.getMessage());
        }
    }

    // Getter methods for the metrics
    public double getCpuUsage() {
        return cpuUsage;
    }

    public double getMemoryUsage() {
        return memoryUsage;
    }

    public double getNetworkLatency() {
        return networkLatency;
    }

    public String checkThresholds() {
        StringBuilder message = new StringBuilder();
        if (cpuUsage > 90) {
            message.append("Warning: CPU usage is high. ");
        }
        if (memoryUsage > 90) {
            message.append("Warning: Memory usage is high. ");
        }
        if (networkLatency > 90) {
            message.append("Warning: Network latency is high. ");
        }
        return message.toString();
    }

    public String getName() {
        return this.name;
    }

    public void restart() {
        //System.out.println("Server is restarting...");  // Debugging line
        this.cpuUsage = 0.0;
        this.memoryUsage = 0.0;
        this.networkLatency = 0.0;

        // Log the restart action
        //System.out.println("Server restarted. Metrics set to 0.0");
        System.out.println("Server " + this.getName() + " metrics after restart: CPU=" + this.cpuUsage + ", Memory=" + this.memoryUsage + ", Latency=" + this.networkLatency);
        //FileHandler.writeLog("server_actions.txt", "Restarted " + this.getName());
    }
}

