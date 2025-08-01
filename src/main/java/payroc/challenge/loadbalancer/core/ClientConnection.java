package payroc.challenge.loadbalancer.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import static payroc.challenge.loadbalancer.config.Constants.BUFFER_SIZE;

public class ClientConnection implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(ClientConnection.class);
    private final Socket sourceSocket;
    private final ServiceNode destinationNode;
    private final Socket overrideBackendSocket;

    public ClientConnection(Socket sourceSocket, ServiceNode destinationNode) {
        this(sourceSocket, destinationNode, null);
    }

    public ClientConnection(Socket sourceSocket, ServiceNode destinationNode, Socket overrideBackendSocket) {
        this.sourceSocket = sourceSocket;
        this.destinationNode = destinationNode;
        this.overrideBackendSocket = overrideBackendSocket;
    }

    @Override
    public void run() {
        try (Socket backendSocket = overrideBackendSocket != null
                ? overrideBackendSocket
                : new Socket(destinationNode.getHostname(), destinationNode.getPort());
             InputStream inClient = sourceSocket.getInputStream();
             OutputStream outClient = sourceSocket.getOutputStream();
             InputStream inBackend = backendSocket.getInputStream();
             OutputStream outBackend = backendSocket.getOutputStream()) {

            destinationNode.incrementTraffic();

            Thread forward = new Thread(() -> proxy(inClient, outBackend));
            Thread backward = new Thread(() -> proxy(inBackend, outClient));

            forward.start();
            backward.start();
            forward.join();
            backward.join();

        } catch (Exception e) {
            logger.error("Proxy connection failure with {}", destinationNode, e);
        } finally {
            try {
                sourceSocket.close();
            } catch (Exception e) {
                logger.error("Error closing client socket", e);
            }
        }
    }

    private void proxy(InputStream input, OutputStream output) {
        try {
            byte[] buffer = new byte[BUFFER_SIZE];
            int count;
            while ((count = input.read(buffer)) != -1) {
                output.write(buffer, 0, count);
                output.flush();
            }
        } catch (Exception ignored) {
        }
    }
}