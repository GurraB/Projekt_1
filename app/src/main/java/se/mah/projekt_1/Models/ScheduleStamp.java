package se.mah.projekt_1.Models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.Serializable;
import java.util.Calendar;

import se.mah.projekt_1.Serializing.ScheduleStampSerializer;

/**
 * Created by Gustaf Bohlin on 05/05/2016.
 * Represents a Schedule timestamp
 */
@JsonDeserialize(using = ScheduleStampSerializer.class)
public class ScheduleStamp implements Serializable {

    private long from;
    private long to;
    private String[] userId;

    /**
     * Returns a String representing the schedulestamp
     * @return the string representing the schedulestamp
     */
    public String toString() {
        Calendar cfrom = Calendar.getInstance();
        Calendar cto = Calendar.getInstance();
        cfrom.setTimeInMillis(from);
        cto.setTimeInMillis(to);
        return cfrom.get(Calendar.DAY_OF_MONTH) + "/" + cfrom.get(Calendar.MONTH) + " " + cfrom.get(Calendar.HOUR_OF_DAY) + ":" + cfrom.get(Calendar.MINUTE) + " - " +
                cto.get(Calendar.DAY_OF_MONTH) + "/" +cto.get(Calendar.MONTH) + " " + cto.get(Calendar.HOUR_OF_DAY) + ":" + cto.get(Calendar.MINUTE);
    }

    /**
     * Empty constructor for serializing
     */
    public ScheduleStamp() {}

    /**
     * Returns the end time
     * @return the end time
     */
    public long getTo() {
        return to;
    }

    /**
     * Sets the end time
     * @param to
     */
    public void setTo(long to) {
        this.to = to;
    }

    /**
     * Returns the start time
     * @return the start time
     */
    public long getFrom() {
        return from;
    }

    /**
     * sets the start time
     * @param from start time
     */
    public void setFrom(long from) {
        this.from = from;
    }

    /**
     * Returns the users that share this schedule
     * @return the users that share this schedule
     */
    public String[] getUserId() {
        return userId;
    }

    /**
     * sets the users that share this schedule
     * @param userId the users that share this schedule
     */
    public void setUserId(String[] userId) {
        this.userId = userId;
    }
}
