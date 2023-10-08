package main.java.models;

import main.java.exceptions.ServerException;
import main.java.utils.FileHandler;
import java.util.HashMap;
import java.util.Map;

public class Server {
    private String name;
    private double cpuUsage;
    private double memoryUsage;
    private double networkLatency;
    private int cpuThreshold;
    private int memoryThreshold;
    private int networkThreshold;
    // A map to hold subscriptions for different alert types
    private Map<String, Boolean> alertSubscriptions;

    public Server(String name, int cpuThreshold, int memoryThreshold, int networkThreshold) {
        this.name = name;
        this.cpuUsage = cpuUsage;
        this.memoryUsage = memoryUsage;
        this.networkLatency = networkLatency;
        // Initialize the thresholds
        this.cpuThreshold = cpuThreshold;
        this.memoryThreshold = memoryThreshold;
        this.networkThreshold = networkThreshold;
        // Initialize the alert subscriptions map
        alertSubscriptions = new HashMap<>();
        alertSubscriptions.put("CPU Usage", false);
        alertSubscriptions.put("Memory Usage", false);
        alertSubscriptions.put("Network Latency", false);
    }


    // Added getter methods for the thresholds
    public int getCpuThreshold() {
        return cpuThreshold;
    }

    public int getMemoryThreshold() {
        return memoryThreshold;
    }

    public int getNetworkThreshold() {
        return networkThreshold;
    }

    // Add these setter methods for the thresholds
    public void setCpuThreshold(int cpuThreshold) {
        this.cpuThreshold = cpuThreshold;
    }

    public void setMemoryThreshold(int memoryThreshold) {
        this.memoryThreshold = memoryThreshold;
    }

    public void setNetworkThreshold(int networkThreshold) {
        this.networkThreshold = networkThreshold;
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
        StringBuilder message = new StringBuilder("SMS sent to 123-456-7890: ");
        boolean hasWarning = false;

        // Use class variables for thresholds
        if (cpuUsage > cpuThreshold) {
            message.append("Warning: Server " + this.name + " CPU usage is high. ");
            hasWarning = true;
        }
        if (memoryUsage > memoryThreshold) {
            message.append("Warning: Server " + this.name + " Memory usage is high. ");
            hasWarning = true;
        }
        if (networkLatency > networkThreshold) {
            message.append("Warning: Server " + this.name + " Network latency is high. ");
            hasWarning = true;
        }

        return hasWarning ? message.toString() : "";
    }


    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCpuUsage(int cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public void setMemoryUsage(int memoryUsage) {
        this.memoryUsage = memoryUsage;
    }

    public void setNetworkLatency(int networkLatency) {
        this.networkLatency = networkLatency;
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

    public void subscribeToAlert(String alertType) {
        if ("All Alerts".equals(alertType)) {
            for (String key : alertSubscriptions.keySet()) {
                alertSubscriptions.put(key, true);
            }
            System.out.println("Subscribed to all alerts for server " + name);
        } else if (alertSubscriptions.containsKey(alertType)) {
            alertSubscriptions.put(alertType, true);
            System.out.println("Subscribed to " + alertType + " alerts for server " + name);
        } else {
            System.out.println("Invalid alert type: " + alertType);
        }
    }

    // Method to check if the server is subscribed to a particular alert
    public boolean isSubscribedToAlert(String alertType) {
        return alertSubscriptions.getOrDefault(alertType, false);
    }
}

