package org.eapps.piratedictionary.security;

import org.eapps.piratedictionary.persistence.UserPDPersistence;
import org.restlet.engine.util.Base64;
import org.restlet.security.SecretVerifier;

/**
 * Classs aims at returning the given password according to the given login.
 * Created by eryshev-alexey on 19/07/15.
 */
public class PassVerifier extends SecretVerifier {
    public static final UserPDPersistence USER_PD_PERSISTENCE = new UserPDPersistence();
    private static final Base64 BASE_64 = new Base64();

    //TODO add salted crypto
    @Override
    public int verify(String id, char[] secret) {
        System.out.println(id + " " + secret);
        if (id == null) return RESULT_MISSING;
        String secretFromES = USER_PD_PERSISTENCE.getSecretById(BASE_64.encode(id.toCharArray(), false));
        if (secretFromES == null) return RESULT_UNKNOWN;
        if (!sampleEquals(secret, secretFromES.toCharArray())) return RESULT_INVALID;
        else return RESULT_VALID;
    }

    public boolean sampleEquals(char[] c1, char[] c2) {
        try {
            for (int i = 0; i < c1.length; i++) {
                if (c1[i] != c2[i]) return false;
            }
            return true;
        }
        catch (Exception ex) {
            return false;
        }
    }
}
