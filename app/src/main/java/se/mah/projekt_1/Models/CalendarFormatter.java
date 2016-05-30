package se.mah.projekt_1.Models;

import java.util.Calendar;

/**
 * Created by Gustaf Bohlin on 11/04/2016.
 * Formats the Calendar object for UI simplicity
 */
public class CalendarFormatter {
    private Calendar calendar;
    private String year;
    private String month;
    private String day;
    private String hour;
    private String minute;
    private String second;

    /**
     * Constructor
     * @param calendar the calendar object to be formatted
     */
    public CalendarFormatter(Calendar calendar) {
        this.calendar = calendar;
        year = Integer.toString(calendar.get(Calendar.YEAR));
        month = Integer.toString(calendar.get(Calendar.MONTH) + 1);
        day = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
        hour = Integer.toString(calendar.get(Calendar.HOUR_OF_DAY));
        minute = Integer.toString(calendar.get(Calendar.MINUTE));
        second = Integer.toString(calendar.get(Calendar.SECOND));
    }

    /**
     * Returns the Calendar object in a sensible format
     * @return
     */
    public String toString() {
        return day + "/" + month + " - " + year;
    }

    /**
     * Returns the Calendar object in a sensible format without the year
     * @return
     */
    public String toStringNoYear() {
        return day + "/" + month;
    }

    public String toStringNoYearWithTime() {
        return day + "/" + month + " " + (hour.length() == 2 ? "" : "0") + hour + ":" + (minute.length() == 2 ? "" : "0") + minute;
    }

    /**
     * Returns the Calendar object
     * @return the Calendar object
     */
    public Calendar getCalendar() {
        return calendar;
    }

    /**
     * Sets the calendar object
     * @param calendar Calendar object
     */
    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    /**
     * Returns the year
     * @return the year
     */
    public String getYear() {
        return year;
    }

    /**
     * Sets the year
     * @param year the year
     */
    public void setYear(String year) {
        this.year = year;
    }

    /**
     * Returns the month
     * @return the month
     */
    public String getMonth() {
        return month;
    }

    /**
     * Sets the month
     * @param month the month
     */
    public void setMonth(String month) {
        this.month = month;
    }

    /**
     * Returns the day
     * @return the day
     */
    public String getDay() {
        return day;
    }

    /**
     * Sets the day
     * @param day the day
     */
    public void setDay(String day) {
        this.day = day;
    }

    public String toStringNoYearWithTimeWithSeconds() {
        return toStringNoYearWithTime() + ":" + second;
    }

    public String toStringTime() {
        return (hour.length() == 2 ? "" : "0") + hour + ":" + (minute.length() == 2 ? "" : "0") + minute;
    }
}
