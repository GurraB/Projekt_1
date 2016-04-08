package se.mah.projekt_1;

import android.os.AsyncTask;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by Gustaf on 07/04/2016.
 */
public class ApiClient extends AsyncTask<String, String, ArrayList<LinkedHashMap<String, Long>>>{

    private RfidKey rfidKey = new RfidKey("1");
    private String url = "http://192.168.1.51:8080/android";
    private RestTemplate restTemplate;
    private MainActivity mainActivity;
    public static final String ALL = "/all";
    public static final String BETWEEN = "/all";
    public static final String USER = "/all";

    public ApiClient(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //TODO loadinganimation
    }

    @Override
    protected ArrayList<LinkedHashMap<String, Long>> doInBackground(String... strings) throws RestClientException {
        url += strings[0];
        ArrayList<LinkedHashMap<String, Long>> timeStamps = new ArrayList<>();
        try {
            timeStamps = restTemplate.postForObject(url, rfidKey, ArrayList.class);
        } catch (RestClientException exception) {
            mainActivity.showConnectionErrorMessage();
        } catch (Exception exception) {
            mainActivity.showConnectionErrorMessage();
        }
        return timeStamps;
    }
    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        //TODO progressupdate
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected void onPostExecute(ArrayList<LinkedHashMap<String, Long>> timeStamps) {
        super.onPostExecute(timeStamps);
        mainActivity.finishedLoading(timeStamps);
    }
}
