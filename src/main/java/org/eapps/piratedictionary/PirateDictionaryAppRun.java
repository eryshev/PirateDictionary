package org.eapps.piratedictionary;

import org.restlet.Application;
import org.restlet.Component;
import org.restlet.data.Protocol;
import org.restlet.engine.Engine;
import org.restlet.service.CorsService;
import org.restlet.service.LogService;

import java.util.Collections;
import java.util.HashSet;
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

        // Add Cors service
        CorsService corsService = new CorsService();
        corsService.setAllowedOrigins(new HashSet<String>(Collections.singletonList("*")));
        corsService.setAllowedCredentials(true);
        corsService.setSkippingResourceForCorsOptions(true);
        Application application = new PirateDictionaryApp();
        application.getServices().add(corsService);

        // Attach the sample application.
        component.getDefaultHost().attach(PirateDictionaryApp.API_URL,
                application);

        // Start the component.
        component.start();

        //Engine.setLogLevel(Level.FINER);

        PirateDictionaryApp.LOGGER.info("Pirate dictionary application started");
        PirateDictionaryApp.LOGGER.info("URL: http://localhost:8182" + PirateDictionaryApp.API_URL);
    }
}
