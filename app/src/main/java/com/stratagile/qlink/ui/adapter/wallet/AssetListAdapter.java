package com.stratagile.qlink.ui.adapter.wallet;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.entity.MyAsset;
import com.vondear.rxtools.RxDataTool;

import java.util.List;







/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: $description  adapter of AssetFragment
 * @date 2018/01/18 20:42:28
 */

public class AssetListAdapter extends BaseQuickAdapter<MyAsset, BaseViewHolder> {

    public AssetListAdapter(@Nullable List<MyAsset> data) {
       super(R.layout.item_asset_list, data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, MyAsset item) {
        TextView tvDesc = baseViewHolder.getView(R.id.tv_desc);
        Drawable drawable;
        if (item.getType() == 0) {
        } else if (item.getType() == 1){
            baseViewHolder.addOnClickListener(R.id.setWifi);
            baseViewHolder.setImageDrawable(R.id.iv_asset, mContext.getResources().getDrawable(R.mipmap.icon_connected_vpn));
            String connectStr =  mContext.getResources().getString(R.string.state_connected) +" : " + item.getVpnEntity().getCurrentConnect() + "/" + item.getVpnEntity().getConnectMaxnumber();
            SpannableString contentSB = new SpannableString(connectStr);
            baseViewHolder.setText(R.id.tv_earn, "+" + (RxDataTool.format2Decimals(item.getVpnEntity().getAssetTranfer() - item.getVpnEntity().getRegisterQlc() + "") + ""));
            if(contentSB.length() >=11)
            {
                contentSB.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.color_333)), 11, contentSB.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            baseViewHolder.setText(R.id.allow, contentSB);
            baseViewHolder.setText(R.id.tv_qlc, item.getVpnEntity().getAssetTranfer() + "");
            baseViewHolder.setText(R.id.ssid, item.getVpnEntity().getVpnName());
            baseViewHolder.setText(R.id.tv_earn, "+" + (RxDataTool.format2Decimals(item.getVpnEntity().getAssetTranfer() - item.getVpnEntity().getRegisterQlc() + "")) + "");
        }
        drawable = mContext.getResources().getDrawable(R.mipmap.bg_icon_rise);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tvDesc.setCompoundDrawables(drawable, null, null, null);
        tvDesc.setCompoundDrawablePadding(10);
        tvDesc.setText("WINQ GAS");
    }
}