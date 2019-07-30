package com.stratagile.qlink.view.sidebar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.socks.library.KLog;
import com.stratagile.qlink.utils.UIUtils;

public class SideBar extends View {

    private int mHeight;
    private int mWidth;
    private Paint mPaint;
    private int singleHeight;
    private Context mContext;
    private indexChangeListener listener;
    private final int TOTAL_MARGIN = 200;
    private final int TOP_MARGIN = 100;

    public SideBar(Context context) {
        this(context, null);
    }

    public SideBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SideBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(35);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //导航栏居中显示，上下各有80dp的边距
        KLog.i("高度为：" + h);
        mHeight = (int) (h - UIUtils.dip2px(TOTAL_MARGIN, mContext));
        mWidth = w;
        singleHeight = mHeight / indexStr.length();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < indexStr.length(); i++) {
            String textTag = indexStr.substring(i, i + 1);
            float xPos = (mWidth - mPaint.measureText(textTag)) / 2;
            canvas.drawText(textTag, xPos, singleHeight * (i + 1) + UIUtils.dip2px(TOP_MARGIN, mContext), mPaint);
        }
    }

    private String indexStr = "ABCDEFGHIJKLMNOPQRSTUVWXY#";

    public void setIndexStr(String indexStr) {
        this.indexStr = indexStr;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //按下
                mPaint.setColor(Color.BLACK);
                invalidate();
            case MotionEvent.ACTION_MOVE:
                //滑动 event.getY()得到在父View中的Y坐标，通过和总高度的比例再乘以字符个数总长度得到按下的位置
                int position = (int) ((event.getY() - getTop() - UIUtils.dip2px(80, mContext)) / mHeight * indexStr.toCharArray().length);
                if (position >= 0 && position < indexStr.length()) {
                    ((IndexBar) getParent()).setDrawData(event.getY(), String.valueOf(indexStr.toCharArray()[position]), position);
                    if (listener != null) {
                        listener.indexChanged(indexStr.substring(position, position + 1));
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                //抬起
                ((IndexBar) getParent()).setTagStatus(false);
                mPaint.setColor(Color.WHITE);
                invalidate();
                break;
            default:
                break;
        }
        return true;
    }


    public interface indexChangeListener {
        void indexChanged(String tag);
    }

    public void setIndexChangeListener(indexChangeListener listener) {
        this.listener = listener;
    }

}