package se.mah.projekt_1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.alamkanak.weekview.WeekViewEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Gustaf on 13/04/2016.
 */
public class Controller {

    private ArrayList<AndroidStamp> dataSet = new ArrayList<>();
    private ArrayList<ScheduleStamp> schedule = new ArrayList<>();
    private CalendarFormatter dateFrom, dateTo;
    private Account user;
    private AsyncTaskCompatible activity;


    public Controller(AsyncTaskCompatible activity) {
        this.activity = activity;
    }

    public Controller() {
    }

    public void getServerStamps() {
        activity.startLoadingAnimation();
        new StampsService(this).execute(
                user.getEncryptedUserCredentials(),
                user.getRfidKey().getId(),
                String.valueOf(dateFrom.getCalendar().getTimeInMillis()),
                String.valueOf(dateTo.getCalendar().getTimeInMillis()));
    }

    public void getSchedule() {
        activity.startLoadingAnimation();
        new ScheduleService(this).execute(user.getEncryptedUserCredentials(),
                user.getId(),
                String.valueOf(dateFrom.getCalendar().getTimeInMillis()),
                String.valueOf(dateTo.getCalendar().getTimeInMillis()));
    }

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

    public void login(String username, String password) {
        activity.startLoadingAnimation();
        new LoginService(this).execute(username, password);
    }

    public void login(String encryptedUserCredentials) {
        activity.startLoadingAnimation();
        new LoginService(this).execute(encryptedUserCredentials);
    }

    public void startNewActivity(Context context, Class newActivity) {
        Intent intent = new Intent(context, newActivity);
        context.startActivity(intent);
    }

    public void startNewActivity(Context context, Class newActivity, Account user) {

        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user);

        Intent intent = new Intent(context, newActivity);
        intent.putExtra("userInformation", bundle);
        context.startActivity(intent);
    }

    public void parseUserInformation(Bundle userInformation) {
        user = (Account) userInformation.getSerializable("user");
    }

    public CalendarFormatter getDateFrom() {
        if (dateFrom == null) {
            Calendar calendarFrom = Calendar.getInstance();
            calendarFrom.set(calendarFrom.get(Calendar.YEAR), calendarFrom.get(Calendar.MONTH), (calendarFrom.get(Calendar.DAY_OF_MONTH) - 7), 0, 0, 1);
            dateFrom = new CalendarFormatter(calendarFrom);
        }
        return dateFrom;
    }

    public CalendarFormatter getDateTo() {
        if (dateTo == null) {
            Calendar calendarTo = Calendar.getInstance();
            calendarTo.set(calendarTo.get(Calendar.YEAR), calendarTo.get(Calendar.MONTH), calendarTo.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
            dateTo = new CalendarFormatter(calendarTo);
        }
        return dateTo;
    }

    public void navDrawerClosed() {
        ((MainActivity) activity).updateDateSpan();
        getServerStamps();
    }

    public void onDateFromSet(CalendarFormatter dateFrom) {
        if (dateFrom != null)
            this.dateFrom = dateFrom;
        ((MainActivity) activity).setEtFrom(getDateFrom().toString());
    }

    public void onDateToSet(CalendarFormatter dateTo) {
        if (dateTo != null)
            this.dateTo = dateTo;
        ((MainActivity) activity).setEtTo(getDateTo().toString());
    }

    public void finishedLoading(AndroidStamp[] stamps) {
        activity.stopLoadingAnimation();
        dataSet.clear();
        int i = 0;
        for (AndroidStamp stamp : stamps) {
            dataSet.add(stamp);
            Log.v("STAMP: " + i++, stamp.toString());
        }
        Collections.sort(dataSet, new AndroidStampSortDescending());
        activity.dataRecieved(dataSet);
        //getSchedule();
    }

    public void finishedLoading(ScheduleStamp[] stamps) {
        activity.stopLoadingAnimation();
        schedule.clear();
        int i = 0;
        for (ScheduleStamp stamp : stamps) {
            schedule.add(stamp);
            Log.v("SCHEDULESTAMP: " + i++, stamp.toString());
        }
    }

    public void finishedLoading(Account user) {
        activity.stopLoadingAnimation();

        if(user != null)
            activity.onLoginSuccess(user);
        else
            activity.onLoginFail();    //null om servern inte är uppe
    }

    public void showConnectionErrorMessage(String message, boolean retry) {
        activity.showConnectionErrorMessage(message, retry);
    }

    public Account getUser() {
        return user;
    }

    public ArrayList<WeekViewEvent> getGraphEvents(Calendar selectedDay) {
        if (selectedDay == null)
            selectedDay = Calendar.getInstance();
        ArrayList<AndroidStamp> stamps = getStampsForDay(selectedDay);
        Collections.sort(stamps, new AndroidStampSortAscending());
        if (stamps == null)
            stamps = new ArrayList();
        ArrayList<WeekViewEvent> events = new ArrayList<>();

        for (int i = 0; i < stamps.size(); i += 2) {
            Calendar startTime = stamps.get(i).getDate();
            Calendar endTime;
            if (i + 1 >= stamps.size()) {
                Calendar now = Calendar.getInstance();
                endTime = Calendar.getInstance();
                endTime.set(Calendar.HOUR_OF_DAY, 23);
                endTime.set(Calendar.MINUTE, 59);
                endTime.set(Calendar.SECOND, 59);
                if (now.before(endTime))
                    endTime = now;
            } else {
                endTime = stamps.get(i + 1).getDate();
            }
            String title = formatTime(String.valueOf(startTime.get(Calendar.HOUR_OF_DAY))) + ":" + formatTime(String.valueOf(startTime.get(Calendar.MINUTE))) + "-" + formatTime(String.valueOf(endTime.get(Calendar.HOUR_OF_DAY))) + ":" + formatTime(String.valueOf(endTime.get(Calendar.MINUTE)));
            WeekViewEvent event = new WeekViewEvent(1, title, startTime, endTime);
            event.setColor(((MainActivity) activity).getResources().getColor(R.color.colorPrimaryLight));
            events.add(event);
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

    public void sortAscending() {
        Collections.sort(dataSet, new AndroidStampSortAscending());
        ((MainActivity) activity).setDataSet(dataSet);
    }

    public void sortDescending() {
        Collections.sort(dataSet, new AndroidStampSortDescending());
        ((MainActivity) activity).setDataSet(dataSet);
    }
}
