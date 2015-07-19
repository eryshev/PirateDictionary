package org.eapps.piratedictionary;

import org.restlet.Component;
import org.restlet.data.Protocol;
import org.restlet.engine.Engine;
import org.restlet.service.LogService;

import java.util.logging.Level;

/**
 * Created by eryshev-alexey on 08/07/15.
 */
public class PirateDictionaryAppRun {
    public static void main(String[] args) throws Exception {
        PirateDictionaryApp.LOGGER.info("Pirate dictionary application starting...");

        // Create a new Component.
        Component component = new Component();

        // Add a new HTTP server listening on port 8182.
        component.getServers().add(Protocol.HTTP, 8182);

        //TODO looking for log configuration
        /*// Declare client connector based on the classloader
		c.getClients().add(Protocol.CLAP);

		// Look for the log configuration file in the current classloader
		c.getLogService().setLogPropertiesRef("clap:///logging.properties");
        * */

        // Attach the sample application.
        component.getDefaultHost().attach(PirateDictionaryApp.API_URL,
                new PirateDictionaryApp());

        // Start the component.
        component.start();

        Engine.setLogLevel(Level.FINER);

        PirateDictionaryApp.LOGGER.info("Pirate dictionary application started");
        PirateDictionaryApp.LOGGER.info("URL: http://localhost:8182" + PirateDictionaryApp.API_URL);
    }
}
