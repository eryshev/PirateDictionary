package org.eapps.piratedictionary.utils;

import org.eapps.piratedictionary.PirateDictionaryApp;
import org.eapps.piratedictionary.persistence.entity.UserPD;
import org.eapps.piratedictionary.representation.UserPDRepresentation;
import org.eapps.piratedictionary.utils.exception.BadEntityException;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

import java.text.Format;
import java.text.SimpleDateFormat;

/** Utility class to realize conversions from and to UserPDRepresentation
 * Created by eryshev-alexey on 16/07/15.
 */
public class UserPDUtils {
    public Format formatter = new SimpleDateFormat("MMMM dd, YYYY");

    public UserPDRepresentation toUserRepresentation(UserPD userPD) {
        return new UserPDRepresentation(
                userPD.getId(),
                userPD.getName(),
                userPD.getEmail(),
                userPD.getRegistrationDate(),
                userPD.getRole()
        );
    }

    /**
     * Checks that the given UserPD representation is valid.
     *
     * @param userRep
     * @throws BadEntityException
     */
    public void validate(UserPDRepresentation userRep)
            throws BadEntityException {
        if (userRep.getEmail() == null ||
                userRep.getName() == null ||
                userRep.getEmail().equals("") ||
                userRep.getName().equals("")
                )
        {
            throw new BadEntityException("Email, name, must be not null.");
        }
    }

    public String getUserURL(String id) {
        return PirateDictionaryApp.API_URL + "/users/" + id;
    }

    public void checkIdAndSecret(String identifier, char[] secret) throws ResourceException {
        if (identifier == null ||
                identifier.equals("") ||
                secret.length < 4
                )
            throw new ResourceException(Status.CLIENT_ERROR_NOT_ACCEPTABLE, "Authentication data is not correct. " +
                    "Identificator shouldn't be null or empty and password should contain at least 4 symbols.");
    }

    public void validateId(String id) {
        if (id == null || id.equals(""))
            throw new ResourceException(Status.CLIENT_ERROR_NOT_ACCEPTABLE, "User id is not correct." +
                    "It shouldn't be null or empty.");
    }
}
