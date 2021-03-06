package se.mah.projekt_1.Controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import se.mah.projekt_1.Activities.MainActivity;
import se.mah.projekt_1.Interfaces.AsyncTaskCompatible;
import se.mah.projekt_1.Models.Account;
import se.mah.projekt_1.Models.AndroidStamp;
import se.mah.projekt_1.Models.CalendarFormatter;
import se.mah.projekt_1.Models.GraphEvent;
import se.mah.projekt_1.Models.ScheduleStamp;
import se.mah.projekt_1.Services.LoginService;
import se.mah.projekt_1.Services.LogoutService;
import se.mah.projekt_1.Services.ScheduleService;
import se.mah.projekt_1.Services.StampsService;
import se.mah.projekt_1.Sorting.AndroidStampSortAscending;
import se.mah.projekt_1.Sorting.AndroidStampSortDescending;

/**
 * Created by Gustaf Bohlin on 13/04/2016.
 * Handles most of the logic in the activities
 */
public class Controller {

    /**
     * Types of error that can occur
     */
    public enum ErrorType {
        UNATHORIZED,
        CONNECTION_ERROR,
        BAD_REQUEST
    }

    private ArrayList<AndroidStamp> dataSet = new ArrayList<>();
    private ArrayList<ScheduleStamp> schedule = new ArrayList<>();
    private CalendarFormatter dateFrom, dateTo;
    private Account user;
    private AsyncTaskCompatible activity;


    /**
     * Constructor
     * @param activity The activity that uses this controller
     */
    public Controller(AsyncTaskCompatible activity) {
        this.activity = activity;
    }

    /**
     * Returns the activity that this controller uses
     * @return the activity
     */
    public AsyncTaskCompatible getActivity() {
        return activity;
    }

    /**
     * Empty constructor
     */
    public Controller() {
    }

    /**
     * Gets the Timestamps from the server
     */
    public void getServerStamps() {
        activity.startLoadingAnimation();
        new StampsService(this).execute(
                user.getEncryptedUserCredentials(),
                user.getRfidKey().getId(),
                String.valueOf(dateFrom.getCalendar().getTimeInMillis()),
                String.valueOf(dateTo.getCalendar().getTimeInMillis()));
    }

    /**
     * Gets the schedule from the server
     */
    public void getSchedule() {
        new ScheduleService(this).execute(user.getEncryptedUserCredentials(),
                user.getId(),
                String.valueOf(dateFrom.getCalendar().getTimeInMillis()),
                String.valueOf(dateTo.getCalendar().getTimeInMillis()));
    }

    public void logout() {
        new LogoutService(this).execute(user.getEncryptedUserCredentials());
    }

    /**
     * Parses the savedInstance to retrieve saved data
     * @param savedInstance savedInstance
     */
    public void parseSavedInstance(Bundle savedInstance) {
        dataSet = new ArrayList<>();
        long[] dataSetDatesArray = savedInstance.getLongArray("ALLTIMES");
        boolean[] dataSetCheckInArray = savedInstance.getBooleanArray("ALLCHECKIN");
        for (int i = 0; i < dataSetDatesArray.length; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(dataSetDatesArray[i]);
            dataSet.add(new AndroidStamp(calendar, dataSetCheckInArray[i]));
        }
        Calendar calendarFrom = Calendar.getInstance();
        calendarFrom.setTimeInMillis(savedInstance.getLong("DATEFROM"));
        dateFrom = new CalendarFormatter(calendarFrom);
        Calendar calendarTo = Calendar.getInstance();
        calendarTo.setTimeInMillis(savedInstance.getLong("DATETO"));
        dateTo = new CalendarFormatter(calendarTo);
    }

    /**
     * Login with username and password
     * @param username the username
     * @param password the password
     */
    public void login(String username, String password) {
        activity.startLoadingAnimation();
        new LoginService(this).execute(username, password);
    }

    /**
     * Login with encrypted user credentials
     * @param encryptedUserCredentials user credentials to use for login
     */
    public void login(String encryptedUserCredentials) {
        activity.startLoadingAnimation();
        new LoginService(this).execute(encryptedUserCredentials);
    }

    /**
     * Starts a new activity
     * @param context from what activity
     * @param newActivity to what activity
     */
    public void startNewActivity(Context context, Class newActivity) {
        Intent intent = new Intent(context, newActivity);
        context.startActivity(intent);
    }

    /**
     * Starts a new activity and sends user as an extra
     * @param context from what activity
     * @param newActivity to what activity
     * @param user the user to send
     */
    public void startNewActivity(Context context, Class newActivity, Account user) {

        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user);

