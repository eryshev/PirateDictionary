package org.eapps.piratedictionary.security;

import org.eapps.piratedictionary.persistence.UserPDPersistence;
import org.restlet.data.Status;
import org.restlet.engine.util.Base64;
import org.restlet.resource.ResourceException;
import org.restlet.security.SecretVerifier;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Classs aims at returning the given password according to the given login.
 * Created by eryshev-alexey on 19/07/15.
 */
public class PassVerifier extends SecretVerifier {
    private static final UserPDPersistence USER_PD_PERSISTENCE = new UserPDPersistence();
    private static final Base64 BASE_64 = new Base64();
    private static final SecurityUtils SECURITY_UTILS = new SecurityUtils();

    public PassVerifier() {
    }

    @Override
    public int verify(String id, char[] secret) {
        if (id == null) return RESULT_MISSING;
        String[] secretFromES = USER_PD_PERSISTENCE.getSecretById(BASE_64.encode(id.toCharArray(), false));
        if (secretFromES == null) return RESULT_UNKNOWN;

        SECURITY_UTILS.MESSAGE_DIGEST.update((secretFromES[0] + new String(secret)).getBytes(StandardCharsets.UTF_8));

        if (!sampleEquals(
                SECURITY_UTILS.toHex(SECURITY_UTILS.MESSAGE_DIGEST.digest()),
                secretFromES[1].getBytes(StandardCharsets.UTF_8))) {
            return RESULT_INVALID;
        }
        else {
            return RESULT_VALID;
        }
    }

    private boolean sampleEquals(byte[] c1, byte[] c2) {
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
