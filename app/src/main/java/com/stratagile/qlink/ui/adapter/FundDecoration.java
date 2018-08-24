package com.stratagile.qlink.ui.adapter;

import android.app.Activity;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.stratagile.qlink.R;

/**
 * Created by huzhipeng on 2018/1/11.
 */

public class FundDecoration extends RecyclerView.ItemDecoration {
    int mSpace;

    /**
     * Retrieve any offsets for the given item. Each field of <code>outRect</code> specifies
     * the number of pixels that the item view should be inset by, similar to padding or margin.
     * The default implementation sets the bounds of outRect to 0 and returns.
     * <p>
     * <p>
     * If this ItemDecoration does not affect the positioning of item views, it should set
     * all four fields of <code>outRect</code> (left, top, right, bottom) to zero
     * before returning.
     * <p>
     * <p>
     * If you need to access Adapter for additional data, you can call
     * {@link RecyclerView#getChildAdapterPosition(View)} to get the adapter position of the
     * View.
     *
     * @param outRect Rect to receive the output.
     * @param view    The child view to decorate
     * @param parent  RecyclerView this ItemDecoration is decorating
     * @param state   The current state of RecyclerView.
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = mSpace;
        int fund = view.getContext().getResources().getDisplayMetrics().widthPixels;

        if (fund < 750) {
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.top = (int) (mSpace * 1.5);
                outRect.left = mSpace * 2;
                outRect.right = mSpace / 2;
            }
            if (parent.getChildAdapterPosition(view) == 1) {
                outRect.top = (int) (mSpace * 1.5);
                outRect.right = mSpace * 2;
                outRect.left = mSpace / 2;
            }
            if (parent.getChildAdapterPosition(view) == 2) {
                outRect.left = mSpace * 2;
                outRect.right = mSpace / 2;
            }
            if (parent.getChildAdapterPosition(view) == 3) {
                outRect.right = mSpace * 2;
                outRect.left = mSpace / 2;
            }
        } else {
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.top = mSpace * 4;
                outRect.left = mSpace * 2;
                outRect.right = mSpace / 2;
            }
            if (parent.getChildAdapterPosition(view) == 1) {
                outRect.top = mSpace * 4;
                outRect.right = mSpace * 2;
                outRect.left = mSpace / 2;
            }
            if (parent.getChildAdapterPosition(view) == 2) {
                outRect.left = mSpace * 2;
                outRect.right = mSpace / 2;
            }
            if (parent.getChildAdapterPosition(view) == 3) {
                outRect.right = mSpace * 2;
                outRect.left = mSpace / 2;
            }
        }
    }

    public FundDecoration(int space) {
        this.mSpace = space;
    }
}