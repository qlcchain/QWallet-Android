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
                ImageView imageView = helper.getView(R.id.ivTokenAvatar);
                KLog.i(item.getTokenImgName());
                if (item.isMainNetToken()) {
                    imageView.setVisibility(View.VISIBLE);
                    if (item.getTokenImgName() != null && !item.getTokenImgName().equals("")) {
                        Glide.with(mContext)
                                .load(mContext.getResources().getIdentifier(item.getTokenSymol().toLowerCase(), "mipmap", mContext.getPackageName()))
                                .into(imageView);
                    }
                    BigDecimal b = new BigDecimal(new Double((item.getTokenValue() * item.getTokenPrice())).toString());
                    double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    if (f1 == 0) {
                        helper.setText(R.id.tvTokenMoney, "");
                    } else {
                        helper.setText(R.id.tvTokenMoney, ConstantValue.currencyBean.getCurrencyImg() + " " + f1);
                    }
                    helper.setText(R.id.tvTokenMoney, ConstantValue.currencyBean.getCurrencyImg() + " " + f1);
                } else {
                    helper.setText(R.id.tvTokenMoney, "- -");
                    imageView.setVisibility(View.INVISIBLE);
                }
            } else {
                String value = item.getTokenValue() / (Math.pow(10.0, item.getTokenDecimals())) + "";
                helper.setText(R.id.tvTokenValue, value);

                ImageView imageView = helper.getView(R.id.ivTokenAvatar);
                KLog.i(item.getTokenImgName());
                if (item.isMainNetToken()) {
                    imageView.setVisibility(View.VISIBLE);
                    if (item.getTokenImgName() != null && !item.getTokenImgName().equals("")) {
                        Glide.with(mContext)
                                .load(mContext.getResources().getIdentifier(item.getTokenSymol().toLowerCase(), "mipmap", mContext.getPackageName()))
                                .into(imageView);
                    }
                    BigDecimal b = new BigDecimal(Double.parseDouble(value) * item.getTokenPrice());
                    double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    if (f1 == 0) {
                        helper.setText(R.id.tvTokenMoney, "");
                    } else {
                        helper.setText(R.id.tvTokenMoney, ConstantValue.currencyBean.getCurrencyImg() + " " + f1);
                    }
                } else {
                    helper.setText(R.id.tvTokenMoney, "- -");
                    imageView.setVisibility(View.INVISIBLE);
                }
            }
        } else if (item.getWalletType() == AllWallet.WalletType.NeoWallet) {
            helper.setText(R.id.tvTokenValue, BigDecimal.valueOf(item.getTokenValue()).stripTrailingZeros().toPlainString());
            ImageView imageView = helper.getView(R.id.ivTokenAvatar);
            KLog.i(item.getTokenImgName());
            if (item.isMainNetToken()) {
                imageView.setVisibility(View.VISIBLE);
                if (item.getTokenImgName() != null && !item.getTokenImgName().equals("")) {
                    Glide.with(mContext)
                            .load(mContext.getResources().getIdentifier(item.getTokenSymol().toLowerCase(), "mipmap", mContext.getPackageName()))
                            .into(imageView);
                }
                BigDecimal b = new BigDecimal(new Double((item.getTokenValue() * item.getTokenPrice())).toString());
                double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                if (f1 == 0) {
                    helper.setText(R.id.tvTokenMoney, "");
                } else {
                    helper.setText(R.id.tvTokenMoney, ConstantValue.currencyBean.getCurrencyImg() + " " + f1);
                }
            } else {
                helper.setText(R.id.tvTokenMoney, "- -");
                imageView.setVisibility(View.INVISIBLE);
            }
        } else if (item.getWalletType() == AllWallet.WalletType.EosWallet) {
            helper.setText(R.id.tvTokenValue, item.getEosTokenValue());
            ImageView imageView = helper.getView(R.id.ivTokenAvatar);
            if (item.isMainNetToken()) {
                imageView.setVisibility(View.VISIBLE);
                if (item.getTokenImgName() != null && !item.getTokenImgName().equals("")) {
                    Glide.with(mContext)
                            .load(mContext.getResources().getIdentifier(item.getTokenSymol().toLowerCase(), "mipmap", mContext.getPackageName()))
                            .into(imageView);
                }
                BigDecimal b = new BigDecimal(new Double((item.getTokenValue() * item.getTokenPrice())).toString());
                double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                if (f1 == 0) {
                    helper.setText(R.id.tvTokenMoney, "");
                } else {
                    helper.setText(R.id.tvTokenMoney, ConstantValue.currencyBean.getCurrencyImg() + " " + f1);
                }
            } else {
                helper.setText(R.id.tvTokenMoney, "- -");
                imageView.setVisibility(View.INVISIBLE);
            }
        } else if (item.getWalletType() == AllWallet.WalletType.QlcWallet) {
            helper.setText(R.id.tvTokenValue, BigDecimal.valueOf(item.getTokenValue()).divide(BigDecimal.TEN.pow(item.getTokenDecimals()), item.getTokenDecimals(), BigDecimal.ROUND_HALF_DOWN).stripTrailingZeros().toPlainString() + "");
            ImageView imageView = helper.getView(R.id.ivTokenAvatar);
            if (item.isMainNetToken()) {
                imageView.setVisibility(View.VISIBLE);
                if (item.getTokenImgName() != null && !item.getTokenImgName().equals("")) {
                    Glide.with(mContext)
                            .load(mContext.getResources().getIdentifier(item.getTokenSymol().toLowerCase(), "mipmap", mContext.getPackageName()))
                            .into(imageView);
                }
                BigDecimal b = new BigDecimal(new Double((BigDecimal.valueOf(item.getTokenValue()).divide(BigDecimal.TEN.pow(item.getTokenDecimals()), item.getTokenDecimals(), BigDecimal.ROUND_HALF_DOWN).doubleValue() * item.getTokenPrice())).toString());
                double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                if (f1 == 0) {
                    helper.setText(R.id.tvTokenMoney, "");
                } else {
                    helper.setText(R.id.tvTokenMoney, ConstantValue.currencyBean.getCurrencyImg() + " " + f1);
                }
            } else {
                helper.setText(R.id.tvTokenMoney, "- -");
                imageView.setVisibility(View.INVISIBLE);
            }
        }
        helper.setText(R.id.tvTokenName, item.getTokenSymol());
    }
}
