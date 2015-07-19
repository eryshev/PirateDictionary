package org.eapps.piratedictionary.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eapps.extern.ElasticSearchDriver;
import org.eapps.piratedictionary.persistence.entity.Term;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * Created by eryshev-alexey on 08/07/15.
 */
public class TermPersistence {
    private static final ElasticSearchDriver ES = new ElasticSearchDriver();
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public TermPersistence() {
    }

    @Deprecated
    public String retrieveTermAsString(String termName) {
        ArrayList<Map<String, Object>> responsesList = ES.getByNameAsObjectsList(termName);
        try {
            return MAPPER.writeValueAsString(responsesList);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return("[]");
        }
    }

    public ArrayList<Term> retrieveTermAsList(String termName) {
        String response = ES.getByNameAsString(termName);
        try {
            return MAPPER.readValue(response, new TypeReference<ArrayList<Term>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<Term>();
        }
    }


    public Term storeTerm(Term term) {
        try {
            ES.put(MAPPER.writeValueAsString(term));
            return term;
        } catch (JsonProcessingException e) {
            //it cannot happen
            e.printStackTrace();
            return null;
        }
    }

    @Deprecated
    public void storeTerm(ArrayList<Object> termsAsObject) {
        ArrayList<String> termsAsJson = new ArrayList<String>();
        try {
            for(Object termAsObject: termsAsObject) {
                termsAsJson.add(MAPPER.writeValueAsString(termAsObject));
            }
            ES.putList(termsAsJson);
        } catch (JsonProcessingException e) {
            //it cannot happen
            e.printStackTrace();
        }
    }

    public void removeTerm(Term.PrimaryKey termKey) {
        ES.delete(termKey.name, termKey.author);
    }

    Collection<Term> findByName(String name) {
        //TODO
        Term term = new Term("test_id", "test_name", "test_def1", "test_example", "test_author", "test_date", 100, 0);
        return Collections.singletonList(term);
    }

    void create(Term term) {
        //TODO
    }

    void update(Term term) {
        //TODO
    }

    void remove(Term term) {
        //TODO
    }
}
