package payroc.challenge.loadbalancer.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import payroc.challenge.loadbalancer.core.ServiceNode;
import payroc.challenge.loadbalancer.core.NetworkLoadBalancer;

import java.util.HashMap;
import java.util.Map;

import static payroc.challenge.loadbalancer.config.Constants.LOAD_BALANCER_PORT;

public class LoadBalancerDashboard extends Application {
    private final Map<ServiceNode, Label> labelMap = new HashMap<>();
    private static final String LOCAL_HOST = "localhost";
    private static final String LOAD_BALANCER_RUNNING = "Load Balancer Running on Port 9000";
    private static final String LOAD_BALANCER_DASHBOARD = "Load Balancer Dashboard";

    @Override
    public void start(Stage stage) {
        NetworkLoadBalancer balancer = new NetworkLoadBalancer(LOAD_BALANCER_PORT);

        ServiceNode node1 = new ServiceNode(LOCAL_HOST, 9100);
        ServiceNode node2 = new ServiceNode(LOCAL_HOST, 9200);

        balancer.registerService(node1);
        balancer.registerService(node2);

        new Thread(balancer::launch).start();

        VBox vbox = new VBox(10);
        vbox.getChildren().add(new Label(LOAD_BALANCER_RUNNING));

        setupLabelUpdater(vbox, node1);
        setupLabelUpdater(vbox, node2);

        stage.setTitle(LOAD_BALANCER_DASHBOARD);
        stage.setScene(new Scene(vbox, 400, 200));
        stage.show();
    }

    private void setupLabelUpdater(VBox vbox, ServiceNode node) {
        Label label = new Label(node.getDisplayName() + " - Requests: 0");
        labelMap.put(node, label);
        vbox.getChildren().add(label);

        new Thread(() -> {
            while (true) {
                String updatedText = node.getDisplayName() + " - Requests: " + node.getTrafficCount();
                Platform.runLater(() -> label.setText(updatedText));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                }
            }
        }).start();
    }
}