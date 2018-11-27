package com.stratagile.qlink.ui.activity.eth;

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
import com.stratagile.qlink.db.EthWallet;
import com.stratagile.qlink.db.EthWalletDao;
import com.stratagile.qlink.ui.activity.eth.component.DaggerImportEthWalletComponent;
import com.stratagile.qlink.ui.activity.eth.contract.ImportEthWalletContract;
import com.stratagile.qlink.ui.activity.eth.module.ImportEthWalletModule;
import com.stratagile.qlink.ui.activity.eth.presenter.ImportEthWalletPresenter;
import com.stratagile.qlink.ui.activity.wallet.AssetListFragment;
import com.stratagile.qlink.ui.activity.wallet.ScanQrCodeActivity;
import com.stratagile.qlink.ui.activity.wallet.UseHistoryListFragment;
import com.stratagile.qlink.view.CustomPopWindow;
import com.stratagile.qlink.view.ParentNoDispatchViewpager;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.eth
 * @Description: $description
 * @date 2018/05/24 16:30:41
 */

public class ImportEthWalletActivity extends BaseActivity implements ImportEthWalletContract.View {

    @Inject
    ImportEthWalletPresenter mPresenter;
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
        setContentView(R.layout.activity_import_eth_wallet);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.import_eth_wallet);
    }

    @Override
    protected void initData() {
        titles.add("Mnemonic");
        titles.add("Official");
        titles.add("Private Key");
        titles.add("Watch");
        viewModel = ViewModelProviders.of(this).get(ImportViewModel.class);
        viewModel.walletAddress.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                EthWallet ethWallet = AppConfig.getInstance().getDaoSession().getEthWalletDao().queryBuilder().where(EthWalletDao.Properties.Address.eq(s)).unique();
                if (!ethWallet.getIsLook()) {
                    mPresenter.reportWalletImported(s);
                } else {
                    closeProgressDialog();
                    finish();
                }
            }
        });
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if (position == 0) {
                    return EthMnemonicFragment.newInstance(titles.get(position));
                } else if (position == 1) {
                    return EthKeyStroeFragment.newInstance(titles.get(position));
                } else if (position == 2) {
                    return EthPrivateKeyFragment.newInstance(titles.get(position));
                } else {
                    return EthWatchFragment.newInstance(titles.get(position));
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
//        viewPager.setCurrentItem(1);
    }

    @Override
    public void onBackPressed() {
        if (!CustomPopWindow.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void setupActivityComponent() {
        DaggerImportEthWalletComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .importEthWalletModule(new ImportEthWalletModule(this))
                .build()
                .inject(this);
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
    public void setPresenter(ImportEthWalletContract.ImportEthWalletContractPresenter presenter) {
        mPresenter = (ImportEthWalletPresenter) presenter;
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
    public void reportCreatedWalletSuccess() {
        closeProgressDialog();
        finish();
    }

}