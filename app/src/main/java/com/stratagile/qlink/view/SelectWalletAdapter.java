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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huzhipeng on 2018/2/3.
 */

public class SelectWalletAdapter extends BaseQuickAdapter<AllWallet, BaseViewHolder> {

    public SelectWalletAdapter(@Nullable ArrayList<AllWallet> data) {
        super(R.layout.select_all_wallet_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AllWallet item) {
        helper.setText(R.id.tv_country_name, item.getWalletName());
        helper.setText(R.id.tvWalletAddess, item.getWalletAddress());
//        if (item.getWalletType() == AllWallet.WalletType.EthWallet && item.getEthWallet().getIsCurrent()) {
//            helper.setTextColor(R.id.tv_country_name, mContext.getResources().getColor(R.color.main_color));
//            helper.setChecked(R.id.checkbox, true);
//        } else if (item.getWalletType() == AllWallet.WalletType.NeoWallet && item.getWallet().getIsCurrent()){
//            helper.setTextColor(R.id.tv_country_name, mContext.getResources().getColor(R.color.main_color));
//            helper.setChecked(R.id.checkbox, true);
//        } else {
//            helper.setTextColor(R.id.tv_country_name, mContext.getResources().getColor(R.color.color_333));
//            helper.setChecked(R.id.checkbox, false);
//        }

        if (item.getWalletType() == AllWallet.WalletType.EthWallet) {
            helper.setVisible(R.id.ivCheck, item.getEthWallet().getIsCurrent());
            helper.setImageDrawable(R.id.iv_country, mContext.getResources().getDrawable(R.mipmap.icons_eth_wallet));
        } else if (item.getWalletType() == AllWallet.WalletType.NeoWallet) {
            helper.setVisible(R.id.ivCheck, item.getWallet().getIsCurrent());
            helper.setImageDrawable(R.id.iv_country, mContext.getResources().getDrawable(R.mipmap.icons_neo_wallet));
        }
    }
}
