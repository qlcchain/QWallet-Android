package com.stratagile.qlink.view;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.utils.UIUtils;

/**
 * Created by huzhipeng on 2018/2/9.
 */

public class TestConnectDialog {
    private Activity activity;
    private View showView;
    public boolean isShow = false;
    private ImageView vpnNameImageView;
    private ImageView vpnConfigurationImageView;
    private ImageView vpnConnectImageView;
    private ImageView vpnBandWidthImageView;

    CustomPopWindow customPopWindow;

    public TestConnectDialog(Activity mContext, View showView) {
        this.activity = mContext;
        this.showView = showView;
    }

    public void show() {
        isShow = true;
        View maskView = LayoutInflater.from(activity).inflate(R.layout.test_connect_vpn_layout, null);
        View contentView = maskView.findViewById(R.id.ll_popup);
        maskView.setAnimation(AnimationUtils.loadAnimation(activity, R.anim.ninjia_fade_in));
        contentView.setAnimation(AnimationUtils.loadAnimation(activity, R.anim.pop_manage_product_in));
        Button btn_left = maskView.findViewById(R.id.btn_left);
        btn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomPopWindow.onBackPressed();
            }
        });
        vpnNameImageView = maskView.findViewById(R.id.iv_vpnName);
        vpnConfigurationImageView = maskView.findViewById(R.id.iv_configuration);
        vpnConnectImageView = maskView.findViewById(R.id.iv_vpn_connect);
        vpnBandWidthImageView = maskView.findViewById(R.id.iv_band_width);
        Animation circle_anim = AnimationUtils.loadAnimation(activity, R.anim.rotate_animation);
        LinearInterpolator interpolator = new LinearInterpolator();  //设置匀速旋转，在xml文件中设置会出现卡顿
        circle_anim.setInterpolator(interpolator);
        if (circle_anim != null) {
            vpnNameImageView.startAnimation(circle_anim);  //开始动画
            vpnConfigurationImageView.startAnimation(circle_anim);
            vpnConnectImageView.startAnimation(circle_anim);
            vpnBandWidthImageView.startAnimation(circle_anim);
        }
        customPopWindow = new CustomPopWindow.PopupWindowBuilder(activity)
                .setView(maskView)
                .setClippingEnable(false)
                .setContenView(contentView)
                .setFocusable(false)
                .setOutsideTouchable(false)
                .size(UIUtils.getDisplayWidth(activity), UIUtils.getDisplayHeigh(activity))
                .create()
                .showAtLocation(showView, Gravity.NO_GRAVITY, 0, 0);
        stopAnimation(0, true);
    }

    public void dismiss() {
        if (isShow && customPopWindow != null) {
            KLog.i("清除dialog。");
            customPopWindow.dismiss();
        }
    }

    public void stopAnimation(int position, boolean isCorrect) {
        switch (position) {
            case 0:
                vpnNameImageView.clearAnimation();
                if (isCorrect) {
                    vpnNameImageView.setImageDrawable(activity.getResources().getDrawable(R.mipmap.icon_correct));
                } else {
                    vpnNameImageView.setImageDrawable(activity.getResources().getDrawable(R.mipmap.icon_error));
                }
                break;
            case 1:
                vpnConfigurationImageView.clearAnimation();
                if (isCorrect) {
                    vpnConfigurationImageView.setImageDrawable(activity.getResources().getDrawable(R.mipmap.icon_correct));
                } else {
                    vpnConfigurationImageView.setImageDrawable(activity.getResources().getDrawable(R.mipmap.icon_error));
                }
                break;
            case 2:
                vpnConnectImageView.clearAnimation();
                if (isCorrect) {
                    vpnConnectImageView.setImageDrawable(activity.getResources().getDrawable(R.mipmap.icon_correct));
                } else {
                    vpnConnectImageView.setImageDrawable(activity.getResources().getDrawable(R.mipmap.icon_error));
                }
                break;
            case 3:
                vpnBandWidthImageView.clearAnimation();
                if (isCorrect) {
                    vpnBandWidthImageView.setImageDrawable(activity.getResources().getDrawable(R.mipmap.icon_correct));
                } else {
                    vpnBandWidthImageView.setImageDrawable(activity.getResources().getDrawable(R.mipmap.icon_error));
                }
                break;
            default:
                break;
        }
    }
}
