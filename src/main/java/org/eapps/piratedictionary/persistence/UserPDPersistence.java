package org.eapps.piratedictionary.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eapps.extern.ElasticSearchDriver;
import org.eapps.piratedictionary.persistence.entity.UserPD;
import org.eapps.piratedictionary.utils.exception.BadEntityException;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

import java.io.IOException;

/**
 * Created by eryshev-alexey on 08/07/15.
 */
public class UserPDPersistence {
    private static final ElasticSearchDriver ES = new ElasticSearchDriver();
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public UserPDPersistence() {
    }

    public String[] getSecretById(String id) {
        return ES.getSecretById(id);
    }

    public UserPD putUserPD(UserPD userPD, String[] secret) {
        try {
            ES.putUserPD(MAPPER.writeValueAsString(userPD),
                    userPD.getId(),
                    secret);
            return userPD;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public UserPD getUserPD(String userId) {
        try {
            return MAPPER.readValue(ES.getUserById(userId), UserPD.class);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Stored user is incorrect.");
        }
    }
}
