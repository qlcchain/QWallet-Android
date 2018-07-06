package com.vondear.rxtools.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;

import com.vondear.rxtools.RxLocationTool;


/**
 * Created by vondear on 2016/7/19.
 * Mainly used for confirmation and cancel.
 */
public class RxDialogGPSCheck extends RxDialogSureCancel {
    
    public RxDialogGPSCheck(Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    public RxDialogGPSCheck(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    public RxDialogGPSCheck(Context context) {
        super(context);
        initView();
    }

    public RxDialogGPSCheck(Activity context) {
        super(context);
        initView();
    }

    public RxDialogGPSCheck(Context context, float alpha, int gravity) {
        super(context, alpha, gravity);
        initView();
    }

    private void initView() {
        getTitleView().setBackgroundDrawable(null);
        setTitle("GPS unopened");
        getTitleView().setTextSize(16f);
        getTitleView().setTextColor(Color.BLACK);
        setContent("You need to open GPS in system settings to collect data.");
        getSureView().setText("setting");
        getCancelView().setText("I know");

        getSureView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxLocationTool.openGpsSettings(mContext);
                cancel();
            }
        });

        getCancelView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
        setCanceledOnTouchOutside(false);
        setCancelable(false);
    }

}
