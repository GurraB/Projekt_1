package se.mah.projekt_1;

import android.text.format.DateFormat;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Gustaf on 06/04/2016.
 */
public class TimeStamp {
    private Date date;
    private boolean checkIn;

    public TimeStamp(Date date, boolean checkIn) {
        this.date = date;
        this.checkIn = checkIn;
    }

    public String getFormattedDate() {
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-mm-dd hh:mm");
        return dt.format(date);
    }
}
