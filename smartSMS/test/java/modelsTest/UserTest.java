package modelsTest;
//package main.java.models;

import static org.junit.Assert.*;
import org.junit.Test;
import main.java.models.User;
import java.util.List;
import java.util.ArrayList;
import main.java.models.Server;



public class UserTest {

    @Test
    public void testGetName() {
        List<Server> servers = new ArrayList<>();
        User user = new User("John", "password", "someOtherString", servers);
        assertEquals("Name should be John", "John", user.getName());
    }

    // Add more test methods as needed

    @Test
    public void testAuthenticate() {
        List<Server> servers = new ArrayList<>();
        User user = new User("John", "password123", "someOtherField", servers);

        // Test successful authentication
        assertTrue("Authentication should succeed", user.authenticate("password123"));

        // Test failed authentication
        assertFalse("Authentication should fail", user.authenticate("wrongPassword"));
    }

    @Test
    public void testGetPassword() {
        List<Server> servers = new ArrayList<>();
        User user = new User("John", "password123", "someOtherField", servers);
        System.out.println("Test password: " + user.getPassword());  // Debug print
        assertEquals("Password should match", "password123", user.getPassword());
    }



}
