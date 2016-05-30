package se.mah.projekt_1.Services;

import android.support.v7.app.AppCompatActivity;

import org.springframework.http.HttpAuthentication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import se.mah.projekt_1.Controller.Controller;
import se.mah.projekt_1.Models.HttpBasicAuthentication;
import se.mah.projekt_1.Models.Account;
import se.mah.projekt_1.Models.AndroidStamp;
import se.mah.projekt_1.Models.ScheduleStamp;

/**
 * Created by Gustaf Bohlin on 03/05/2016.
 * Handles all the calls to the server
 */
public class ServerCommunicationService {

    private Controller controller;

    /**
     * Constructor
     * @param controller the controller being used
     */
    public ServerCommunicationService(Controller controller) {
        this.controller = controller;
    }

    /**
     * Logs in with username and password
     * @param url the url to the server
     * @param username the username
     * @param password the password
     * @return the Account being logged in to
     * @throws RuntimeException
     */
    public Account login(String url, String username, String password) throws RuntimeException {
        return login(url, encryptAuthentication(username, password));
    }

    /**
     * Calls the current activities to display a connection error message
     * @param type what type of error
     * @param message the message to display
     */
    private void showConnectionErrorMessage(final Controller.ErrorType type, final String message) {
        ((AppCompatActivity)controller.getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                controller.showConnectionErrorMessage(type, message);
            }
        });
    }

    /**
     * Logs in with encryptedUserCredentials
     * @param url the url to the server
     * @param encryptedUserCredentials the encryptedUserCredentials
     * @return the Account being logged in to
     * @throws RuntimeException
     */
    public Account login(String url, String encryptedUserCredentials) throws RuntimeException {
        RestTemplate restTemplate = createRestTemplate();
        HttpEntity<String> requestEntity = createHttpEntity(encryptedUserCredentials);

        Account acc = null;
        try {
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Map.class);
            LinkedHashMap<String, Object> responseMap = (LinkedHashMap<String, Object>) response.getBody();
            LinkedHashMap<String, Object> accountMap = (LinkedHashMap<String, Object>) responseMap.get("principal");
            acc = new Account();
            acc.createAccountFromMap(accountMap, encryptedUserCredentials);
        } catch (Exception e) {
            if(e.getMessage().equals("401 Unauthorized"))
                showConnectionErrorMessage(Controller.ErrorType.UNATHORIZED, "");
            else
                showConnectionErrorMessage(Controller.ErrorType.CONNECTION_ERROR, "");
        }
        return acc;
    }

    /**
     * Gets the users timestamps
     * @param url the url to the server
     * @param encryptedUserCredentials the users encryptedUserCredentials
     * @param rfid the users rfid key
     * @param from the starting value for the between query
     * @param to the end value for the between query
     * @return Array with the stamps
     */
    public AndroidStamp[] getStampsForUser(String url, String encryptedUserCredentials, String rfid, String from, String to) {

        RestTemplate restTemplate = createRestTemplate();
        HttpEntity<String> requestEntity = createHttpEntity(encryptedUserCredentials);

        //Temporary url TODO change to URIComponentBuilder
        url += "&";
        url += "from=" + from;
        url += "&to=" + to;
        url += "&rfid=" + rfid;


        AndroidStamp[] stamps = new AndroidStamp[0];
        try {
            ResponseEntity<AndroidStamp[]> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, AndroidStamp[].class);
            stamps = response.getBody();
        } catch (Exception e) {
            showConnectionErrorMessage(Controller.ErrorType.CONNECTION_ERROR, "");
        }
        return stamps;
    }

    /**
     * Gets the users Schedule
     * @param url the url to the server
     * @param encryptedUserCredentials the users encryptedUserCredentials
     * @param id the users id
     * @param from the starting value for the between query
     * @param to the end value for the between query
     * @return Array with the Schedule
     */
    public ScheduleStamp[] getScheduleForUser(String url, String encryptedUserCredentials, String id, String from, String to) {
        RestTemplate restTemplate = createRestTemplate();
        HttpEntity<String> requestEntity = createHttpEntity(encryptedUserCredentials);

        //Temporary url TODO change to URIComponentBuilder
        url += "&";
        url += "from=" + from;
        url += "&to=" + to;
        url += "&id=" + id;
        //url = buildURI(url, from, to, id);

        ScheduleStamp[] stamps = new ScheduleStamp[0];
        try {
            ResponseEntity<ScheduleStamp[]> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, ScheduleStamp[].class);
            stamps = response.getBody();
        } catch (Exception e) {
            showConnectionErrorMessage(Controller.ErrorType.CONNECTION_ERROR, "");
        }
        return stamps;
    }

    /**
     * logs out
     * @param encryptedUserCredentials the users encryptedUserCredentials
     */
    public void logout(String encryptedUserCredentials) {
        RestTemplate restTemplate = createRestTemplate();
        HttpEntity<String> requestEntity = createHttpEntity(encryptedUserCredentials);
        try {
            restTemplate.exchange("https://projektessence.se/logout", HttpMethod.POST, requestEntity, String.class);
        } catch (Exception e) {}
    }

    private RestTemplate createRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        SimpleClientHttpRequestFactory a = new SimpleClientHttpRequestFactory();
        a.setConnectTimeout(3000);
        a.setReadTimeout(3000);
        restTemplate.setRequestFactory(a);
        return restTemplate;
    }

    private HttpEntity createHttpEntity(String encryptedUserCredentials) {
        HttpAuthentication auth = new HttpBasicAuthentication(encryptedUserCredentials);
        HttpHeaders headers = new HttpHeaders();
        headers.setAuthorization(auth);
        return new HttpEntity(headers);
    }

    private String encryptAuthentication(String username, String password) {
        String plainCreds = username + ":" + password;
        byte[] plainCredsByte = plainCreds.getBytes();
        byte[] base64 = Base64Utils.encode(plainCredsByte);
        return new String(base64);
    }

    private String buildURI(String uri, String from, String to, String rfid) {
        LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        ArrayList<String> param = new ArrayList<>();
        param.add(from);
        map.put("from", param);
        param.set(0, to);
        map.put("to", param);
        param.set(0, rfid);
        map.put("id", param);
        UriComponents uriComp = UriComponentsBuilder.fromUriString(uri).queryParams(map).build();
        String completeUri = uriComp.toString().replace('?', '&');
        return completeUri;
    }
}
