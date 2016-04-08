package se.mah.projekt_1;

import android.text.format.DateFormat;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Gustaf on 06/04/2016.
 */

public class TimeStamp {
    private Calendar date;
    private boolean checkIn;

    public TimeStamp(Calendar date, boolean checkIn) {
        this.date = date;
        this.checkIn = checkIn;
    }

    public String toString() {
        return "en sträng";
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public boolean isCheckIn() {
        return checkIn;
    }

    public void setCheckIn(boolean checkIn) {
        this.checkIn = checkIn;
    }

    public String getFormattedDate() {
        String formattedDate;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        formattedDate = simpleDateFormat.format(date.getTime());
        Log.v("FORMATTEDDATE", formattedDate);
        return formattedDate;
    }
}
