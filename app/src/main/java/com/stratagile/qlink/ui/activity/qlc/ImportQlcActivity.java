package com.stratagile.qlink.ui.activity.qlc;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.Menu;
import android.view.MenuItem;

import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.db.EosAccount;
import com.stratagile.qlink.db.EthWallet;
import com.stratagile.qlink.db.EthWalletDao;
import com.stratagile.qlink.db.QLCAccount;
import com.stratagile.qlink.db.QLCAccountDao;
import com.stratagile.qlink.db.Wallet;
import com.stratagile.qlink.ui.activity.eth.EthKeyStroeFragment;
import com.stratagile.qlink.ui.activity.eth.EthMnemonicFragment;
import com.stratagile.qlink.ui.activity.eth.EthPrivateKeyFragment;
import com.stratagile.qlink.ui.activity.eth.EthWatchFragment;
import com.stratagile.qlink.ui.activity.eth.ImportViewModel;
import com.stratagile.qlink.ui.activity.eth.presenter.ImportEthWalletPresenter;
import com.stratagile.qlink.ui.activity.qlc.component.DaggerImportQlcComponent;
import com.stratagile.qlink.ui.activity.qlc.contract.ImportQlcContract;
import com.stratagile.qlink.ui.activity.qlc.module.ImportQlcModule;
import com.stratagile.qlink.ui.activity.qlc.presenter.ImportQlcPresenter;
import com.stratagile.qlink.ui.activity.wallet.ScanQrCodeActivity;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.eth.ETHWalletUtils;
import com.stratagile.qlink.view.ParentNoDispatchViewpager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.qlc
 * @Description: $description
 * @date 2019/05/21 09:40:38
 */

public class ImportQlcActivity extends BaseActivity implements ImportQlcContract.View {

    @Inject
    ImportQlcPresenter mPresenter;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ParentNoDispatchViewpager viewPager;

    private ArrayList<String> titles = new ArrayList<>();

    private ImportViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainColor = R.color.white;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_import_qlc);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.import_eth_wallet);
    }

    @Override
    protected void initData() {
        titles.add(getString(R.string.mnemonic));
        titles.add(getString(R.string.wallet_seed));
        viewModel = ViewModelProviders.of(this).get(ImportViewModel.class);
        viewModel.walletAddress.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                List<Wallet> wallets = AppConfig.getInstance().getDaoSession().getWalletDao().loadAll();
                for (Wallet wallet : wallets) {
                    if (wallet.getIsCurrent()) {
                        wallet.setIsCurrent(false);
                        AppConfig.getInstance().getDaoSession().getWalletDao().update(wallet);
                        break;
                    }
                }
                List<EthWallet> ethWallets = AppConfig.getInstance().getDaoSession().getEthWalletDao().loadAll();
                for (EthWallet wallet : ethWallets) {
                    if (wallet.getIsCurrent()) {
                        wallet.setIsCurrent(false);
                        AppConfig.getInstance().getDaoSession().getEthWalletDao().update(wallet);
                        break;
                    }
                }
                List<EosAccount> wallets2 = AppConfig.getInstance().getDaoSession().getEosAccountDao().loadAll();
                if (wallets2 != null && wallets2.size() != 0) {
                    for (int i = 0; i < wallets2.size(); i++) {
                        if (wallets2.get(i).getIsCurrent()) {
                            wallets2.get(i).setIsCurrent(false);
                            AppConfig.getInstance().getDaoSession().getEosAccountDao().update(wallets2.get(i));
                            break;
                        }
                    }
                }
                List<QLCAccount> wallets3 = AppConfig.getInstance().getDaoSession().getQLCAccountDao().loadAll();
                if (wallets3 != null && wallets3.size() != 0) {
                    for (int i = 0; i < wallets3.size(); i++) {
                        if (wallets3.get(i).getIsCurrent()) {
                            wallets3.get(i).setIsCurrent(false);
                            AppConfig.getInstance().getDaoSession().getQLCAccountDao().update(wallets3.get(i));
                            break;
                        }
                    }
                }
                QLCAccount qlcAccount = AppConfig.getInstance().getDaoSession().getQLCAccountDao().queryBuilder().where(QLCAccountDao.Properties.Address.eq(s)).unique();
                qlcAccount.setIsCurrent(true);
                AppConfig.getInstance().getDaoSession().getQLCAccountDao().update(qlcAccount);
                setResult(RESULT_OK);
                finish();
            }
        });
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if (position == 0) {
                    return new ImportQlcMnemonicFragment();
                } else if (position == 1) {
                    return new ImportQlcSeedFragment();
                } else {
                    return null;
                }
            }

            @Override
            public int getCount() {
                return titles.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titles.get(position);
            }
        });
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.qrcode_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.qrcode) {
            startActivityForResult(new Intent(this, ScanQrCodeActivity.class), 1);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            viewModel.qrCode.postValue(data.getStringExtra("result"));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void setupActivityComponent() {
       DaggerImportQlcComponent
               .builder()
               .appComponent(((AppConfig) getApplication()).getApplicationComponent())
               .importQlcModule(new ImportQlcModule(this))
               .build()
               .inject(this);
    }
    @Override
    public void setPresenter(ImportQlcContract.ImportQlcContractPresenter presenter) {
        mPresenter = (ImportQlcPresenter) presenter;
    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void closeProgressDialog() {
        progressDialog.hide();
    }

}