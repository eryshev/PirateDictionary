package org.eapps.piratedictionary.resource.server;

import org.eapps.piratedictionary.PirateDictionaryApp;
import org.eapps.piratedictionary.persistence.UserPDPersistence;
import org.eapps.piratedictionary.persistence.entity.UserPD;
import org.eapps.piratedictionary.representation.UserPDRepresentation;
import org.eapps.piratedictionary.utils.UserPDUtils;
import org.eapps.piratedictionary.utils.exception.UserExistsException;
import org.restlet.data.Status;
import org.restlet.engine.util.Base64;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.logging.Level;

import static java.net.URLDecoder.decode;

/**
 * Users resource
 * Created by eryshev-alexey on 18/07/15.
 */
public class UserPDResource extends ServerResource {
    private String userId;
    private static final UserPDUtils USER_PD_UTILS = new UserPDUtils();
    private static final Base64 BASE_64 = new Base64();
    private static final UserPDPersistence USER_PD_FACTORY = new UserPDPersistence();

    /**
     * Method called at the creation of the Resource (ie : each time the
     * resource is called).
     */
    @Override
    public void doInit() {
        getLogger().finer("Initialization of UserPDResource.");

        // Get user's name from URL
        userId = getAttribute("userId");
        try {
            userId = userId != null ? decode(userId, "UTF-8") : null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        getLogger().finer("Initialization of UserPDResource ended. userId = " + userId);
    }


    /**
     * Method called to handle GET requests.
     */
    @Get
    public UserPDRepresentation retrieveUser() {
        getLogger().finer("Retrieve a user.");

        UserPDRepresentation userRep = USER_PD_UTILS.toUserRepresentation(
                USER_PD_FACTORY.getUserPD(userId));

        getLogger().finer("User successfully retrieved.");
        return userRep;
    }

    /**
     * Method called to handle PUT requests.
     */
    @Put
    public UserPDRepresentation createUser(UserPDRepresentation userRep) throws UserExistsException {
        getLogger().finer("Add a user.");

        // Business validation here
        // Check UserPD
        USER_PD_UTILS.validate(userRep);

        // Check if user is already existed
        String id = BASE_64.encode(userRep.getEmail().toCharArray(), false);
        String secret = USER_PD_FACTORY.getSecretById(id);
        System.out.println("SECRET=" + secret);
        if (secret != null) {
            getLogger().log(Level.WARNING, "This user is already existed.");
            throw new UserExistsException("This user is already existed.");
        }

        secret = getChallengeResponse().getRawValue();
        UserPD user = new UserPD(
                id,
                getChallengeResponse().getIdentifier(),
                secret,
                userRep.getEmail(),
                USER_PD_UTILS.formatter.format(new Date()),
                userRep.getName(),
                PirateDictionaryApp.ROLE_USER
                );

        userRep = USER_PD_UTILS.toUserRepresentation(USER_PD_FACTORY.putUserPD(user, secret));

        // Set status as 201 and create http header Location that contains
        // reference to created user
        getResponse().setLocationRef(
                USER_PD_UTILS.getUserURL(userRep.getId()));
        getResponse().setStatus(Status.SUCCESS_CREATED);

        getLogger().finer("User successfully added.");
        return userRep;
    }
//
//    @Deprecated
//    @Post("json")
//    public void storeTerm(ArrayList<Object> termsAsObject) {
//        TERMS_FACTORY.storeTerm(termsAsObject);
//    }
//
//    /**
//     * Method called to handle DELETE requests.
//     */
//    @Delete
//    public void removeTerm(Term.PrimaryKey termKey) {
//        getLogger().finer("Delete a term.");
//
//        // Check authorization
//        RESOURCE_UTILS.checkRole(this, PirateDictionaryApp.ROLE_ADMIN);
//        getLogger().finer("UserPD allowed to delete a term.");
//
//        //TODO check author
//        TERMS_FACTORY.removeTerm(termKey);
//
//        getLogger().finer("Term successfully deleted.");
//    }

}
