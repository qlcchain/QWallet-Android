package com.stratagile.qlink.ui.adapter.settings;

import android.support.annotation.Nullable;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.kyleduo.switchbutton.SwitchButton;
import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.entity.SettingBean;

import java.util.List;

public class SettingsAdapter extends BaseQuickAdapter<SettingBean, BaseViewHolder> {

    private boolean isFirst = true;

    private OnCheckChangeListener onCheckChangeListener;

    public OnCheckChangeListener getOnCheckChangeListener() {
        return onCheckChangeListener;
    }

    public void setOnCheckChangeListener(OnCheckChangeListener onCheckChangeListener) {
        this.onCheckChangeListener = onCheckChangeListener;
    }

    public SettingsAdapter(@Nullable List<SettingBean> data) {
        super(R.layout.item_setting, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SettingBean item) {
        helper.setText(R.id.tv_desc, item.getDesc());
        if (item.isCheckMode() == 1) {
            helper.setGone(R.id.iv_next, false);
            helper.setGone(R.id.switchBar, true);
            SwitchButton switchButton = helper.getView(R.id.switchBar);
            switchButton.setChecked(item.isFingerprintUnLock());
            switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (onCheckChangeListener != null) {
                        KLog.i("切换点击...");
                        onCheckChangeListener.onCheck(isChecked, helper.getLayoutPosition(), item.getIcon());
                    }
                    helper.setText(R.id.tv_desc, isChecked ? item.getOnStr() : item.getOffStr());
                    item.setFingerprintUnLock(isChecked);
                }
            });
            helper.setText(R.id.tv_desc, item.isFingerprintUnLock() ? item.getOnStr() : item.getOffStr());
        } else if (item.isCheckMode() == 0) {
            helper.setGone(R.id.iv_next, true);
            helper.setGone(R.id.switchBar, false);
        } else {
            helper.setGone(R.id.iv_next, false);
            helper.setGone(R.id.switchBar, false);
        }
//        if (helper.getLayoutPosition() == 3) {
//            helper.setGone(R.id.tv_desc, false);
//        } else {
//            helper.setGone(R.id.tv_desc, true);
//        }
        if (helper.getLayoutPosition() == 0) {
            helper.setVisible(R.id.view, false);
        } else {
            helper.setVisible(R.id.view, true);
        }
        helper.setText(R.id.tv_name, item.getName());
        ImageView ivicon = helper.getView(R.id.iv_icon);
        Glide.with(mContext)
                .load(mContext.getResources().getIdentifier(item.getIcon(), "mipmap", mContext.getPackageName()))
                .into(ivicon);
    }

    public interface OnCheckChangeListener {
        void onCheck(boolean isCheck, int position, String name);
    }

}
