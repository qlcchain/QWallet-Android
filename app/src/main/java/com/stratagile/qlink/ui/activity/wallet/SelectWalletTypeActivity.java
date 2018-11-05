package com.stratagile.qlink.ui.activity.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.db.EthWallet;
import com.stratagile.qlink.ui.activity.eth.EthWalletActivity;
import com.stratagile.qlink.ui.activity.eth.EthWalletCreatedActivity;
import com.stratagile.qlink.ui.activity.eth.ImportEthWalletActivity;
import com.stratagile.qlink.ui.activity.wallet.component.DaggerSelectWalletTypeComponent;
import com.stratagile.qlink.ui.activity.wallet.contract.SelectWalletTypeContract;
import com.stratagile.qlink.ui.activity.wallet.module.SelectWalletTypeModule;
import com.stratagile.qlink.ui.activity.wallet.presenter.SelectWalletTypePresenter;
import com.stratagile.qlink.utils.eth.ETHWalletUtils;
import com.stratagile.qlink.view.SmoothCheckBox;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: $description
 * @date 2018/10/19 10:51:45
 */

public class SelectWalletTypeActivity extends BaseActivity implements SelectWalletTypeContract.View {

    @Inject
    SelectWalletTypePresenter mPresenter;
    @BindView(R.id.tvChoose)
    TextView tvChoose;
    @BindView(R.id.ivEth)
    ImageView ivEth;
    @BindView(R.id.tvEth)
    TextView tvEth;
    @BindView(R.id.llEth)
    LinearLayout llEth;
    @BindView(R.id.ivEos)
    ImageView ivEos;
    @BindView(R.id.tvEos)
    TextView tvEos;
    @BindView(R.id.llEos)
    LinearLayout llEos;
    @BindView(R.id.ivNeo)
    ImageView ivNeo;
    @BindView(R.id.tvNeo)
    TextView tvNeo;
    @BindView(R.id.llNeo)
    LinearLayout llNeo;
    @BindView(R.id.btCreate)
    Button btCreate;
    @BindView(R.id.btImport)
    Button btImport;
    @BindView(R.id.checkBox)
    SmoothCheckBox checkBox;

    private int currentWalletType = ETH_WALLET_TYPE;
    private static int ETH_WALLET_TYPE = 0;
    private static int NEO_WALLET_TYPE = 1;
    private static int EOS_WALLET_TYPE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_select_wallet_type);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.wallet);
    }

    @Override
    protected void initData() {
        currentWalletType = ETH_WALLET_TYPE;
        ivEth.setImageDrawable(getResources().getDrawable(R.mipmap.eth_wallet_select));
        ivEos.setImageDrawable(getResources().getDrawable(R.mipmap.eos_wallet_unselected));
        ivNeo.setImageDrawable(getResources().getDrawable(R.mipmap.neo_wallet_unselected));
    }

    @Override
    protected void setupActivityComponent() {
        DaggerSelectWalletTypeComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .selectWalletTypeModule(new SelectWalletTypeModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(SelectWalletTypeContract.SelectWalletTypeContractPresenter presenter) {
        mPresenter = (SelectWalletTypePresenter) presenter;
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
    public void createEthWalletSuccess(EthWallet ethWallet) {
        startActivity(new Intent(this, EthWalletCreatedActivity.class).putExtra("wallet", ethWallet));
        finish();
    }

    @OnClick({R.id.llEth, R.id.llEos, R.id.llNeo, R.id.btCreate, R.id.btImport})
    public void onViewClicked(View view) {
        KLog.i(view.getId() + "");
        switch (view.getId()) {
            case R.id.llEth:
                currentWalletType = ETH_WALLET_TYPE;
                ivEth.setImageDrawable(getResources().getDrawable(R.mipmap.eth_wallet_select));
                ivEos.setImageDrawable(getResources().getDrawable(R.mipmap.eos_wallet_unselected));
                ivNeo.setImageDrawable(getResources().getDrawable(R.mipmap.neo_wallet_unselected));
//                List<EthWallet> ethWallets = AppConfig.getInstance().getDaoSession().getEthWalletDao().loadAll();
//                for (int i = 0; i < ethWallets.size(); i++) {
//                    if (!ethWallets.get(i).getIsBackup() && ethWallets.get(i).getMnemonic() != null) {
//                        startActivity(new Intent(this, EthWalletCreatedActivity.class).putExtra("wallet", ethWallets.get(i)));
//                        finish();
//                        return;
//                    }
//                }
                break;
            case R.id.llEos:
                currentWalletType = EOS_WALLET_TYPE;
                ivEth.setImageDrawable(getResources().getDrawable(R.mipmap.eth_wallet_unselected));
                ivEos.setImageDrawable(getResources().getDrawable(R.mipmap.eos_wallet_select));
                ivNeo.setImageDrawable(getResources().getDrawable(R.mipmap.neo_wallet_unselected));
                break;
            case R.id.llNeo:
                currentWalletType = NEO_WALLET_TYPE;
                ivEth.setImageDrawable(getResources().getDrawable(R.mipmap.eth_wallet_unselected));
                ivEos.setImageDrawable(getResources().getDrawable(R.mipmap.eos_wallet_unselected));
                ivNeo.setImageDrawable(getResources().getDrawable(R.mipmap.neo_wallet_select));
                break;
            case R.id.btCreate:
                if (currentWalletType == ETH_WALLET_TYPE) {
                    mPresenter.createEthWallet();
                }
                break;
            case R.id.btImport:
                startActivity(new Intent(this, ImportEthWalletActivity.class));
                break;
            default:
                break;
        }
    }

}