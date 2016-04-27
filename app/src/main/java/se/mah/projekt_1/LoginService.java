package se.mah.projekt_1;

import android.os.AsyncTask;

import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Gustaf on 27/04/2016.
 */
public class LoginService extends AsyncTask<String, String, User> {

    private Controller controller;
    private RestTemplate restTemplate;
    private String url = "http://195.178.224.74:44344/android/login";

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
    protected User doInBackground(String... strings) {
        Map<String, Object> loginParams = new LinkedHashMap<>();
        loginParams.put("firstName", strings[0]);
        loginParams.put("lastName", strings[1]);
        User returnedUser = null;
        try {
            returnedUser = restTemplate.postForObject(url, loginParams, User.class);
        } catch (RestClientException exception) {
            controller.showConnectionErrorMessage("CANCER", false);
        }
        return returnedUser;
    }

    @Override
    protected void onPostExecute(User user) {
        super.onPostExecute(user);
        controller.finishedLoading(user);
    }
}
