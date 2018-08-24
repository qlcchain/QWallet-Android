package com.stratagile.qlink.ui.adapter.wallet;

import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.stratagile.qlink.R;
import com.stratagile.qlink.db.RecordSave;
import com.stratagile.qlink.entity.BaseBack;

import java.util.Calendar;
import java.util.List;


/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: $description  adapter of UseHistoryFragment
 * @date 2018/01/19 11:44:00
 */

public class UseHistoryListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public UseHistoryListAdapter(@Nullable List<String> data) {
        super(R.layout.item_use_history_list, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, String item) {
        CardView cardView = baseViewHolder.getView(R.id.cardView);
        baseViewHolder.setText(R.id.tv_name, item);
        switch (baseViewHolder.getLayoutPosition()) {
            case 0:
                cardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.colorWhite));
                baseViewHolder.setImageDrawable(R.id.iv_avater, mContext.getResources().getDrawable(R.mipmap.icon_receive_funds));
                break;
            case 1:
                cardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.colorWhite));
                baseViewHolder.setImageDrawable(R.id.iv_avater, mContext.getResources().getDrawable(R.mipmap.icon_send_funds));
                break;
            case 2:
                cardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.colorWhite));
                baseViewHolder.setImageDrawable(R.id.iv_avater, mContext.getResources().getDrawable(R.mipmap.icon_view_history));
                break;
            case 3:
                cardView.setCardBackgroundColor(mContext.getResources().getColor(R.color.colorWhite));
                baseViewHolder.setImageDrawable(R.id.iv_avater, mContext.getResources().getDrawable(R.mipmap.icon_buy_qlc));
                break;
            default:
                break;
        }
    }
}