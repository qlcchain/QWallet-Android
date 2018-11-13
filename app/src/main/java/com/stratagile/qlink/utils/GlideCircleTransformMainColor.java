package com.stratagile.qlink.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;

import java.security.MessageDigest;

/**
 * Created by huzhipeng on 2018/1/3.
 */

public class GlideCircleTransformMainColor extends BitmapTransformation {
    private Context context;
    public GlideCircleTransformMainColor(Context context) {
//        super(context);
        this.context = context;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return circleCrop(pool, toTransform, context);
    }

    private static Bitmap circleCrop(BitmapPool pool, Bitmap source, Context context) {
        if (source == null) {
            return null;
        }
        int size = Math.min(source.getWidth(), source.getHeight());
        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;
        // TODO this could be acquired from the pool too
        Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);
        Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);

        //绘制边框
        Paint mBorderPaint = new Paint();
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setStrokeWidth(UIUtils.dip2px(2, AppConfig.getInstance()));//画笔宽度为4px
        mBorderPaint.setColor(context.getResources().getColor(R.color.mainColor));//边框颜色
        mBorderPaint.setStrokeCap(Paint.Cap.ROUND);
        mBorderPaint.setAntiAlias(true);
        float r = size / 2f;
        float r1 = (size - 2 * 4) / 2f;
        canvas.drawCircle(r, r, r1, paint);
//        canvas.drawCircle(r, r, r1, mBorderPaint);//画边框
        return result;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

    }
}