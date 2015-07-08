import org.restlet.Component;
import org.restlet.data.Protocol;

/**
 * Created by eryshev-alexey on 08/07/15.
 */
public class PirateDictionaryAppRun {
    public static void main(String[] args) throws Exception {
        // Create a new Component.
        Component component = new Component();

        // Add a new HTTP server listening on port 8182.
        component.getServers().add(Protocol.HTTP, 8182);

        // Attach the sample application.
        component.getDefaultHost().attach("/piratedictionary/api/v0",
                new PirateDictionaryApp());

        // Start the component.
        component.start();
    }
}
