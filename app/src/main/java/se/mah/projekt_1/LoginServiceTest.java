package se.mah.projekt_1;

import android.os.AsyncTask;
import android.util.Log;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Calendar;

/**
 * Created by Gustaf on 02/05/2016.
 */
public class LoginServiceTest extends AsyncTask<String, String, String> {

    LogInActivity ac;

    public LoginServiceTest(LogInActivity ac) {
        this.ac = ac;
    }

    @Override
    protected String doInBackground(String... strings) {
        String url = "http://195.178.224.74:44344/api/account";
        String url1 = "http://195.178.224.74:44344/api/android/between&";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        Account acc = new ServerCommunicationService().login(url, strings[0], strings[1]);
        Calendar from = Calendar.getInstance();
        Calendar to = Calendar.getInstance();
        from.set(2016, 3, 18, 0, 0, 1);
        to.set(2016, 4, 4, 0, 0, 1);

        AndroidStamp[] stamps = new ServerCommunicationService().getStampsForUser(url1, "user", "pass", acc.getRfidKey().getId(), from.getTimeInMillis(), to.getTimeInMillis());
        Log.v("IS STAMPS NULL?????", String.valueOf(stamps == null));
        Log.v("HOW MANY STAMPS IS DERE", String.valueOf(stamps.length));
        for (int i = 0; i < stamps.length; i++)
            Log.v("Stamp: " + i, String.valueOf(stamps[i].getDate().get(Calendar.DAY_OF_MONTH)));


        Log.v("ACCOUNT", acc.toString());

        //String fancy = restTemplate.getForObject(url, String.class, loginParams);
        return acc.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        ac.finishedLoading(s);
    }
}
