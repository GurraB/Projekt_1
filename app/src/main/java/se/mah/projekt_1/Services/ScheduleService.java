package se.mah.projekt_1.Services;

import android.os.AsyncTask;

import se.mah.projekt_1.Controller.Controller;
import se.mah.projekt_1.Models.ScheduleStamp;

/**
 * Created by Gustaf Bohlin on 12/05/2016.
 * Asynchronous call to the server to get the Schedule
 */
public class ScheduleService extends AsyncTask<String, String, ScheduleStamp[]> {

    private Controller controller;
    private String url = "https://projektessence.se/api/schedule/between";

    /**
     * Constructor
     * @param controller the controller used
     */
    public ScheduleService(Controller controller) { this.controller = controller; }

    /**
     * The method that calls the server
     * @param strings strings[0] = encryptedUsercredentials, strings[1] = id, strings[2] = from, strings[3] = to
     * @return the schedulestamps
     */
    @Override
    protected ScheduleStamp[] doInBackground(String... strings) {
        ScheduleStamp[] retValue = new ServerCommunicationService(controller).getScheduleForUser(url, strings[0], strings[1], strings[2], strings[3]);
        return retValue;
    }

    @Override
    protected void onPostExecute(ScheduleStamp[] scheduleStamps) {
        controller.finishedLoading(scheduleStamps);
        super.onPostExecute(scheduleStamps);
    }
}
