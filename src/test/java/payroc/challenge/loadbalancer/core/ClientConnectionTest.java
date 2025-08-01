package payroc.challenge.loadbalancer.core;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;

import static org.mockito.Mockito.*;

public class ClientConnectionTest {

    @Test
    public void testDataForwarding() throws Exception {
        Socket mockClientSocket = mock(Socket.class);
        InputStream clientIn = new ByteArrayInputStream("ping\n".getBytes());
        OutputStream clientOut = new ByteArrayOutputStream();
        when(mockClientSocket.getInputStream()).thenReturn(clientIn);
        when(mockClientSocket.getOutputStream()).thenReturn(clientOut);

        Socket mockBackendSocket = mock(Socket.class);
        InputStream backendIn = new ByteArrayInputStream("pong\n".getBytes());
        OutputStream backendOut = new ByteArrayOutputStream();
        when(mockBackendSocket.getInputStream()).thenReturn(backendIn);
        when(mockBackendSocket.getOutputStream()).thenReturn(backendOut);

        ServiceNode mockNode = mock(ServiceNode.class);
        when(mockNode.getHostname()).thenReturn("localhost");
        when(mockNode.getPort()).thenReturn(9100);

        ClientConnection proxy = new ClientConnection(mockClientSocket, mockNode, mockBackendSocket);
        proxy.run();

        verify(mockNode, times(1)).incrementTraffic();
    }
}
