package com.stratagile.qlink.ui.activity.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseFragment;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.db.Wallet;
import com.stratagile.qlink.entity.Balance;
import com.stratagile.qlink.entity.Raw;
import com.stratagile.qlink.entity.eventbus.AssetRefrash;
import com.stratagile.qlink.entity.eventbus.ChangeWalletNeedRefesh;
import com.stratagile.qlink.entity.eventbus.NeoRefrash;
import com.stratagile.qlink.entity.eventbus.Set2Asset;
import com.stratagile.qlink.ui.activity.eth.EthWalletActivity;
import com.stratagile.qlink.ui.activity.setting.SettingsActivity;
import com.stratagile.qlink.ui.activity.wallet.component.DaggerWalletComponent;
import com.stratagile.qlink.ui.activity.wallet.contract.WalletContract;
import com.stratagile.qlink.ui.activity.wallet.module.WalletModule;
import com.stratagile.qlink.ui.activity.wallet.presenter.WalletPresenter;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.eth.WalletStorage;
import com.stratagile.qlink.view.ParentNoDispatchViewpager;
import com.vondear.rxtools.view.popupwindows.tools.RxPopupView;
import com.vondear.rxtools.view.popupwindows.tools.RxPopupViewManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
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
 * @date 2018/01/18 19:08:00
 */

public class WalletFragment extends BaseFragment implements WalletContract.View, RxPopupViewManager.TipListener {

