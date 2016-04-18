package se.mah.projekt_1;

import android.util.Log;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by Gustaf on 12/04/2016.
 */
public class ApiClientTest extends TestCase {

    public void testOnPreExecute() throws Exception {

    }

    public void testDoInBackground() throws Exception {
        User user;
        ApiClient client = new ApiClient(new MainActivity(), user = new User("CANCER"));
        user.setKey(new RfidKey("247615E"));
        ArrayList<LinkedHashMap<String, Long>> got = client.doInBackground(new String[]{"/between"});
        Log.v("SADSAJKFSMDNLKDGN", got.toArray().toString());
        Assert.assertNotNull(got);
    }

    public void testOnProgressUpdate() throws Exception {

    }

    public void testOnCancelled() throws Exception {

    }

    public void testOnPostExecute() throws Exception {

    }
}