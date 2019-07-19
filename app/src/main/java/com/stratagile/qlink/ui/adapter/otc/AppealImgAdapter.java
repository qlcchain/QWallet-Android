package com.stratagile.qlink.ui.adapter.otc;

import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.data.api.API;
import com.stratagile.qlink.entity.otc.TradeOrderList;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.TimeUtil;

import java.math.BigDecimal;
import java.util.List;

public class AppealImgAdapter extends BaseQuickAdapter<ImageEntity, BaseViewHolder> {

    private int setCount = 0;

    public int getSetCount() {
        return setCount;
    }

    public void setSetCount(int setCount) {
        this.setCount = setCount;
    }

    public AppealImgAdapter(@Nullable List<ImageEntity> data) {
        super(R.layout.appeal_upload_img_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ImageEntity item) {
        helper.addOnClickListener(R.id.ivDelete);
        if (item.isSet()) {
            Glide.with(mContext)
                    .load(Environment.getExternalStorageDirectory() + "/QWallet/otc/" + item.getName())
                    .apply(AppConfig.getInstance().optionsAppeal)
                    .into((ImageView) helper.getView(R.id.imageView));
            helper.setVisible(R.id.ivDelete, true);
            helper.setVisible(R.id.imageView, true);
        } else {
            helper.setGone(R.id.ivDelete, false);
            helper.setGone(R.id.imageView, false);
        }
    }
}
