package org.eapps.piratedictionary.utils.exception;

import org.restlet.resource.Status;

@Status(409)
public class UserExistsException extends BusinessException {

    private static final long serialVersionUID = 1L;

    public UserExistsException(String message) {
        super(409, message);
    }
}