    @Inject
    WalletPresenter mPresenter;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ParentNoDispatchViewpager viewPager;
    @BindView(R.id.tv_qlc)
    TextView tvQlc;
    @BindView(R.id.tv_neo)
    TextView tvNeo;
    @BindView(R.id.qlc_to_neo)
    TextView qlcToNeo;
    @BindView(R.id.neo_to_qlc)
    TextView neoToQlc;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_avater)
    ImageView ivAvater;
    @BindView(R.id.iv_wallet)
    ImageView ivWallet;
    @BindView(R.id.tv_gas)
    TextView tvGas;
    @BindView(R.id.bnb_to_qlc)
    TextView bnbToQlc;
    @BindView(R.id.rl_walet)
    LinearLayout rlWalet;
    @BindView(R.id.parent_qlc)
    RelativeLayout parentQlc;
    private ArrayList<String> titles;

    private Wallet wallet;
    private RxPopupViewManager mRxPopupViewManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallet, null);
        ButterKnife.bind(this, view);
        Bundle mBundle = getArguments();
        initData(view);
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
        tvTitle.setText(getString(R.string.my_wallet).toUpperCase());
        return view;
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ChangeToTestWallet(ChangeWalletNeedRefesh changeWalletNeedRefesh) {
        preGetBalance(wallet);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        mPresenter.unsubscribe();
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshNeo(NeoRefrash neoRefrash) {
        preGetBalance(wallet);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setToAssets(Set2Asset set2Asset) {
        viewPager.setCurrentItem(0);
    }

    private void initData(View view) {
        titles = new ArrayList<>();
        titles.add(getString(R.string.MY_WIFI_VPN_ASSETS).toUpperCase());
        titles.add(getString(R.string.MANAGE_FUNDS).toUpperCase());
        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if (position == 0) {
                    return AssetListFragment.newInstance(titles.get(position));
                } else if (position == 1) {
                    return UseHistoryListFragment.newInstance(titles.get(position));
                } else {
                    return new Fragment();
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
        viewPager.setCurrentItem(1);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void setupFragmentComponent() {
        DaggerWalletComponent
                .builder()
                .appComponent(((AppConfig) getActivity().getApplication()).getApplicationComponent())
                .walletModule(new WalletModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(WalletContract.WalletContractPresenter presenter) {
        mPresenter = (WalletPresenter) presenter;
    }

    @Override
    protected void initDataFromLocal() {

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
        if(refreshLayout != null)
        {
            refreshLayout.setRefreshing(false);
        }
        this.wallet = AppConfig.getInstance().getDaoSession().getWalletDao().loadAll().get(SpUtil.getInt(getActivity(), ConstantValue.currentWallet, 0));
        wallet = this.wallet;
        KLog.i("获取资产");
        Map<String, String> map = new HashMap<>();
        map.put("address", wallet.getAddress());
        mPresenter.getBalance(map);
//
//        if (WalletStorage.getInstance(getActivity()).get() != null && WalletStorage.getInstance(getActivity()).get().size() != 0) {
//            String ethWalletAddress = WalletStorage.getInstance(getActivity()).get().get(SpUtil.getInt(getActivity(), ConstantValue.currentEthWallet, 0)).getPubKey();
//            Map<String, String> infoMap = new HashMap<>();
//            infoMap.put("apiKey", "freekey");
//            mPresenter.getETHWalletDetail(ethWalletAddress, infoMap);
//        }
    }

    /**
     * @see UseHistoryListFragment#refrashNeo(Balance)
     * @param balance
     */
    @Override
    public void onGetBalancelSuccess(Balance balance) {
        ConstantValue.mBalance = balance;
        if ("".equals(balance.getData().getQLC())) {
            tvQlc.setText("0");
        } else {
            BigDecimal b = new BigDecimal(new Double(balance.getData().getQLC()).toString());
            double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            tvQlc.setText(f1 + "");
        }
        tvNeo.setText(balance.getData().getNEO() + "");
//        tvGas.setText(balance.getData().getGAS() + "");
        EventBus.getDefault().post(balance);
        mPresenter.getRaw(new HashMap<String, String>());
    }

    /**
     *
     * @see UseHistoryListFragment#refrashNeo(Raw)
     * @param raw
     */
    @Override
    public void onGetRawSuccess(Raw raw) {
        refreshLayout.setRefreshing(false);
        neoToQlc.setText("1 NEO = " + raw.getData().getRates().getNEO().getQLC() + " QLC");
        double qlc2neo = (1 / raw.getData().getRates().getNEO().getQLC());
        KLog.i(qlc2neo);
        BigDecimal b = new BigDecimal(new Double(qlc2neo).toString());
        double f1 = b.setScale(8, BigDecimal.ROUND_HALF_UP).doubleValue();
        qlcToNeo.setText("1 QLC = " + f1 + " NEO");
        bnbToQlc.setText("1 BNB = " + raw.getData().getRates().getBNB().getQLC() + " QLC");
//        gasToNeo.setText("1 GAS = " + raw.getData().getRates().getGAS().getQLC() + " QLC");
        EventBus.getDefault().post(raw);
        /**
         * @see AssetListFragment#onRefrash(AssetRefrash)
         */
        EventBus.getDefault().post(new AssetRefrash());

        if (WalletStorage.getInstance(getActivity()).get() != null && WalletStorage.getInstance(getActivity()).get().size() != 0) {
            String ethWalletAddress = WalletStorage.getInstance(getActivity()).get().get(SpUtil.getInt(getActivity(), ConstantValue.currentEthWallet, 0)).getPubKey();
            Map<String, String> infoMap = new HashMap<>();
            infoMap.put("apiKey", "freekey");
            mPresenter.getETHWalletDetail(ethWalletAddress, infoMap);
        }
    }

    @Override
    public void onGetRawError() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void setBnbValue(String value) {
        tvGas.setText(value);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            fetchData();
        }
    }

    public void fetchData() {
        List<Wallet> walletList = AppConfig.getInstance().getDaoSession().getWalletDao().queryBuilder().list();
        if (walletList != null && walletList.size() != 0) {
            this.wallet = walletList.get(SpUtil.getInt(getActivity(), ConstantValue.currentWallet, 0));
            preGetBalance(walletList.get(SpUtil.getInt(getActivity(), ConstantValue.currentWallet, 0)));
        } else {
            Intent intent = new Intent(getActivity(), NoWalletActivity.class);
            intent.putExtra("flag", "nowallet");
            startActivity(intent);
        }
    }

    @OnClick({R.id.iv_avater, R.id.iv_wallet, R.id.tv_title, R.id.tv_qlc, R.id.tv_gas, R.id.iv_qlc})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_avater:
                Intent intent = new Intent(getActivity(), ProfilePictureActivity.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.iv_wallet:
                break;
            case R.id.tv_title:
//                startActivity(WalletDetailActivity.class);
                break;
            case R.id.tv_gas:
                startActivity(new Intent(getActivity(), EthWalletActivity.class));
                break;
            case R.id.tv_qlc:
                View tipvView;
                RxPopupView.Builder builder;
                if (mRxPopupViewManager == null) {
                    mRxPopupViewManager = new RxPopupViewManager(this);
                    mRxPopupViewManager.findAndDismiss(tvQlc);
                }
                builder = new RxPopupView.Builder(getActivity(), tvQlc, parentQlc, tvQlc.getText().toString(), RxPopupView.POSITION_ABOVE);
                builder.setBackgroundColor(getResources().getColor(R.color.white));
                builder.setTextColor(getResources().getColor(R.color.mainColor));
                builder.setGravity(RxPopupView.GRAVITY_CENTER);
                builder.setTextSize(12);
                tipvView = mRxPopupViewManager.show(builder.build());
                tipvView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRxPopupViewManager.dismiss(tipvView, true);
                    }
                }, 3000);
                break;
            case R.id.iv_qlc:
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                break;
            default:
                break;
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == -1) {
            File dataFile = new File(Environment.getExternalStorageDirectory() + "/Qlink/image/" + SpUtil.getString(getActivity(), ConstantValue.myAvaterUpdateTime, "") + ".jpg", "");
            Glide.with(getActivity())
                    .load(dataFile)
                    .apply(AppConfig.getInstance().options)
                    .into(ivAvater);
        }
    }

    @Override
    public void onTipDismissed(View view, int anchorViewId, boolean byUser) {

    }
}