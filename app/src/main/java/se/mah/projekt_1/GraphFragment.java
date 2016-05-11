package se.mah.projekt_1;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;

import java.util.ArrayList;
import java.util.Calendar;

import static se.mah.projekt_1.R.style.AppTheme;

/**
 * Created by Gustaf on 06/04/2016.
 */
public class GraphFragment extends Fragment {

    private Controller controller;
    private CalendarViewListener listener;
    private FixedCalendarView calendarView;
    private EditText etDate;
    private Calendar selectedDay = Calendar.getInstance();

    public GraphFragment() {
        super();
    }

    public static GraphFragment newInstance(Controller controller) {
        GraphFragment graphFragment = new GraphFragment();
        graphFragment.setController(controller);
        return graphFragment;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_graph, container, false);
        calendarView = (FixedCalendarView) rootView.findViewById(R.id.calendarView);
        etDate = (EditText) rootView.findViewById(R.id.etGraphDate);
        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        selectedDay.set(Calendar.YEAR, i);
                        selectedDay.set(Calendar.MONTH, i1);
                        selectedDay.set(Calendar.DAY_OF_MONTH, i2);
                        calendarView.goToDate(selectedDay);
                        showGraph();
                    }
                },
                        selectedDay.get(Calendar.YEAR), selectedDay.get(Calendar.MONTH), selectedDay.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
        listener = new CalendarViewListener(new ArrayList<WeekViewEvent>());
        calendarView.setMonthChangeListener(listener);
        etDate.setText(new CalendarFormatter(selectedDay).toStringNoYear());
        return rootView;
    }

    public void showGraph() {
        ArrayList<WeekViewEvent> graphEvents = controller.getGraphEvents(selectedDay);
        Log.v("AMOUNT OF EVENTS", "WEEKVIEW EVENTS: " + String.valueOf(graphEvents.size()));
        listener.setEvents(graphEvents);
        calendarView.notifyDatasetChanged();
        etDate.setText(new CalendarFormatter(selectedDay).toStringNoYear());
        if(graphEvents.size() != 0)
            calendarView.goToHour(graphEvents.get(0).getStartTime().get(Calendar.HOUR_OF_DAY));
    }

}
