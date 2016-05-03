package se.mah.projekt_1;

import android.app.TimePickerDialog;
import android.database.DataSetObserver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;
import android.widget.TimePicker;

import java.util.ArrayList;

public class NotifyActivity extends AppCompatActivity implements MenuItem.OnMenuItemClickListener, View.OnClickListener, TimePickerDialog.OnTimeSetListener {

    private AppCompatSpinner reasonSpinner;
    private AppCompatEditText etMessage;
    private MenuItem sendButton;
    private Toolbar toolbar;
    private int hour, minute;
    private ArrayList<String> reasons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);
        initComponents();
        initToolbar();
        reasons.add("Sickness");
        reasons.add("Delayed");
        reasons.add("Other");
        reasonSpinner.setAdapter(new ReasonSpinnerAdapter(this, reasons));
        showTimePicker();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        setTitle("Report Absence");
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
        toolbar.setNavigationOnClickListener(this);
        toolbar.setElevation(8);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.notify_menu, menu);
        sendButton = menu.findItem(R.id.notify_send);
        sendButton.setOnMenuItemClickListener(this);
        return true;
    }

    private void initComponents() {
        reasonSpinner = (AppCompatSpinner) findViewById(R.id.reasonSpinner);
        etMessage = (AppCompatEditText) findViewById(R.id.etMessage);
        toolbar = (Toolbar) findViewById(R.id.app_bar_notify);
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        return false; //TODO send all the shit
    }

    private void showTimePicker() {
        TimePickerDialog dialog = new TimePickerDialog(this, null, 8, 0, true);
        dialog.setTitle("Expected time to arrive");
        dialog.show();
    }

    @Override
    public void onClick(View view) {
        onBackPressed();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        hour = i;
        minute = i1;
    }
}
