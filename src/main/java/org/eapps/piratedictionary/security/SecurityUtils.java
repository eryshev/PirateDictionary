package org.eapps.piratedictionary.security;

import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by eryshev-alexey on 21/07/15.
 */
public class SecurityUtils {
    public static MessageDigest MESSAGE_DIGEST;

    public SecurityUtils() {
        try {
            MESSAGE_DIGEST = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL, "Security cannot be provided at the moment.");
        }
    }

    // Convert the byte to hex format
    public byte[] toHex(byte[] byteData) {
        StringBuilder hexString = new StringBuilder();
        for (byte aByteData : byteData) {
            String hex = Integer.toHexString(0xff & aByteData);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString().getBytes(StandardCharsets.UTF_8);
    }
}
