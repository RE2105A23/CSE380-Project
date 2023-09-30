import static org.junit.Assert.*;
import org.junit.Test;
import java.util.List;
import main.java.models.Server;  // Replace with the actual package name of your Server class


public class ServerTest {

    @Test
    public void testGetCpuUsage() {
        Server server = new Server("Server1", 20, 50, 100);
        double expectedCpuUsage = 20.0;
        double delta = 0.001;  // The acceptable error margin, adjust as needed
        assertEquals(expectedCpuUsage, server.getCpuUsage(), delta);
    }


    @Test
    public void testGetMemoryUsage() {
        Server server = new Server("Server1", 20, 50, 100);
        double expectedMemoryUsage = 50.0; // Replace with the expected value
        double delta = 0.001;  // The acceptable error margin, adjust as needed
        assertEquals(expectedMemoryUsage, server.getMemoryUsage(), delta);
    }

    @Test
    public void testRestart() {
        Server server = new Server("Server1", 20, 50, 100);
        // Your logic to restart the server
        server.restart();

        double expectedValue = 0.0; // Replace with the expected value after restart
        double delta = 0.001;  // The acceptable error margin, adjust as needed

        assertEquals(expectedValue, server.getCpuUsage(), delta);
    }

}
