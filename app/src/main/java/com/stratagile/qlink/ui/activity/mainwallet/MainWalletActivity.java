package com.stratagile.qlink.ui.activity.mainwallet;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.db.Wallet;
import com.stratagile.qlink.entity.Balance;
import com.stratagile.qlink.entity.Raw;
import com.stratagile.qlink.entity.eventbus.AssetRefrash;
import com.stratagile.qlink.entity.eventbus.SetToMatchs;
import com.stratagile.qlink.ui.activity.mainwallet.component.DaggerMainWalletComponent;
import com.stratagile.qlink.ui.activity.mainwallet.contract.MainWalletContract;
import com.stratagile.qlink.ui.activity.mainwallet.module.MainWalletModule;
import com.stratagile.qlink.ui.activity.mainwallet.presenter.MainWalletPresenter;
import com.stratagile.qlink.ui.activity.wallet.AssetListFragment;
import com.stratagile.qlink.ui.activity.wallet.UseHistoryListFragment;
import com.stratagile.qlink.ui.activity.wallet.WalletDetailActivity;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.eth.WalletStorage;
import com.stratagile.qlink.view.ParentNoDispatchViewpager;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author zl
 * @Package com.stratagile.qlink.ui.activity.mainwallet
 * @Description: $description
 * @date 2018/06/13 14:09:33
 */

public class MainWalletActivity extends BaseActivity implements MainWalletContract.View {

    @Inject
    MainWalletPresenter mPresenter;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_wallet)
    ImageView ivWallet;
    @BindView(R.id.iv_qlc)
    ImageView ivQlc;
    @BindView(R.id.flag_qlc)
    TextView flagQlc;
    @BindView(R.id.tv_qlc)
    TextView tvQlc;
    @BindView(R.id.qlc_to_neo)
    TextView qlcToNeo;
    @BindView(R.id.iv_change)
    ImageView ivChange;
    @BindView(R.id.tv_change)
    TextView tvChange;
    @BindView(R.id.parent_qlc)
    RelativeLayout parentQlc;
    @BindView(R.id.tv_gas)
    TextView tvGas;
    @BindView(R.id.bnb_to_qlc)
    TextView bnbToQlc;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ParentNoDispatchViewpager viewPager;
    @BindView(R.id.rl_walet)
    LinearLayout rlWalet;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    private ArrayList<String> titles;
    private Wallet wallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        needFront= true;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_mainwallet);
        setToorBar(false);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initData();
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.mainColor));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                preGetBalance(wallet);
            }
        });
        tvTitle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                startActivity(WalletDetailActivity.class);
                return true;
            }
        });
        EventBus.getDefault().post(new SetToMatchs());
        tvTitle.setText(getString(R.string.my_wallet).toUpperCase());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void initData() {
        titles = new ArrayList<>();
        titles.add(getString(R.string.TRANSACTIONS).toUpperCase());
        titles.add(getString(R.string.send).toUpperCase());
        titles.add(getString(R.string.RECEIVE).toUpperCase());
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if (position == 0) {
                    return MainTransactionRecordListFragment.newInstance("mainwallet_" + titles.get(position));
                } else if (position == 1) {
                    return MainSendFragment.newInstance("mainwallet_" + titles.get(position));
                } else {
                    return MainReceiveFragment.newInstance("mainwallet_" + titles.get(position));
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
        viewPager.setCurrentItem(0);
        preGetBalance(wallet);
    }

    @Override
    protected void setupActivityComponent() {
        DaggerMainWalletComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .mainWalletModule(new MainWalletModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(MainWalletContract.MainWalletContractPresenter presenter) {
        mPresenter = (MainWalletPresenter) presenter;
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
    public void onCreatWalletSuccess(Wallet wallet) {
//        this.wallet = wallet;
//        AppConfig.getInstance().getDaoSession().getWalletDao().insert(wallet);
    }

    private void preGetBalance(Wallet wallet) {
//        refreshLayout.setRefreshing(true);
        this.wallet = AppConfig.getInstance().getDaoSession().getWalletDao().loadAll().get(SpUtil.getInt(this, ConstantValue.currentMainWallet, 0));
        wallet = this.wallet;
        KLog.i("获取资产");
        Map<String, String> map = new HashMap<>();
        map.put("address", wallet.getAddress());
        mPresenter.getBalance(map);
    }

    /**
     * @param balance
     * @see UseHistoryListFragment#refrashNeo(Balance)
     */
    @Override
    public void onGetBalancelSuccess(Balance balance) {
        ConstantValue.mBalance = balance;
        tvQlc.setText(balance.getData().getQLC() + "");
        //tvNeo.setText(balance.getData().getNEO() + "");
        tvGas.setText(balance.getData().getGAS() + "");
        EventBus.getDefault().post(balance);
        mPresenter.getRaw(new HashMap<String, String>());
    }

    /**
     * @param raw
     * @see UseHistoryListFragment#refrashNeo(Raw)
     */
    @Override
    public void onGetRawSuccess(Raw raw) {
        refreshLayout.setRefreshing(false);
        //neoToQlc.setText("1 NEO = " + raw.getData().getRates().getNEO().getQLC() + " QLC");
        double qlc2neo = (1 / raw.getData().getRates().getNEO().getQLC());
        KLog.i(qlc2neo);
        BigDecimal b = new BigDecimal(new Double(qlc2neo).toString());
        double f1 = b.setScale(8, BigDecimal.ROUND_HALF_UP).doubleValue();
        qlcToNeo.setText("1 QLC = " + f1 + " NEO");
        bnbToQlc.setText("1 GAS = " + raw.getData().getRates().getGAS().getQLC() + " QLC");
//        gasToNeo.setText("1 GAS = " + raw.getData().getRates().getGAS().getQLC() + " QLC");
        EventBus.getDefault().post(raw);
        /**
         * @see AssetListFragment#onRefrash(AssetRefrash)
         */
        EventBus.getDefault().post(new AssetRefrash());

        String ethWalletAddress = WalletStorage.getInstance(this).get().get(SpUtil.getInt(this, ConstantValue.currentEthWallet, 0)).getPubKey();
        Map<String, String> infoMap = new HashMap<>();
        infoMap.put("apiKey", "freekey");
        mPresenter.getETHWalletDetail(ethWalletAddress, infoMap);
    }

    @Override
    public void setBnbValue(String value) {
        //tvGas.setText(value);
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}