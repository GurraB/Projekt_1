package se.mah.projekt_1;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Gustaf on 06/04/2016.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {

    ArrayList<String> dataSet = new ArrayList<>();

    public RecyclerViewAdapter(ArrayList<String> dataSet) {
        this.dataSet = dataSet;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_row, parent, false);
        return new RecyclerViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        //TODO handle recyclerview_row content
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {

        View itemView;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
        }
    }
}
