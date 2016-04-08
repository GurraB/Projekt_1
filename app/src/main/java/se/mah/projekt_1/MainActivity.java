package se.mah.projekt_1;

import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private LogFragment logFragment;
    private GraphFragment graphFragment;
    private ArrayList<TimeStamp> dataSet = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("Harald Svensson");
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setUpViewPager();

        tabLayout = (TabLayout) findViewById(R.id.tab);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setUpViewPager() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        logFragment = (LogFragment) fragmentManager.findFragmentByTag("Log");
        graphFragment = (GraphFragment) fragmentManager.findFragmentByTag("Graph");
        ViewPagerAdapter adapter = new ViewPagerAdapter(fragmentManager, this);

        if(logFragment == null) {
            Log.v("MainActivity", "Log fragment is NULL!");
            Log.v("ViewPagerAdapterCount", "--------------------------------------" + Integer.toString(adapter.getCount()));
            fragmentManager.beginTransaction().add((logFragment = new LogFragment()), "Log").commit();
            Log.v("ViewPagerAdapterCount", "--------------------------------------" + Integer.toString(adapter.getCount()));
        }
        if (graphFragment == null) {
            fragmentManager.beginTransaction().add(graphFragment = new GraphFragment(), "Graph").commit();
        }
        fragmentManager.executePendingTransactions();
        loadData(ApiClient.ALL);
        dataSet = logFragment.getDataSet();
        adapter.addFragment(logFragment, "Log");
        adapter.addFragment(graphFragment, "Graphs");
        viewPager.setAdapter(adapter);
    }

    private void loadData(String type) {
        ApiClient apiClient = new ApiClient(this);
        apiClient.execute(type);
    }

    public void finishedLoading(ArrayList<LinkedHashMap<String, Long>> data) {
        ArrayList<TimeStamp> timeStamps = new ArrayList<>();
        for (LinkedHashMap<String, Long> dataItem : data) {
            Calendar calendar = new GregorianCalendar();
            calendar.setTimeInMillis(dataItem.get("date"));
            //boolean checkIn = Boolean.parseBoolean(dataItem.get("CheckIn").toString());
            boolean checkIn = true;
            timeStamps.add(new TimeStamp(calendar, checkIn));
            timeStamps.get(0).getFormattedDate();
        }
        dataSet = timeStamps;
        logFragment.setDataSet(dataSet);
        logFragment.setRecyclerViewData();
        Log.v("MainActivity", "Finished Loading" + dataSet.size());
    }

    public void showConnectionErrorMessage() {
        Snackbar snackbar = Snackbar
                .make(tabLayout ,"Can't connect to Server!", Snackbar.LENGTH_LONG)
                .setAction("RETRY", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        loadData(ApiClient.ALL);
                    }
                });
        snackbar.show();
    }
}
