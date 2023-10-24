package main.java.utils;

import main.java.models.AbstractUser;
import main.java.models.Admin;
import main.java.models.User;
import main.java.models.Server;

import javax.swing.JOptionPane;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FileHandler {

    // Save Serializable data to a file
    public static void saveToFile(String filename, Serializable data) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(data);
        }
    }

    // Read Serializable data from a file
    public static Object readFromFile(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return ois.readObject();
        }
    }

    // Write log data to a file
    public static void writeLog(String fileName, String content) {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)))) {
            out.println(content);
        } catch (IOException e) {
            handleException("Error writing log to file", e);
        }
    }

    // Write users to a text file
    public static void writeUsersToTextFile(String filename, List<AbstractUser> users) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (AbstractUser user : users) {
                writer.write(formatUser(user));
                writer.newLine();
            }
        }
    }

    // Read users from a text file
    public static List<AbstractUser> readUsersFromTextFile(String filename) {
        return readUsersFromFile(filename, "text");
    }

    // Write users to a CSV file
    public static void writeUsersToCSVFile(String filename, List<AbstractUser> users) {
        Set<AbstractUser> uniqueUsers = new HashSet<>(users); // Create a set of unique users
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (AbstractUser user : uniqueUsers) {
                writer.write(formatUser(user));
                writer.newLine();
            }
        } catch (IOException e) {
            handleException("Error writing users to file", e);
        }
    }

    // Read users from a CSV file
    public static List<AbstractUser> readUsersFromCSVFile(String filename) {
        return readUsersFromFile(filename, "CSV");
    }

    private static String formatUser(AbstractUser user) {
        return user.getUsername() + "," + user.getPassword() + "," + user.getRole() + "," + user.getPhoneNumber();
    }

    private static List<AbstractUser> readUsersFromFile(String filename, String fileType) {
        List<AbstractUser> users = new ArrayList<>();
        File file = new File(filename);

        // Check if the file exists; if not, create it
        if (!file.exists()) {
            handleException(filename + " not found. Creating an empty file.", null);
            try {
                file.createNewFile();
            } catch (IOException e) {
                handleException("Error creating new file " + filename, e);
            }
            return users; // Return an empty list as the file was not there
        }

        // Read the file
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                addUserFromLine(line, users);
            }
        } catch (IOException e) {
            handleException("Error reading users from " + fileType + " file", e);
        }
        return users;
    }

    private static void addUserFromLine(String line, List<AbstractUser> users) {
        String[] parts = line.split(",");
        if (parts.length >= 4) {  // Make sure you have at least 4 parts
            String username = parts[0];
            String password = parts[1];
            String role = parts[2];
            String phoneNumber = parts[3];  // Make sure this is not null or empty
            AbstractUser user = createUser(username, password, role, phoneNumber);
            if (user != null) {
                users.add(user);
            }
        }
    }

    private static AbstractUser createUser(String username, String password, String role, String phoneNumber) {
        if ("admin".equals(role)) {
            return new Admin(username, password, role, phoneNumber, null);
        } else {
            return new User(username, password, role, phoneNumber, null);
        }
    }

    public static void handleException(String message, Exception e) {
        if (message != null) {
            System.err.println(message);
        }
        if (e != null) {
            e.printStackTrace();
        }
    }

    // Append a new user to a CSV file
    public static void appendUserToCSVFile(String filename, AbstractUser newUser) {
        List<AbstractUser> existingUsers = readUsersFromCSVFile(filename);

        // Check for duplicate users based on username
        for (AbstractUser user : existingUsers) {
            if (user.getUsername().equals(newUser.getUsername())) {
                handleException("User already exists", null);
                return;
            }
        }

        // Append the new user to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            // Check if this is the first line in the file
            if (existingUsers.size() > 0) {
                writer.newLine();
            }
            writer.write(formatUser(newUser));
        } catch (IOException e) {
            handleException("Error appending user to file", e);
        }
    }

    // Write servers to a CSV file
    public static void writeServersToCSVFile(String filename, ArrayList<Server> servers) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            // Write the header
            //writer.write("Server Name,CPU Limit,Memory Limit,Network Limit");
            writer.newLine();

            // Write the server data
            for (Server server : servers) {
                writer.write(server.getName() + "," + server.getCpuThreshold() + "," + server.getMemoryThreshold() + "," + server.getNetworkThreshold());
                writer.newLine();
            }
        } catch (IOException e) {
            handleException("Error writing servers to CSV file", e);
        }
    }

    // Read servers from a CSV file
    public static List<Server> readServersFromCSVFile(String filename) {
        List<Server> servers = new ArrayList<>();
        File file = new File(filename);
        if (!file.exists()) {
            handleException(filename + " not found. Creating an empty file.", null);
            try {
                file.createNewFile();
            } catch (IOException e) {
                handleException("Error creating new file " + filename, e);
            }
            return servers;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                addServerFromLine(line, servers);
            }
        } catch (IOException e) {
            handleException("Error reading servers from CSV file", e);
        }
        return servers;
    }

    private static String formatServer(Server server) {
        return server.getName() + "," + server.getCpuThreshold() + "," + server.getMemoryThreshold() + "," + server.getNetworkThreshold();
    }

    private static void addServerFromLine(String line, List<Server> servers) {
        String[] parts = line.split(",");
        if (parts.length >= 4) {
            String name = parts[0];
            int cpuThreshold = Integer.parseInt(parts[1]);
            int memoryThreshold = Integer.parseInt(parts[2]);
            int networkThreshold = Integer.parseInt(parts[3]);
            Server server = new Server(name, cpuThreshold, memoryThreshold, networkThreshold);
            servers.add(server);
        }
    }


}
