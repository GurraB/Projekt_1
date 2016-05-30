package se.mah.projekt_1.Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;

import se.mah.projekt_1.Models.GraphEvent;
import se.mah.projekt_1.R;

/**
 * Created by Gustaf Bohlin on 21/05/2016.
 * This view is a timeline showing daily events
 */
public class VisualSchedule extends View {

    private ArrayList<GraphEvent> events = new ArrayList<>();
    private Calendar day;

    private int mBackgroundColor = Color.rgb(238, 238, 238);
    private int mStampColor = Color.argb(128, 212, 225, 87);
    private int mScheduleColor = Color.rgb(38, 166, 154);
    private int mLineColor = Color.argb(128, 33, 33, 33);
    private int mPrimaryTextColor = Color.rgb(0, 0, 0);
    private int mTitleTextColor = Color.rgb(255, 255, 255);

    private float mHourHeight = 100;
    private float mTextSize = 25;
    private float mTitleTextSize = 20;
    private float mSidebarWidth = 100;

    private Paint mBackgroundPaint;
    private Paint mLinePaint;
    private Paint mPrimaryTextPaint;
    private Paint mEventPaint;
    private Paint mTitleTextPaint;

    private float mWidth;
    private float mHeight;


    /**
     * Constructor
     * @param context context
     */
    public VisualSchedule(Context context) {
        super(context);
    }

