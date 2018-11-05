package com.stratagile.qlink.view;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.stratagile.qlink.R;
import com.stratagile.qlink.entity.AllWallet;
import com.stratagile.qlink.entity.ContinentAndCountry;

import java.util.List;

/**
 * Created by huzhipeng on 2018/2/3.
 */

public class SelectWalletAdapter extends BaseQuickAdapter<AllWallet, BaseViewHolder> {

    private int currentSelectItem = -1;

    public int getCurrentSelectItem() {
        return currentSelectItem;
    }

    public void setCurrentSelectItem(int currentSelectItem) {
        this.currentSelectItem = currentSelectItem;
    }

    private OnItemChangeListener onItemChangeListener;

    public OnItemChangeListener getOnItemChangeListener() {
        return onItemChangeListener;
    }

    public void setOnItemChangeListener(OnItemChangeListener onItemChangeListener) {
        this.onItemChangeListener = onItemChangeListener;
    }

    public SelectWalletAdapter(@Nullable List<AllWallet> data) {
        super(R.layout.select_all_wallet_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AllWallet item) {
        ImageView iv_country = helper.getView(R.id.iv_country);
//        Glide.with(mContext)
//                .load(mContext.getResources().getIdentifier(item.getCountryImage(), "mipmap", mContext.getPackageName()))
//                .into(iv_country);
        helper.setText(R.id.tv_country_name, item.getWalletName());
        if (currentSelectItem == helper.getLayoutPosition()) {
            helper.setTextColor(R.id.tv_country_name, mContext.getResources().getColor(R.color.main_color));
            helper.setChecked(R.id.checkbox, true);
            if (onItemChangeListener != null) {
                onItemChangeListener.onItemSelect(item);
            }
        } else {
            helper.setTextColor(R.id.tv_country_name, mContext.getResources().getColor(R.color.color_333));
            helper.setChecked(R.id.checkbox, false);
        }

        if (item.getWalletType() == AllWallet.WalletType.EthWallet) {
            helper.setImageDrawable(R.id.iv_country, mContext.getResources().getDrawable(R.mipmap.icons_eth_wallet));
        } else if (item.getWalletType() == AllWallet.WalletType.NeoWallet) {
            helper.setImageDrawable(R.id.iv_country, mContext.getResources().getDrawable(R.mipmap.icons_neo_wallet));
        }
        CheckBox checkBox = helper.getView(R.id.checkbox);

        if (helper.getLayoutPosition() == currentSelectItem) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSelectItem = helper.getLayoutPosition();
                notifyDataSetChanged();
                if (onItemChangeListener != null) {
                    onItemChangeListener.onItemChange(currentSelectItem);
                }
            }
        });
    }

    public interface OnItemChangeListener {
        void onItemChange(int position);
        void onItemSelect(AllWallet item);
    }
}
