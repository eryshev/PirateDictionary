package org.eapps.piratedictionary.persistence.entity;

/***
 * Represents a UserPD stored in the database.
 * Created by eryshev-alexey on 18/07/15.
 */
public class UserPD {
    private String id;
    private String login;
    private String password;
    private String email;
    private String registrationDate;
    private String name;
    private String role;

    public UserPD() {
    }

    public UserPD(String id, String login, String password, String email, String registrationDate, String name, String role) {
        this.id = id;
        this.login = login;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
