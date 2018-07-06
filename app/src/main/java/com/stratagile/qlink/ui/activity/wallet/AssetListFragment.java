package com.stratagile.qlink.ui.activity.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.db.VpnEntity;
import com.stratagile.qlink.db.Wallet;
import com.stratagile.qlink.entity.MyAsset;
import com.stratagile.qlink.ui.activity.vpn.RegisteVpnActivity;
import com.stratagile.qlink.utils.LocalAssetsUtils;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.UIUtils;
import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.db.WifiEntity;
import com.stratagile.qlink.entity.eventbus.AssetRefrash;
import com.stratagile.qlink.ui.activity.base.MyBaseFragment;
import com.stratagile.qlink.ui.activity.wallet.component.DaggerAssetListComponent;
import com.stratagile.qlink.ui.activity.wallet.contract.AssetListContract;
import com.stratagile.qlink.ui.activity.wallet.module.AssetListModule;
import com.stratagile.qlink.ui.activity.wallet.presenter.AssetListPresenter;
import com.stratagile.qlink.ui.activity.wifi.RegisterWifiActivity;
import com.stratagile.qlink.ui.adapter.SpaceItemDecoration;
import com.stratagile.qlink.ui.adapter.wallet.AssetListAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
 * @date 2018/01/18 20:42:28
 * 资产列表页面
 */

public class AssetListFragment extends MyBaseFragment implements AssetListContract.View, SwipeRefreshLayout.OnRefreshListener {

    @Inject
    AssetListPresenter mPresenter;
    @Inject
    AssetListAdapter assetListAdapter;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ll_no_content)
    LinearLayout llNoContent;
    @BindView(R.id.rl_asset_tip)
    RelativeLayout rlAssetTip;
    private String mType;
    private int mNextPage;
    private static final int ONE_PAGE_SIZE = 5;
    private int total;
    private static final String ARG_TYPE = "arg_type";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_asset, null);
        ButterKnife.bind(this, view);
        Bundle mBundle = getArguments();
