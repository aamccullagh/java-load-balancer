package payroc.challenge.loadbalancer.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static payroc.challenge.loadbalancer.config.Constants.HEALTH_CHECK_INTERVAL_MS;

public class NetworkLoadBalancer {
    private static final Logger logger = LoggerFactory.getLogger(NetworkLoadBalancer.class);
    private final List<ServiceNode> serviceNodes = new ArrayList<>();
    private int currentIndex = 0;
    private final int listeningPort;

    public NetworkLoadBalancer(int listeningPort) {
        this.listeningPort = listeningPort;
    }

    public void registerService(ServiceNode node) {
        serviceNodes.add(node);
    }

    public void launch() {
        new Thread(this::monitorHealth).start();

        try (ServerSocket serverSocket = new ServerSocket(listeningPort)) {
            logger.info("Load balancer started on port {}", listeningPort);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ServiceNode next = selectNextAvailable();
                if (next != null) {
                    new Thread(new ClientConnection(clientSocket, next)).start();
                } else {
                    logger.warn("No available backend services.");
                    clientSocket.close();
                }
            }
        } catch (IOException e) {
            logger.error("Failed to run load balancer", e);
        }
    }

    private synchronized ServiceNode selectNextAvailable() {
        int attempts = 0;
        while (attempts < serviceNodes.size()) {
            ServiceNode node = serviceNodes.get(currentIndex);
            currentIndex = (currentIndex + 1) % serviceNodes.size();
            if (node.isOnline()) {
                return node;
            }
            attempts++;
        }
        return null;
    }

    private void monitorHealth() {
        while (true) {
            for (ServiceNode node : serviceNodes) {
                node.checkHealth();
            }
            try {
                Thread.sleep(HEALTH_CHECK_INTERVAL_MS);
            } catch (InterruptedException e) {
                logger.error("Health monitor interrupted", e);
                Thread.currentThread().interrupt();
            }
        }
    }

    public List<ServiceNode> getServiceNodes() {
        return serviceNodes;
    }
}