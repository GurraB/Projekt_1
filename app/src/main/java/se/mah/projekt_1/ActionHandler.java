package se.mah.projekt_1;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;

import java.util.Calendar;

/**
 * Created by Gustaf on 15/04/2016.
 */
public class ActionHandler extends ActionBarDrawerToggle implements View.OnClickListener, DatePickerDialog.OnDateSetListener, ViewPager.OnPageChangeListener {

    private CalendarFormatter dateFrom;
    private CalendarFormatter dateTo;
    private Activity activity;
    private DatePicker dPFrom;
    private DatePicker dPTo;
    private Controller controller;
    private DrawerLayout drawerLayout;
    private RelativeLayout left_drawer;

    /**
     * Constructor
     * @param activity the activity
     * @param drawerLayout the drawerLayout to apply
     * @param toolbar the activity toolbar
     * @param openDrawerContentDescRes content description on open
     * @param closeDrawerContentDescRes content description on open
     */
    public ActionHandler(Activity activity, DrawerLayout drawerLayout, RelativeLayout left_drawer, Toolbar toolbar, int openDrawerContentDescRes, int closeDrawerContentDescRes, Controller controller) {
        super(activity, drawerLayout, toolbar, openDrawerContentDescRes, closeDrawerContentDescRes);
        this.left_drawer = left_drawer;
        this.drawerLayout = drawerLayout;
        this.activity = activity;
        this.controller = controller;
    }

    /** Called when a drawer has settled in a completely closed state. */
    public void onDrawerClosed(View view) {
        super.onDrawerClosed(view);
        controller.onDateFromSet(dateFrom);
        controller.onDateToSet(dateTo);
        controller.navDrawerClosed();
    }

    /** Called when a drawer has settled in a completely open state. */
    public void onDrawerOpened(View drawerView) {
        super.onDrawerOpened(drawerView);
        controller.onDateFromSet(null);
        controller.onDateToSet(null);
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
                drawerLayout.openDrawer(left_drawer);
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
            controller.onDateFromSet(dateFrom);
        }
        else if(datePicker.equals(dPTo)) {
            calendarTo.set(i, i1, i2, 23, 59, 59);  //borde ändra så att man får slutet på dan istället för just nu
            dateTo = new CalendarFormatter(calendarTo);
            controller.onDateToSet(dateTo);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                break;
            case 1:
                ((MainActivity) activity).loadGraph();
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
