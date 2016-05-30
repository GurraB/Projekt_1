package se.mah.projekt_1.Fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import java.util.ArrayList;
import java.util.Calendar;

import se.mah.projekt_1.Models.CalendarFormatter;
import se.mah.projekt_1.Controller.Controller;
import se.mah.projekt_1.Models.GraphEvent;
import se.mah.projekt_1.R;
import se.mah.projekt_1.Views.VisualSchedule;

/**
 * Created by Gustaf Bohlin on 06/04/2016.
 * A Fragment that shows a graph
 */
public class GraphFragment extends Fragment {

    private Controller controller;
    private VisualSchedule calendarView;
    private EditText etDate;
    private Calendar selectedDay = Calendar.getInstance();

    /**
     * Constructor for the Fragment, use newInstance instead
     */
    public GraphFragment() {
        super();
    }

    /**
     * Creates a new Instance of the Fragment
     * @param controller the controller to use
     * @return An instance of this fragment
     */
    public static GraphFragment newInstance(Controller controller) {
        GraphFragment graphFragment = new GraphFragment();
        graphFragment.setController(controller);
        return graphFragment;
    }

    /**
     * Sets the controller
     * @param controller
     */
    public void setController(Controller controller) {
        this.controller = controller;
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
     * When the view is created, initializes components etc.
     * @param inflater LayoutInflater
     * @param container ViewGroup
     * @param savedInstanceState SavedInstance
     * @return the rootView
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_graph, container, false);
        calendarView = (VisualSchedule) rootView.findViewById(R.id.calendarView);
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
                        showGraph();
                    }
                },
                        selectedDay.get(Calendar.YEAR), selectedDay.get(Calendar.MONTH), selectedDay.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
        etDate.setText(new CalendarFormatter(selectedDay).toStringNoYear());
        return rootView;
    }

    /**
     * Update and Show the graph
     */
    public void showGraph() {
        ArrayList<GraphEvent> graphEvents = controller.getGraphEvents(selectedDay);
        calendarView.setDay(selectedDay);
        calendarView.notifyDataChanged(graphEvents);
        etDate.setText(new CalendarFormatter(selectedDay).toStringNoYear());
    }

}
