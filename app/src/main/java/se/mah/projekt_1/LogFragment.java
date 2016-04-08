package se.mah.projekt_1;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Gustaf on 06/04/2016.
 */
public class LogFragment extends Fragment {

    private MainActivity mainActivity;
    private ArrayList<TimeStamp> dataSet;
    private RecyclerView recyclerView;
    private View rootView;

    public LogFragment() {
        super();
        dataSet = new ArrayList<>();
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_log, container, false);
        setUpRecyclerView(rootView);
        return rootView;
    }

    private void setUpRecyclerView(View rootView) {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new RecyclerViewDivider(10));
        recyclerView.setAdapter(new RecyclerViewAdapter(dataSet));
        recyclerView.setHasFixedSize(true);
    }

    public void setRecyclerViewData() {
        Log.v("LOGFRAGMENT", "----------------SetRecyclerViewData----------------" + dataSet.size());
        if(recyclerView == null) {
            Log.v("LOGFRAGMENT", "----------------RecyclerView == null----------------" + dataSet.size());
            recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        }
        recyclerView.setAdapter(new RecyclerViewAdapter(dataSet));
    }

    public ArrayList<TimeStamp> getDataSet() {
        return dataSet;
    }

    public void setDataSet(ArrayList<TimeStamp> dataSet) {
        this.dataSet = dataSet;
    }
}
