package com.stratagile.qlink.ui.activity.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.stratagile.qlink.Account;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.db.EosAccount;
import com.stratagile.qlink.db.EosAccountDao;
import com.stratagile.qlink.db.EthWallet;
import com.stratagile.qlink.db.QLCAccount;
import com.stratagile.qlink.db.Wallet;
import com.stratagile.qlink.entity.AllWallet;
import com.stratagile.qlink.entity.eventbus.ChangeWallet;
import com.stratagile.qlink.ui.activity.wallet.component.DaggerChooseWalletComponent;
import com.stratagile.qlink.ui.activity.wallet.contract.ChooseWalletContract;
import com.stratagile.qlink.ui.activity.wallet.module.ChooseWalletModule;
import com.stratagile.qlink.ui.activity.wallet.presenter.ChooseWalletPresenter;
import com.stratagile.qlink.utils.LocalWalletUtil;
import com.stratagile.qlink.utils.ToastUtil;
import com.stratagile.qlink.view.SelectWalletAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: $description
 * @date 2018/11/06 11:21:13
 */

public class ChooseWalletActivity extends BaseActivity implements ChooseWalletContract.View {

    @Inject
    ChooseWalletPresenter mPresenter;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private SelectWalletAdapter downCheckAdapter;
    ArrayList<AllWallet> allWallets = new ArrayList<>();
    private AllWallet currentSelectWallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainColor = R.color.white;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_choose_wallet);
        ButterKnife.bind(this);
        setTitle(getString(R.string.choose_wallet));
    }

    @Override
    protected void initData() {
        allWallets.clear();
        List<EthWallet> ethWallets = AppConfig.getInstance().getDaoSession().getEthWalletDao().loadAll();
        if (ethWallets.size() != 0) {
            for (int i = 0; i < ethWallets.size(); i++) {
                AllWallet allWallet = new AllWallet();
                allWallet.setEthWallet(ethWallets.get(i));
                allWallet.setWalletType(AllWallet.WalletType.EthWallet);
                allWallet.setWalletAddress(ethWallets.get(i).getAddress());
                allWallet.setWalletName(ethWallets.get(i).getName());
                allWallets.add(allWallet);
                if (ethWallets.get(i).getName().equals(getIntent().getStringExtra("walletName"))) {
                    currentSelectWallet = allWallet;
//                    getEthToken(ethWallets.get(i));
                }
            }
        }
        List<Wallet> neoWallets = AppConfig.getInstance().getDaoSession().getWalletDao().loadAll();
        if (neoWallets.size() != 0) {
            for (int i = 0; i < neoWallets.size(); i++) {
                if (neoWallets.get(i).getPrivateKey().equalsIgnoreCase(neoWallets.get(i).getPublicKey())) {
                    Account.INSTANCE.fromWIF(neoWallets.get(i).getWif());
                    neoWallets.get(i).setPublicKey(Account.INSTANCE.byteArray2String(Account.INSTANCE.getWallet().getPublicKey()).toLowerCase());
                    AppConfig.getInstance().getDaoSession().getWalletDao().update(neoWallets.get(i));
                }
                AllWallet allWallet = new AllWallet();
                allWallet.setWallet(neoWallets.get(i));
                allWallet.setWalletAddress(neoWallets.get(i).getAddress());
                allWallet.setWalletType(AllWallet.WalletType.NeoWallet);
                if (neoWallets.get(i).getName() == null || "".equals(neoWallets.get(i).getName())) {
                    allWallet.setWalletName(neoWallets.get(i).getAddress());
                } else {
                    allWallet.setWalletName(neoWallets.get(i).getName());
                }
                allWallets.add(allWallet);
                if (neoWallets.get(i).getAddress().equals(getIntent().getStringExtra("walletName"))) {
                    currentSelectWallet = allWallet;
                }
            }
        }

        List<EosAccount> wallets2 = AppConfig.getInstance().getDaoSession().getEosAccountDao().loadAll();
        if (wallets2 != null && wallets2.size() != 0) {
            for (int i = 0; i < wallets2.size(); i++) {
                if (wallets2.get(i).getAccountName() == null) {
                    continue;
                }
                if (wallets2.get(i).getIsCreating()) {
                    mPresenter.getEosAccountInfo(wallets2.get(i));
                }
                AllWallet allWallet = new AllWallet();
                allWallet.setEosAccount(wallets2.get(i));
                allWallet.setWalletAddress(wallets2.get(i).getAccountName());
                allWallet.setWalletName(wallets2.get(i).getWalletName() == null? wallets2.get(i).getAccountName() : wallets2.get(i).getWalletName());
                allWallet.setWalletType(AllWallet.WalletType.EosWallet);
                allWallets.add(allWallet);
                if (wallets2.get(i).getAccountName() != null && wallets2.get(i).getAccountName().equals(getIntent().getStringExtra("walletName"))) {
                    currentSelectWallet = allWallet;
                }
            }
        }

        List<QLCAccount> qlcAccounts = AppConfig.getInstance().getDaoSession().getQLCAccountDao().loadAll();
        if (qlcAccounts != null && qlcAccounts.size() != 0) {
            for (int i = 0; i < qlcAccounts.size(); i++) {
                if (qlcAccounts.get(i).getAccountName() == null) {
                    continue;
                }
                AllWallet allWallet = new AllWallet();
                allWallet.setQlcAccount(qlcAccounts.get(i));
                allWallet.setWalletAddress(qlcAccounts.get(i).getAddress());
                allWallet.setWalletName(qlcAccounts.get(i).getAccountName() == null? qlcAccounts.get(i).getAccountName() : qlcAccounts.get(i).getAccountName());
                allWallet.setWalletType(AllWallet.WalletType.QlcWallet);
                allWallets.add(allWallet);
                if (qlcAccounts.get(i).getAccountName() != null && qlcAccounts.get(i).getAccountName().equals(getIntent().getStringExtra("walletName"))) {
                    currentSelectWallet = allWallet;
                }
            }
        }

        downCheckAdapter = new SelectWalletAdapter(allWallets);
        recyclerView.setAdapter(downCheckAdapter);
        downCheckAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (downCheckAdapter.getItem(position).getWalletType() == AllWallet.WalletType.EosWallet && downCheckAdapter.getItem(position).getWalletAddress() == null) {
                    ToastUtil.displayShortToast(getString(R.string.this_is_a_wallet_waitting_to_be_created));
                    return;
                }
                showProgressDialog();
                downCheckAdapter.notifyDataSetChanged();
                currentSelectWallet = downCheckAdapter.getItem(position);
                for (int i = 0; i < downCheckAdapter.getData().size(); i++) {
                    if (downCheckAdapter.getData().get(i).getWalletType() == AllWallet.WalletType.EthWallet && downCheckAdapter.getData().get(i).getEthWallet().getIsCurrent()) {
                        downCheckAdapter.getData().get(i).getEthWallet().setCurrent(false);
                        AppConfig.getInstance().getDaoSession().getEthWalletDao().update(downCheckAdapter.getData().get(i).getEthWallet());
                    }
                    if (downCheckAdapter.getData().get(i).getWalletType() == AllWallet.WalletType.NeoWallet && downCheckAdapter.getData().get(i).getWallet().getIsCurrent()) {
                        downCheckAdapter.getData().get(i).getWallet().setIsCurrent(false);
                        AppConfig.getInstance().getDaoSession().getWalletDao().update(downCheckAdapter.getData().get(i).getWallet());
                    }
                    if (downCheckAdapter.getData().get(i).getWalletType() == AllWallet.WalletType.EosWallet && downCheckAdapter.getData().get(i).getEosAccount().getIsCurrent()) {
                        downCheckAdapter.getData().get(i).getEosAccount().setIsCurrent(false);
                        AppConfig.getInstance().getDaoSession().getEosAccountDao().update(downCheckAdapter.getData().get(i).getEosAccount());
                    }
                    if (downCheckAdapter.getData().get(i).getWalletType() == AllWallet.WalletType.QlcWallet && downCheckAdapter.getData().get(i).getQlcAccount().getIsCurrent()) {
                        downCheckAdapter.getData().get(i).getQlcAccount().setIsCurrent(false);
                        AppConfig.getInstance().getDaoSession().getQLCAccountDao().update(downCheckAdapter.getData().get(i).getQlcAccount());
                    }



                    if (downCheckAdapter.getData().get(i).getWalletType() == AllWallet.WalletType.EosWallet && position == i) {
                        downCheckAdapter.getData().get(i).getEosAccount().setIsCurrent(true);
                        AppConfig.getInstance().getDaoSession().getEosAccountDao().update(downCheckAdapter.getData().get(i).getEosAccount());
                        closeProgressDialog();
                        setResult(RESULT_OK);
                        onBackPressed();
                    }
                    if (downCheckAdapter.getData().get(i).getWalletType() == AllWallet.WalletType.EthWallet && position == i) {
                        downCheckAdapter.getData().get(i).getEthWallet().setCurrent(true);
                        AppConfig.getInstance().getDaoSession().getEthWalletDao().update(downCheckAdapter.getData().get(i).getEthWallet());
                        closeProgressDialog();
                        setResult(RESULT_OK);
                        onBackPressed();
                    }
                    if (downCheckAdapter.getData().get(i).getWalletType() == AllWallet.WalletType.NeoWallet && position == i) {
                        downCheckAdapter.getData().get(i).getWallet().setIsCurrent(true);
                        Wallet wallet = downCheckAdapter.getData().get(i).getWallet();
                        AppConfig.getInstance().getDaoSession().getWalletDao().update(downCheckAdapter.getData().get(i).getWallet());
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
                    if (downCheckAdapter.getData().get(i).getWalletType() == AllWallet.WalletType.QlcWallet && position == i) {
                        downCheckAdapter.getData().get(i).getQlcAccount().setCurrent(true);
                        AppConfig.getInstance().getDaoSession().getQLCAccountDao().update(downCheckAdapter.getData().get(i).getQlcAccount());
                        closeProgressDialog();
                        setResult(RESULT_OK);
                        onBackPressed();
                    }
                }
            }
        });
        reNmaeEosWalletName();
        reNmaeNeoWalletName();
        LocalWalletUtil.updateLocalNeoWallet();
        LocalWalletUtil.updateLocalEthWallet();
        LocalWalletUtil.updateLocalEosWallet();
        LocalWalletUtil.updateLocalQlcWallet();
    }

    @Override
    protected void setupActivityComponent() {
        DaggerChooseWalletComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .chooseWalletModule(new ChooseWalletModule(this))
                .build()
                .inject(this);
    }

    private void reNmaeNeoWalletName() {
        List<Wallet> neoWallets = AppConfig.getInstance().getDaoSession().getWalletDao().loadAll();
        if (neoWallets.size() != 0) {
            for (int i = 0; i < neoWallets.size(); i++) {
                if (neoWallets.get(i).getName() == null) {
                    neoWallets.get(i).setName("NEO-Wallet " + getIndex(i));
                    AppConfig.getInstance().getDaoSession().getWalletDao().update(neoWallets.get(i));
                }
            }
        }
    }

    private void reNmaeEosWalletName() {
        List<EosAccount> neoWallets = AppConfig.getInstance().getDaoSession().getEosAccountDao().loadAll();
        if (neoWallets.size() != 0) {
            for (int i = 0; i < neoWallets.size(); i++) {
                if (neoWallets.get(i).getWalletName() == null) {
                    neoWallets.get(i).setWalletName("EOS-Wallet " + getIndex(i));
                    AppConfig.getInstance().getDaoSession().getEosAccountDao().update(neoWallets.get(i));
                }
            }
        }
    }

    private String getIndex(int i) {
        if (i < 9) {
            return "0" + (i +1);
        } else {
            return "" + (i +1);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(0, R.anim.activity_translate_out_1);
    }

    @Override
    public void setPresenter(ChooseWalletContract.ChooseWalletContractPresenter presenter) {
        mPresenter = (ChooseWalletPresenter) presenter;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        initData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add) {
            startActivityForResult(new Intent(this, SelectWalletTypeActivity.class), 1);
        }
        return super.onOptionsItemSelected(item);
    }

}