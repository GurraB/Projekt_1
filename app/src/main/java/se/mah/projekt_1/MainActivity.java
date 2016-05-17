package se.mah.projekt_1;

import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Gustaf on 06/04/2016.
 * The activity after the login. This acts as a Controller for the LogFragment and the GraphFragment
 */

public class MainActivity extends AppCompatActivity implements AsyncTaskCompatible {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private DrawerLayout drawer;
    private RelativeLayout left_drawer;
    private MenuItem sort;
    private ActionHandler actionHandler;
    private LogFragment logFragment;
    private GraphFragment graphFragment;
    private ArrayList<AndroidStamp> dataSet = new ArrayList<>();
    private Controller controller;
    private TextView userName;
    private EditText etFrom;
    private EditText etTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        controller = new Controller(this);
        if (savedInstanceState != null)
            controller.parseSavedInstance(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();
        if (savedInstanceState == null)
            controller.getServerStamps();
    }

    private void initComponents() {
        //om den inte har bundle?
        Bundle userInformation = getIntent().getBundleExtra("userInformation");
        if (userInformation != null)
            controller.parseUserInformation(userInformation);
        else
            Log.v("MAINACTIVITY", "BUNDLE IS NULL");

        findComponents();
        actionHandler = new ActionHandler(this, drawer, left_drawer, toolbar, R.string.open_drawer_string, R.string.close_drawer_string, controller);
        setUpToolbar();
        setUpViewPager();
        setUpNavDrawer();
        tabLayout.setupWithViewPager(viewPager);
        setViewPagerIcons();
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
        Log.v("MENU CREATED", "CANCEEEER");
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh_button:
                controller.getServerStamps();
                break;
            case R.id.sort_button:
                break;
            case R.id.sort_newest_first:
                controller.sortDescending();
                break;
            case R.id.sort_oldest_first:
                controller.sortAscending();
                break;
            case R.id.logout:
                controller.startNewActivity(this, StandardLogInActivity.class);
                finish();
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
        toolbar.setNavigationOnClickListener(actionHandler);
        getSupportActionBar().setTitle(controller.getDateFrom().toStringNoYear() + " - " + controller.getDateTo().toStringNoYear());
    }

    /**
     * Initialize the navigation drawer
     */
    private void setUpNavDrawer() {
        drawer.addDrawerListener(actionHandler);
        drawer.closeDrawer(left_drawer);
        etTo.setOnClickListener(actionHandler);
        etFrom.setOnClickListener(actionHandler);
        userName.setText(controller.getUser().getName());
    }

    /**
     * Initialize the ViewPager with fragments
     */
    private void setUpViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(logFragment = LogFragment.newInstance(dataSet), "Log");
        adapter.addFragment(graphFragment = GraphFragment.newInstance(controller), "Graphs");
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(actionHandler);
    }

    private void setViewPagerIcons() {
        RelativeLayout logView = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.tab_layout_title_view, null);
        TextView logTitle = (TextView) logView.findViewById(R.id.tab_title);
        logTitle.setText("LOG");
        ImageView logTitle_icon = (ImageView) logView.findViewById(R.id.tab_icon);
        logTitle_icon.setImageDrawable(getApplicationContext().getDrawable(R.drawable.ic_list_white_24dp));
        tabLayout.getTabAt(0).setCustomView(logView);

        RelativeLayout graphView = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.tab_layout_title_view, null);
        TextView graphTitle = (TextView) graphView.findViewById(R.id.tab_title);
        graphTitle.setText("GRAPH");
        ImageView graphTitle_icon = (ImageView) graphView.findViewById(R.id.tab_icon);
        graphTitle_icon.setImageDrawable(getApplicationContext().getDrawable(R.drawable.ic_timeline_white_24dp));
        tabLayout.getTabAt(1).setCustomView(graphView);
    }

    public void setEtFrom(String from) {
        etFrom.setText(from);
    }

    public void setEtTo(String to) {
        etTo.setText(to);
    }

    public ActionHandler getActionHandler() {
        return actionHandler;
    }

    /**
     * Shows a snackbar displaying that the server can't be reached
     */
    @Override
    public void showConnectionErrorMessage(Controller.ErrorType type, final String message) {
        switch (type) {
            case CONNECTION_ERROR:
                Snackbar snackbar = Snackbar.make(viewPager, "Unable to connect to server", Snackbar.LENGTH_LONG);
                snackbar.setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        controller.getServerStamps();
                    }
                });
                snackbar.show();
                break;
            case UNATHORIZED:
                onLoginFail();
                break;
            case BAD_REQUEST:
                Snackbar.make(viewPager, "Bad request", Snackbar.LENGTH_LONG).show();
                break;
            default:
                Snackbar.make(viewPager, "Unknown error", Snackbar.LENGTH_LONG).show();
                break;
        }
    }

    public void setDataSet(ArrayList<AndroidStamp> stamps) {
        logFragment.setRecyclerViewData(stamps);
    }

    @Override
    public void dataRecieved(ArrayList<AndroidStamp> stamps) {
        setDataSet(stamps);
    }

    @Override
    public void startLoadingAnimation() {
        logFragment.startLoadingAnim();
    }

    public void stopLoadingAnimation() {
        logFragment.stopLoadingAnim();
    }

    public void updateDateSpan() {
        toolbar.setTitle(controller.getDateFrom().toStringNoYear() + " - " + controller.getDateTo().toStringNoYear());
    }

    public void loadGraph() {
        graphFragment.showGraph();
    }

    @Deprecated
    @Override
    public void onLoginSuccess(Account user) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onLoginFail() {
        Snackbar.make(viewPager, "Unathorized", Snackbar.LENGTH_LONG).show();
    }
}