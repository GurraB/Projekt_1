package se.mah.projekt_1;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Gustaf on 06/04/2016.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private View itemView;
        public TextView textViewDate;
        public TextView textViewCheckIn;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            textViewDate = (TextView) itemView.findViewById(R.id.row_textview);
            textViewCheckIn = (TextView) itemView.findViewById(R.id.check_in_textview);
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
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
