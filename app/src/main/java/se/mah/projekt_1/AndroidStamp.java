package se.mah.projekt_1;

import android.text.format.DateFormat;
import android.util.Log;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Created by Gustaf on 06/04/2016.
 * A timestamp containing time and whether it is check out or in
 */

public class AndroidStamp implements Serializable {
    private Calendar date;
    private boolean checkIn;


    public AndroidStamp() {

    }

    public String toString() {
        CalendarFormatter formatter = new CalendarFormatter(date);
        return "AndroidStamp: " + formatter.toStringNoYearWithTime() + " | " + checkIn;
    }

    /**
     * Constructor
     * @param date time and date of event
     * @param checkIn true if it is a check in
     */
    public AndroidStamp(Calendar date, boolean checkIn) {
        this.date = date;
        this.checkIn = checkIn;
    }

    /**
     * Returns the date
     * @return a Calendar object as the date
     */
    public Calendar getDate() {
        return date;
    }

    /**
     * Sets the date
     * @param date the date
     */
    public void setDate(Calendar date) {
        date.set(Calendar.HOUR_OF_DAY, date.get(Calendar.HOUR_OF_DAY) + 2);
        this.date = date;
    }

    /**
     * returns true if it is a check in otherwise false
     * @return
     */
    public boolean isCheckIn() {
        return checkIn;
    }

    /**
     * set whether it is check in or check out
     * @param checkIn true if it is a check in
     */
    public void setCheckIn(boolean checkIn) {
        this.checkIn = checkIn;
    }
}
