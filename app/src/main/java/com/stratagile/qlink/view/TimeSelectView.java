package com.stratagile.qlink.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.stratagile.qlink.R;

/**
 * Created by huzhipeng on 2018/1/16.
 */

public class TimeSelectView extends LinearLayout {

    private float density;
    private int screenWidth;
    private int screenHeight;
    private View view;

    public TimeSelectView(Context context) {
        super(context);
    }

    public TimeSelectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public TimeSelectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attrs) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        density = dm.density; // 屏幕密度（像素比例：0.75/1.0/1.5/2.0）
        int densityDPI = dm.densityDpi; // 屏幕密度（每寸像素：120/160/240/320）
        screenWidth = dm.widthPixels; // 屏幕宽（像素，如：3200px）
        screenHeight = dm.heightPixels; // 屏幕高（像素，如：1280px）
        view = LayoutInflater.from(getContext()).inflate(R.layout.time_select_view, this, true);
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        setMeasuredDimension((int) (screenWidth - (80 * density)) / 2, 300);
//    }
}
