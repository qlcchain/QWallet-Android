package com.stratagile.qlink.view;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.IdRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.stratagile.qlink.R;


public class ActiveTogglePopWindow extends PopupWindow {
    private OnItemClickListener mOnItemClickListener;
    @SuppressLint("InflateParams")
    public ActiveTogglePopWindow(final Activity context) {
        LayoutInflater inflater = (LayoutInflater) context
                                  .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View content = inflater.inflate(R.layout.popupwindow_active, null);

        // 设置SelectPicPopupWindow的View
        this.setContentView(content);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);

        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimationPreview);


        LinearLayout rlAll = (LinearLayout) content.findViewById(R.id.rl_detail);
        LinearLayout rlGain = (LinearLayout) content.findViewById(R.id.rl_rank);
        rlAll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ActiveTogglePopWindow.this.dismiss();
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v.getId());
                }
            }
        });
        rlGain.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                ActiveTogglePopWindow.this.dismiss();
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v.getId());
                }
            }

        });
    }

    /**
     * 显示popupWindow
     *
     * @param parent
     */
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            this.showAsDropDown(parent, -270, 50);
        } else {
            this.dismiss();
        }
    }

    public interface OnItemClickListener {
        void onItemClick(@IdRes int id);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
}
