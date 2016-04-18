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

public class ApiClient extends AsyncTask<String, String, ArrayList<LinkedHashMap<String, Object>>>{

    private User user;
    private String url = "http://192.168.1.51:8080/android";    //Server address
    private RestTemplate restTemplate;
    private Controller controller;
    private long from = 0, to = 0;

    public static final String ALL = "/all";
    public static final String BETWEEN = "/between";
    public static final String LOGIN = "/login";

    /**
     * Constructor
     * Initalizes the objects needed to contact the server
     * @param user the user to get data for
     */
    public ApiClient(Controller controller, User user) {
        this.controller = controller;
        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter()); //Send JSON objects to the server to get correct data
        this.user = user;
        SimpleClientHttpRequestFactory requestFactory =
                (SimpleClientHttpRequestFactory) restTemplate.getRequestFactory();
        requestFactory.setReadTimeout(3000);    //Timeout after 3 seconds
        requestFactory.setConnectTimeout(3000);
    }

    /**
     * Constructor if we need to get data between to time periods
     * @param user the user to get data for
     * @param from date in milliseconds to get data from
     * @param to date in milliseconds to get data to
     */
    public ApiClient(Controller controller, User user, long from, long to) {
        this(controller, user);
        this.user = user;
        this.from = from;
        this.to = to;
    }

    /**
     * Is called right before we get the data
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    /**
     * Gets the data from the server
     * @param strings what type of data to be fetched
     * @return an ArrayList with LinkedHashMaps containing the data
     * @throws RestClientException Unable to connect to server
     */
    @Override
    protected ArrayList<LinkedHashMap<String, Object>> doInBackground(String... strings) throws RestClientException {
        ArrayList<LinkedHashMap<String, Object>> retValue = new ArrayList<>();
        LinkedHashMap<String, Object> retValueLogin;
        Log.v("ApiClient", "----------CONNECTING TO SERVER-------------");
        url += strings[0];
        try {
            switch (strings[0]) {
                case ALL:
                    retValue = restTemplate.postForObject(url, user.getKey(), ArrayList.class);
                    break;
                case BETWEEN:
                    Map<String, Object> params = new LinkedHashMap<>();
                    params.put("id", user.getKey().getId());
                    Log.v("MITT ID", user.getKey().getId());
                    params.put("from",from);
                    params.put("to", to);
                    retValue = restTemplate.postForObject(url, params, ArrayList.class);
                    break;
                case LOGIN:
                    Map<String, Object> loginParams = new LinkedHashMap<>();
                    loginParams.put("firstName", user.getFistname());
                    loginParams.put("lastName", user.getLastname());
                    retValueLogin = restTemplate.postForObject(url, loginParams, LinkedHashMap.class);
                    retValue = new ArrayList<>();
                    retValue.add(retValueLogin);
                    break;
                default:
                    controller.stopLoadingAnimation();
                    controller.showConnectionErrorMessage(strings[0], false);
                    break;
            }
        } catch (RestClientException exception) {
            controller.stopLoadingAnimation();
            controller.showConnectionErrorMessage(strings[0], true);
        }
        return retValue;
    }

    /**
     * Update loadingbars etc whilst getting the data
     * @param values
     */
    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }

    /**
     * If the request is cancelled
     */
    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    /**
     * After the data is fetched
     * @param retValue retrieved data from server
     */
    @Override
    protected void onPostExecute(ArrayList<LinkedHashMap<String, Object>> retValue) {
        super.onPostExecute(retValue);
        controller.stopLoadingAnimation();
        controller.finishedLoading(retValue);
    }
}
