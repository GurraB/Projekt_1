package se.mah.projekt_1;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Gustaf on 06/04/2016.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {

    private int[] colors = {R.color.c0,
            R.color.c1,
            R.color.c2,
            R.color.c3,
            R.color.c4,
            R.color.c6,
            R.color.c5,};

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private View itemView;
        public TextView textViewColor;
        public TextView textViewDate;
        public TextView textViewCheckIn;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            textViewDate = (TextView) itemView.findViewById(R.id.row_textview);
            textViewCheckIn = (TextView) itemView.findViewById(R.id.check_in_textview);
            textViewColor = (TextView) itemView.findViewById(R.id.tvColor);
        }
    }

    ArrayList<AndroidStamp> dataSet = new ArrayList<>();

    public RecyclerViewAdapter(ArrayList<AndroidStamp> dataSet) {
        this.dataSet = dataSet;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_row, parent, false);
        return new RecyclerViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        CalendarFormatter formatter = new CalendarFormatter(dataSet.get(position).getDate());
        holder.textViewDate.setText(formatter.toStringNoYearWithTime());
        holder.textViewCheckIn.setText((dataSet.get(position).isCheckIn() ? "Check in" : "Check out"));
        holder.textViewColor.setBackgroundResource(colors[dataSet.get(position).getDate().get(Calendar.DAY_OF_WEEK) - 1]);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
