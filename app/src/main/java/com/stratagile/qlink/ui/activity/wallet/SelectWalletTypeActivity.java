package com.stratagile.qlink.ui.activity.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;
import com.stratagile.qlc.QLCAPI;
import com.stratagile.qlc.utils.QlcUtil;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.db.EosAccount;
import com.stratagile.qlink.db.EthWallet;
import com.stratagile.qlink.db.QLCAccount;
import com.stratagile.qlink.db.Wallet;
import com.stratagile.qlink.entity.AllWallet;
import com.stratagile.qlink.entity.eventbus.ChangeWallet;
import com.stratagile.qlink.ui.activity.eos.EosCreateActivity;
import com.stratagile.qlink.ui.activity.eos.EosImportActivity;
import com.stratagile.qlink.ui.activity.eth.EthWalletCreatedActivity;
import com.stratagile.qlink.ui.activity.eth.ImportEthWalletActivity;
import com.stratagile.qlink.ui.activity.main.WebViewActivity;
import com.stratagile.qlink.ui.activity.neo.WalletCreatedActivity;
import com.stratagile.qlink.ui.activity.qlc.ImportQlcActivity;
import com.stratagile.qlink.ui.activity.qlc.QlcWalletCreatedActivity;
import com.stratagile.qlink.ui.activity.wallet.component.DaggerSelectWalletTypeComponent;
import com.stratagile.qlink.ui.activity.wallet.contract.SelectWalletTypeContract;
import com.stratagile.qlink.ui.activity.wallet.module.SelectWalletTypeModule;
import com.stratagile.qlink.ui.activity.wallet.presenter.SelectWalletTypePresenter;
import com.stratagile.qlink.utils.LocalWalletUtil;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.ToastUtil;
import com.stratagile.qlink.utils.eth.ETHWalletUtils;
import com.stratagile.qlink.view.SmoothCheckBox;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import qlc.mng.AccountMng;
import qlc.network.QlcException;
import qlc.rpc.AccountRpc;
import qlc.utils.Helper;

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
    @BindView(R.id.servicePrivacyPolicy)
    TextView servicePrivacyPolicy;
    @BindView(R.id.view2)
    View view2;
    @BindView(R.id.ivQlc)
    ImageView ivQlc;
    @BindView(R.id.tvQlc)
    TextView tvQlc;
    @BindView(R.id.llQlc)
    LinearLayout llQlc;

    private AllWallet.WalletType walletType;


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
        walletType = AllWallet.WalletType.QlcWallet;
        ivEth.setImageDrawable(getResources().getDrawable(R.mipmap.eth_wallet_unselected));
        ivEos.setImageDrawable(getResources().getDrawable(R.mipmap.eos_wallet_unselected));
        ivNeo.setImageDrawable(getResources().getDrawable(R.mipmap.neo_wallet_unselected));
        ivQlc.setImageDrawable(getResources().getDrawable(R.mipmap.qlc_wallet_select));
        tvEth.setSelected(false);
        tvQlc.setSelected(true);
        tvNeo.setSelected(false);
        tvEos.setSelected(false);
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
        closeProgressDialog();
        EventBus.getDefault().post(new ChangeWallet());
        startActivityForResult(new Intent(this, EthWalletCreatedActivity.class).putExtra("wallet", ethWallet), 0);
        String signData = SpUtil.getString(AppConfig.getInstance(), ConstantValue.P2PID, "") + ethWallet.getAddress();
        mPresenter.reportWalletCreated(ethWallet.getAddress(), "ETH", ETHWalletUtils.derivePublickKey(ethWallet.getId()), ETHWalletUtils.signPublickKey(ethWallet.getId(), signData));
    }

    @Override
    public void createNeoWalletSuccess(Wallet wallet) {
        closeProgressDialog();
        EventBus.getDefault().post(new ChangeWallet());
        startActivityForResult(new Intent(this, WalletCreatedActivity.class).putExtra("wallet", wallet), 0);
        mPresenter.reportWalletCreated(wallet.getAddress(), "NEO", wallet.getPublicKey(), "");
    }

    private void createQlcWallet() {
        showProgressDialog();
        List<EthWallet> ethWallets = AppConfig.getInstance().getDaoSession().getEthWalletDao().loadAll();
        for (EthWallet wallet : ethWallets) {
            if (wallet.getIsCurrent()) {
                wallet.setIsCurrent(false);
                AppConfig.getInstance().getDaoSession().getEthWalletDao().update(wallet);
                break;
            }
        }
        List<Wallet> wallets = AppConfig.getInstance().getDaoSession().getWalletDao().loadAll();
        for (Wallet wallet : wallets) {
            if (wallet.getIsCurrent()) {
                wallet.setIsCurrent(false);
                AppConfig.getInstance().getDaoSession().getWalletDao().update(wallet);
                break;
            }
        }
        List<EosAccount> eosAccounts = AppConfig.getInstance().getDaoSession().getEosAccountDao().loadAll();
        for (EosAccount wallet : eosAccounts) {
            if (wallet.getIsCurrent()) {
                wallet.setIsCurrent(false);
                AppConfig.getInstance().getDaoSession().getEosAccountDao().update(wallet);
                break;
            }
        }
        List<QLCAccount> qlcAccounts = AppConfig.getInstance().getDaoSession().getQLCAccountDao().loadAll();
        for (QLCAccount wallet : qlcAccounts) {
            if (wallet.getIsCurrent()) {
                wallet.setIsCurrent(false);
                AppConfig.getInstance().getDaoSession().getQLCAccountDao().update(wallet);
                break;
            }
        }
        String seed = QlcUtil.generateSeed().toLowerCase();
//        int index = -1;
//        List<QLCAccount> mainQlcAccount = AppConfig.getInstance().getDaoSession().getQLCAccountDao().queryBuilder().where(QLCAccountDao.Properties.IsAccountSeed.eq(true)).list();
//        if (mainQlcAccount.size() > 0) {
//            for (int i = 0; i < mainQlcAccount.size(); i++) {
//                if (mainQlcAccount.get(i).getSeed() != null && !mainQlcAccount.get(i).getSeed().equals("") && mainQlcAccount.get(i).getIsAccountSeed()) {
//                    seed = mainQlcAccount.get(i).getSeed();
//                    index = i;
//                }
//            }
//        }

        try {
            JSONObject jsonObject = AccountMng.keyPairFromSeed(Helper.hexStringToBytes(seed), 0);
            String priKey = jsonObject.getString("privKey");
            String pubKey = jsonObject.getString("pubKey");
            KLog.i(jsonObject.toJSONString());
            JSONArray jsonArray = new JSONArray();
            jsonArray.add(seed);
            String mnemonics = AccountRpc.seedToMnemonics(jsonArray);
            KLog.i(mnemonics);
            String address =  QlcUtil.publicToAddress(pubKey).toLowerCase();
            QLCAccount qlcAccount = new QLCAccount();
            qlcAccount.setPrivKey(priKey.toLowerCase());
            qlcAccount.setPubKey(pubKey);
            qlcAccount.setAddress(address);
            qlcAccount.setMnemonic(mnemonics);
            qlcAccount.setIsCurrent(true);
            qlcAccount.setAccountName(QLCAPI.Companion.getQlcWalletName());
            qlcAccount.setSeed(seed);
            qlcAccount.setIsAccountSeed(true);
            qlcAccount.setWalletIndex(0);
            AppConfig.instance.getDaoSession().getQLCAccountDao().insert(qlcAccount);
            closeProgressDialog();
            startActivityForResult(new Intent(this, QlcWalletCreatedActivity.class).putExtra("wallet", qlcAccount), 0);
        } catch (QlcException e) {
            closeProgressDialog();
            e.printStackTrace();
        }

//        String seed = "68740dc90101252e60d70dad82f356a03e4b54816789f9c5bd6f031a34da5812";
    }

    @Override
    public void reportCreatedWalletSuccess() {
        finish();
    }

    @Override
    public void createQlcWalletSuccess() {

    }

    @Override
    protected void onDestroy() {
        LocalWalletUtil.updateLocalNeoWallet();
        LocalWalletUtil.updateLocalEthWallet();
        LocalWalletUtil.updateLocalEosWallet();
        LocalWalletUtil.updateLocalQlcWallet();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            setResult(RESULT_OK);
            finish();
        } else if (resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }

    @OnClick({R.id.llEth, R.id.llEos, R.id.llNeo, R.id.llQlc, R.id.btCreate, R.id.btImport, R.id.servicePrivacyPolicy})
    public void onViewClicked(View view) {
        KLog.i(view.getId() + "");
        switch (view.getId()) {
            case R.id.llEth:
                walletType = AllWallet.WalletType.EthWallet;
                ivEth.setImageDrawable(getResources().getDrawable(R.mipmap.eth_wallet_select));
                ivEos.setImageDrawable(getResources().getDrawable(R.mipmap.eos_wallet_unselected));
                ivNeo.setImageDrawable(getResources().getDrawable(R.mipmap.neo_wallet_unselected));
                ivQlc.setImageDrawable(getResources().getDrawable(R.mipmap.qlc_wallet_unselected));
                tvQlc.setSelected(false);
                tvEth.setSelected(true);
                tvNeo.setSelected(false);
                tvEos.setSelected(false);
                break;
            case R.id.llEos:
                walletType = AllWallet.WalletType.EosWallet;
                ivEth.setImageDrawable(getResources().getDrawable(R.mipmap.eth_wallet_unselected));
                ivEos.setImageDrawable(getResources().getDrawable(R.mipmap.eos_wallet_select));
                ivNeo.setImageDrawable(getResources().getDrawable(R.mipmap.neo_wallet_unselected));
                ivQlc.setImageDrawable(getResources().getDrawable(R.mipmap.qlc_wallet_unselected));
                tvQlc.setSelected(false);
                tvEth.setSelected(false);
                tvNeo.setSelected(false);
                tvEos.setSelected(true);
                break;
            case R.id.llNeo:
                walletType = AllWallet.WalletType.NeoWallet;
                ivEth.setImageDrawable(getResources().getDrawable(R.mipmap.eth_wallet_unselected));
                ivEos.setImageDrawable(getResources().getDrawable(R.mipmap.eos_wallet_unselected));
                ivNeo.setImageDrawable(getResources().getDrawable(R.mipmap.neo_wallet_select));
                ivQlc.setImageDrawable(getResources().getDrawable(R.mipmap.qlc_wallet_unselected));
                tvQlc.setSelected(false);
                tvEth.setSelected(false);
                tvNeo.setSelected(true);
                tvEos.setSelected(false);
                break;
            case R.id.llQlc:
                walletType = AllWallet.WalletType.QlcWallet;
                ivEth.setImageDrawable(getResources().getDrawable(R.mipmap.eth_wallet_unselected));
                ivEos.setImageDrawable(getResources().getDrawable(R.mipmap.eos_wallet_unselected));
                ivNeo.setImageDrawable(getResources().getDrawable(R.mipmap.neo_wallet_unselected));
                ivQlc.setImageDrawable(getResources().getDrawable(R.mipmap.qlc_wallet_select));
                tvQlc.setSelected(true);
                tvEth.setSelected(false);
                tvNeo.setSelected(false);
                tvEos.setSelected(false);
                break;
            case R.id.btCreate:
                if (!checkBox.isChecked()) {
                    ToastUtil.displayShortToast(getString(R.string.please_agree_to_the_service_agreement));
                    return;
                }
                if (walletType == AllWallet.WalletType.EthWallet) {
                    mPresenter.createEthWallet();
                } else if (walletType == AllWallet.WalletType.NeoWallet) {
                    mPresenter.createNeoWallet();
                } else if (walletType == AllWallet.WalletType.EosWallet) {
                    Intent intent = new Intent(this, EosCreateActivity.class);
                    startActivityForResult(intent, 2);
                } else if (walletType == AllWallet.WalletType.QlcWallet) {
                    createQlcWallet();
                }
                break;
            case R.id.btImport:
                if (!checkBox.isChecked()) {
                    ToastUtil.displayShortToast(getString(R.string.please_agree_to_the_service_agreement));
                    return;
                }
                if (walletType == AllWallet.WalletType.EthWallet) {
                    startActivityForResult(new Intent(this, ImportEthWalletActivity.class), 1);
                } else if (walletType == AllWallet.WalletType.NeoWallet) {
                    Intent intent = new Intent(this, ImportWalletActivity.class);
                    startActivityForResult(intent, 1);
                } else if (walletType == AllWallet.WalletType.EosWallet) {
                    Intent intent = new Intent(this, EosImportActivity.class);
                    startActivityForResult(intent, 2);
                } else if (walletType == AllWallet.WalletType.QlcWallet) {
                    Intent intent = new Intent(this, ImportQlcActivity.class);
                    startActivityForResult(intent, 3);
                }
                break;
            case R.id.servicePrivacyPolicy:
                Intent intent = new Intent(this, WebViewActivity.class);
                intent.putExtra("url", "https://docs.google.com/document/d/1yTr1EDXmOclDuSt4o0RRUc0fVjJU3zPREK97C1RmYdI/edit?usp=sharing");
                intent.putExtra("title", R.string.service_agreement);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}