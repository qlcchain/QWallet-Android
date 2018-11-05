package com.stratagile.qlink.ui.adapter.wallet;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.entity.AllWallet;
import com.stratagile.qlink.entity.TokenInfo;

import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.List;

import static com.stratagile.qlink.utils.BalanceUtils.weiToEth;

public class TokensAdapter extends BaseQuickAdapter<TokenInfo, BaseViewHolder> {

    public TokensAdapter(@Nullable List<TokenInfo> data) {
        super(R.layout.item_token_info, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TokenInfo item) {
        if (item.getWalletType() == AllWallet.WalletType.EthWallet) {
            if (item.getTokenSymol().equals("ETH")) {
                helper.setText(R.id.tvTokenValue, weiToEth(BigDecimal.valueOf(item.getTokenValue()).multiply(BigDecimal.TEN.pow(18)).toBigInteger(), 5));
            } else {
                BigDecimal decimalDivisor = new BigDecimal(Math.pow(10, item.getTokenDecimals()));
                BigDecimal ethBalance = item.getTokenDecimals() > 0 ? new BigDecimal(item.getTokenValue()).divide(decimalDivisor) : new BigDecimal(item.getTokenValue());
                String value = ethBalance.compareTo(BigDecimal.ZERO) == 0 ? "0" : ethBalance.toPlainString();
                helper.setText(R.id.tvTokenValue, value);
            }
        } else if (item.getWalletType() == AllWallet.WalletType.NeoWallet) {
            helper.setText(R.id.tvTokenValue, item.getTokenValue() + "");
        }
        helper.setText(R.id.tvTokenName, item.getTokenSymol());
        ImageView imageView = helper.getView(R.id.ivTokenAvatar);
        KLog.i(item.getTokenImgName());
        if (item.isMainNetToken()) {
            imageView.setVisibility(View.VISIBLE);
            if (item.getTokenImgName() != null && !item.getTokenImgName().equals("")) {
                Glide.with(mContext)
                        .load(mContext.getResources().getIdentifier(item.getTokenImgName(), "mipmap", mContext.getPackageName()))
                        .into(imageView);
            }
            BigDecimal b = new BigDecimal(new Double((item.getTokenValue() * item.getTokenPrice())).toString());
            double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            helper.setText(R.id.tvTokenMoney, ConstantValue.currencyBean.getCurrencyImg() + " " + f1);
        } else {
            helper.setText(R.id.tvTokenMoney, "- -");
            imageView.setVisibility(View.INVISIBLE);
        }
    }
}
