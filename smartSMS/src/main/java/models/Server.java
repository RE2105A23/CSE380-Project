package main.java.models;

public class Server {
    private String name;
    private double cpuUsage;
    private double memoryUsage;
    private double networkLatency;

    public Server(String name) {
        this.name = name;
    }

    public void simulateMonitoring() {
        // Simulate CPU usage
        this.cpuUsage = Math.random() * 100;

        // Simulate Memory usage
        this.memoryUsage = Math.random() * 100;

        // Simulate Network latency
        this.networkLatency = Math.random() * 100;
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

}