        Intent intent = new Intent(context, newActivity);
        intent.putExtra("userInformation", bundle);
        context.startActivity(intent);
    }

    /**
     * Parses the userInformation sent via an intent
     * @param userInformation
     */
    public void parseUserInformation(Bundle userInformation) {
        user = (Account) userInformation.getSerializable("user");
    }

    /**
     * Returns the date from which to retrieve timestamps and schedule
     * @return the date
     */
    public CalendarFormatter getDateFrom() {
        if (dateFrom == null) {
            Calendar calendarFrom = Calendar.getInstance();
            calendarFrom.set(calendarFrom.get(Calendar.YEAR), calendarFrom.get(Calendar.MONTH), (calendarFrom.get(Calendar.DAY_OF_MONTH) - 7), 0, 0, 1);
            dateFrom = new CalendarFormatter(calendarFrom);
        }
        return dateFrom;
    }

    /**
     * Returns the date to which to retrieve timestamps and schedule
     * @return the date
     */
    public CalendarFormatter getDateTo() {
        if (dateTo == null) {
            Calendar calendarTo = Calendar.getInstance();
            calendarTo.set(calendarTo.get(Calendar.YEAR), calendarTo.get(Calendar.MONTH), calendarTo.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
            dateTo = new CalendarFormatter(calendarTo);
        }
        return dateTo;
    }

    /**
     * When nav drawer is closed update the timespan and refresh
     */
    public void navDrawerClosed() {
        ((MainActivity) activity).updateDateSpan();
        getServerStamps();
    }

    /**
     * Sets the from date
     * @param dateFrom from
     */
    public void onDateFromSet(CalendarFormatter dateFrom) {
        if (dateFrom != null)
            this.dateFrom = dateFrom;
        ((MainActivity) activity).setEtFrom(getDateFrom().toString());
    }

    /**
     * Sets the to date
     * @param dateTo to
     */
    public void onDateToSet(CalendarFormatter dateTo) {
        if (dateTo != null)
            this.dateTo = dateTo;
        ((MainActivity) activity).setEtTo(getDateTo().toString());
    }

    /**
     * When the asynctask is finished
     * @param stamps the new timestamps
     */
    public void finishedLoading(AndroidStamp[] stamps) {
        dataSet.clear();
        int i = 0;
        for (AndroidStamp stamp : stamps) {
            dataSet.add(stamp);
        }
        Collections.sort(dataSet, new AndroidStampSortDescending());
        getSchedule();
    }

    /**
     * When the asynctask is finished
     * @param stamps the new schedule stamps
     */
    public void finishedLoading(ScheduleStamp[] stamps) {
        activity.stopLoadingAnimation();
        schedule.clear();
        int i = 0;
        for (ScheduleStamp stamp : stamps) {
            schedule.add(stamp);
        }
        activity.dataRecieved(dataSet);
    }

    /**
     * When the asynctask is finished
     * @param user the user logged in as
     */
    public void finishedLoading(Account user) {
        activity.stopLoadingAnimation();

        if(user != null)
            activity.onLoginSuccess(user);
    }

    /**
     * Show a connection error
     * @param type what type of error
     * @param message message
     */
    public void showConnectionErrorMessage(ErrorType type, String message) {
        activity.showConnectionErrorMessage(type, message);
    }

    /**
     * Returns the user
     * @return user
     */
    public Account getUser() {
        return user;
    }

    /**
     * Gets the relevant graphevents and returns them in a list
     * @return the events
     */
    public ArrayList<GraphEvent> getGraphEvents(Calendar selectedDay) {
        ArrayList<ScheduleStamp> scheduleStamps = getScheduleForDay(selectedDay);
        if(scheduleStamps == null)
            scheduleStamps = new ArrayList<>();
        ArrayList<AndroidStamp> androidStamps = getStampsForDay(selectedDay);
        if(androidStamps == null)
            androidStamps = new ArrayList<>();
        Collections.sort(androidStamps, new AndroidStampSortAscending());
        ArrayList<GraphEvent> events = new ArrayList<>();
        for (ScheduleStamp scheduleStamp : scheduleStamps) {
            Calendar startTime = Calendar.getInstance();
            startTime.setTimeInMillis(scheduleStamp.getFrom());
            Calendar endTime = Calendar.getInstance();
            endTime.setTimeInMillis(scheduleStamp.getTo());
            String title = formatTime(String.valueOf(startTime.get(Calendar.HOUR_OF_DAY))) + ":" + formatTime(String.valueOf(startTime.get(Calendar.MINUTE))) + "-" + formatTime(String.valueOf(endTime.get(Calendar.HOUR_OF_DAY))) + ":" + formatTime(String.valueOf(endTime.get(Calendar.MINUTE)));
            GraphEvent event = new GraphEvent(scheduleStamp.getFrom(), scheduleStamp.getTo(), false, title);
            events.add(event);
        }

        if(androidStamps.size() > 0) {
            int start = 0;
            if(!androidStamps.get(0).isCheckIn()) {
                Calendar startTime = Calendar.getInstance();
                Calendar endTime = Calendar.getInstance();
                startTime.setTimeInMillis(androidStamps.get(0).getDate().getTimeInMillis());
                startTime.set(Calendar.HOUR_OF_DAY, 0);
                startTime.set(Calendar.MINUTE, 0);
                startTime.set(Calendar.SECOND, 1);
                endTime.setTimeInMillis(androidStamps.get(0).getDate().getTimeInMillis());
                String title = formatTime(String.valueOf(startTime.get(Calendar.HOUR_OF_DAY))) + ":" + formatTime(String.valueOf(startTime.get(Calendar.MINUTE))) + "-" + formatTime(String.valueOf(endTime.get(Calendar.HOUR_OF_DAY))) + ":" + formatTime(String.valueOf(endTime.get(Calendar.MINUTE)));
                GraphEvent event = new GraphEvent(startTime.getTimeInMillis(), endTime.getTimeInMillis(), true, title);
                events.add(event);
                start = 1;
            }

            for (int i = start; i < androidStamps.size(); i += 2) {
                Calendar startTime = Calendar.getInstance();
                Calendar endTime = Calendar.getInstance();
                startTime.setTimeInMillis(androidStamps.get(i).getDate().getTimeInMillis());
                if (i + 1 < androidStamps.size())
                    endTime.setTimeInMillis(androidStamps.get(i + 1).getDate().getTimeInMillis());
                else {
                    endTime.setTimeInMillis(androidStamps.get(i).getDate().getTimeInMillis());
                    endTime.set(Calendar.HOUR_OF_DAY, 23);
                    endTime.set(Calendar.MINUTE, 59);
                    endTime.set(Calendar.SECOND, 59);
                }
                String endTitle = formatTime(String.valueOf(endTime.get(Calendar.HOUR_OF_DAY))) + ":" + formatTime(String.valueOf(endTime.get(Calendar.MINUTE)));
                endTitle = endTitle.equals("23:59") ? "24:00" : endTitle;
                String title = formatTime(String.valueOf(startTime.get(Calendar.HOUR_OF_DAY))) + ":" + formatTime(String.valueOf(startTime.get(Calendar.MINUTE))) + "-" + endTitle;
                GraphEvent event = new GraphEvent(startTime.getTimeInMillis(), endTime.getTimeInMillis(), true, title);
                events.add(event);
            }
        }
        return events;
    }

    private String formatTime(String time) {
        return time.length() == 2 ? time : "0" + time;
    }

    private ArrayList<AndroidStamp> getStampsForDay(Calendar calendar) {
        ArrayList<AndroidStamp> stamps = new ArrayList<>();
        if (dataSet == null)
            return null;
        for (AndroidStamp androidStamp : dataSet)
            if (androidStamp.getDate().get(Calendar.YEAR) == calendar.get(Calendar.YEAR) &&
                    androidStamp.getDate().get(Calendar.MONTH) == calendar.get(Calendar.MONTH) &&
                    androidStamp.getDate().get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH))
                stamps.add(androidStamp);
        return stamps;
    }

    private ArrayList<ScheduleStamp> getScheduleForDay(Calendar calendar) {
        ArrayList<ScheduleStamp> stamps = new ArrayList<>();
        if (schedule == null)
            return null;
        for (ScheduleStamp scheduleStamp : schedule) {
            Calendar fromDate = Calendar.getInstance();
            Calendar toDate = Calendar.getInstance();
            fromDate.setTimeInMillis(scheduleStamp.getFrom());
            toDate.setTimeInMillis(scheduleStamp.getTo());
            if ((fromDate.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) &&
                 fromDate.get(Calendar.MONTH) == calendar.get(Calendar.MONTH) &&
                 fromDate.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH))
                 ||
                (toDate.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) &&
                 toDate.get(Calendar.MONTH) == calendar.get(Calendar.MONTH) &&
                 toDate.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)))
                stamps.add(scheduleStamp);
        }
        return stamps;
    }

    /**
     * Sort the timestamps ascending
     */
    public void sortAscending() {
        Collections.sort(dataSet, new AndroidStampSortAscending());
        ((MainActivity) activity).setDataSet(dataSet);
    }

    /**
     * Sort the timestamps descending
     */
    public void sortDescending() {
        Collections.sort(dataSet, new AndroidStampSortDescending());
        ((MainActivity) activity).setDataSet(dataSet);
    }
}
