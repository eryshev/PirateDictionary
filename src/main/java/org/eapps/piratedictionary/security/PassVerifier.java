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
    private static MessageDigest MESSAGE_DIGEST;
    private static final int SALT_BYTE_SIZE = 24;

    public PassVerifier() {
        try {
            MESSAGE_DIGEST = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Security cannot be provided at the moment.");
        }
    }
    
    @Override
    public int verify(String id, char[] secret) {
//        System.out.println(id + " " + Arrays.toString(secret));
        if (id == null) return RESULT_MISSING;
        String[] secretFromES = USER_PD_PERSISTENCE.getSecretById(BASE_64.encode(id.toCharArray(), false));
        if (secretFromES == null) return RESULT_UNKNOWN;

//        System.out.println("salt: " + secretFromES[0]);
        MESSAGE_DIGEST.update((secretFromES[0] + new String(secret)).getBytes(StandardCharsets.UTF_8));

        if (!sampleEquals(
                toHex(MESSAGE_DIGEST.digest()),
                secretFromES[1].getBytes(StandardCharsets.UTF_8))) return RESULT_INVALID;
        else return RESULT_VALID;
    }

    private boolean sampleEquals(byte[] c1, byte[] c2) {
//        System.out.println("1: " + Arrays.toString(c1));
//        System.out.println("2: " + Arrays.toString(c2));
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

//    // Generate a random salt
//    SecureRandom random = new SecureRandom();
//    byte[] salt = new byte[SALT_BYTE_SIZE];
//    random.nextBytes(salt);
//    System.out.println("salt: " + new String(toHex(salt)));

    // Convert the byte to hex format
    private byte[] toHex(byte[] byteData) {
        StringBuilder hexString = new StringBuilder();
        for (byte aByteData : byteData) {
            String hex = Integer.toHexString(0xff & aByteData);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString().getBytes(StandardCharsets.UTF_8);
    }
}
