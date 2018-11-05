package com.stratagile.qlink.ui.activity.wifi;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.android.phone.mrpc.core.NetworkUtils;
import com.stratagile.qlink.constant.BroadCastAction;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.entity.eventbus.P2pBack;
import com.stratagile.qlink.ui.activity.main.LogActivity;
import com.stratagile.qlink.utils.FileUtil;
import com.stratagile.qlink.utils.LogUtil;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.UIUtils;
import com.stratagile.qlink.view.NoScrollViewPager;
import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseFragment;
import com.stratagile.qlink.db.Wallet;
import com.stratagile.qlink.db.WifiEntity;
import com.stratagile.qlink.qlink.P2PCallBack;
import com.stratagile.qlink.qlinkcom;
import com.stratagile.qlink.ui.activity.wallet.CreateWalletPasswordActivity;
import com.stratagile.qlink.ui.activity.wallet.NoWalletActivity;
import com.stratagile.qlink.ui.activity.wallet.VerifyWalletPasswordActivity;
import com.stratagile.qlink.ui.activity.wifi.component.DaggerWifiComponent;
import com.stratagile.qlink.ui.activity.wifi.contract.WifiContract;
import com.stratagile.qlink.ui.activity.wifi.module.WifiModule;
import com.stratagile.qlink.ui.activity.wifi.presenter.WifiPresenter;
import com.stratagile.qlink.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wifi
 * @Description: $description
 * @date 2018/01/09 13:46:43
 */

public class WifiFragment extends BaseFragment implements WifiContract.View {

