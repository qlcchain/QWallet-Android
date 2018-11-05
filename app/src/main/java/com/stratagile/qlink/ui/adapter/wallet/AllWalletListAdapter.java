package com.stratagile.qlink.ui.adapter.wallet;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.stratagile.qlink.R;
import com.stratagile.qlink.db.Wallet;
import com.stratagile.qlink.entity.AllWallet;

import java.util.List;


/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: $description  adapter of WalletFragment
 * @date 2018/01/09 15:08:22
 */

public class AllWalletListAdapter extends BaseQuickAdapter<AllWallet, BaseViewHolder> {


    private OnItemChangeListener onItemChangeListener;

    public OnItemChangeListener getOnItemChangeListener() {
        return onItemChangeListener;
    }

    public void setOnItemChangeListener(OnItemChangeListener onItemChangeListener) {
        this.onItemChangeListener = onItemChangeListener;
    }

    public AllWalletListAdapter(@Nullable List<AllWallet> data) {
        super(R.layout.item_all_wallet_list, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, AllWallet item) {
       if (item.getWalletType() == AllWallet.WalletType.EthWallet) {
            baseViewHolder.setText(R.id.tvName, item.getEthWallet().getName());
            baseViewHolder.setImageDrawable(R.id.ivAvatar, mContext.getResources().getDrawable(R.mipmap.icons_eth_wallet));
       } else if (item.getWalletType() == AllWallet.WalletType.NeoWallet) {
           baseViewHolder.setText(R.id.tvName, item.getWalletName());
           baseViewHolder.setImageDrawable(R.id.ivAvatar, mContext.getResources().getDrawable(R.mipmap.icons_neo_wallet));
       }
    }

    public interface OnItemChangeListener {
        void onItemChange(int position);
    }
}