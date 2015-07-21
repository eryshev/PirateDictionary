package org.eapps.piratedictionary.resource.server;

import org.eapps.piratedictionary.PirateDictionaryApp;
import org.eapps.piratedictionary.persistence.UserPDPersistence;
import org.eapps.piratedictionary.persistence.entity.UserPD;
import org.eapps.piratedictionary.representation.UserPDRepresentation;
import org.eapps.piratedictionary.security.SecurityUtils;
import org.eapps.piratedictionary.utils.ResourceUtils;
import org.eapps.piratedictionary.utils.UserPDUtils;
import org.eapps.piratedictionary.utils.exception.UserExistsException;
import org.restlet.data.Status;
import org.restlet.engine.util.Base64;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
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
    private static final ResourceUtils RESOURCE_UTILS = new ResourceUtils();
    private static final SecurityUtils SECURITY_UTILS = new SecurityUtils();
    private static final int SALT_BYTE_SIZE = 24;

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

        // Check authorization
        RESOURCE_UTILS.checkRole(this, PirateDictionaryApp.ROLE_USER_NAME);
        getLogger().finer("UserPD allowed to delete a term.");

        // Validation logic, take userId from authentication's token
        userId = BASE_64.encode(getChallengeResponse().getIdentifier().toCharArray(), false);

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
        String id = BASE_64.encode(getChallengeResponse().getIdentifier().toCharArray(), false);
        String[] secret = USER_PD_FACTORY.getSecretById(id);

        if (secret != null) {
            getLogger().log(Level.WARNING, "This user is already existed.");
            throw new UserExistsException("This user is already existed.");
        }

        // Validate authentication data
        USER_PD_UTILS.checkIdAndSecret(getChallengeResponse().getIdentifier(), getChallengeResponse().getSecret());

        // Generate a random salt
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_BYTE_SIZE];
        random.nextBytes(salt);

        salt = SECURITY_UTILS.toHex(salt);
        secret = new String[2];
        secret[0] = new String(salt);

        SECURITY_UTILS.MESSAGE_DIGEST.update((secret[0] + new String(getChallengeResponse().getSecret()))
                .getBytes(StandardCharsets.UTF_8));

        secret[1] = new String(SECURITY_UTILS.toHex(SECURITY_UTILS.MESSAGE_DIGEST.digest()));

        UserPD user = new UserPD(
                id,
                userRep.getEmail(),
                USER_PD_UTILS.formatter.format(new Date()),
                userRep.getName(),
                PirateDictionaryApp.ROLE_USER_NAME
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
