package com.stratagile.qlink.view;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;

public class DefiPageTransformer implements ViewPager.PageTransformer {
    //透明度和高度最小值
    private static final float MIN_SCALE = 0.70f;
    private static final float MIN_ALPHA = 0.5F;
    @Override
    public void transformPage(@NonNull View page, float position) {
        int width = page.getWidth();
        int offset = 20 / width;  // 20为PageMargin属性值

        if(position < -1 - offset){
            page.setAlpha(MIN_ALPHA);
            page.setScaleY(MIN_SCALE);
            page.setScaleX(MIN_SCALE);
        }else if(position <= 1 + offset){//在[-1-offset,1+offset]范围
            if(position == 0){ //当前页面
                page.setAlpha(1.0f);
                page.setScaleY(1.0f);
                page.setScaleX(1.0f);
            }else{
                if(position < 0){ //在[-1-offset,0]范围
                    //平滑变化
                    float f = MIN_ALPHA + (1 - MIN_ALPHA) * (1 + position + offset);
                    page.setAlpha(f);
                    float s = MIN_SCALE +(1 - MIN_SCALE) * (1+position + offset);
                    page.setScaleY(s);
                    page.setScaleX(s);
                }else{ //在[0，1+offset]范围
                    //平滑变化
                    float f = MIN_ALPHA + (1 - MIN_ALPHA) * (1 - position + offset);
                    page.setAlpha(f);
                    float s = MIN_SCALE +(1 - MIN_SCALE) * (1 - position + offset);
                    page.setScaleY(s);
                    page.setScaleX(s);
                }

            }
        }
    }
}