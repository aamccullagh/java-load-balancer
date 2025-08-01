package payroc.challenge.loadbalancer.core;

import java.io.IOException;
import java.net.Socket;

public class ServiceNode {
    private final String hostname;
    private final int port;
    private final String displayName;
    private boolean online = true;
    private int trafficCount = 0;

    public ServiceNode(String hostname, int port) {
        this(hostname, port, hostname + ":" + port);
    }

    public ServiceNode(String hostname, int port, String displayName) {
        this.hostname = hostname;
        this.port = port;
        this.displayName = displayName;
    }

    public String getHostname() {
        return hostname;
    }

    public int getPort() {
        return port;
    }

    public String getDisplayName() {
        return displayName;
    }

    public synchronized void incrementTraffic() {
        trafficCount++;
    }

    public synchronized int getTrafficCount() {
        return trafficCount;
    }

    public boolean isOnline() {
        return online;
    }

    public void checkHealth() {
        try (Socket socket = new Socket(hostname, port)) {
            online = true;
        } catch (IOException e) {
            online = false;
        }
    }

    @Override
    public String toString() {
        return displayName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServiceNode)) return false;
        ServiceNode that = (ServiceNode) o;
        return port == that.port && hostname.equals(that.hostname);
    }

    @Override
    public int hashCode() {
        return hostname.hashCode() * 31 + port;
    }
}