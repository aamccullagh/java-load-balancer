package payroc.challenge.loadbalancer.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class NetworkLoadBalancerTest {

    private NetworkLoadBalancer loadBalancer;
    private ServiceNode node1;
    private ServiceNode node2;

    @BeforeEach
    void setup() {
        loadBalancer = new NetworkLoadBalancer(9000);
        node1 = mock(ServiceNode.class);
        node2 = mock(ServiceNode.class);

        when(node1.isOnline()).thenReturn(true);
        when(node2.isOnline()).thenReturn(true);

        loadBalancer.registerService(node1);
        loadBalancer.registerService(node2);
    }

    @Test
    void testRoundRobinLogic() {
        ServiceNode selected1 = loadBalancer.getServiceNodes().get(0);
        ServiceNode selected2 = loadBalancer.getServiceNodes().get(1);

        assertNotNull(selected1);
        assertNotNull(selected2);
        assertNotEquals(selected1, selected2); // Should alternate in round-robin
    }
}
