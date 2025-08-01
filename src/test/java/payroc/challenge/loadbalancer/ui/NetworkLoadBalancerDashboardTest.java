package payroc.challenge.loadbalancer.ui;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import payroc.challenge.loadbalancer.ui.LoadBalancerDashboard;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class NetworkLoadBalancerDashboardTest {

    @Test
    public void testLaunchGui() {
        new JFXPanel();
        assertDoesNotThrow(() -> Platform.runLater(() -> {
            try {
                LoadBalancerDashboard gui = new LoadBalancerDashboard();
                gui.start(new Stage());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }));
    }
}
