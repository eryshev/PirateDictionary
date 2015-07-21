package org.eapps.piratedictionary.security;

import org.eapps.piratedictionary.PirateDictionaryApp;
import org.eapps.piratedictionary.persistence.UserPDPersistence;
import org.eapps.piratedictionary.persistence.entity.UserPD;
import org.restlet.data.ClientInfo;
import org.restlet.data.Status;
import org.restlet.engine.util.Base64;
import org.restlet.resource.ResourceException;
import org.restlet.security.Enroler;
import org.restlet.security.User;

import java.util.Collections;

/**
 * Created by eryshev-alexey on 21/07/15.
 */
public class CustomEnroler implements Enroler {
    private static final UserPDPersistence USER_PD_PERSISTENCE = new UserPDPersistence();
    private static final Base64 BASE_64 = new Base64();

    public void enrole(ClientInfo clientInfo) {
        User authUser = clientInfo.getUser();

        try {
            UserPD userPD = USER_PD_PERSISTENCE.getUserPD(BASE_64.encode(authUser.getIdentifier().toCharArray(), false));
            if (userPD.getRole().equals("admin"))
                clientInfo.setRoles(Collections.singletonList(PirateDictionaryApp.ROLE_ADMIN));
            else if (userPD.getRole().equals("user"))
                clientInfo.setRoles(Collections.singletonList(PirateDictionaryApp.ROLE_USER));
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Security cannot be provided at the moment.");
        }
    }
}
