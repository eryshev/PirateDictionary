package org.eapps.piratedictionary.representation;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * It's a container for the representation of a UserPD
 * The {@link JacksonXmlRootElement} annotation : the root element of the
 * XML representation will be called "UserPD".
 * Created by eryshev-alexey on 18/07/15.
 */

@JacksonXmlRootElement(localName = "UserPD")
public class UserPDRepresentation {
    private String id;
    private String name;
    private String email;
    private String registrationDate;
    private String role;

    public UserPDRepresentation() {
    }

    public UserPDRepresentation(String id,  String name, String email, String registrationDate, String role) {
        this.id = id;
        this.email = email;
        this.registrationDate = registrationDate;
        this.name = name;
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
