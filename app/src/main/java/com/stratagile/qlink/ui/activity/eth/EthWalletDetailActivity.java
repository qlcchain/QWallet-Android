package com.stratagile.qlink.ui.activity.eth;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.db.EthWallet;
import com.stratagile.qlink.db.Wallet;
import com.stratagile.qlink.entity.AllWallet;
import com.stratagile.qlink.ui.activity.eth.component.DaggerEthWalletDetailComponent;
import com.stratagile.qlink.ui.activity.eth.contract.EthWalletDetailContract;
import com.stratagile.qlink.ui.activity.eth.module.EthWalletDetailModule;
import com.stratagile.qlink.ui.activity.eth.presenter.EthWalletDetailPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.eth
 * @Description: $description
 * @date 2018/10/25 15:02:11
 */

public class EthWalletDetailActivity extends BaseActivity implements EthWalletDetailContract.View {

    @Inject
    EthWalletDetailPresenter mPresenter;
    @BindView(R.id.ivWalletAvatar)
    ImageView ivWalletAvatar;
    @BindView(R.id.tvWalletName)
    TextView tvWalletName;
    @BindView(R.id.tvWalletAddress)
    TextView tvWalletAddress;
    @BindView(R.id.tvWalletAsset)
    TextView tvWalletAsset;
    @BindView(R.id.llAbucoins)
    LinearLayout llAbucoins;
    @BindView(R.id.llExportKeystore)
    LinearLayout llExportKeystore;
    @BindView(R.id.llExportPrivateKey)
    LinearLayout llExportPrivateKey;
    @BindView(R.id.llExportNeoEncryptedKey)
    LinearLayout llExportNeoEncryptedKey;
    @BindView(R.id.llExportNeoPrivateKey)
    LinearLayout llExportNeoPrivateKey;

    private EthWallet ethWallet;

    private Wallet wallet;

    private AllWallet.WalletType walletType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainColor = R.color.white;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_eth_wallet_detail);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (getIntent().getIntExtra("walletType", 0) == AllWallet.WalletType.EthWallet.ordinal()) {
            ethWallet = getIntent().getParcelableExtra("ethwallet");
            walletType = AllWallet.WalletType.EthWallet;

            ivWalletAvatar.setImageDrawable(getResources().getDrawable(R.mipmap.icons_eth_wallet));
            tvWalletName.setText(ethWallet.getName());
            tvWalletAddress.setText(ethWallet.getAddress());

            llExportNeoEncryptedKey.setVisibility(View.GONE);
            llExportNeoPrivateKey.setVisibility(View.GONE);
        } else if (getIntent().getIntExtra("walletType", 0) == AllWallet.WalletType.NeoWallet.ordinal()) {
            walletType = AllWallet.WalletType.NeoWallet;
            wallet = getIntent().getParcelableExtra("neowallet");

            ivWalletAvatar.setImageDrawable(getResources().getDrawable(R.mipmap.icons_neo_wallet));
            tvWalletName.setText(wallet.getAddress());
            tvWalletAddress.setText(wallet.getAddress());

            llAbucoins.setVisibility(View.GONE);
            llExportKeystore.setVisibility(View.GONE);
            llExportPrivateKey.setVisibility(View.GONE);
        }

    }

    @Override
    protected void initData() {
        setTitle("Administration");
    }

    @Override
    protected void setupActivityComponent() {
        DaggerEthWalletDetailComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .ethWalletDetailModule(new EthWalletDetailModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(EthWalletDetailContract.EthWalletDetailContractPresenter presenter) {
        mPresenter = (EthWalletDetailPresenter) presenter;
    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void closeProgressDialog() {
        progressDialog.hide();
    }

    @OnClick({R.id.llAbucoins, R.id.llExportKeystore, R.id.llExportPrivateKey, R.id.llExportNeoEncryptedKey, R.id.llExportNeoPrivateKey})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.llAbucoins:
                break;
            case R.id.llExportKeystore:
                break;
            case R.id.llExportPrivateKey:
                break;
            case R.id.llExportNeoEncryptedKey:
                break;
            case R.id.llExportNeoPrivateKey:
                break;
            default:
                break;
        }
    }
}