package org.eapps.piratedictionary;

import org.eapps.piratedictionary.resource.server.PingServerResource;
import org.eapps.piratedictionary.resource.server.TermResource;
import org.eapps.piratedictionary.resource.server.UserPDResource;
import org.eapps.piratedictionary.security.PirateEnroler;
import org.eapps.piratedictionary.security.PassVerifier;
import org.restlet.Application;
import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.Method;
import org.restlet.engine.Engine;
import org.restlet.routing.Router;
import org.restlet.security.ChallengeAuthenticator;
import org.restlet.security.MethodAuthorizer;
import org.restlet.security.Role;

import java.util.logging.Logger;

/**
 * Created by eryshev-alexey on 08/07/15.
 */

/**
 * The main class of Pirate Dictionary Application
 */
public class PirateDictionaryApp extends Application {
    /**
     * Application's parameters
     */
    public static final String API_URL = "/piratedictionary/api/v0";
    public static final Logger LOGGER = Engine.getLogger(PirateDictionaryApp.class);

    /**
     * //TODO Now, ROLE_ADMIN doesn't include ROLE_USER, do it.
     * Roles definition
     */
    public static final String ROLE_ADMIN_NAME = "admin";
    public static final String ROLE_USER_NAME = "user";
    public static Role ROLE_ADMIN;
    public static Role ROLE_USER;

    public PirateDictionaryApp() {
        ROLE_ADMIN = new Role(this, ROLE_ADMIN_NAME);
        ROLE_USER = new Role(this, ROLE_USER_NAME);
        getRoles().add(ROLE_ADMIN);
        getRoles().add(ROLE_USER);
    }

    public PirateDictionaryApp(Context context) {
        super(context);
    }

    /**
     * The main class to redefine, we have to configure here
     * the routing, security, filtering
     * @return Configured restlet that represents the chain
     * security->routing->filtering
     */
    @Override
    public Restlet createInboundRoot() {
        // Router's object that points to publicly accessible resources
        Router router = publicResource();

        // Create the api router, protected by a guard
        ChallengeAuthenticator termsApiGuard = createTermsApiGuard();

        // Add protected terms resource
        router.attach("/terms", termsApiGuard);

        // Add protected users resource
        router.attach("/users", createUsersApiGuard());

        return router;
    }


    /** Public accessible resources
     * @return router's object that points to publicly accessible resources
     */
    private Router publicResource() {
        Router router = new Router();
        router.attach("/ping", PingServerResource.class);
        router.attach("/", PingServerResource.class);
        return router;
    }

    /** The main application resources, must be protected by a guard
     * @return router's object that points to main application resources
     */
    private Router createTermsApiRouter() {
        Router router = new Router(this.getContext());
        router.attach("/{termName}", TermResource.class);
        router.attach("/", TermResource.class);
        router.attach("", TermResource.class);
        return router;
    }

    /** The users application resources, must be protected by a guard
     * @return router's object that points to users application resources
     */
    private Router createUsersApiRouter() {
        Router router = new Router(this.getContext());
        router.attach("/{userId}", UserPDResource.class);
        router.attach("/", UserPDResource.class);
        router.attach("", UserPDResource.class);
        return router;
    }

    /**
     * Authorize GET for anonymous users and GET, PUT, DELETE for
     * authenticated users
     */
    private MethodAuthorizer createTermsMethodAuthorizer() {
        MethodAuthorizer methodAuth = new MethodAuthorizer();
        methodAuth.getAnonymousMethods().add(Method.GET);
        methodAuth.getAuthenticatedMethods().add(Method.GET);
        methodAuth.getAuthenticatedMethods().add(Method.PUT);
        methodAuth.getAuthenticatedMethods().add(Method.DELETE);
        return methodAuth;
    }

    /**
     * Authorize PUT for anonymous users and GET, PUT, DELETE for
     * authenticated users
     */
    private MethodAuthorizer createUsersMethodAuthorizer() {
        MethodAuthorizer methodAuth = new MethodAuthorizer();
        methodAuth.getAnonymousMethods().add(Method.PUT);
        methodAuth.getAuthenticatedMethods().add(Method.GET);
        methodAuth.getAuthenticatedMethods().add(Method.PUT);
        methodAuth.getAuthenticatedMethods().add(Method.DELETE);
        return methodAuth;
    }

    /**
     * Create api guard for terms resource
     * @return created guard
     */
    private ChallengeAuthenticator createTermsApiGuard() {
        ChallengeAuthenticator apiGuard = new ChallengeAuthenticator(
                getContext(), ChallengeScheme.HTTP_BASIC, "pirateDict");

        PassVerifier passVerifier = new PassVerifier();
        apiGuard.setVerifier(passVerifier);

        PirateEnroler customEnroller = new PirateEnroler();
        apiGuard.setEnroler(customEnroller);

        apiGuard.setOptional(true);

        MethodAuthorizer ma = createTermsMethodAuthorizer();
        ma.setNext(createTermsApiRouter());
        apiGuard.setNext(ma);

        return apiGuard;
    }

    /**
     * Create api guard for users resource
     * @return created guard
     */
    private ChallengeAuthenticator createUsersApiGuard() {
        ChallengeAuthenticator apiGuard = new ChallengeAuthenticator(
                getContext(), ChallengeScheme.HTTP_BASIC, "pirateDict");

        PassVerifier passVerifier = new PassVerifier();
        apiGuard.setVerifier(passVerifier);

        PirateEnroler customEnroller = new PirateEnroler();
        apiGuard.setEnroler(customEnroller);

        apiGuard.setOptional(true);

        MethodAuthorizer ma = createUsersMethodAuthorizer();
        ma.setNext(createUsersApiRouter());
        apiGuard.setNext(ma);

        return apiGuard;
    }
}
