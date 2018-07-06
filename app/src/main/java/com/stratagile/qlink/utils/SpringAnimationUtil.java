package com.stratagile.qlink.utils;

import android.support.animation.DynamicAnimation;
import android.support.animation.SpringAnimation;
import android.support.animation.SpringForce;
import android.view.View;

/**
 * Created by huzhipeng on 2018/3/9.
 */

public class SpringAnimationUtil {
    /**
     * 缩放动画, 进入
     * @param springView
     */
    public static void startScaleSpringViewAnimation(View springView) {
//        .setDampingRatio(SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY)
//                .setStiffness(SpringForce.STIFFNESS_LOW);
        SpringForce spring = new SpringForce(1f)
                .setDampingRatio(0.6f)
                .setStiffness(200f);
        new SpringAnimation(springView, DynamicAnimation.SCALE_X)
                .setStartValue(0f)
                .setSpring(spring)
                .setMaxValue(1.1f)
                .setMinValue(0f)
                .start();
        new SpringAnimation(springView, DynamicAnimation.SCALE_Y)
                .setStartValue(0f)
                .setSpring(spring)
                .setMaxValue(1.1f)
                .setMinValue(0f)
                .start();
    }

    /**
     * 缩放动画，退出
     * @param springView
     */
    public static void endScaleSpringViewAnimation(View springView, DynamicAnimation.OnAnimationEndListener onAnimationEndListener) {
//        .setDampingRatio(SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY)
//                .setStiffness(SpringForce.STIFFNESS_LOW);
        SpringForce spring = new SpringForce(0f)
                .setDampingRatio(0.6f)
                .setStiffness(200f);
        new SpringAnimation(springView, DynamicAnimation.SCALE_X)
                .setStartValue(1.0f)
                .setSpring(spring)
                .setMaxValue(1.1f)
                .setMinValue(0f)
                .start();
        new SpringAnimation(springView, DynamicAnimation.SCALE_Y)
                .setStartValue(1.0f)
                .setSpring(spring)
                .setMaxValue(1.1f)
                .setMinValue(0f)
                .addEndListener(onAnimationEndListener)
                .start();
    }
}
