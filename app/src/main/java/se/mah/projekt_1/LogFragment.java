package se.mah.projekt_1;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;

/**
 * Created by Gustaf on 06/04/2016.
 * A Fragment containing a log with all the timestamps
 */
public class LogFragment extends Fragment {

    private static ArrayList<AndroidStamp> dataSet;
    private RecyclerView recyclerView;
    private View rootView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;

    /**
     * Constructor
     */
    public LogFragment() {
        super();
    }

    /**
     * returns a new instance of the LogFragment
     * @param data the data to display in the fragment
     * @return the new instance
     */
    public static LogFragment newInstance(ArrayList<AndroidStamp> data) {
        dataSet = data;
        return new LogFragment();
    }

    /**
     * OnCreate
     * @param savedInstanceState savedInstance
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Called when the view is created, initializes components etc
     * @param inflater LayoutInflater
     * @param container Viewgroup
     * @param savedInstanceState savedInstance
     * @return rootView
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_log, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new RecyclerViewDivider(getContext(), 1, R.drawable.vertical_divider));
        recyclerView.setAdapter(new RecyclerViewAdapter(dataSet));

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(((MainActivity) getContext()).getActionHandler());
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));

        progressBar = (ProgressBar) rootView.findViewById(R.id.log_progressbar);
        progressBar.setVisibility(View.GONE);

        Log.v("LOGFRAGMENT", "ONCREATEVIEW");
        return rootView;
    }

    /**
     * Sets the data in the recyclerview
     * @param androidStamps data to be displayed
     */
    public void setRecyclerViewData(ArrayList<AndroidStamp> androidStamps) {
        this.dataSet = androidStamps;
        swipeRefreshLayout.setRefreshing(false);
        recyclerView.swapAdapter(new RecyclerViewAdapter(dataSet), false);
    }

    /**
     * Start the swipe to refresh loading animation
     */
    public void startLoadingAnim() {
        if(progressBar != null && !swipeRefreshLayout.isRefreshing())
            progressBar.setVisibility(View.VISIBLE);
        if(recyclerView != null)
            recyclerView.setAdapter(new RecyclerViewAdapter(new ArrayList<AndroidStamp>()));
    }

    /**
     * Stop the swipe to refresh loading animation
     */
    public void stopLoadingAnim() {
        if(progressBar != null)
            progressBar.setVisibility(View.GONE);
    }
}
