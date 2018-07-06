package com.stratagile.qlink.ui.adapter.eth;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.stratagile.qlink.R;
import com.stratagile.qlink.interfaces.StorableWallet;

import java.util.List;

public class EthWalletsAdapter extends BaseQuickAdapter<StorableWallet, BaseViewHolder> {

    public EthWalletsAdapter(@Nullable List<StorableWallet> data) {
        super(R.layout.item_eth_wallet, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, StorableWallet item) {
        helper.setText(R.id.tv_address, item.getPubKey());
    }
}