    /**
     * Constructor
     * @param context context
     * @param attrs attributes
     */
    public VisualSchedule(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.VisualSchedule, 0, 0);
        try {
            mBackgroundColor = array.getColor(R.styleable.VisualSchedule_backgroundColor, mBackgroundColor);
            mStampColor = array.getColor(R.styleable.VisualSchedule_stampColor, mStampColor);
            mScheduleColor = array.getColor(R.styleable.VisualSchedule_scheduleColor, mScheduleColor);
            mLineColor = array.getColor(R.styleable.VisualSchedule_lineColor, mLineColor);
            mPrimaryTextColor = array.getColor(R.styleable.VisualSchedule_textColor, mPrimaryTextColor);

            mHourHeight = array.getFloat(R.styleable.VisualSchedule_hourHeight, mHourHeight);
            mTextSize = array.getFloat(R.styleable.VisualSchedule_textSize, mTextSize);
            mSidebarWidth = array.getFloat(R.styleable.VisualSchedule_sideBarWidth, mSidebarWidth);
        } finally {
            array.recycle();
        }
        init();
    }

    /**
     * Constructor
     * @param context context
     * @param attrs attributeset
     * @param defStyleAttr
     */
    public VisualSchedule(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        mBackgroundPaint = createBackgroundPaint();
        mPrimaryTextPaint = createPrimaryTextPaint();
        mLinePaint = createLinePaint();
        mEventPaint = createEventPaint();
        mTitleTextPaint = createTitleTextPaint();
    }

    private Paint createBackgroundPaint() {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(mBackgroundColor);
        return paint;
    }

    private Paint createPrimaryTextPaint() {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(mPrimaryTextColor);
        paint.setTextSize(mTextSize);
        return paint;
    }

    private Paint createLinePaint() {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(mLineColor);
        paint.setStyle(Paint.Style.FILL);
        return paint;
    }

    private Paint createEventPaint() {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(mStampColor);
        paint.setStyle(Paint.Style.FILL);
        return paint;
    }

    private Paint createTitleTextPaint() {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(mTitleTextColor);
        paint.setTextSize(mTitleTextSize);
        paint.setStyle(Paint.Style.FILL);
        return paint;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        float xpad = (float) (getPaddingLeft() + getPaddingRight());
        float ypad = (float) (getPaddingTop() + getPaddingBottom());

        mWidth = w - xpad;
        mHeight = (mHourHeight * 24) + mTextSize + ypad;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, (int) mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBackground(canvas);
        drawSidebar(canvas);
        drawHourLines(canvas);
        drawEvents(canvas);
    }

    private void drawBackground(Canvas canvas) {
        canvas.drawRect(0, 0, mWidth, mHeight, mBackgroundPaint);
    }

    private void drawSidebar(Canvas canvas) {
        for (int i = 0; i < 25; i++) {
            canvas.drawText((i < 10 ? "0" + i : i) + ":00", 10, (mHourHeight * i) + mTextSize, mPrimaryTextPaint);
        }
    }

    private void drawHourLines(Canvas canvas) {
        for (int i = 0; i < 25; i++) {
            float y = (mHourHeight * i) + (mTextSize / 2);
            canvas.drawLine(mSidebarWidth, y, mWidth, y, mLinePaint);
        }
    }

    private void drawEvents(Canvas canvas) {
        float widthPerDay = mWidth - mSidebarWidth;
        ArrayList<GraphEvent> dailyEvents = getGraphsForDay();
        for (GraphEvent event : dailyEvents) {
            Calendar startTime = Calendar.getInstance();
            Calendar endTime = Calendar.getInstance();
            startTime.setTimeInMillis(event.getStart());
            endTime.setTimeInMillis(event.getStop());
            int startMinute = (startTime.get(Calendar.HOUR_OF_DAY) * 60) + startTime.get(Calendar.MINUTE);
            int endMinute = (startTime.get(Calendar.DAY_OF_YEAR) < endTime.get(Calendar.DAY_OF_YEAR) ? 1440 : (endTime.get(Calendar.HOUR_OF_DAY) * 60) + endTime.get(Calendar.MINUTE));
            float startX;
            float endX;
            float startY = startMinute * (mHourHeight / 60) + (mTextSize / 2);
            float endY = endMinute * (mHourHeight / 60) + (mTextSize / 2);
            if (!event.isStamp()) {
                mEventPaint.setColor(mStampColor);
                startX = widthPerDay / 2 + mSidebarWidth;
                endX = widthPerDay + mSidebarWidth;
            }
            else {
                mEventPaint.setColor(mScheduleColor);
                startX = mSidebarWidth;
                endX = widthPerDay / 2 + mSidebarWidth;
            }
            canvas.drawRect(startX, startY, endX , endY, mEventPaint);
            drawTitle(event, startX, startY + mTitleTextSize, endX, endY, canvas);
        }
    }

    private void drawTitle(GraphEvent event, float startX, float startY, float endX, float endY, Canvas canvas) {
        if ((endY - startY) - (mTitleTextSize - 4) < 0) return;
        String start = event.getTitle().substring(0, event.getTitle().indexOf('-'));
        String end = event.getTitle().substring(event.getTitle().indexOf('-') + 1, event.getTitle().length());
        canvas.drawText(start, startX, startY, mTitleTextPaint);
        canvas.drawText(end, endX - (float) (mTitleTextSize * 2.5) - 2, endY - 4, mTitleTextPaint);
    }

    /**
     * To set the data the view works with
     * @param events all events
     */
    public void notifyDataChanged(ArrayList<GraphEvent> events) {
        this.events = events;
        invalidate();
    }

    private ArrayList<GraphEvent> getGraphsForDay() {
        ArrayList<GraphEvent> eventsforDay = new ArrayList<>();
        if (day == null)
            day = Calendar.getInstance();

        Calendar start = Calendar.getInstance();
        start.setTimeInMillis(day.getTimeInMillis());

        Calendar end = Calendar.getInstance();
        end.setTimeInMillis(day.getTimeInMillis());

        start.set(Calendar.HOUR_OF_DAY, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 1);

        end.set(Calendar.HOUR_OF_DAY, 23);
        end.set(Calendar.MINUTE, 59);
        end.set(Calendar.SECOND, 59);

        for (GraphEvent event : events)
            if ((event.getStart() >= start.getTimeInMillis() && event.getStart() <= end.getTimeInMillis())
                    ||
                    (event.getStop() <= end.getTimeInMillis() && event.getStop() >= start.getTimeInMillis()))
                eventsforDay.add(event);
        return eventsforDay;
    }


    /****************************************************************
     * Variables                           *
     ****************************************************************/

    public int getmBackgroundColor() {
        return mBackgroundColor;
    }

    public void setmBackgroundColor(int mBackgroundColor) {
        this.mBackgroundColor = mBackgroundColor;
        invalidate();
        requestLayout();
    }

    /**
     * Sets the day to display and draws it again
     * @param day day to display
     */
    public void setDay(Calendar day) {
        this.day = day;
        invalidate();
    }

    public int getmStampColor() {
        return mStampColor;
    }

    public void setmStampColor(int mStampColor) {
        this.mStampColor = mStampColor;
    }

    public int getmScheduleColor() {
        return mScheduleColor;
    }

    public void setmScheduleColor(int mScheduleColor) {
        this.mScheduleColor = mScheduleColor;
    }

    public int getmLineColor() {
        return mLineColor;
    }

    public void setmLineColor(int mLineColor) {
        this.mLineColor = mLineColor;
    }

    public int getmPrimaryTextColor() {
        return mPrimaryTextColor;
    }

    public void setmPrimaryTextColor(int mPrimaryTextColor) {
        this.mPrimaryTextColor = mPrimaryTextColor;
    }

    public int getmTitleTextColor() {
        return mTitleTextColor;
    }

    public void setmTitleTextColor(int mTitleTextColor) {
        this.mTitleTextColor = mTitleTextColor;
    }

    public float getmHourHeight() {
        return mHourHeight;
    }

    public void setmHourHeight(float mHourHeight) {
        this.mHourHeight = mHourHeight;
    }

    public float getmTextSize() {
        return mTextSize;
    }

    public void setmTextSize(float mTextSize) {
        this.mTextSize = mTextSize;
    }

    public float getmSidebarWidth() {
        return mSidebarWidth;
    }

    public void setmSidebarWidth(float mSidebarWidth) {
        this.mSidebarWidth = mSidebarWidth;
    }

    public float getmTitleTextSize() {
        return mTitleTextSize;
    }

    public void setmTitleTextSize(float mTitleTextSize) {
        this.mTitleTextSize = mTitleTextSize;
    }

    public Calendar getDay() {
        return day;
    }
}