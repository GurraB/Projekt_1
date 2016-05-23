package se.mah.projekt_1;

import android.os.AsyncTask;
import android.util.Log;

import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Gustaf on 27/04/2016.
 * Handles the login
 */
public class LoginService extends AsyncTask<String, String, Account> {

    private Controller controller;
    private String url = "https://projektessence.se/api/account";
//    private String url = "http://192.168.1.46:44344/api/account";

    /**
     * Constructor
     * @param controller the controller
     */
    public LoginService(Controller controller) {
        this.controller = controller;
    }

    /**
     * Sends the request to the server and returns the user
     * @param strings [username], [password] or [encryptedUserCredentials]
     * @return the user logged in as
     */
    @Override
    protected Account doInBackground(String... strings) {
        if(strings.length == 2)
            return new ServerCommunicationService(controller).login(url, strings[0], strings[1]);
        return new ServerCommunicationService(controller).login(url, strings[0]);
    }

    /**
     * When the user has been retrieved from the server it sends it to the controller
     * @param account the retrieved user
     */
    @Override
    protected void onPostExecute(Account account) {
        super.onPostExecute(account);
        controller.finishedLoading(account);
    }
}
