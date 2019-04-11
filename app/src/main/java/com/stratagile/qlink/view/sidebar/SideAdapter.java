package com.stratagile.qlink.view.sidebar;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.stratagile.qlink.R;

import java.util.List;

public class SideAdapter extends BaseQuickAdapter<SideBean, BaseViewHolder> {
    public SideAdapter(@Nullable List<SideBean> data) {
        super(R.layout.item_select_country, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SideBean item) {
        helper.setText(R.id.phone, "+" + item.getNumber());
        helper.setText(R.id.countryName, item.getName());
    }
}
