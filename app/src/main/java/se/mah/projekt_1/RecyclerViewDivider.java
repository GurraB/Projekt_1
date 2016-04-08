package se.mah.projekt_1;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Gustaf on 07/04/2016.
 */
public class RecyclerViewDivider extends RecyclerView.ItemDecoration{
    private int dividerHeight;

    public RecyclerViewDivider(int dividerHeight) {
        this.dividerHeight = dividerHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
            outRect.bottom = dividerHeight;
    }
}
