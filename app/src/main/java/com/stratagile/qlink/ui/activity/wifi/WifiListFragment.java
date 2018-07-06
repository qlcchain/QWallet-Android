package com.stratagile.qlink.ui.activity.wifi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alipay.android.phone.mrpc.core.NetworkUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.entity.eventbus.ChangeWalletNeedRefesh;
import com.stratagile.qlink.entity.im.Message;
import com.stratagile.qlink.qlinkcom;
import com.stratagile.qlink.ui.activity.im.ConversationActivity;
import com.stratagile.qlink.ui.activity.seize.SeizeActivity;
import com.stratagile.qlink.utils.LogUtil;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.UIUtils;
import com.socks.library.KLog;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.R;
import com.stratagile.qlink.db.Wallet;
import com.stratagile.qlink.db.WifiEntity;
import com.stratagile.qlink.entity.qlink.QlinkEntity;
import com.stratagile.qlink.ui.activity.base.MyBaseFragment;
import com.stratagile.qlink.ui.activity.wallet.CreateWalletPasswordActivity;
import com.stratagile.qlink.ui.activity.wallet.NoWalletActivity;
import com.stratagile.qlink.ui.activity.wallet.VerifyWalletPasswordActivity;
import com.stratagile.qlink.ui.adapter.SpaceItemDecoration;
import com.stratagile.qlink.ui.adapter.wifi.WifiListAdapter;
import com.stratagile.qlink.ui.activity.wifi.component.DaggerWifiListComponent;
import com.stratagile.qlink.ui.activity.wifi.contract.WifiListContract;
import com.stratagile.qlink.ui.activity.wifi.module.WifiListModule;
import com.stratagile.qlink.ui.activity.wifi.presenter.WifiListPresenter;
import com.stratagile.qlink.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wifi
 * @Description: $description
 * @date 2018/01/09 14:02:09
 */

