package com.stratagile.qlink.ui.activity.eth;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.db.Wallet;
import com.stratagile.qlink.ui.activity.eth.component.DaggerBnbToQlcComponent;
import com.stratagile.qlink.ui.activity.eth.contract.BnbToQlcContract;
import com.stratagile.qlink.ui.activity.eth.module.BnbToQlcModule;
import com.stratagile.qlink.ui.activity.eth.presenter.BnbToQlcPresenter;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.ToastUtil;

import java.util.HashMap;
import java.util.UUID;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.eth
 * @Description: $description
 * @date 2018/05/24 10:46:37
 * bnb转为qlc的页面
 */

public class BnbToQlcActivity extends BaseActivity implements BnbToQlcContract.View {

    @Inject
    BnbToQlcPresenter mPresenter;
    @BindView(R.id.tv_transaction)
    TextView tvTransaction;
    @BindView(R.id.et_amount)
    EditText etAmount;
    @BindView(R.id.et_wallet_password)
    EditText etWalletPassword;
    private Wallet wallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_bnb_to_qlc);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {
        wallet = AppConfig.getInstance().getDaoSession().getWalletDao().loadAll().get(SpUtil.getInt(this, ConstantValue.currentWallet, 0));

    }

    @Override
    protected void setupActivityComponent() {
        DaggerBnbToQlcComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .bnbToQlcModule(new BnbToQlcModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(BnbToQlcContract.BnbToQlcContractPresenter presenter) {
        mPresenter = (BnbToQlcPresenter) presenter;
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
    public void startTransaction(String transactionHash) {
        String uuid = UUID.randomUUID().toString().replace("-", "");
        uuid = uuid.substring(0, 32);
        HashMap<String, String> infoMap = new HashMap<>();
        infoMap.put("exchangeId", uuid);
        infoMap.put("ethAddress", getIntent().getStringExtra("walletAddress"));
        infoMap.put("neoAddress", wallet.getAddress());
        infoMap.put("bnb", etAmount.getText().toString());
        infoMap.put("tx", transactionHash);
        mPresenter.startBnb2Qlc(infoMap);
    }

    @Override
    public void transactionSuccess(String recordId) {
        closeProgressDialog();
        ToastUtil.displayShortToast(getString(R.string.Your_withdrawal_will_arrive_soon));
        finish();
    }

    @OnClick(R.id.tv_transaction)
    public void onViewClicked() {
        String fromAddress = getIntent().getStringExtra("walletAddress");
        mPresenter.parseWallet(etWalletPassword.getText().toString(), fromAddress, etAmount.getText().toString());
    }
}