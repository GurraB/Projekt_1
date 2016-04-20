package se.mah.projekt_1;

import android.graphics.Color;
import android.graphics.RectF;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Gustaf on 19/04/2016.
 */
public class CalendarViewListener implements MonthLoader.MonthChangeListener, WeekView.EventClickListener, WeekView.EventLongPressListener{

    private List<WeekViewEvent> events;

    public CalendarViewListener(List<WeekViewEvent> events) {
        this.events = events;
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {

    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {

    }

    @Override
    public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        return events;
    }

    public void setEvents(ArrayList<WeekViewEvent> graphEvents) {
        events = graphEvents;
    }
}
