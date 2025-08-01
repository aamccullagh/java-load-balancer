package payroc.challenge.loadbalancer.servers;

import java.io.*;
import java.net.*;

import static payroc.challenge.loadbalancer.config.Constants.BACKEND_SERVER_1_PORT;

public class BackendNode1 {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(BACKEND_SERVER_1_PORT);
        System.out.println("BackendServer1 running on port 9100");
        while (true) {
            Socket socket = serverSocket.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            String line;
            while ((line = in.readLine()) != null) {
                out.println("Backend1: " + line);
            }
        }
    }
}
