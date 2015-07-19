package org.eapps.piratedictionary.resource.server;

import org.eapps.piratedictionary.PirateDictionaryApp;
import org.eapps.piratedictionary.persistence.TermPersistence;
import org.eapps.piratedictionary.persistence.entity.Term;
import org.eapps.piratedictionary.representation.TermRepresentation;
import org.eapps.piratedictionary.utils.ResourceUtils;
import org.eapps.piratedictionary.utils.TermUtils;
import org.eapps.piratedictionary.utils.exception.BadEntityException;
import org.restlet.data.Status;
import org.restlet.resource.*;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import static java.net.URLDecoder.decode;

/**
 * Created by eryshev-alexey on 08/07/15.
 */
public class TermResource extends ServerResource {
    private static final TermPersistence TERMS_FACTORY = new TermPersistence();
    private static final TermUtils TERMS_UTILS = new TermUtils();
    private static final ResourceUtils RESOURCE_UTILS = new ResourceUtils();
    private String termName;

    /**
     * Method called at the creation of the Resource (ie : each time the
     * resource is called).
     */
    @Override
    public void doInit() {
        getLogger().finer("Initialization of TermResource.");

        // Get term's name from URL
        termName = getAttribute("termName");
        try {
            termName = decode(termName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        getLogger().finer("Initialization of TermResource ended. termName = " + termName);
    }

    //TODO validation and Business exceptions
    /**
     * Method called to handle GET requests.
     */
    @Get
    public ArrayList<TermRepresentation> retrieveTerm() {
        getLogger().finer("Retrieve term.");

        ArrayList<TermRepresentation> termRep = TERMS_UTILS.termListToRepList(
                TERMS_FACTORY.retrieveTermAsList(termName));

        getLogger().finer("Term successfully retrieved.");
        return termRep;
    }

    /**
     * Method called to handle PUT requests.
     */
    @Put
    public TermRepresentation storeTerm(TermRepresentation term) throws BadEntityException {
        getLogger().finer("Add a term.");

        // Check authorization
        RESOURCE_UTILS.checkRole(this, PirateDictionaryApp.ROLE_USER);
        getLogger().finer("UserPD allowed to add a term.");

        // Business validation here
        // Check Term
        TERMS_UTILS.validate(term);

        TermRepresentation termRep = TERMS_UTILS.toTermRepresentation(
                TERMS_FACTORY.storeTerm(TERMS_UTILS.toTerm(term)));

        // Set status as 201 and create http header Location that contains
        // reference to created term
        getResponse().setLocationRef(
                TERMS_UTILS.getTermURL(termRep.getName()));
        getResponse().setStatus(Status.SUCCESS_CREATED);

        getLogger().finer("Term successfully added.");
        return termRep;
    }

    @Deprecated
    @Post("json")
    public void storeTerm(ArrayList<Object> termsAsObject) {
        TERMS_FACTORY.storeTerm(termsAsObject);
    }

    /**
     * Method called to handle DELETE requests.
     */
    @Delete
    public void removeTerm(Term.PrimaryKey termKey) {
        getLogger().finer("Delete a term.");

        // Check authorization
        RESOURCE_UTILS.checkRole(this, PirateDictionaryApp.ROLE_ADMIN);
        getLogger().finer("UserPD allowed to delete a term.");

        //TODO check author
        TERMS_FACTORY.removeTerm(termKey);

        getLogger().finer("Term successfully deleted.");
    }
}
