package com.stratagile.qlink.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by huzhipeng on 2018/1/26.
 */

public class ParentNoDispatchNoScrollViewpager extends ParentNoDispatchViewpager {
    private boolean isScroll;

    public ParentNoDispatchNoScrollViewpager(Context context) {
        super(context);
    }

    public ParentNoDispatchNoScrollViewpager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setCurrentItem(int item) {
        setCurrentItem(item, false);
    }
}
