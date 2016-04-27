package se.mah.projekt_1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alamkanak.weekview.WeekViewEvent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.concurrent.Callable;

/**
 * Created by Gustaf on 13/04/2016.
 */
public class Controller {

    private ArrayList<AndroidStamp> dataSet = new ArrayList<>();
    private CalendarFormatter dateFrom, dateTo;
    private User user;
    private AsyncTaskCompatible activity;
    private boolean sortAscending = true;

    public Controller(AsyncTaskCompatible activity) {
        this.activity = activity;
    }

    public Controller() {
    }

    public void getServerStamps() {
        activity.startLoadingAnimation();
        new ApiClient(this, user, dateFrom.getCalendar().getTimeInMillis(), dateTo.getCalendar().getTimeInMillis()).execute();
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

    public void startNewActivity(Context context, Class newActivity) {
        Intent intent = new Intent(context, newActivity);
        context.startActivity(intent);
    }

    public void startNewActivity(Context context, Class newActivity, User user) {

        Bundle bundle = new Bundle();
        bundle.putString("firstname", user.getFirstName());
        bundle.putString("lastname", user.getLastName());
        bundle.putString("rfidkey", user.getRfid().getId());
        bundle.putString("id", user.getId());

        Intent intent = new Intent(context, newActivity);
        intent.putExtra("userInformation", bundle);
        context.startActivity(intent);
    }

    public User createUser(ArrayList<LinkedHashMap<String, Object>> result) {
        User user = null;
        if (result.isEmpty())
            ((StandardLogInActivity) activity).onLoginFail();
        else {
            LinkedHashMap<String, Object> keyObject;
            String firstname, lastname, key, id;
            try {
                LinkedHashMap<String, Object> userInformation = result.get(0);
                firstname = (String) userInformation.get("firstName");
                lastname = (String) userInformation.get("lastName");
                keyObject = (LinkedHashMap<String, Object>) userInformation.get("rfid");
                key = (String) keyObject.get("id");
                id = (String) userInformation.get("id");
                user = new User(firstname, lastname, new RfidKey(key), id);
            } catch (NullPointerException exception) {
                Log.v("CREATEUSER", "NULLPOINTEREXCEPTION");
            }
        }
        return user;
    }

    public void parseUserInformation(Bundle userInformation) {
        String firstName = (String) userInformation.get("firstname");
        String lastName = (String) userInformation.get("lastname");
        String key = (String) userInformation.get("rfidkey");
        String id = (String) userInformation.get("id");
        user = new User(firstName, lastName, new RfidKey(key), id);
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
        for (AndroidStamp stamp : stamps) {
            dataSet.add(stamp);
        }
        activity.dataRecieved(dataSet);
    }

    public void finishedLoading(User user) {
        activity.stopLoadingAnimation();
        Log.v("User rfid", user.getRfid().getId());
        if(user != null)
            ((StandardLogInActivity)activity).onLoginSuccess(user);
        else
            ((StandardLogInActivity)activity).onLoginFail();
    }

    public void showConnectionErrorMessage(String message, boolean retry) {
        activity.showConnectionErrorMessage(message, retry);
    }

    public User getUser() {
        return user;
    }

    public ArrayList<WeekViewEvent> getGraphEvents(Calendar selectedDay) {
        if (selectedDay == null)
            selectedDay = Calendar.getInstance();
        ArrayList<AndroidStamp> stamps = getStampsForDay(selectedDay);
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

            WeekViewEvent event = new WeekViewEvent(1, "", startTime, endTime);
            event.setColor(((MainActivity) activity).getResources().getColor(R.color.colorPrimaryLight));
            events.add(event);
        }
        return events;
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
        sortAscending = true;
        ((MainActivity) activity).setDataSet(dataSet);
    }

    public void sortDescending() {
        Collections.sort(dataSet, new AndroidStampSortDescending());
        sortAscending = false;
        ((MainActivity) activity).setDataSet(dataSet);
    }
}
