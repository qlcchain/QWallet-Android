package com.stratagile.qlink.ui.activity.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.entity.AllWallet;
import com.stratagile.qlink.entity.TokenInfo;
import com.stratagile.qlink.entity.TransactionInfo;
import com.stratagile.qlink.ui.activity.wallet.component.DaggerEthTransactionRecordComponent;
import com.stratagile.qlink.ui.activity.wallet.contract.EthTransactionRecordContract;
import com.stratagile.qlink.ui.activity.wallet.module.EthTransactionRecordModule;
import com.stratagile.qlink.ui.activity.wallet.presenter.EthTransactionRecordPresenter;
import com.stratagile.qlink.ui.adapter.wallet.TransacationHistoryAdapter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: $description
 * @date 2018/10/29 16:12:21
 */

public class EthTransactionRecordActivity extends BaseActivity implements EthTransactionRecordContract.View {

    @Inject
    EthTransactionRecordPresenter mPresenter;
    @BindView(R.id.tvTokenValue)
    TextView tvTokenValue;
    @BindView(R.id.tvTokenMoney)
    TextView tvTokenMoney;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.llSend)
    LinearLayout llSend;
    @BindView(R.id.llRecive)
    LinearLayout llRecive;

    private TokenInfo tokenInfo;

    TransacationHistoryAdapter transacationHistoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainColor = R.color.white;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_eth_transaction_record);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {
        HashMap infoMap = new HashMap<String, Object>();
        tokenInfo = getIntent().getParcelableExtra("tokenInfo");
        infoMap.put("address", tokenInfo.getWalletAddress());
        if (tokenInfo.getWalletType() == AllWallet.WalletType.EthWallet) {
            infoMap.put("token", tokenInfo.getTokenAddress());
            mPresenter.getEthWalletTransaction(infoMap, tokenInfo.getWalletAddress());
        } else {
            infoMap.put("page", 15);
            mPresenter.getNeoWalletTransaction(infoMap);
        }
        setTitle(tokenInfo.getTokenSymol());
        String value = tokenInfo.getTokenValue() / (Math.pow(10.0, tokenInfo.getTokenDecimals())) + "";
        tvTokenValue.setText(value);
        BigDecimal b = new BigDecimal(new Double((tokenInfo.getTokenValue() * tokenInfo.getTokenPrice())).toString());
        double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        tvTokenMoney.setText("≈" + ConstantValue.currencyBean.getCurrencyImg() + f1);
        transacationHistoryAdapter = new TransacationHistoryAdapter(new ArrayList<>());
        recyclerView.setAdapter(transacationHistoryAdapter);
    }

    @Override
    protected void setupActivityComponent() {
        DaggerEthTransactionRecordComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .ethTransactionRecordModule(new EthTransactionRecordModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(EthTransactionRecordContract.EthTransactionRecordContractPresenter presenter) {
        mPresenter = (EthTransactionRecordPresenter) presenter;
    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void closeProgressDialog() {
        progressDialog.hide();
    }

    @Override
    public void setEthTransactionHistory(ArrayList<TransactionInfo> transactionInfos) {
        transacationHistoryAdapter.setNewData(transactionInfos);
    }

    @OnClick({R.id.llSend, R.id.llRecive})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.llSend:
                Intent intent1 = new Intent(this, EthTransferActivity.class);
                intent1.putExtra("tokenInfo", tokenInfo);
                startActivity(intent1);
                break;
            case R.id.llRecive:
                Intent intent = new Intent(this, WalletQRCodeActivity.class);
                intent.putExtra("tokenInfo", tokenInfo);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}