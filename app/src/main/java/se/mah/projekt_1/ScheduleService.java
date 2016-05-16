package se.mah.projekt_1;

import android.os.AsyncTask;

/**
 * Created by Gustaf on 12/05/2016.
 */
public class ScheduleService extends AsyncTask<String, String, ScheduleStamp[]> {

    private Controller controller;
    private String url = "https://projektessence.se/api/schedule/between";

    public ScheduleService(Controller controller) { this.controller = controller; }

    @Override
    protected ScheduleStamp[] doInBackground(String... strings) {
        ScheduleStamp[] retValue = new ServerCommunicationService().getScheduleForUser(url, strings[0], strings[1], strings[2], strings[3]);
        return retValue;
    }

    @Override
    protected void onPostExecute(ScheduleStamp[] scheduleStamps) {
        controller.finishedLoading(scheduleStamps);
        super.onPostExecute(scheduleStamps);
    }
}
