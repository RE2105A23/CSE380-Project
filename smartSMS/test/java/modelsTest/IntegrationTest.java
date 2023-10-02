package modelsTest;
import main.java.models.User;
import main.java.models.Server;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class IntegrationTest {

    private User user;
    private Server server;

    @Before
    public void setUp() {
        server = new Server("Server1", 16, 500, 4);
        List<Server> servers = new ArrayList<>();
        servers.add(server);
        user = new User("Sri", "password123", "admin", servers);
        System.out.println("Debug: Server list size: " + servers.size());  // Debug print
    }

    @Test
    public void testUserServerInteraction() {
        // Test authentication
        assertTrue(user.authenticate("password123"));
        assertFalse(user.authenticate("wrongPassword"));

        // Test server list
        List<Server> serverList = user.getServerList();
        assertNotNull(serverList);
        assertEquals(1, serverList.size());

        // Test server interaction
        server.simulateMonitoring();
        double cpuUsage = server.getCpuUsage();
        double memoryUsage = server.getMemoryUsage();
        double networkLatency = server.getNetworkLatency();

        assertTrue(cpuUsage >= 0 && cpuUsage <= 100);
        assertTrue(memoryUsage >= 0 && memoryUsage <= 100);
        assertTrue(networkLatency >= 0 && networkLatency <= 100);

        // Test server restart
        server.restart();
        assertEquals(0, server.getCpuUsage(), 0.001);
        assertEquals(0, server.getMemoryUsage(), 0.001);
        assertEquals(0, server.getNetworkLatency(), 0.001);

        // Test user details
        assertEquals("Sri", user.getName());
        assertEquals("password123", user.getPassword());
    }
}
