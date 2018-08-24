package com.stratagile.qlink.view;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by huzhipeng on 2018/2/17.
 */

/**
 * 此Layout是为解决 当设定状态栏为透明后，android:windowSoftInputMode="adjustResize"
 * 失效，导致输入法覆盖View的Bug
 * zhaoxuan.li
 * 2015/10/21.
 */
public class FitLinearLayout extends LinearLayout {

    private int[] mInsets = new int[4];

    public FitLinearLayout(Context context) {
        super(context);
    }

    public FitLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FitLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public final int[] getInsets() {
        return mInsets;
    }

    /**
     * 此方法以过期，当应用最低API支持为20后，可以重写以下方法
     * @Override
    public final WindowInsets onApplyWindowInsets(WindowInsets insets) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
    mInsets[0] = insets.getSystemWindowInsetLeft();
    mInsets[1] = insets.getSystemWindowInsetTop();
    mInsets[2] = insets.getSystemWindowInsetRight();
    return super.onApplyWindowInsets(insets.replaceSystemWindowInsets(0, 0, 0,
    insets.getSystemWindowInsetBottom()));
    } else {
    return insets;
    }
    }
     *  未测试……
     */
    @Override
    protected final boolean fitSystemWindows(Rect insets) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            mInsets[0] = insets.left;
            mInsets[1] = 0;
            mInsets[2] = insets.right;
            mInsets[3] = 0;
            insets.set(insets.left, 0, insets.right, insets.bottom);
            return super.fitSystemWindows(insets);
        } else {
            return super.fitSystemWindows(insets);
        }
    }
}