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
import com.stratagile.qlink.R;
import com.stratagile.qlink.utils.UIUtils;

public class ProductView extends View {

    private int mHeight;
    private int mWidth;
    private Paint mPaint;
    private Paint whitePaint;
    private Context mContext;
    private int circleR = 0;
    private int whiteCircleR = 0;
    private int lineHeight;

    public ProductView(Context context) {
        this(context, null);
    }

    public ProductView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProductView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPaint.setColor(mContext.getResources().getColor(R.color.mainColor));


        whitePaint = new Paint();
        whitePaint.setDither(true);
        whitePaint.setAntiAlias(true);
        whitePaint.setColor(mContext.getResources().getColor(R.color.white));


        circleR = (int) mContext.getResources().getDimension(R.dimen.x10);
        whiteCircleR = (int) mContext.getResources().getDimension(R.dimen.x6);
        lineHeight = (int) mContext.getResources().getDimension(R.dimen.x4);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = h;
        mWidth = w;
    }


    @Override
    protected void onDraw(Canvas canvas) {

        //float startX, float startY, float stopX, float stopY,
        mPaint.setStrokeWidth(lineHeight);
        canvas.drawLine(circleR, circleR, mWidth - circleR, circleR, mPaint);

        //绘制第一个圆环
        /**
         * Draw the specified circle using the specified paint. If radius is <= 0, then nothing will be
         * drawn. The circle will be filled or framed based on the Style in the paint.
         *
         * @param cx The x-coordinate of the center of the cirle to be drawn
         * @param cy The y-coordinate of the center of the cirle to be drawn
         * @param radius The radius of the cirle to be drawn
         * @param paint The paint used to draw the circle
         */
        canvas.drawCircle(circleR, circleR, circleR, mPaint);
        canvas.drawCircle(circleR, circleR, whiteCircleR, whitePaint);

        //绘制第二个圆环
        canvas.drawCircle(mWidth / 2, circleR, circleR, mPaint);
        canvas.drawCircle(mWidth / 2, circleR, whiteCircleR, whitePaint);

        //绘制第三个圆环
        canvas.drawCircle(mWidth - circleR, circleR, circleR, mPaint);
        canvas.drawCircle(mWidth - circleR, circleR, whiteCircleR, whitePaint);
    }
}