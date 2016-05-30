package se.mah.projekt_1.Models;

import java.util.Calendar;

/**
 * Created by Gustaf Bohlin on 21/05/2016.
 */
public class GraphEvent {
    private long start;
    private long stop;
    private String title;
    private boolean stamp;

    /**
     * Returns a string representing the GraphEvent
     * @return a string representing the GraphEvent
     */
    public String toString() {
        Calendar cfrom = Calendar.getInstance();
        Calendar cto = Calendar.getInstance();
        cfrom.setTimeInMillis(start);
        cto.setTimeInMillis(stop);
        return cfrom.get(Calendar.DAY_OF_MONTH) + "/" + cfrom.get(Calendar.MONTH) + " " + cfrom.get(Calendar.HOUR_OF_DAY) + ":" + cfrom.get(Calendar.MINUTE) + " - " +
                cto.get(Calendar.DAY_OF_MONTH) + "/" +cto.get(Calendar.MONTH) + " " + cto.get(Calendar.HOUR_OF_DAY) + ":" + cto.get(Calendar.MINUTE);
    }

    /**
     * Constructor
     * @param start startTime
     * @param stop endTime
     * @param stamp is it a timestamp or a schedulestamp
     */
    public GraphEvent(long start, long stop, boolean stamp) {
        this.start = start;
        this.stop = stop;
        this.stamp = stamp;
    }

    /**
     * Constructor
     * @param start startTime
     * @param stop endTime
     * @param stamp is it a timestamp or a schedulestamp
     * @param title the title of the timestamp ex 08:00-12:00
     */
    public GraphEvent(long start, long stop, boolean stamp, String title) {
        this(start, stop, stamp);
        this.title = title;
    }

    /**
     * Returns the start time
     * @return the start time
     */
    public long getStart() {
        return start;
    }

    /**
     * Sets the start time
     * @param start start time
     */
    public void setStart(long start) {
        this.start = start;
    }

    /**
     * Returns the end time
     * @return the end time
     */
    public long getStop() {
        return stop;
    }

    /**
     * sets the end time
     * @param stop end time
     */
    public void setStop(long stop) {
        this.stop = stop;
    }

    /**
     * Returns the title
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * is it a TimeStamp?
     * @return true if its a timestamp
     */
    public boolean isStamp() {
        return stamp;
    }

    /**
     * sets whether it is a timestamp
     * @param stamp true for timestamp
     */
    public void setStamp(boolean stamp) {
        this.stamp = stamp;
    }
}
