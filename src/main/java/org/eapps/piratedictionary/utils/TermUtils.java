package org.eapps.piratedictionary.utils;

import org.eapps.piratedictionary.PirateDictionaryApp;
import org.eapps.piratedictionary.persistence.entity.Term;
import org.eapps.piratedictionary.representation.TermRepresentation;
import org.eapps.piratedictionary.utils.exception.BadEntityException;

import java.util.ArrayList;

/** Utility class to realize convertions from and to TermRepresentation
 * Created by eryshev-alexey on 16/07/15.
 */
public class TermUtils {

    public TermRepresentation toTermRepresentation(Term term) {
        return new TermRepresentation(
                term.getName(),
                term.getDefinition(),
                term.getExample(),
                term.getAuthor(),
                term.getDate(),
                term.getScore().getPositive(),
                term.getScore().getNegative()
                );
    }

    public Term toTerm(TermRepresentation term) {
        return new Term(
                term.getName(),
                term.getName(),
                term.getDefinition(),
                term.getExample(),
                term.getAuthor(),
                term.getDate(),
                term.getScore().getPositive(),
                term.getScore().getNegative()
        );
    }

    public ArrayList<TermRepresentation> termListToRepList(ArrayList<Term> termList) {
        ArrayList<TermRepresentation> termRepList = new ArrayList<TermRepresentation>();

        for (Term term : termList) {
            termRepList.add(toTermRepresentation(term));
        }

        return termRepList;
    }

    public ArrayList<Term> termRepListToTermList(ArrayList<TermRepresentation> termRepList) {
        ArrayList<Term> termList = new ArrayList<Term>();

        for (TermRepresentation termRep : termRepList) {
            termList.add(toTerm(termRep));
        }

        return termList;
    }

    /**
     * Checks that the given term is valid.
     *
     * @param termRep
     * @throws BadEntityException
     */
    public void validate(TermRepresentation termRep)
            throws BadEntityException {
        if (termRep.getName() == null ||
                termRep.getDefinition() == null ||
                termRep.getExample() == null ||
                termRep.getAuthor() == null ||
                termRep.getName().equals("") ||
                termRep.getDefinition().equals("") ||
                termRep.getExample().equals("") ||
                termRep.getAuthor().equals("")
                ) {
            throw new BadEntityException("Name, definition, example, author must be not null.");
        }
    }

    public String getTermURL(String name) {
        return PirateDictionaryApp.API_URL + "/terms/" + name;
    }
}
