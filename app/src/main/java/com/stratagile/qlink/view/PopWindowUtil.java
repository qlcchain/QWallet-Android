package com.stratagile.qlink.view;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.stratagile.qlink.R;
import com.stratagile.qlink.utils.UIUtils;

/**
 * 作者：hu on 2017/6/8
 * 邮箱：365941593@qq.com
 * 描述：
 */

/**
 * 公共的popwindow弹出类。所有的popwindow都可以封装在这个类里边
 */
public class PopWindowUtil {

    /**
     * @param activity  上下文
     * @param showView  从activity中传进来的view,用于让popWindow附着的
     */
    public static void showPopWindow(Activity activity, View showView, View.OnClickListener clickListener, String tipContentStr) {
        showPopWindow(activity, showView, clickListener, tipContentStr, "", "");
    }

    /**
     *
     * @param activity  上下文
     * @param showView  从activity中传进来的view,用于让popWindow附着的
     */
    public static void showPopWindow(Activity activity, View showView, View.OnClickListener clickListener, String tipContentStr, String comfirmContent, String cancalContent) {
        View maskView = LayoutInflater.from(activity).inflate(R.layout.confirm_cancal_layout, null);
        View contentView = maskView.findViewById(R.id.ll_popup);
        maskView.setAnimation(AnimationUtils.loadAnimation(activity, R.anim.ninjia_fade_in));
        contentView.setAnimation(AnimationUtils.loadAnimation(activity, R.anim.pop_manage_product_in));
        //对具体的view的事件的处理
        TextView confirm = (TextView) maskView.findViewById(R.id.bt_confirm);
        TextView cancal = (TextView) maskView.findViewById(R.id.bt_cancal);
        TextView tipContent = (TextView) maskView.findViewById(R.id.tip_content);
        if (!"".equals(comfirmContent)) {
            confirm.setText(comfirmContent);
        }
        if (!"".equals(cancalContent)) {
            cancal.setText(cancalContent);
        }
        tipContent.setText(tipContentStr);
        maskView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomPopWindow.onBackPressed();
            }
        });
        tipContent.setOnClickListener(clickListener);
        cancal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomPopWindow.onBackPressed();
            }
        });

        confirm.setOnClickListener(clickListener);

        new CustomPopWindow.PopupWindowBuilder(activity)
                .setView(maskView)
                .setClippingEnable(false)
                .setContenView(contentView)
                .setFocusable(false)
                .size(UIUtils.getDisplayWidth(activity), UIUtils.getDisplayHeigh(activity))
                .create()
                .showAtLocation(showView, Gravity.NO_GRAVITY, 0, 0);
    }
}
