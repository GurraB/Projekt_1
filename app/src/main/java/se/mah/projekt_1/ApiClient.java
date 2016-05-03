package se.mah.projekt_1;

import android.os.AsyncTask;
import android.util.Log;

import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Gustaf on 07/04/2016.
 * ApiClient talks to the server and retrieves all necessary data in a separate thread
 */

public class ApiClient extends AsyncTask<String, String, AndroidStamp[]>{

    //private String url = "http://195.178.224.74:44344/android/between";    //Server
    private String url = "192.168.1.46:8080/android/between";
    private RestTemplate restTemplate;
    private Controller controller;
    private long from = 0, to = 0;

    /**
     * Constructor
     * Initalizes the objects needed to contact the server
     */
    public ApiClient(Controller controller) {
        this.controller = controller;
        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter()); //Send JSON objects to the server to get correct data
        SimpleClientHttpRequestFactory requestFactory =
                (SimpleClientHttpRequestFactory) restTemplate.getRequestFactory();
        requestFactory.setReadTimeout(3000);    //Timeout after 3 seconds
        requestFactory.setConnectTimeout(3000);
    }

    /**
     * Constructor if we need to get data between to time periods
     * @param from date in milliseconds to get data from
     * @param to date in milliseconds to get data to
     */
    public ApiClient(Controller controller, long from, long to) {
        this(controller);
        this.from = from;
        this.to = to;
    }

    /**
     * Gets the data from the server
     * @param strings what type of data to be fetched
     * @return an ArrayList with LinkedHashMaps containing the data
     * @throws RestClientException Unable to connect to server
     */
    @Override
    protected AndroidStamp[] doInBackground(String... strings) throws RestClientException {
        AndroidStamp[] retValue = null;
        Log.v("ApiClient", "----------CONNECTING TO SERVER-------------");
        try {
            Map<String, Object> params = new LinkedHashMap<>();
            //params.put("id", user.getRfid().getId());
            //Log.v("MITT ID", user.getRfid().getId());
            params.put("from",from);
            params.put("to", to);
            retValue = restTemplate.postForObject(url, params, AndroidStamp[].class);
        } catch (RestClientException exception) {
            controller.showConnectionErrorMessage("RestClientException", true);
            controller.showConnectionErrorMessage("APICLIENT CANCER", true);
        }
        return retValue;
    }

    /**
     * After the data is fetched
     * @param retValue retrieved data from server
     */
    @Override
    protected void onPostExecute(AndroidStamp[] retValue) {
        super.onPostExecute(retValue);
        controller.finishedLoading(retValue);
    }
}
