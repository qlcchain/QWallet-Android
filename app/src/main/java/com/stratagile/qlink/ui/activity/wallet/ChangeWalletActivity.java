package com.stratagile.qlink.ui.activity.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.stratagile.qlink.Account;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.db.Wallet;
import com.stratagile.qlink.entity.Balance;
import com.stratagile.qlink.ui.activity.wallet.component.DaggerChangeWalletComponent;
import com.stratagile.qlink.ui.activity.wallet.contract.ChangeWalletContract;
import com.stratagile.qlink.ui.activity.wallet.module.ChangeWalletModule;
import com.stratagile.qlink.ui.activity.wallet.presenter.ChangeWalletPresenter;
import com.stratagile.qlink.ui.adapter.wallet.WalletListAdapter;
import com.stratagile.qlink.utils.FileUtil;
import com.stratagile.qlink.utils.SpUtil;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: $description
 * @date 2018/03/05 11:36:39
 */

public class ChangeWalletActivity extends BaseActivity implements ChangeWalletContract.View, WalletListAdapter.OnItemChangeListener {

    @Inject
    ChangeWalletPresenter mPresenter;
    @Inject
    WalletListAdapter walletListAdapter;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.add_wallet)
    Button addWallet;
    @BindView(R.id.select_wallet)
    Button selectWallet;
    @BindView(R.id.tv_neo)
    TextView tvNeo;
    @BindView(R.id.tv_qlc)
    TextView tvQlc;
    @BindView(R.id.tv_gas)
    TextView tvGas;
    private String fromType;//跳转来源

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_change_wallet);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {
        setTitle(R.string.SELECT_WALLET);
        fromType = getIntent().getStringExtra("fromType");
        walletListAdapter.setFromType(fromType);
        List<Wallet> walletList = AppConfig.getInstance().getDaoSession().getWalletDao().loadAll();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        walletListAdapter.setSelectItem(SpUtil.getInt(this, ConstantValue.currentWallet, 0));
        walletListAdapter.setNewData(walletList);
        walletListAdapter.setOnItemChangeListener(this);
        recyclerView.setAdapter(walletListAdapter);
        Map<String, String> map = new HashMap<>();
        map.put("address", walletList.get(SpUtil.getInt(this, ConstantValue.currentWallet, 0)).getAddress());
        mPresenter.getBalance(map);
//        neoutils.Wallet wallet = Account.INSTANCE.getWallet();
//        KLog.i(Account.INSTANCE.byteArray2String(wallet.getPrivateKey()));
//        String privateKey = WalletKtutil.byteArrayToHex(wallet.getPrivateKey());
//        KLog.i(privateKey);
    }

    @Override
    protected void setupActivityComponent() {
        DaggerChangeWalletComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .changeWalletModule(new ChangeWalletModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(ChangeWalletContract.ChangeWalletContractPresenter presenter) {
        mPresenter = (ChangeWalletPresenter) presenter;
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
    public void onGetBalancelSuccess(Balance balance) {
        tvNeo.setText(balance.getData().getNEO() + "");
        BigDecimal b = new BigDecimal(new Double(balance.getData().getQLC()).toString());
        double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        tvQlc.setText(f1 + "");
        tvGas.setText(balance.getData().getGAS() + "");
    }

    @OnClick({R.id.add_wallet, R.id.select_wallet})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.add_wallet:
                Intent intent = new Intent(this, NoWalletActivity.class);
                intent.putExtra("fromType", fromType);
                intent.putExtra("flag", "");
                startActivityForResult(intent, 0);
                break;
            case R.id.select_wallet:
                changeSelectWallet();
                break;
            default:
                break;
        }
    }

    private void changeSelectWallet() {
        SpUtil.putInt(this, ConstantValue.currentWallet, walletListAdapter.getSelectItem());
        FileUtil.savaData("/Qlink/Address/index.txt",walletListAdapter.getSelectItem()+"");
        showProgressDialog();
        List<Wallet> walletList = AppConfig.getInstance().getDaoSession().getWalletDao().loadAll();
        if (walletList != null && walletList.size() != 0) {
            Wallet wallet = walletList.get(SpUtil.getInt(ChangeWalletActivity.this, ConstantValue.currentWallet, 0));
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Account.INSTANCE.fromWIF(wallet.getWif());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            setResult(RESULT_OK);
                            onBackPressed();
                        }
                    });
                }
            }).start();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == RESULT_OK) {
            List<Wallet> walletList = AppConfig.getInstance().getDaoSession().getWalletDao().loadAll();
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            walletListAdapter.setSelectItem(SpUtil.getInt(this, ConstantValue.currentWallet, 0));
            walletListAdapter.setNewData(walletList);
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    public void onItemChange(int position) {
        List<Wallet> walletList = AppConfig.getInstance().getDaoSession().getWalletDao().loadAll();
        Map<String, String> map = new HashMap<>();
        map.put("address", walletList.get(position).getAddress());
        mPresenter.getBalance(map);
    }
}