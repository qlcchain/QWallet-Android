package com.stratagile.qlink.ui.adapter.settings;


import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.stratagile.qlink.R;
import com.stratagile.qlink.entity.LanguageCountry;

import java.util.List;


public class LanguageCityAdapter extends BaseQuickAdapter<LanguageCountry.ContinentBean.CountryBean, BaseViewHolder> {

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


    public LanguageCityAdapter(@Nullable List<LanguageCountry.ContinentBean.CountryBean> data) {
        super(R.layout.item_wave_country, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, LanguageCountry.ContinentBean.CountryBean item) {
        ImageView iv_country = helper.getView(R.id.iv_country);
        iv_country.setVisibility(View.GONE);

        helper.setText(R.id.tv_country_name, item.getLocalLanguage());

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
