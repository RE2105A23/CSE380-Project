package main.java.utils;

import main.java.models.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class UserDatabase {
    public static List<User> users = new ArrayList<>();

    static {
        users.add(new User("admin", "123", "admin", Collections.emptyList()));
        users.add(new User("user", "u123", "user", Collections.emptyList()));
    }

    public static boolean authenticate(String username, String password) {
        for (User user : users) {
            if (user.getName().equals(username) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }
}
