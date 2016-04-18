package se.mah.projekt_1;

import android.text.format.DateFormat;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Gustaf on 06/04/2016.
 * A timestamp containing time and whether it is check out or in
 */

public class AndroidStamp implements Comparable {
    private Calendar date;
    private boolean checkIn;

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


    @Override
    public int compareTo(Object o) {
        if(o instanceof AndroidStamp) {
            Calendar calendar = (Calendar) o;
            if(getDate().before(calendar))
                return 1;
            else
                return -1;
        }
        return -2;
    }
}