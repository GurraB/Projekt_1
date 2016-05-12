package se.mah.projekt_1;

import android.os.AsyncTask;

import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Gustaf on 07/04/2016.
 * StampsService talks to the server and retrieves all necessary data in a separate thread
 */

public class StampsService extends AsyncTask<String, String, AndroidStamp[]>{

    private Controller controller;
    private String url = "https://projektessence.se/api/android/between";

    /**
     * Constructor
     * Initalizes the objects needed to contact the server
     */
    public StampsService(Controller controller) {
        this.controller = controller;
    }

    /**
     * Gets the data from the server
     * @param strings username, password, rfid, from, to
     * @return an Array with AndroidStamps
     * @throws RestClientException Unable to connect to server
     */
    @Override
    protected AndroidStamp[] doInBackground(String... strings) throws RestClientException {
        AndroidStamp[] retValue = new ServerCommunicationService().getStampsForUser(url, strings[0], strings[1], strings[2], strings[3]);
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
