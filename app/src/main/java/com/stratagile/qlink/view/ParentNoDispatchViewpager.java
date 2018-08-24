package com.stratagile.qlink.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by huzhipeng on 2018/1/26.
 */

public class ParentNoDispatchViewpager extends ViewPager {
    private boolean isScroll;

    public ParentNoDispatchViewpager(Context context) {
        super(context);
    }

    public ParentNoDispatchViewpager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return this.isScroll?super.onInterceptTouchEvent(ev):false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return this.isScroll?super.onTouchEvent(ev):true;
    }

    public void setScroll(boolean scroll) {
        this.isScroll = scroll;
    }
    //事件分发
    //dispatchTouchEvent->onInterceptTouchEvent->onTouchEvent
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //请求所有父控件及祖宗控件不要拦截事件
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }
}