    @Inject
    WifiPresenter mPresenter;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.fragment_wifi_AppCompactImageViewQLinkReg)
    AppCompatImageView fragmentWifiAppCompactImageViewQLinkReg;
    @BindView(R.id.fragment_wifi_TextViewInfo)
    TextView fragmentWifiTextViewInfo;
    @BindView(R.id.viewPager)
    NoScrollViewPager viewPager;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private ArrayList<String> titles;

    private WifiManager mainWifiObj;

    private WiFiBroadcastReceiver wiFiBroadcastReceiver;

    private long lastUpdateTime = 0;
    private boolean isRefrash = false;
    private final long intervalTime = 1000 * 60 * 5;

    public static final int START_NO_WALLLET = 0;
    public static final int START_CREATE_PASSWORD = 2;
    public static final int START_VERTIFY_PASSWORD = 3;
    private ConnectivityManager cManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wifi, null);
        ButterKnife.bind(this, view);
        Bundle mBundle = getArguments();
        titles = new ArrayList<>();
        titles.add("REGISTERED");
        //titles.add("UNREGISTERED");//注册和未注册的放到一个列表
        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return WifiListFragment.newInstance(titles.get(position));
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
        mainWifiObj = (WifiManager) AppConfig.getInstance().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        mainWifiObj.startScan();
        initData();
        getActivity().getMenuInflater().inflate(R.menu.registe_wifi, toolbar.getMenu());
        title.setText(getResources().getString(R.string.AvailableWiFi));
        title.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                startActivity(new Intent(getActivity(), LogActivity.class));
                return true;
            }
        });
        RelativeLayout relativeLayout_root = (RelativeLayout) view.findViewById(R.id.root_rl);
        View spaceView = view.findViewById(R.id.view);
        spaceView.setLayoutParams(new RelativeLayout.LayoutParams(UIUtils.getDisplayWidth(getActivity()), (int) (UIUtils.getStatusBarHeight(getActivity()))));
        toolbar.setTitle("");
        toolbar.setNavigationIcon(null);
        toolbar.getMenu().getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.add:
                        List<Wallet> walletList = AppConfig.getInstance().getDaoSession().getWalletDao().loadAll();
                        if (SpUtil.getString(getContext(), ConstantValue.walletPassWord, "").equals("") && SpUtil.getString(getContext(), ConstantValue.fingerPassWord, "").equals("")) {
                            Intent intent = new Intent(getActivity(), CreateWalletPasswordActivity.class);
                            startActivityForResult(intent, START_CREATE_PASSWORD);
                            return true;
                        }
                        if (walletList == null || walletList.size() == 0) {
                            Intent intent = new Intent(getActivity(), NoWalletActivity.class);
                            intent.putExtra("flag", "nowallet");
                            startActivityForResult(intent, START_NO_WALLLET);
                            return true;
                        }
                        if (ConstantValue.isShouldShowVertifyPassword) {
                            Intent intent = new Intent(getActivity(), VerifyWalletPasswordActivity.class);
                            startActivityForResult(intent, START_VERTIFY_PASSWORD);
                            return true;
                        }
                        if (ConstantValue.connectedWifiInfo != null) {
                            if (ConstantValue.isConnectToP2p) {
                                Intent intent = new Intent(getActivity(), RegisterWifiActivity.class);
                                List<WifiEntity> wifiEntityList = AppConfig.getInstance().getDaoSession().getWifiEntityDao().queryBuilder().list();
                                for (WifiEntity wifiEntity : wifiEntityList) {
                                    if (wifiEntity.getSsid().equals(ConstantValue.connectedWifiInfo.getSSID().replace("\"", ""))) {
                                        intent.putExtra("wifiInfo", wifiEntity);
                                        startActivityForResult(intent, 0);
                                        getActivity().overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
                                        return true;
                                    }
                                }
                                for (WifiEntity wifiEntity : wifiEntityList) {
                                    if (wifiEntity.getOwnerP2PId().equals(SpUtil.getString(getActivity(), ConstantValue.P2PID, ""))) {
                                        intent.putExtra("wifiInfo", wifiEntity);
                                        startActivityForResult(intent, 0);
                                        getActivity().overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
                                        return true;
                                    }
                                }
                            } else {
                                ToastUtil.displayShortToast(getString(R.string.waitP2pNetWork));
                            }
                        } else {
                            ToastUtil.displayShortToast(getString(R.string.pleaseEnableWifi));
                        }
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == START_CREATE_PASSWORD && resultCode == RESULT_OK) {
            toolbar.findViewById(R.id.add).performClick();
            return;
        }
        if (requestCode == START_NO_WALLLET && resultCode == RESULT_OK) {
            toolbar.findViewById(R.id.add).performClick();
            return;
        }
        if (requestCode == START_VERTIFY_PASSWORD && resultCode == RESULT_OK) {
            toolbar.findViewById(R.id.add).performClick();
            return;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //申请获取列表的WiFi权限
        //mPresenter.requestPermission();
        //注册扫描到了WiFi列表的广播
        cManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        wiFiBroadcastReceiver = new WiFiBroadcastReceiver();
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION); // 添加接收网络连接状态改变的Action
        mFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        mFilter.addAction(BroadCastAction.disconnectWiFi);
        mFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        activity.registerReceiver(wiFiBroadcastReceiver, mFilter);
        qlinkcom.getP2PConnnectStatus(new P2PCallBack() {
            @Override
            public void onResult(String result) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (activity !=null && NetworkUtils.isNetworkAvailable(activity)) {
                                SpUtil.putString(activity, ConstantValue.P2PID, result);
                                EventBus.getDefault().post(new P2pBack());
                                String p2pId = result;
                                String saveResult = FileUtil.saveP2pId2Local(result);
                                if ("".equals(saveResult)) {

                                } else {
                                    showp2pIdChangeDialog(result, saveResult);
                                }
                                LogUtil.addLog("自己的p2pid为：" + result, getClass().getSimpleName());
                                if (p2pId != null && !p2pId.isEmpty()) {
                                    ConstantValue.isConnectToP2p = true;
                                    fragmentWifiAppCompactImageViewQLinkReg.setVisibility(View.VISIBLE);
                                    fragmentWifiTextViewInfo.setText(R.string.qlinkRegistedWifi);
                                } else {
                                    fragmentWifiAppCompactImageViewQLinkReg.setVisibility(View.GONE);
                                    fragmentWifiTextViewInfo.setText(R.string.CreateP2pNetwork);
                                }
                            } else {
                                fragmentWifiAppCompactImageViewQLinkReg.setVisibility(View.GONE);
                                fragmentWifiTextViewInfo.setText(R.string.NoNetWork);
                            }
                        }catch (Exception e)
                        {

                        }

                    }
                });
            }
        });
    }

    private void initData() {
        fragmentWifiTextViewInfo.setText(R.string.CreateP2pNetwork);
    }

    private void showp2pIdChangeDialog(String currentP2pId, String lastP2pId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = View.inflate(getActivity(), R.layout.p2pid_change_dialog_layout, null);
        builder.setView(view);
        builder.setCancelable(true);
        TextView title = (TextView) view.findViewById(R.id.title);//设置标题
        TextView tvContent = (TextView) view.findViewById(R.id.tv_content);//输入内容
        Button btn_cancel = (Button) view.findViewById(R.id.btn_left);//取消按钮
        Button btn_comfirm = (Button) view.findViewById(R.id.btn_right);//确定按钮
        tvContent.setText(getString(R.string.currentP2pId) + currentP2pId + "\n\n" + getString(R.string.lastP2pId) + lastP2pId);
        title.setText(R.string.P2pId_is_changed);
        //取消或确定按钮监听事件处l
        AlertDialog dialog = builder.create();
        btn_cancel.setText(getString(R.string.cancel).toLowerCase());
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
            }
        });
        dialog.show();
    }

    public class WiFiBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                KLog.i("扫描出结果了。。");
                if (Calendar.getInstance().getTimeInMillis() - lastUpdateTime < intervalTime && !isRefrash) {
                    KLog.i("间隔太短，拦截");
                    return;
                }
                lastUpdateTime = Calendar.getInstance().getTimeInMillis();
                isRefrash = false;
                String logInfo = "";
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ToastUtil.displayShortToast(getString(R.string.Permission_Denied_please_go_to_setting_open_location_permission));
                    return;
                }
                List<ScanResult> scanResults = mainWifiObj.getScanResults();
                List<ScanResult> tempScanResults = new ArrayList<>();
                if (LogUtil.isShowLog) {
                    logInfo += "wifi列表为：";
                    try {
                        for (int i = 0; i < scanResults.size(); i++) {
                            logInfo += scanResults.get(i).SSID + "  ";
                        }
                    } catch (Exception e) {

                    }
                }
                LogUtil.addLog(logInfo, getClass().getSimpleName());

                for (ScanResult scanResult : scanResults) {
                    boolean isContain = false;
                    for (int i = 0; i < tempScanResults.size(); i++) {
                        if (scanResult.SSID.equals(tempScanResults.get(i).SSID)) {
                            isContain = true;
                            break;
                        }
                    }
                    if (!isContain) {
                        tempScanResults.add(scanResult);
                    }
                }
                ConstantValue.connectedWifiInfo = mainWifiObj.getConnectionInfo();
                KLog.i("保存的已经连接的wifi为》" + ConstantValue.connectedWifiInfo.getSSID());
                mPresenter.handlerWifiChange(tempScanResults);
            }
            String action = intent.getAction();
            if (action.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
                if (mainWifiObj.getWifiState() == WifiManager.WIFI_STATE_DISABLING) {
                    KLog.i("WIFI_STATE_DISABLING");
                }
            } else if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                KLog.i("CONNECTIVITY_ACTION");
                NetworkInfo networkInfo = cManager.getActiveNetworkInfo();
                if (networkInfo == null) {
                    return;
                }
                NetworkInfo.State state = networkInfo.getState();
                KLog.i(state);
                if (state == NetworkInfo.State.CONNECTED) {
                    KLog.i("State.CONNECTED");
                    switch (networkInfo.getType()) {
                        case ConnectivityManager.TYPE_WIFI:
                            KLog.i("变为WiFi网络了");
//                            monitorWifiThread.onThreadResume();
                            KLog.i("系统给的连接上的ssid为：" + mainWifiObj.getConnectionInfo().getSSID());
                            KLog.i("系统给的连接上的mac地址为：" + mainWifiObj.getConnectionInfo().getBSSID());
                            for (WifiEntity wifiEntity : AppConfig.getInstance().getDaoSession().getWifiEntityDao().loadAll()) {
                                if (wifiEntity.getIsConnected()) {
                                    wifiEntity.setIsConnected(false);
                                    AppConfig.getInstance().getDaoSession().getWifiEntityDao().update(wifiEntity);
                                }
                            }

                            for (WifiEntity wifiEntity : AppConfig.getInstance().getDaoSession().getWifiEntityDao().loadAll()) {
                                // && wifiEntity.getMacAdrees().equals(wifiManager.getConnectionInfo().getBSSID())
                                //  && wifiEntity.getMacAdrees().equals(mainWifiObj.getConnectionInfo().getBSSID())
                                if (wifiEntity.getSsid() != null && wifiEntity.getSsid().equals(mainWifiObj.getConnectionInfo().getSSID().replace("\"", ""))&& wifiEntity.getMacAdrees() != null) {
                                    wifiEntity.setIsConnected(true);
                                    AppConfig.getInstance().getDaoSession().getWifiEntityDao().update(wifiEntity);
                                    KLog.i("连接的wifi为：" + wifiEntity.getSsid());
                                    KLog.i("更新wifi列表的状态");
                                    EventBus.getDefault().post(new ArrayList<WifiEntity>());
                                    break;
                                }
                            }
                            break;
                        case ConnectivityManager.TYPE_MOBILE:
                            KLog.i("变为手机网络了");
//                            monitorWifiThread.onThreadWait();
                            for (WifiEntity wifiEntity : AppConfig.getInstance().getDaoSession().getWifiEntityDao().loadAll()) {
                                if (wifiEntity.getIsConnected()) {
                                    wifiEntity.setIsConnected(false);
                                    AppConfig.getInstance().getDaoSession().getWifiEntityDao().update(wifiEntity);
                                }
                            }
                            EventBus.getDefault().post(new ArrayList<WifiEntity>());
                            break;
                        default:
                            break;
                    }
                    // 开始不断获取最近的流量信息，值为0时，跳过

                } else if (state == NetworkInfo.State.DISCONNECTED) {
//                    monitorWifiThread.onThreadWait();
                    for (WifiEntity wifiEntity : AppConfig.getInstance().getDaoSession().getWifiEntityDao().loadAll()) {
                        if (wifiEntity.getIsConnected()) {
                            wifiEntity.setIsConnected(false);
                            AppConfig.getInstance().getDaoSession().getWifiEntityDao().update(wifiEntity);
                        }
                    }
                    EventBus.getDefault().post(new ArrayList<WifiEntity>());
                }
            } else if (action.equals(BroadCastAction.disconnectWiFi)) {
                mainWifiObj.removeNetwork(mainWifiObj.getConnectionInfo().getNetworkId());
                mainWifiObj.saveConfiguration();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mainWifiObj == null) {
            mainWifiObj = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        }
        if (mainWifiObj != null && !mainWifiObj.isWifiEnabled()) {
            mainWifiObj.setWifiEnabled(true);
        }
//        mainWifiObj.startScan();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshWifi(String refreshWifi) {
        if (refreshWifi.equals("refreshWifi")) {
            isRefrash = true;
            mainWifiObj.startScan();
            mPresenter.clearData();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        getActivity().unregisterReceiver(wiFiBroadcastReceiver);
    }

    @Override
    protected void setupFragmentComponent() {
        DaggerWifiComponent
                .builder()
                .appComponent(((AppConfig) getActivity().getApplication()).getApplicationComponent())
                .wifiModule(new WifiModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(WifiContract.WifiContractPresenter presenter) {
        mPresenter = (WifiPresenter) presenter;
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
    public void startUpdateWIfiInfo() {
        KLog.i("开始扫描wifi了。。。。。");
        mainWifiObj.startScan();
    }

    @Override
    public void setPager(int position) {
        viewPager.setCurrentItem(position);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}