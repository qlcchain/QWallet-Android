package com.stratagile.qlink.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.socks.library.KLog;

/**
 * Created by huzhipeng on 2018/3/12.
 */

public class SelectContinentImageView extends android.support.v7.widget.AppCompatImageView {
    public SelectContinentImageView(Context context) {
        super(context);
    }

    public SelectContinentImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            KLog.i(event.getX());
            KLog.i(event.getRawX());
            KLog.i(event.getY());
            KLog.i(event.getRawY());
        }
        return true;
    }
}
