package se.mah.projekt_1.Models;

import org.springframework.http.HttpAuthentication;
import org.springframework.util.Base64Utils;

/**
 * Created by Gustaf Bohlin on 11/05/2016.
 * A class to Authenticate using encryptedUserCredentials
 */
public class HttpBasicAuthentication extends HttpAuthentication {

    private final String encryptedUserCredentials;

    /**
     * Constructor
     * @param encryptedUserCredentials the encryptedUsercredentials
     */
    public HttpBasicAuthentication(String encryptedUserCredentials) {
        this.encryptedUserCredentials = encryptedUserCredentials;
    }

    /**
     * Returns the header value
     * @return the value for the 'Authorization' HTTP header.
     */
    public String getHeaderValue() {
        return String.format("Basic %s", encryptedUserCredentials);
    }

    /**
     * Returns a string representing the header
     * @return a string representing the header
     */
    @Override
    public String toString() {
        String s = null;
        try {
            s = String.format("Authorization: %s", getHeaderValue());
        } catch (RuntimeException re) {
            return ">:(";
        }
        return s;
    }
}
