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
    private String url = "https://projektessence.se/api/account";

    public LoginService(Controller controller) {
        this.controller = controller;
    }

    @Override
    protected Account doInBackground(String... strings) {
        if(strings.length == 2)
            return new ServerCommunicationService().login(url, strings[0], strings[1]);
        return new ServerCommunicationService().login(url, strings[0]);
    }

    @Override
    protected void onPostExecute(Account account) {
        super.onPostExecute(account);
        controller.finishedLoading(account);
    }
}
