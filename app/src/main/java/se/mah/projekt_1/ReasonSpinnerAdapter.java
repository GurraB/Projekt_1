package se.mah.projekt_1;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Gustaf on 27/04/2016.
 */
public class ReasonSpinnerAdapter extends BaseAdapter {

    private ArrayList<String> reasons = new ArrayList<>();
    private Context context;

    public ReasonSpinnerAdapter(Context context, ArrayList<String> content) {
        this.reasons = content;
        this.context = context;
    }

    @Override
    public int getCount() {
        return reasons.size();
    }

    @Override
    public Object getItem(int i) {
        return reasons.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.spinner_row, null);
        TextView textView = (TextView) rootView.findViewById(R.id.spinner_textView);
        textView.setText(reasons.get(i));
        return rootView;
    }
}
