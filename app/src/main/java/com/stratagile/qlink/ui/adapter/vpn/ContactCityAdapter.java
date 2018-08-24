package com.stratagile.qlink.ui.adapter.vpn;


import android.media.Image;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.stratagile.qlink.R;
import com.stratagile.qlink.entity.ContinentAndCountry;
import com.stratagile.qlink.ui.adapter.wallet.WalletListAdapter;

import java.util.List;


public class ContactCityAdapter extends BaseQuickAdapter<ContinentAndCountry.ContinentBean.CountryBean, BaseViewHolder> {

    private int selectItem = 0;

    public int getSelectItem() {
        return selectItem;
    }

    public void setSelectItem(int selectItem) {
        this.selectItem = selectItem;
    }

    private OnItemChangeListener onItemChangeListener;

    public OnItemChangeListener getOnItemChangeListener() {
        return onItemChangeListener;
    }

    public void setOnItemChangeListener(OnItemChangeListener onItemChangeListener) {
        this.onItemChangeListener = onItemChangeListener;
    }


    public ContactCityAdapter(@Nullable List<ContinentAndCountry.ContinentBean.CountryBean> data) {
        super(R.layout.item_wave_country, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ContinentAndCountry.ContinentBean.CountryBean item) {
        ImageView iv_country = helper.getView(R.id.iv_country);
        if("".equals(item.getCountryImage()))
        {
            iv_country.setVisibility(View.GONE);
        }else{
            iv_country.setVisibility(View.VISIBLE);
            Glide.with(mContext)
                    .load(mContext.getResources().getIdentifier(item.getCountryImage(), "mipmap", mContext.getPackageName()))
                    .into(iv_country);
        }

        helper.setText(R.id.tv_country_name, item.getName());

        CheckBox checkBox = helper.getView(R.id.checkbox);

        if (helper.getLayoutPosition() == selectItem) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectItem = helper.getLayoutPosition();
                notifyDataSetChanged();
                if (onItemChangeListener != null) {
                    onItemChangeListener.onItemChange(selectItem);
                }
            }
        });
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (helper.getLayoutPosition() == selectItem) {

                } else {
                    selectItem = helper.getLayoutPosition();
                    notifyDataSetChanged();
                    if (onItemChangeListener != null) {
                        onItemChangeListener.onItemChange(selectItem);
                    }
                }
            }
        });
    }

    public interface OnItemChangeListener {
        void onItemChange(int position);
    }
}
