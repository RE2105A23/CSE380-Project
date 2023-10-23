package main.java.utils;

import main.java.models.AbstractUser;
import main.java.models.Admin;
import main.java.models.User;

import java.util.ArrayList;
import java.util.List;

public class UserDatabase {
    public static List<AbstractUser> users = new ArrayList<>();

    static {
        users.add(new Admin("admin", "123", "admin", "123-4567-890" , null));
        users.add(new User("user", "u123", "user", "098-7654-321" ,null));
    }

    public static AbstractUser authenticate(String username, String password) {
        for (AbstractUser user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }
}

