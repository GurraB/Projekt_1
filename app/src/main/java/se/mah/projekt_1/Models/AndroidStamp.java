package se.mah.projekt_1.Models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.Serializable;
import java.util.Calendar;

import se.mah.projekt_1.Serializing.AndroidStampSerializing;

/**
 * Created by Gustaf Bohlin on 06/04/2016.
 * A timestamp containing time and whether it is check out or in
 */

@JsonDeserialize(using = AndroidStampSerializing.class)
public class AndroidStamp implements Serializable {
    private Calendar date;
    private Long time;
    private boolean checkIn;


    /**
     * Empty constructor
     */
    public AndroidStamp() {

    }

    /**
     * Returns a readable string about the timestamp
     * @return a readable string about the timestamp
     */
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

    /**
     * Returns the time as a Long value
     * @return
     */
    public Long getTime() {
        return time;
    }

    /**
     * Sets the time
     * @param time time in milliseconds
     */
    public void setTime(Long time) {
        this.time = time;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        setDate(calendar);
    }
}
