package se.mah.projekt_1;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

        public TextView textViewDate;
        public TextView textViewCheckIn;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            textViewDate = (TextView) itemView.findViewById(R.id.row_textview);
            textViewCheckIn = (TextView) itemView.findViewById(R.id.check_in_textview);
        }
    }

    ArrayList<TimeStamp> dataSet = new ArrayList<>();

    public RecyclerViewAdapter(ArrayList<TimeStamp> dataSet) {
        this.dataSet = dataSet;
        Log.v("RecyclerViewAdapter", "Constructor" + getItemCount());
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_row, parent, false);
        Log.v("RecyclerViewAdapter", "onCreateViewHolder executed");
        return new RecyclerViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.textViewDate.setText(dataSet.get(position).getFormattedDate());
        holder.textViewCheckIn.setText((dataSet.get(position).isCheckIn() ? "Check in" : "Check out"));
        Log.v("RecyclerViewAdapter", "onBindViewHolder executed");
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}
