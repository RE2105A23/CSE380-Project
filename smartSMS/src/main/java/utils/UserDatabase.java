package main.java.utils;

import java.util.HashMap;

public class UserDatabase {
    private static HashMap<String, String> users = new HashMap<>();

    static {
        // Adding some sample users
        users.put("admin", "password");
        users.put("user1", "password1");
    }

    public static boolean authenticate(String username, String password) {
        return users.containsKey(username) && users.get(username).equals(password);
    }
}
