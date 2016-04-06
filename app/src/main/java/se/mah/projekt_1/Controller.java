package se.mah.projekt_1;

import android.text.format.DateUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Gustaf on 06/04/2016.
 */
public class Controller {
    private LogInActivity logInActivity;
    private MainActivity mainActivity;
    private ArrayList<TimeStamp> timeStamps = new ArrayList<>();

    public Controller(LogInActivity logInActivity) {
        this.logInActivity = logInActivity;
        timeStamps.add(new TimeStamp(new Date(), true));
        Log.v("Date", timeStamps.get(0).getFormattedDate());
    }
}
