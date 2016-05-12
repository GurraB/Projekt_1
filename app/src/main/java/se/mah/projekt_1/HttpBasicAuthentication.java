package se.mah.projekt_1;

import org.springframework.http.HttpAuthentication;
import org.springframework.util.Base64Utils;

/**
 * Created by Gustaf on 11/05/2016.
 */
public class HttpBasicAuthentication extends HttpAuthentication {

    private final String encryptedUserCredentials;

    public HttpBasicAuthentication(String encryptedUserCredentials) {
        this.encryptedUserCredentials = encryptedUserCredentials;
    }

    /**
     * @return the value for the 'Authorization' HTTP header.
     */
    public String getHeaderValue() {
        return String.format("Basic %s", encryptedUserCredentials);
    }

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
