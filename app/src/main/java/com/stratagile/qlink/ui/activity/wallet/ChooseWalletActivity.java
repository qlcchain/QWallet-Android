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
import com.stratagile.qlink.db.EthWallet;
import com.stratagile.qlink.db.Wallet;
import com.stratagile.qlink.entity.AllWallet;
import com.stratagile.qlink.entity.eventbus.ChangeWallet;
import com.stratagile.qlink.ui.activity.wallet.component.DaggerChooseWalletComponent;
import com.stratagile.qlink.ui.activity.wallet.contract.ChooseWalletContract;
import com.stratagile.qlink.ui.activity.wallet.module.ChooseWalletModule;
import com.stratagile.qlink.ui.activity.wallet.presenter.ChooseWalletPresenter;
import com.stratagile.qlink.utils.LocalWalletUtil;
import com.stratagile.qlink.view.SelectWalletAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

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
        setTitle("Choose a Wallet");
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

        downCheckAdapter = new SelectWalletAdapter(allWallets);
        recyclerView.setAdapter(downCheckAdapter);
        downCheckAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
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
                }
            }
        });
        LocalWalletUtil.updateNeoWallet();
        LocalWalletUtil.updateLocalEthWallet();
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