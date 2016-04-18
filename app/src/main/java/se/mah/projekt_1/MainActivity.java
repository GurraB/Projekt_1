package se.mah.projekt_1;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fasterxml.jackson.annotation.JacksonAnnotation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;

/**
 * Created by Gustaf on 06/04/2016.
 * The activity after the login. This acts as a Controller for the LogFragment and the GraphFragment
 */

public class MainActivity extends AppCompatActivity implements AsyncTaskCompatible{

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private DrawerLayout drawer;
    private RelativeLayout left_drawer;
    private ImageView actionView;
    private MenuItem refreshItem;
    private MenuItem sort;
    private ActionHandler drawerListener;
    private LogFragment logFragment;
    private ArrayList<AndroidStamp> dataSet = new ArrayList<>();
    private Animation loadingAnimation;
    private Controller controller;
    private TextView userName;
    private EditText etFrom;
    private EditText etTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        controller = new Controller(this);
        if(savedInstanceState != null)
            controller.parseSavedInstance(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();
        if(savedInstanceState == null) {
            controller.getServerData(ApiClient.BETWEEN);
        }
    }

    private void initComponents() {
        //om den inte har bundle?
        Bundle userInformation = getIntent().getBundleExtra("userInformation");
        if(userInformation != null)
            controller.parseUserInformation(userInformation);
        else
            Log.v("MAINACTIVITY", "BUNDLE IS NULL");

        findComponents();
        drawerListener = new ActionHandler(this, drawer, left_drawer, toolbar, R.string.open_drawer_string, R.string.close_drawer_string, controller);
        setUpToolbar();
        setUpViewPager();
        setUpNavDrawer();
        tabLayout.setupWithViewPager(viewPager);
        userName.setText(controller.getUser().getName());
    }

    private void findComponents() {
        userName = (TextView) findViewById(R.id.nav_drawer_name);
        etFrom = (EditText) findViewById(R.id.nav_drawer_from);
        etTo = (EditText) findViewById(R.id.nav_drawer_to);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        drawer = (DrawerLayout) findViewById(R.id.nav_drawer);
        left_drawer = (RelativeLayout) findViewById(R.id.left_drawer);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        tabLayout = (TabLayout) findViewById(R.id.tab);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.mainmenu, menu);
        refreshItem = menu.findItem(R.id.refresh_button);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh_button:
                startLoadingAnimation(refreshItem);
                controller.getServerData(ApiClient.BETWEEN);
                break;
            case R.id.sort_button:
                break;
        }
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        long[] dataSetDatesArray = new long[dataSet.size()];
        boolean[] dataSetCheckInArray = new boolean[dataSet.size()];
        for (int i = 0; i < dataSet.size(); i++) {
            dataSetDatesArray[i] = dataSet.get(i).getDate().getTimeInMillis();
            dataSetCheckInArray[i] = dataSet.get(i).isCheckIn();
        }
        outState.putLong("DATETO", controller.getDateTo().getCalendar().getTimeInMillis());
        outState.putLong("DATEFROM", controller.getDateFrom().getCalendar().getTimeInMillis());
        outState.putLongArray("ALLTIMES", dataSetDatesArray);
        outState.putBooleanArray("ALLCHECKIN", dataSetCheckInArray);
        super.onSaveInstanceState(outState);
    }

    public void setUpToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_date_range_white2_24dp);
        toolbar.setNavigationOnClickListener(drawerListener);
        getSupportActionBar().setTitle(controller.getDateFrom().toStringNoYear() + " - " + controller.getDateTo().toStringNoYear());
    }

    /**
     * Initialize the navigation drawer
     */
    private void setUpNavDrawer() {
        drawer.addDrawerListener(drawerListener);
        drawer.closeDrawer(left_drawer);
        etTo.setOnClickListener(drawerListener);
        etFrom.setOnClickListener(drawerListener);
    }

    /**
     * Initialize the ViewPager with fragments
     */
    private void setUpViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(logFragment = LogFragment.newInstance(dataSet), "Log");
        adapter.addFragment(new GraphFragment(), "Graphs");
        viewPager.setAdapter(adapter);
    }

    /**
     * Start the loading animation
     * @param item menuItem be spinnin'
     */
    public void startLoadingAnimation(MenuItem item) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        actionView = (ImageView) inflater.inflate(R.layout.refresh_action_view, null);  //ActionView required to apply animation to
        loadingAnimation = AnimationUtils.loadAnimation(this, R.anim.refresh_spin);
        item.setActionView(actionView);
        actionView.startAnimation(loadingAnimation);
    }

    /**
     * Stops the loading animation when the data has finished loading
     */
    public void stopLoadingAnimation() {
        try {
            loadingAnimation.cancel();
            actionView.clearAnimation();
            refreshItem.setActionView(null);
        } catch (NullPointerException exception) {
            Log.v("MAINACTIVITY", "LOADINGANIMATION IS NULL");
        } catch (RuntimeException exception) {
            Log.v("MAINACTIVITY", "CAN'T TOUCH THIS");
        }
    }

    public void setEtFrom(String from) {
        etFrom.setText(from);
    }

    public void setEtTo(String to) {
        etTo.setText(to);
    }

    /**
     * Shows a snackbar displaying that the server can't be reached
     * @param type What type of data that couldn't be retrieved
     * @param button true if there should be a button to retry
     */
    public void showConnectionErrorMessage(final String type, boolean button) {
        Snackbar snackbar = Snackbar
                .make(tabLayout, "Can't connect to Server!", Snackbar.LENGTH_LONG);
        if(button)
                snackbar.setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //startLoadingAnimation(refreshItem);
                        //getServerData(type);
                    }
                });
        snackbar.show();
    }

    public void setDataSet(ArrayList<AndroidStamp> stamps) {
        logFragment.setRecyclerViewData(stamps);
    }

    @Override
    public void dataRecieved(Object data) {
        setDataSet(controller.parseTimeStamps((ArrayList<LinkedHashMap<String, Object>>) data));
    }

    public void updateDateSpan() {
        toolbar.setTitle(controller.getDateFrom().toStringNoYear() + " - " + controller.getDateTo().toStringNoYear());
    }


    //can
    /**
     * NavDrawerListener handles the events for the drawer, onClick for buttons and the datepicker dialog events.
     */
