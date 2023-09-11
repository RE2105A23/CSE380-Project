package main.java.utils;

import java.io.*;
import java.util.ArrayList;

public class FileHandler {

    // Method to save data to a file
    public static void saveDataToFile(String filename, ArrayList<Object> data) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filename);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(data);
        }
    }

    // Method to read data from a file
    public static ArrayList<Object> readDataFromFile(String filename) throws IOException, ClassNotFoundException {
        ArrayList<Object> data = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(filename);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            data = (ArrayList<Object>) ois.readObject();
        }
        return data;
    }
}
