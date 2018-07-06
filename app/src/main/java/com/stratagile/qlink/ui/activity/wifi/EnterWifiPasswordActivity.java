package com.stratagile.qlink.ui.activity.wifi;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.stratagile.qlink.utils.UIUtils;
import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.db.WifiEntity;
import com.stratagile.qlink.ui.activity.wifi.component.DaggerEnterWifiPasswordComponent;
import com.stratagile.qlink.ui.activity.wifi.contract.EnterWifiPasswordContract;
import com.stratagile.qlink.ui.activity.wifi.module.EnterWifiPasswordModule;
import com.stratagile.qlink.ui.activity.wifi.presenter.EnterWifiPasswordPresenter;
import com.stratagile.qlink.utils.ToastUtil;
import com.stratagile.qlink.view.TextSpaceView;

import java.lang.reflect.Method;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wifi
 * @Description: $description
 * @date 2018/02/01 13:26:40
 */

public class EnterWifiPasswordActivity extends BaseActivity implements EnterWifiPasswordContract.View {

    @Inject
    EnterWifiPasswordPresenter mPresenter;
//    @BindView(R.id.view)
//    View view;
//    @BindView(R.id.tv_title)
//    TextView tvTitle;
//    @BindView(R.id.iv_avater)
//    ImageView ivAvater;
    @BindView(R.id.et_ssid)
    EditText etSsid;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.bt_back)
    Button btBack;
    @BindView(R.id.bt_confirm)
    Button btConfirm;
    private WifiEntity wifiEntity;
    private WifiManager wifiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        needFront = true;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_enter_wifi_password);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {
//        tvTitle.setText("wifi".toUpperCase());
        setTitle(getString(R.string.wifi).toUpperCase());
//        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(UIUtils.getDisplayWidth(this), UIUtils.getStatusBarHeight(this));
//        view.setLayoutParams(rlp);
        wifiEntity = getIntent().getParcelableExtra("wifientity");
        //capabilities='[WPA2-PSK-CCMP][ESS]   连不上。。
        //              [WPA2-PSK-CCMP][ESS]   5g能够脸上
        KLog.i(wifiEntity.toString());
        etSsid.setText(wifiEntity.getSsid());
        etPassword.requestFocus();
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
//        forgetAllQlinkWifi(wifiEntity.getSsid());
        int configId = isWifiConfig(wifiEntity.getSsid());
        if (configId != -1) {
            showProgressDialog();
            wifiManager.disconnect();
            wifiManager.enableNetwork(configId, true);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ConnectivityManager connManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                    if (connManager != null) {
                        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                        KLog.i( wifiManager.getConnectionInfo().getSSID().replace("\"", ""));
                        KLog.i(wifiEntity.getSsid());
                        if (mWifi.isConnected() && wifiManager.getConnectionInfo().getSSID().replace("\"", "").equals(wifiEntity.getSsid())) {
                            KLog.i("wifi通过qlink连接成功~~~");
                            ToastUtil.displayShortToast(getString(R.string.connect_to_wifi_success,wifiEntity.getSsid()));
                            connectSuccess();
                        } else {
                            ToastUtil.displayShortToast(getString(R.string.connect_to_wifi_failure_password_error,wifiEntity.getSsid()));
                            connectOver();
                        }
                    } else {
                        ToastUtil.displayShortToast(getString(R.string.connect_to_wifi_failure_system_error,wifiEntity.getSsid()));
                        connectOver();
                    }
                }
            }, 6000);
        }
    }

    @Override
    protected void setupActivityComponent() {
        DaggerEnterWifiPasswordComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .enterWifiPasswordModule(new EnterWifiPasswordModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(EnterWifiPasswordContract.EnterWifiPasswordContractPresenter presenter) {
        mPresenter = (EnterWifiPasswordPresenter) presenter;
    }

    /**
     * 删除/忘记一个wifi（也就是通常的不保存）
     *
     * @param ssid 要忘记网络名成
     * @return 执行结果
     */
    public boolean forgetAllQlinkWifi(String ssid) {
        for (WifiConfiguration c : getConfigWifiList()) {
            wifiManager.removeNetwork(c.networkId);
        }
        return false;
    }

    /**
     * 获取已经保存的wifi列表
     */
    public List<WifiConfiguration> getConfigWifiList() {
        List<WifiConfiguration> configurations = wifiManager.getConfiguredNetworks();
        return configurations;
    }

    /**
     * 判断该wifi是否已经保存
     *
     * @return 返回-1表示没保存，已经保存返回网络id
     */
    public int isWifiConfig(String ssid) {
        if (ssid == null) {
            return -1;
        }
        List<WifiConfiguration> lists = getConfigWifiList();
        if (lists == null) {
            return -1;
        }
        for (WifiConfiguration c : lists) {
            if (c.SSID.equals("\"" + ssid + "\"")) {
                return c.networkId;
            }
        }
        return -1;
    }


    private void connectToWifi() {
        if (wifiEntity.getCapabilities().contains("WEP")) {
            if (etPassword.getText().toString().trim().length() < 8) {
                ToastUtil.displayShortToast(getString(R.string.password_error));
                return;
            }
        } else if (wifiEntity.getCapabilities().contains("WPA")) {
            if (etPassword.getText().toString().trim().length() < 8) {
                ToastUtil.displayShortToast(getString(R.string.password_error));
                return;
            }
        } else {

        }
        setWiFiInfoAndConnect();
    }

    private void disAbleAllWiFi() {
        List<WifiConfiguration> lists = getConfigWifiList();
        if(lists != null)
        {
            for (WifiConfiguration c : lists) {
                KLog.i(c.SSID + "   " + wifiManager.disableNetwork(c.networkId));
            }
        }
    }

    /**
     * 通过反射出不同版本的connect方法来连接Wifi
     *
     * @author jiangping.li
     * @param netId
     * @return
     * @since MT 1.0
     *
     *
     */
    @Deprecated
    private Method connectWifiByReflectMethod(int netId) {
        Method connectMethod = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            KLog.i("connectWifiByReflectMethod road 1");
            // 反射方法： connect(int, listener) , 4.2 <= phone's android version
            for (Method methodSub : wifiManager.getClass().getDeclaredMethods()) {
                if ("connect".equalsIgnoreCase(methodSub.getName())) {
                    Class<?>[] types = methodSub.getParameterTypes();
                    if (types != null && types.length > 0) {
                        if ("int".equalsIgnoreCase(types[0].getName())) {
                            connectMethod = methodSub;
                        }
                    }
                }
            }
            if (connectMethod != null) {
                try {
                    KLog.i("反射连接wifi。。。。");
                    connectMethod.invoke(wifiManager, netId, null);
                } catch (Exception e) {
                    e.printStackTrace();
                    KLog.i( "connectWifiByReflectMethod Android " + Build.VERSION.SDK_INT + " error!");
                    return null;
                }
            }
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN) {
            // 反射方法: connect(Channel c, int networkId, ActionListener listener)
            // 暂时不处理4.1的情况 , 4.1 == phone's android version
            KLog.i("connectWifiByReflectMethod road 2");
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            KLog.i( "connectWifiByReflectMethod road 3");
            // 反射方法：connectNetwork(int networkId) ,
            // 4.0 <= phone's android version < 4.1
            for (Method methodSub : wifiManager.getClass()
                    .getDeclaredMethods()) {
                if ("connectNetwork".equalsIgnoreCase(methodSub.getName())) {
                    Class<?>[] types = methodSub.getParameterTypes();
                    if (types != null && types.length > 0) {
                        if ("int".equalsIgnoreCase(types[0].getName())) {
                            connectMethod = methodSub;
                        }
                    }
                }
            }
            if (connectMethod != null) {
                try {
                    connectMethod.invoke(wifiManager, netId);
                } catch (Exception e) {
                    e.printStackTrace();
                    KLog.i("connectWifiByReflectMethod Android "
                            + Build.VERSION.SDK_INT + " error!");
                    return null;
                }
            }
        } else {
            // < android 4.0
            return null;
        }
        return connectMethod;
    }


    private void setWiFiInfoAndConnect() {
        disAbleAllWiFi();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etPassword.getWindowToken(), 0);
        showProgressDialog();
        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = "\"" + wifiEntity.getSsid() + "\"";
        if (wifiEntity.getCapabilities().contains("WEP")) {
            conf.wepKeys[0] = "\"" + etPassword.getText().toString().trim() + "\"";
            conf.wepTxKeyIndex = 0;
            conf.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            conf.wepTxKeyIndex = 0;
        } else if (wifiEntity.getCapabilities().contains("WPA")) {
            conf.preSharedKey = "\"" + etPassword.getText().toString().trim() + "\"";
            conf.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            conf.status = WifiConfiguration.Status.ENABLED;
        } else {
            //conf.wepKeys[0] = "";
            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            //conf.wepTxKeyIndex = 0;
        }
        if (wifiManager != null) {
            if (!wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(true);
            }
            for (WifiConfiguration existingConfig : getConfigWifiList()) {//先删除所有的配置，android6.0删除不了
                if (existingConfig.SSID.equals(conf.SSID)) {
                    wifiManager.removeNetwork(existingConfig.networkId);
                    wifiManager.saveConfiguration();
                }

            }
            int networkid = wifiManager.addNetwork(conf);
            KLog.i("添加网络：：" + networkid);
            if (networkid != -1) {
                //成为app配置的wifi
                wifiManager.disconnect();
                wifiManager.enableNetwork(networkid, true);
//                wifiManager.reconnect();
                KLog.i(conf.toString());
                KLog.i(wifiEntity.toString());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ConnectivityManager connManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                        if (connManager != null) {
                            NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                            KLog.i( wifiManager.getConnectionInfo().getSSID().replace("\"", ""));
                            KLog.i(wifiEntity.getSsid());
                            if (mWifi.isConnected() && wifiManager.getConnectionInfo().getSSID().replace("\"", "").equals(wifiEntity.getSsid())) {
                                KLog.i("wifi通过qlink连接成功~~~");
                                ToastUtil.displayShortToast(getString(R.string.connect_to_wifi_success,wifiEntity.getSsid()));
                                connectSuccess();
                            } else {
                                ToastUtil.displayShortToast(getString(R.string.connect_to_wifi_failure_password_error,wifiEntity.getSsid()));
                                connectOver();
                            }
                        } else {
                            ToastUtil.displayShortToast(getString(R.string.connect_to_wifi_failure_system_error,wifiEntity.getSsid()));
                            connectOver();
                        }
                    }
                }, 6000);
            } else {
                //系统配置的wifi，其实这一步是不会走的，在initdata方法里已经判断过了
                wifiManager.disconnect();
                wifiManager.enableNetwork(isWifiConfig(wifiEntity.getSsid()), true);
                wifiManager.reconnect();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ConnectivityManager connManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                        if (connManager != null) {
                            NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                            KLog.i( wifiManager.getConnectionInfo().getSSID().replace("\"", ""));
                            KLog.i(wifiEntity.getSsid());
                            if (mWifi.isConnected() && wifiManager.getConnectionInfo().getSSID().replace("\"", "").equals(wifiEntity.getSsid())) {
                                KLog.i("wifi通过qlink连接成功~~~");
                                ToastUtil.displayShortToast(getString(R.string.connect_to_wifi_success,wifiEntity.getSsid()));
                                connectSuccess();
                            } else {
                                ToastUtil.displayShortToast(getString(R.string.connect_to_wifi_failure_password_error,wifiEntity.getSsid()));
                                connectOver();
                            }
                        } else {
                            ToastUtil.displayShortToast(getString(R.string.connect_to_wifi_failure_system_error,wifiEntity.getSsid()));
                            connectOver();
                        }
                    }
                }, 6000);
            }
        }
    }

    private void connectOver() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                closeProgressDialog();
            }
        });
    }
    private void connectSuccess() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                closeProgressDialog();
            }
        });
        finish();
    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void closeProgressDialog() {
        progressDialog.hide();
    }

    @OnClick({R.id.bt_back, R.id.bt_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_back:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(etPassword.getWindowToken(), 0);
                finish();
                break;
            case R.id.bt_confirm:
                connectToWifi();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}