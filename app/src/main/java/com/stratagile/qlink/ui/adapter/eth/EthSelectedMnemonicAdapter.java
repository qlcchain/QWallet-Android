package com.stratagile.qlink.ui.adapter.eth;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.stratagile.qlink.R;

import java.util.List;

public class EthSelectedMnemonicAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public EthSelectedMnemonicAdapter(@Nullable List<String> data) {
        super(R.layout.item_selected_mnemonic, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_content, item);
    }
}