/*    private class NavDrawerListener extends ActionBarDrawerToggle implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

        private CalendarFormatter dateFrom;
        private CalendarFormatter tempDateFrom;
        private CalendarFormatter dateTo;
        private CalendarFormatter tempDateTo;
        private EditText eTFrom;
        private EditText eTTo;
        private Activity activity;
        private DatePicker dPFrom;
        private DatePicker dPTo;

        /**
         * Constructor
         * @param activity the activity
         * @param drawerLayout the drawerLayout to apply
         * @param toolbar the activity toolbar
         * @param openDrawerContentDescRes content description on open
         * @param closeDrawerContentDescRes content description on open
         * @param tempDateFrom current dateFrom
         * @param tempDateTo current dateTo
         */
/*        public NavDrawerListener(Activity activity, DrawerLayout drawerLayout, Toolbar toolbar, int openDrawerContentDescRes, int closeDrawerContentDescRes, CalendarFormatter tempDateFrom, CalendarFormatter tempDateTo) {
            super(activity, drawerLayout, toolbar, openDrawerContentDescRes, closeDrawerContentDescRes);
            this.activity = activity;
            this.tempDateFrom = tempDateFrom;
            this.tempDateTo = tempDateTo;
            eTFrom = (EditText) findViewById(R.id.nav_drawer_from);
            eTTo = (EditText) findViewById(R.id.nav_drawer_to);
            eTFrom.setText(tempDateFrom.toString());
            eTTo.setText(tempDateTo.toString());
            eTFrom.setOnClickListener(this);
            eTTo.setOnClickListener(this);
        }

        /** Called when a drawer has settled in a completely closed state. */
/*        public void onDrawerClosed(View view) {
            super.onDrawerClosed(view);
            tempDateFrom = getDateFrom();
            tempDateTo = getDateTo();
            toolbar.setTitle(getDateFrom().toStringNoYear() + " - " + getDateTo().toStringNoYear());
            startLoadingAnimation(refreshItem);
            getServerData(ApiClient.BETWEEN);
        }

        /** Called when a drawer has settled in a completely open state. */
/*        public void onDrawerOpened(View drawerView) {
            super.onDrawerOpened(drawerView);
            dateFrom = null;
            dateTo = null;
        }

        @Override
        public void onClick(View view) {
            Calendar calendar = Calendar.getInstance();
            switch (view.getId()) {
                case R.id.nav_drawer_from:
                    DatePickerDialog datePickerDialogFrom = new DatePickerDialog(activity, this,
                            calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                    dPFrom = datePickerDialogFrom.getDatePicker();
                    datePickerDialogFrom.show();
                    break;
                case R.id.nav_drawer_to:
                    DatePickerDialog datePickerDialogTo = new DatePickerDialog(activity, this,
                            calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                    dPTo = datePickerDialogTo.getDatePicker();
                    datePickerDialogTo.show();
                    break;
                default:
                    drawer.openDrawer(left_drawer);
                    break;
            }
        }

        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            Calendar calendarFrom = Calendar.getInstance();
            Calendar calendarTo = Calendar.getInstance();
            if(datePicker.equals(dPFrom)) {
                calendarFrom.set(i, i1, i2, 0, 0, 1);   //borde ändra så att man får början på dan istället för just nu
                dateFrom = new CalendarFormatter(calendarFrom);
                eTFrom.setText(dateFrom.toString());
            }
            else if(datePicker.equals(dPTo)) {
                calendarTo.set(i, i1, i2, 23, 59, 59);  //borde ändra så att man får slutet på dan istället för just nu
                dateTo = new CalendarFormatter(calendarTo);
                eTTo.setText(dateTo.toString());
            }
        }

        /**
         * Returns the dateFrom from the dialog, if that is null it returns the previous dateFrom
         * @return CalendarFormatter for dateFrom
         */
/*        public CalendarFormatter getDateFrom() {
            if(dateFrom != null)
                return dateFrom;
            return tempDateFrom;
        }

        /**
         * Returns the dateTo from the dialog, if that is null it returns the previous dateTo
         * @return CalendarFormatter for dateTo
         */
/*        public CalendarFormatter getDateTo() {
            if(dateTo != null)
                return dateTo;
            return tempDateTo;
        }
    }*/
}
