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
 */
public class LoginService extends AsyncTask<String, String, Account> {

    private Controller controller;
    private RestTemplate restTemplate;
    //private String url = "http://195.178.224.74:44344/android/login";
    private String url = "195.178.224.74:44344/api/account";

    public LoginService(Controller controller) {
        this.controller = controller;
        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter()); //Send JSON objects to the server to get correct data
        SimpleClientHttpRequestFactory requestFactory =
                (SimpleClientHttpRequestFactory) restTemplate.getRequestFactory();
        requestFactory.setReadTimeout(3000);    //Timeout after 3 seconds
        requestFactory.setConnectTimeout(3000);
    }

    @Override
    protected Account doInBackground(String... strings) {
        Map<String, Object> loginParams = new LinkedHashMap<>();
        loginParams.put("username", strings[0]);
        loginParams.put("password", strings[1]);
        Account account = null;
        ResponseEntity<String> fancy = null;
        try {
            fancy = restTemplate.postForEntity(url, loginParams, String.class);
            Log.v("RESPONSEENTITIYYITIY", fancy.getStatusCode().toString());
        } catch (RestClientException exception) {
            controller.showConnectionErrorMessage("CANCER", false);
        } catch (RuntimeException exception) {
            controller.showConnectionErrorMessage("Couldn't connect to server", false);
        }
        Log.v("RESPONSEENTITIYYITIY", fancy.getStatusCode().toString());
        return account;
    }

    @Override
    protected void onPostExecute(Account account) {
        super.onPostExecute(account);
        controller.finishedLoading(account);
    }
}