public class WifiListFragment extends MyBaseFragment implements WifiListContract.View, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @Inject
    WifiListPresenter mPresenter;
    @Inject
    WifiListAdapter wifiListAdapter;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    private String mType;
    private int mNextPage;
    private static final int ONE_PAGE_SIZE = 5;
    private int total;
    private static final String ARG_TYPE = "arg_type";
    private boolean onRefreshing = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.normal_list_layout, null);
        ButterKnife.bind(this, view);
        Bundle mBundle = getArguments();
        refreshLayout.setOnRefreshListener(this);
        recyclerView.getItemAnimator().setChangeDuration(0);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.mainColor));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        KLog.i("density的大小为:" + getResources().getDisplayMetrics().density);
        KLog.i(getResources().getDisplayMetrics().widthPixels);
        KLog.i(getResources().getDisplayMetrics().heightPixels);
        KLog.i(getResources().getDimension(R.dimen.x1280));
        KLog.i("测试dimens使用的是哪个:" + getResources().getDimension(R.dimen.fundspace));
        recyclerView.addItemDecoration(new SpaceItemDecoration(UIUtils.dip2px(10, getActivity())));
        wifiListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                if (SpUtil.getString(getContext(), ConstantValue.walletPassWord, "").equals("") && SpUtil.getString(getContext(), ConstantValue.fingerPassWord, "").equals("")) {
                    Intent intent = new Intent(getActivity(), CreateWalletPasswordActivity.class);
                    intent.putExtra("position", i + "");
                    startActivityForResult(intent, 3);
                    return;
                }
                if (ConstantValue.isShouldShowVertifyPassword) {
                    Intent intent = new Intent(getActivity(), VerifyWalletPasswordActivity.class);
                    intent.putExtra("position", i + "");
                    startActivityForResult(intent, 3);
                    return;
                }
                List<Wallet> walletList = AppConfig.getInstance().getDaoSession().getWalletDao().loadAll();
                if (walletList == null || walletList.size() == 0) {
                    Intent intent = new Intent(getActivity(), NoWalletActivity.class);
                    intent.putExtra("flag", "nowallet");
                    intent.putExtra("position", i + "");
                    startActivityForResult(intent, 3);
                    return;
                }
                if (NetworkUtils.isNetworkAvailable(getActivity())) {
                    String ownP2Pid = SpUtil.getString(getActivity(), ConstantValue.P2PID, "");
                    if (ownP2Pid != null && !ownP2Pid.isEmpty() && ConstantValue.isConnectToP2p) {
                        //qlink联通到节点成功
                        if (wifiListAdapter.getData().get(i).getOwnerP2PId() == null || wifiListAdapter.getData().get(i).getOwnerP2PId().equals("")) {
                            //该WiFi还没有注册为qlink  WiFi
                            Intent intent = new Intent(getActivity(), EnterWifiPasswordActivity.class);
                            intent.putExtra("wifientity", wifiListAdapter.getItem(i));
                            startActivity(intent);
//                           ToastUtil.displayShortToast("Not a registered Qlink wifi");
                        } else {
                            //WiFi已经注册，可以连接了
                            if (wifiListAdapter.getData().get(i).getOwnerP2PId().equals(SpUtil.getString(getActivity(), ConstantValue.P2PID, ""))) {
                                //该WiFi是我自己注册的
                                Intent intent = new Intent(getActivity(), RegisterWifiActivity.class);
                                intent.putExtra("wifiInfo", wifiListAdapter.getData().get(i));
                                startActivityForResult(intent, 0);
                                getActivity().overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
                            } else {
                                //该WiFi不是我自己注册的，需要连接
                                if (wifiListAdapter.getData().get(i).isOnline()) {
                                    //好友在线，可以连接
                                    WifiManager mainWifiObj = (WifiManager) AppConfig.getInstance().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                                    if (wifiListAdapter.getItem(i).getIsConnected()) {
                                        //我已经连接成功了，
                                        Intent intent = new Intent(getActivity(), ConnectWifiSuccessActivity.class);
                                        intent.putExtra("wifientity", wifiListAdapter.getData().get(i));
                                        intent.putExtra("ssid", wifiListAdapter.getData().get(i).getSsid());
                                        startActivity(intent);
                                    } else {
                                        //未连接成功，需要连接。
                                        Intent intent = new Intent(getActivity(), ConnectWifiConfirmActivity.class);
                                        intent.putExtra("wifientity", wifiListAdapter.getData().get(i));
                                        startActivity(intent);
                                        getActivity().overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
                                    }
                                } else {
                                    //好友不在线，不能连接
                                    ToastUtil.displayShortToast(getString(R.string.Try_to_connect_to_a_WiFi_with_green_WiFi_icon));
                                }
                            }
                        }
                    } else {
                        //qlink还没有联通到节点
                        Toast.makeText(getActivity(), getString(R.string.waitP2pNetWork), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    //网络错误
                    ToastUtil.displayShortToast(getString(R.string.Network_problem_Please_try_again));
                }
            }
        });
        wifiListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.iv_wifi_status:
                        if(ConstantValue.isCloseRegisterAssetsInMain && SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false))
                        {
                            return;
                        }
                        if (!wifiListAdapter.getItem(position).getOwnerP2PId().equals(SpUtil.getString(getActivity(), ConstantValue.P2PID, ""))) {
                            Intent intent = new Intent(getActivity(), SeizeActivity.class);
                            intent.putExtra("wifiEntity", wifiListAdapter.getItem(position));
                            intent.putExtra("seizeType", 1);
                            startActivity(intent);
                           /* if (wifiListAdapter.getItem(position).getIsConnected()) {
                                showWiFiDisconnectDialog(wifiListAdapter.getItem(position));
                            } else {
                                wifiListAdapter.getViewByPosition(recyclerView, position, R.id.adapter_wifi_LinearLayoutContainer).performClick();
                            }*/
                        } else {
                            wifiListAdapter.getViewByPosition(recyclerView, position, R.id.adapter_wifi_LinearLayoutContainer).performClick();
                        }
                        break;
                    case R.id.freind_avater:
                        if (wifiListAdapter.getItem(position).isOnline()) {
                            Intent intent = new Intent(getActivity(), ConversationActivity.class);
                            intent.putExtra("wifiEntity", wifiListAdapter.getItem(position));
                            intent.putExtra("assetType", "0");
                            startActivity(intent);
                        }
                        break;
                    default:
                        break;
                }
            }
        });
        recyclerView.setAdapter(wifiListAdapter);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onRefreshing = true;
                EventBus.getDefault().post("refreshWifi");
                ArrayList<String> p2pIdList = new ArrayList<>();
                p2pIdList.add(SpUtil.getString(getActivity(), ConstantValue.P2PID, ""));
                for (int i = 0; i < wifiListAdapter.getItemCount(); i++) {
                    if (!wifiListAdapter.getItem(i).isOnline()) {
                        if (p2pIdList.contains(wifiListAdapter.getItem(i).getOwnerP2PId())) {

                        } else {
                            //
                            if (qlinkcom.GetP2PConnectionStatus() > 0) {
                                qlinkcom.AddFriend(wifiListAdapter.getItem(i).getOwnerP2PId());
                                KLog.i("重新添加好友 " + wifiListAdapter.getItem(i).getOwnerP2PId());
                                LogUtil.addLog("重新添加好友 " + wifiListAdapter.getItem(i).getOwnerP2PId(), getClass().getSimpleName());
                                p2pIdList.add(wifiListAdapter.getItem(i).getOwnerP2PId());
                            }
                        }
                    }
                }
            }
        });
        return view;
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ChangeToTestWallet(ChangeWalletNeedRefesh changeWalletNeedRefesh) {
        onRefreshing = true;
        EventBus.getDefault().post("refreshWifi");
        ArrayList<String> p2pIdList = new ArrayList<>();
        p2pIdList.add(SpUtil.getString(getActivity(), ConstantValue.P2PID, ""));
        for (int i = 0; i < wifiListAdapter.getItemCount(); i++) {
            if (!wifiListAdapter.getItem(i).isOnline()) {
                if (p2pIdList.contains(wifiListAdapter.getItem(i).getOwnerP2PId())) {

                } else {
                    //
                    if (qlinkcom.GetP2PConnectionStatus() > 0) {
                        qlinkcom.AddFriend(wifiListAdapter.getItem(i).getOwnerP2PId());
                        KLog.i("重新添加好友 " + wifiListAdapter.getItem(i).getOwnerP2PId());
                        LogUtil.addLog("重新添加好友 " + wifiListAdapter.getItem(i).getOwnerP2PId(), getClass().getSimpleName());
                        p2pIdList.add(wifiListAdapter.getItem(i).getOwnerP2PId());
                    }
                }
            }
        }
    }
    /**
     * 显示vpn注册时需要扣费的dialog
     */
    private void showWiFiDisconnectDialog(WifiEntity wifiEntity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = View.inflate(getActivity(), R.layout.dialog_layout, null);
        builder.setView(view);
        builder.setCancelable(true);
        TextView title = (TextView) view.findViewById(R.id.title);//设置标题
        TextView tvContent = (TextView) view.findViewById(R.id.tv_content);//输入内容
        Button btn_cancel = (Button) view.findViewById(R.id.btn_left);//取消按钮
        Button btn_comfirm = (Button) view.findViewById(R.id.btn_right);//确定按钮
        title.setText(R.string.Disconnect_WiFi);
        tvContent.setText(R.string.Are_you_sure_to_disconnect);
        //取消或确定按钮监听事件处l
        AlertDialog dialog = builder.create();
        btn_cancel.setText(getResources().getString(R.string.cancel).toLowerCase());
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                disconnectWifi(wifiEntity);
            }
        });
        dialog.show();
    }

    private void disconnectWifi(WifiEntity wifiEntity) {
        WifiManager mainWifiObj = (WifiManager) AppConfig.getInstance().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        for (WifiConfiguration c : mainWifiObj.getConfiguredNetworks()) {
            if (c.SSID.equals("\"" + wifiEntity.getSsid() + "\"")) {
                mainWifiObj.removeNetwork(c.networkId);
                wifiEntity.setIsConnected(false);
                AppConfig.getInstance().getDaoSession().getWifiEntityDao().update(wifiEntity);
                wifiListAdapter.notifyDataSetChanged();
                return;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3 && resultCode == RESULT_OK) {
//            wifiListAdapter.getViewByPosition(recyclerView, Integer.parseInt(data.getStringExtra("position")), R.id.adapter_wifi_LinearLayoutContainer).performClick();
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
    public void fetchData() {
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                try {
                    if (getString(R.string.nearby).equals(mType)) {
                        initDataFromLocal();
                        loadDataFromServer(true);
                    }
                }catch (IllegalStateException e)
                {

                }

            }
        });
    }

    public void loadDataFromServer(boolean showRefresh) {
        refreshLayout.setRefreshing(showRefresh);
        if (showRefresh) {
            mNextPage = 1;
        }
        mPresenter.getWifiListFromServer(mType, mNextPage, ONE_PAGE_SIZE);
    }

    public static WifiListFragment newInstance(String param) {
        WifiListFragment fragment = new WifiListFragment();
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
    public void onLoadMoreRequested() {
//        if (wifiListAdapter.getData().size() < ONE_PAGE_SIZE || wifiListAdapter.getData().size() >= total) {
//            wifiListAdapter.loadMoreEnd(false);
//            return;
//        }
//        mNextPage ++;
//        mPresenter.getWifiListFromServer(mType, mNextPage, ONE_PAGE_SIZE);
    }


    @Override
    protected void setupFragmentComponent() {
        DaggerWifiListComponent
                .builder()
                .appComponent(((AppConfig) getActivity().getApplication()).getApplicationComponent())
                .wifiListModule(new WifiListModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(WifiListContract.WifiListContractPresenter presenter) {
        mPresenter = (WifiListPresenter) presenter;
    }

    @Override
    protected void initDataFromLocal() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setListData(List<WifiEntity> wifiInfo) {
        if (wifiInfo == null || wifiInfo.size() == 0) {
            List<WifiEntity> list = wifiListAdapter.getData();
            for (int i = 0; i < list.size(); i++) {
                for (WifiEntity wifiEntity1 : AppConfig.getInstance().getDaoSession().getWifiEntityDao().loadAll()) {
                    if (list.get(i).getSsid().equals(wifiEntity1.getSsid())) {
                        list.set(i, wifiEntity1);
                        break;
                    }
                }
            }
            Collections.sort(list);
            List<WifiEntity> tempwifi = new ArrayList<>();
            if (mType.equals("REGISTERED")) {
                for (WifiEntity wifiEntity : list) {
                    //if (wifiEntity.isRegiste()) {//注册和未注册的放到一个列表
                        tempwifi.add(wifiEntity);
                    //}
                }
            } else {
                for (WifiEntity wifiEntity : list) {
                    if (!wifiEntity.isRegiste()) {
                        tempwifi.add(wifiEntity);
                    }
                }
            }
            list.clear();
            list.addAll(tempwifi);
            wifiListAdapter.setNewData(list);
            return;
        }
        KLog.i("更新数据" + wifiInfo.size());
        ArrayList<WifiEntity> list = new ArrayList<>();
        list.addAll(wifiInfo);
        Collections.sort(list);
        List<WifiEntity> tempwifi = new ArrayList<>();
        if (mType.equals("REGISTERED")) {
            for (WifiEntity wifiEntity : list) {
                if (wifiEntity.isRegiste()) {
                    if (wifiEntity.getOwnerP2PId().equals(SpUtil.getString(getActivity(), ConstantValue.P2PID, ""))) {
                        //修正我的wifi资产的状态
                        if (qlinkcom.GetP2PConnectionStatus() > 0) {
                            //在线
                            if (!wifiEntity.isOnline()) {
                                wifiEntity.setOnline(true);
                                AppConfig.getInstance().getDaoSession().getWifiEntityDao().update(wifiEntity);
                            }
                        } else {
                            if (wifiEntity.isOnline()) {
                                wifiEntity.setOnline(false);
                                AppConfig.getInstance().getDaoSession().getWifiEntityDao().update(wifiEntity);
                            }
                        }
                    }
                    tempwifi.add(wifiEntity);
                }
            }
            for (WifiEntity wifiEntity : list) {//注册和未注册的放到一个列表
                if (!wifiEntity.isRegiste()) {
                    tempwifi.add(wifiEntity);
                }
            }
        } else {
            for (WifiEntity wifiEntity : list) {
                if (!wifiEntity.isRegiste()) {
                    tempwifi.add(wifiEntity);
                }
            }
        }
        list.clear();
        list.addAll(tempwifi);
        wifiListAdapter.setNewData(list);
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 收到聊天消息，加上未读提醒
     * @param message
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receivedMessage(Message message) {
        if (message.getAssetType() == 0) {
            for (int i = 0; i < wifiListAdapter.getData().size(); i++) {
                if (wifiListAdapter.getItem(i).getSsid().equals(message.getAssetName())) {
                    wifiListAdapter.getItem(i).setUnReadMessageCount(1);
                    if (wifiListAdapter.getItem(i).getGroupNum() == -1) {
                        wifiListAdapter.getItem(i).setGroupNum(message.getGroupNum());
                        AppConfig.getInstance().getDaoSession().getWifiEntityDao().update(wifiListAdapter.getItem(i));
                    }
                    wifiListAdapter.notifyItemChanged(i);
                    return;
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getDataFromQlink(QlinkEntity qlinkEntity) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void clearVpnMessageCount(WifiEntity wifiEntity) {
        for (int i = 0; i < wifiListAdapter.getData().size(); i++) {
            if (wifiListAdapter.getItem(i).getSsid().equals(wifiEntity.getSsid())) {
                wifiListAdapter.getItem(i).setUnReadMessageCount(0);
                wifiListAdapter.notifyItemChanged(i);
                return;
            }
        }
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