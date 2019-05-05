package com.stratagile.qlink.ui.adapter.finance;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.data.api.API;
import com.stratagile.qlink.db.Wallet;
import com.stratagile.qlink.entity.finance.EarnRank;
import com.stratagile.qlink.entity.finance.HistoryRecord;
import com.stratagile.qlink.utils.LanguageUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class HistoryRecordAdapter extends BaseQuickAdapter<HistoryRecord.TransactionListBean, BaseViewHolder> {
    HashMap<String, String> walletMap;
    public HistoryRecordAdapter(@Nullable List<HistoryRecord.TransactionListBean> data) {
        super(R.layout.item_history_record_list, data);
        walletMap = new HashMap<>();
        List<Wallet> wallets = AppConfig.getInstance().getDaoSession().getWalletDao().loadAll();
        for (Wallet wallet : wallets) {
            walletMap.put(wallet.getAddress(), wallet.getName());
        }
    }

    @Override
    protected void convert(BaseViewHolder helper, HistoryRecord.TransactionListBean item) {
        if (LanguageUtil.isCN(mContext)) {
            helper.setText(R.id.tvProductName, item.getProductName());
            switch (item.getType()) {
                case "BUY":
                    helper.setText(R.id.tvOpreate, mContext.getResources().getString(R.string.buy));
                    break;
                case "REDEEM":
                    helper.setText(R.id.tvOpreate, mContext.getResources().getString(R.string.redeem));
                    break;
                default:
                    helper.setText(R.id.tvOpreate, item.getType());
                    break;
            }
        } else {
            helper.setText(R.id.tvProductName, item.getProductNameEn());
            helper.setText(R.id.tvOpreate, item.getType());
        }
        helper.setText(R.id.tvOpreateTime, item.getCreateTime());
        switch (item.getType()) {
            case "BUY":
                helper.setTextColor(R.id.tvOpreate, mContext.getResources().getColor(R.color.color_01b5ab));
                helper.setText(R.id.tvQlcCount, "+" + item.getAmount() + " QLC");
                helper.setText(R.id.tvWalletAddress, "");
                break;
            case "REDEEM":
                helper.setTextColor(R.id.tvOpreate, mContext.getResources().getColor(R.color.color_ff3669));
                helper.setText(R.id.tvQlcCount, "-" + item.getAmount() + " QLC");
                if (walletMap.containsKey(item.getAddress())) {
                    helper.setText(R.id.tvWalletAddress, walletMap.get(item.getAddress()));
                } else {
                    helper.setText(R.id.tvWalletAddress, item.getAddress());
                }
                break;
            default:
                break;
        }
    }
}