//        refreshLayout.setOnRefreshListener(this);
        recyclerView.setAdapter(assetListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new SpaceItemDecoration(UIUtils.dip2px(10, getActivity())));
        assetListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

            }
        });
        assetListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.setWifi:
                        if (assetListAdapter.getItem(position).getType() == 0) {
                            Intent intent = new Intent(getActivity(), RegisterWifiActivity.class);
                            intent.putExtra("wifiInfo", assetListAdapter.getData().get(position).getWifiEntity());
                            startActivityForResult(intent, 0);
                            getActivity().overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
                        } else if (assetListAdapter.getItem(position).getType() == 1) {
                            Intent intent = new Intent(getActivity(), RegisteVpnActivity.class);
                            KLog.i(assetListAdapter.getItem(position).getVpnEntity().toString());
                            intent.putExtra("flag", "update");
                            intent.putExtra("vpnentity", assetListAdapter.getItem(position).getVpnEntity());
                            startActivityForResult(intent, 0);
                            getActivity().overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
                        }
                        break;
                    default:
                        break;
                }
            }
        });
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        KLog.i(isVisibleToUser);
        KLog.i(isViewInitiated);
        if (isVisibleToUser && isViewInitiated) {
            initDataFromLocal();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        if (getArguments() != null) {
            mType = getArguments().getString(ARG_TYPE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * @see WalletFragment#onCreateView
     * @param assetRefrash
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefrash(AssetRefrash assetRefrash) {
        ArrayList<MyAsset> assetArrayList = getasseList();
        assetListAdapter.setNewData(assetArrayList);
        if (assetListAdapter.getData() == null || assetListAdapter.getData().size() == 0) {
            llNoContent.setVisibility(View.VISIBLE);
            rlAssetTip.setVisibility(View.GONE);
        } else {
            llNoContent.setVisibility(View.GONE);
            rlAssetTip.setVisibility(View.VISIBLE);
        }
        Map<String, Object> ssidMap = new HashMap<>();
        ArrayList<MyAsset> localAssetsList = LocalAssetsUtils.getLocalAssetsList();
        assetArrayList = localAssetsList;
        String[] ssids = new String[assetArrayList.size()];
        for (int i = 0; i < assetArrayList.size(); i++) {
            //0 wifi
            if (assetArrayList.get(i).getType() == 0) {
                ssids[i] = assetArrayList.get(i).getWifiEntity().getSsid();
            } else {
                ssids[i] = assetArrayList.get(i).getVpnEntity().getVpnName();
            }
        }
        ssidMap.put("ssIds", ssids);
        mPresenter.getAssetInfoFromServer(ssidMap);
    }

    @Override
    public void fetchData() {
//        initDataFromLocal();
    }

    @Override
    public void onResume() {
        super.onResume();
        assetListAdapter.setNewData(getasseList());
        if (assetListAdapter.getData() == null || assetListAdapter.getData().size() == 0) {
            llNoContent.setVisibility(View.VISIBLE);
            rlAssetTip.setVisibility(View.GONE);
        } else {
            llNoContent.setVisibility(View.GONE);
            rlAssetTip.setVisibility(View.VISIBLE);
        }
//        refreshLayout.setRefreshing(false);
    }

    public void loadDataFromServer(boolean showRefresh) {
//        refreshLayout.setRefreshing(showRefresh);
        if (showRefresh) {
            mNextPage = 1;
        }
        mPresenter.getAssetListFromServer(mType, mNextPage, ONE_PAGE_SIZE);
    }

    public static AssetListFragment newInstance(String param) {
        AssetListFragment fragment = new AssetListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onRefresh() {
        mNextPage = 1;
        loadDataFromServer(true);
    }

    @Override
    protected void setupFragmentComponent() {
        DaggerAssetListComponent
                .builder()
                .appComponent(((AppConfig) getActivity().getApplication()).getApplicationComponent())
                .assetListModule(new AssetListModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(AssetListContract.AssetListContractPresenter presenter) {
        mPresenter = (AssetListPresenter) presenter;
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
    public void getAssetSuccess() {
        assetListAdapter.setNewData(getasseList());
    }

    private ArrayList<MyAsset> getasseList() {
        List<Wallet> walletList = AppConfig.getInstance().getDaoSession().getWalletDao().loadAll();
        Wallet wallet;
        if (walletList != null && walletList.size() != 0) {
            int currentWallet = SpUtil.getInt(getActivity(), ConstantValue.currentWallet, 0);
            if (currentWallet >= walletList.size()) {
                return null;
            }
            wallet = walletList.get(currentWallet);
        } else {
            return null;
        }
        //读取sd卡资产数据begin
        KLog.i("开始同步本地资产");
        LocalAssetsUtils.updateGreanDaoFromLocal();//以本地资产配置为准
        //读取sd卡资产数据begin
        ArrayList<MyAsset> assetArrayList = new ArrayList<>();
        List<WifiEntity> wifiEntityList = AppConfig.getInstance().getDaoSession().getWifiEntityDao().queryBuilder().list();
        for (WifiEntity wifiEntity : wifiEntityList) {
            if (SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false) && !wifiEntity.getIsInMainWallet()) {//主网
                continue;
            }
            if (!SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false) && wifiEntity.getIsInMainWallet()) {//测试网
                continue;
            }
            if (wifiEntity.getOwnerP2PId() != null && wifiEntity.getOwnerP2PId().equals(SpUtil.getString(getActivity(), ConstantValue.P2PID, ""))) {
                if (wifiEntity.getWalletAddress() != null && wifiEntity.getWalletAddress().equals(wallet.getAddress())) {
                    MyAsset myAsset = new MyAsset();
                    myAsset.setType(0);
                    myAsset.setWifiEntity(wifiEntity);
                    assetArrayList.add(myAsset);
                }
            }
        }
        List<VpnEntity> vpnEntityList = AppConfig.getInstance().getDaoSession().getVpnEntityDao().loadAll();
        for (VpnEntity vpnEntity : vpnEntityList) {
            if (SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false) && !vpnEntity.getIsInMainWallet()) {//主网
                continue;
            }
            if (!SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false) && vpnEntity.getIsInMainWallet()) {//测试网
                continue;
            }
            if (vpnEntity.getP2pId() != null && vpnEntity.getP2pId().equals(SpUtil.getString(getActivity(), ConstantValue.P2PID, ""))) {
                if (vpnEntity.getAddress() != null && vpnEntity.getAddress().equals(wallet.getAddress())) {
                    MyAsset myAsset = new MyAsset();
                    myAsset.setType(1);
                    myAsset.setVpnEntity(vpnEntity);
                    assetArrayList.add(myAsset);
                }
            }
        }
        //更新sd卡资产数据begin
        LocalAssetsUtils.updateList(assetArrayList);
        //更新sd卡资产数据end
        return assetArrayList;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}