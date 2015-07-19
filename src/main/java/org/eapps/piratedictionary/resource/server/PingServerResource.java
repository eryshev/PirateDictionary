package org.eapps.piratedictionary.resource.server;

/**
 * Created by eryshev-alexey on 16/07/15.
 */

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class PingServerResource extends ServerResource {
    private static final String PING = "Pirate dictionary version: 0 running";

    @Get("txt")
    public String ping() {
        return PING;
    }

}
