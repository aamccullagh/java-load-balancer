package payroc.challenge.loadbalancer.core;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ServiceNodeTest {

    @Test
    public void testHealthCheckOnline() {
        ServiceNode node = new ServiceNode("localhost", 9100);
        node.checkHealth();
        System.out.println("Health check (localhost:9100): " + node.isOnline());
    }

    @Test
    public void testRequestCounting() {
        ServiceNode node = new ServiceNode("localhost", 9100);
        assertEquals(0, node.getTrafficCount());
        node.incrementTraffic();
        node.incrementTraffic();
        assertEquals(2, node.getTrafficCount());
    }
}
