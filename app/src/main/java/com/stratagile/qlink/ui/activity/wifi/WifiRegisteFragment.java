package com.stratagile.qlink.ui.activity.wifi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseFragment;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.db.Wallet;
import com.stratagile.qlink.db.WifiEntity;
import com.stratagile.qlink.db.WifiEntityDao;
import com.stratagile.qlink.entity.Balance;
import com.stratagile.qlink.entity.MyAsset;
import com.stratagile.qlink.entity.VertifyVpn;
import com.stratagile.qlink.qlinkcom;
import com.stratagile.qlink.ui.activity.wifi.component.DaggerWifiRegisteComponent;
import com.stratagile.qlink.ui.activity.wifi.contract.WifiRegisteContract;
import com.stratagile.qlink.ui.activity.wifi.module.WifiRegisteModule;
import com.stratagile.qlink.ui.activity.wifi.presenter.WifiRegistePresenter;
import com.stratagile.qlink.utils.LocalAssetsUtils;
import com.stratagile.qlink.api.transaction.SendBackWithTxId;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.StringUitl;
import com.stratagile.qlink.utils.ToastUtil;
import com.stratagile.qlink.api.transaction.TransactionApi;
import com.stratagile.qlink.view.QlinkSeekBar;
import com.stratagile.qlink.view.WiFiInfoView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wifi
 * @Description: $description
 * @date 2018/01/15 11:52:32
 */

public class WifiRegisteFragment extends BaseFragment implements WifiRegisteContract.View {

    @Inject
    WifiRegistePresenter mPresenter;
    @BindView(R.id.info_mac)
    WiFiInfoView infoMac;
    @BindView(R.id.info_ssid)
    WiFiInfoView infoSsid;
    @BindView(R.id.info_location)
    WiFiInfoView infoLocation;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.button1)
    Button button1;
    @BindView(R.id.button2)
    Button button2;

    private FirebaseAnalytics mFirebaseAnalytics;

    static WifiEntity mWifiEntity;
    static String seizeQlc;
    //    @BindView(R.id.connect_seekbar)
//    SeekBar conncetSeekbar;
//    @BindView(R.id.connection_move)
//    TextMoveLayout connectionMove;
//    @BindView(R.id.price_move)
//    TextMoveLayout priceMove;
//    @BindView(R.id.price_seekbar)
//    SeekBar priceSeekbar;
    @BindView(R.id.et_your_bet)
    EditText etYourBet;
    @BindView(R.id.ll_your_bet)
    LinearLayout llYourBet;
    @BindView(R.id.et_asset_tranfer)
    EditText etAssetTranfer;
    @BindView(R.id.asset_tranfer)
    LinearLayout assetTranfer;
    @BindView(R.id.bet_tip)
    ImageView betTip;
    @BindView(R.id.qlc_seekbar)
    QlinkSeekBar qlcSeekbar;
    @BindView(R.id.connect_seekbar)
    QlinkSeekBar connectSeekbar;
    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.seizeNoShow)
    LinearLayout seizeNoShow;

    private WifiEntityDao mWifiEntityDao;

    List<WifiConfiguration> existingConfigsLeft = new ArrayList<>();
    private int flag = 0;
    private boolean isOverTime = true;
    private int networkid;

    /**
     * 作为未修改的WiFientity
     */
    WifiEntity tempWifiEntity;

    /**
     * 修改了的wifientity
     */
    WifiEntity newWifiEntity;
    private boolean wifiIsRegisted;
    private  Activity activityRoot;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wifi_registe, null);
        ButterKnife.bind(this, view);
        Bundle mBundle = getArguments();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initData();
    }

    public static WifiRegisteFragment newInstacnce(Intent intent) {
        mWifiEntity = intent.getParcelableExtra("wifiInfo");
        seizeQlc =  intent.getStringExtra("seizeQlc");
        KLog.i(mWifiEntity.toString());
        return new WifiRegisteFragment();
    }

    private void initData() {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(AppConfig.getInstance());
        if(mWifiEntity == null)
        {
            return;
        }
        KLog.i(mWifiEntity.getMacAdrees());
        infoMac.setInfoContent(mWifiEntity.getMacAdrees());
        infoSsid.setInfoContent(mWifiEntity.getSsid().replace("\"", ""));
        mWifiEntityDao = AppConfig.getInstance().getDaoSession().getWifiEntityDao();
        List<WifiEntity> wifiEntityDaoList = mWifiEntityDao.queryBuilder().list();
        if(seizeQlc != null)
        {
            etYourBet.setText(seizeQlc);
            seizeNoShow.setVisibility(View.GONE);
        }else{
            seizeNoShow.setVisibility(View.VISIBLE);
        }
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner.setSelection(0, true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        for (WifiEntity wifiEntity : wifiEntityDaoList) {
            if (wifiEntity.getSsid().equals(mWifiEntity.getSsid().replace("\"", ""))) {
                KLog.i("循环到了当前的wifi了");
                tempWifiEntity = wifiEntity;
                newWifiEntity = wifiEntity;
                if (wifiEntity.getOwnerP2PId() != null && wifiEntity.getOwnerP2PId().equals(SpUtil.getString(getActivity(), ConstantValue.P2PID, ""))) {
                    //查找到了我注册的WiFi信息
                    //如果该WiFi已经让我分享了，那么就是更新该WiFi详情，该详情是保存在app的本地的
                    KLog.i("自己分享的WiFi");
                    setWifiInfo(tempWifiEntity, true);
                    button2.setText(getString(R.string.update).toUpperCase());
                    llYourBet.setVisibility(View.GONE);
                    assetTranfer.setVisibility(View.VISIBLE);
                    etAssetTranfer.setText(wifiEntity.getAssetTranfer() + "");
                    if (ConstantValue.mLatitude != 0f) {
                        infoLocation.setInfoContent(ConstantValue.mLatitude + ", " + ConstantValue.mLongitude);
                    }
                    break;
                } else if (wifiEntity.getOwnerP2PId() != null && !wifiEntity.getOwnerP2PId().equals("")) {
                    //别人分享的WiFi
                    setWifiInfo(tempWifiEntity, false);
                    llYourBet.setVisibility(View.VISIBLE);
                    assetTranfer.setVisibility(View.VISIBLE);
                    etAssetTranfer.setText(wifiEntity.getAssetTranfer() + "");
//                    button2.setClickable(false);
//                    button2.setTextColor(getResources().getColor(R.color.colorFade));
                    if (mWifiEntity.getLatitude() != 0f) {
                        infoLocation.setInfoContent(mWifiEntity.getLatitude() + ", " + mWifiEntity.getLongitude());
                    }
                    KLog.i("别人分享的WiFi");
                    Map<String, String> map = new HashMap<>();
                    map.put("vpnName", infoSsid.getText().toString().trim());
                    map.put("type", "0");
                    mPresenter.vertifyWiFiName(map);
                    Map<String, String> map1 = new HashMap<>();
                    map1.put("address", AppConfig.getInstance().getDaoSession().getWalletDao().loadAll().get(SpUtil.getInt(getActivity(), ConstantValue.currentWallet, 0)).getAddress());
                    mPresenter.getBalance(map1);
                }
                if (wifiEntity.getOwnerP2PId() == null || wifiEntity.getOwnerP2PId().equals("")) {
                    //还未分享的WiFi
                    KLog.i("还未分享的WiFi");
                    llYourBet.setVisibility(View.VISIBLE);
                    assetTranfer.setVisibility(View.GONE);
                    connectSeekbar.setProgress(1);
                    if (ConstantValue.mLatitude != 0f) {
                        infoLocation.setInfoContent(ConstantValue.mLatitude + ", " + ConstantValue.mLongitude);
                    }
                    Map<String, String> map = new HashMap<>();
                    map.put("vpnName", infoSsid.getText().toString().trim());
                    map.put("type", "0");
                    mPresenter.vertifyWiFiName(map);
                    Map<String, String> map1 = new HashMap<>();
                    map1.put("address", AppConfig.getInstance().getDaoSession().getWalletDao().loadAll().get(SpUtil.getInt(getActivity(), ConstantValue.currentWallet, 0)).getAddress());
                    mPresenter.getBalance(map1);
                }
            }
        }

        etYourBet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if ("".equals(charSequence.toString())) {
                    return;
                }
                if ("0".equals(charSequence.toString())) {
                    etYourBet.setText("");
                    return;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void setWifiInfoWithNotRegiste(WifiEntity wifiInfo) {

    }

    private void setWifiInfo(WifiEntity wifiInfo, boolean isMyWifi) {
//        if (wifiInfo.getPriceMode() == 0) {
//            switchButton.setChecked(false);
//        } else {
//            switchButton.setChecked(true);
//        }

        qlcSeekbar.setProgress((int) (wifiInfo.getPriceInQlc() * 10));
        connectSeekbar.setProgress(wifiInfo.getDeviceAllowed());
//        if (wifiInfo.getPaymentType() == 0) {
//            cbPerconnect.setChecked(true);
//            cbPerhour.setChecked(false);
//        } else {
//            cbPerconnect.setChecked(false);
//            cbPerhour.setChecked(true);
//        }


        if (isMyWifi) {
            if (wifiInfo.getWifiPassword() != null) {
                password.setText(wifiInfo.getWifiPassword());
            }
        } else {
//            switchButton.setEnabled(false);
//            priceSeekbar.setEnabled(false);
//            conncetSeekbar.setEnabled(false);
//            cbPerhour.setEnabled(false);
//            cbPerconnect.setEnabled(false);
        }

    }

    /**
     * 检查WiFi信息是否齐全
     */
    private boolean checkLocalWIfiInfo() {
        if (newWifiEntity.getCapabilities().contains("WEP")) {
            if ("".equals(password.getText().toString().trim())) {
                ToastUtil.displayShortToast(getString(R.string.please_enter_password));
                return false;
            }
        } else if (newWifiEntity.getCapabilities().contains("WPA")) {
            if ("".equals(password.getText().toString().trim())) {
                ToastUtil.displayShortToast(getString(R.string.please_enter_password));
                return false;
            }
        } else {

        }
        newWifiEntity.setWifiPassword(password.getText().toString().trim());
        //0 = off， 1 = on   0 = free, 1 = tippingMode
        newWifiEntity.setPriceMode(1);
        Double value = (qlcSeekbar.getProgress() / (double) qlcSeekbar.getDefaultMax() * 10);
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
        newWifiEntity.setPriceInQlc(bd.floatValue());
        newWifiEntity.setPaymentType(1);
        newWifiEntity.setDeviceAllowed(connectSeekbar.getProgress());
        newWifiEntity.setLatitude(ConstantValue.mLatitude);
        newWifiEntity.setLongitude(ConstantValue.mLongitude);
        tempWifiEntity = newWifiEntity;
        return true;
    }

    /**
     * 显示下注的解释dialog
     */
    private void showVpnAlreadyRegisterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = View.inflate(getActivity(), R.layout.vpn_bettip_dialog_layout, null);
        builder.setView(view);
        builder.setCancelable(true);
        TextView tvContent = (TextView) view.findViewById(R.id.tv_content);//输入内容
        TextView tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText(getString(R.string.THIS_WiFi_NAME_HAS_ALREADY_BEEN_REGISTERED).toUpperCase());
        Button bt_left = view.findViewById(R.id.btn_left);
        Button btn_comfirm = (Button) view.findViewById(R.id.btn_right);//确定按钮
        tvContent.setText(getString(R.string.If_you_want_to_claim,"WiFi"));
        //取消或确定按钮监听事件处l
        AlertDialog dialog = builder.create();
        btn_comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if(mBalance == null)
                {
                    ToastUtil.displayShortToast(getResources().getString(R.string.wait_a_moment));
                    Map<String, String> map = new HashMap<>();
                    map.put("address", AppConfig.getInstance().getDaoSession().getWalletDao().loadAll().get(SpUtil.getInt(getActivity(), ConstantValue.currentWallet, 0)).getAddress());
                    mPresenter.getBalance(map);
                    return;
                }
                if (Float.parseFloat(etYourBet.getText().toString()) > Float.parseFloat(etAssetTranfer.getText().toString())) {
                    if (Float.parseFloat(etYourBet.getText().toString()) < Float.parseFloat(mBalance.getData().getQLC() + "") && mBalance.getData().getGAS() >0.0001) {
                        //可以正常抢注册
                        checkPassword();
                    } else {
                        //qlc不足
                        ToastUtil.displayShortToast(getString(R.string.Not_enough_QLC_Or_GAS));
                    }
                } else {
                    //抢注册的qlc不够
                    ToastUtil.displayShortToast(getString(R.string.Your_bet_should_be_greater_than_the_current_asset_value));
                }
            }
        });
        bt_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    @Override
    protected void setupFragmentComponent() {
        DaggerWifiRegisteComponent
                .builder()
                .appComponent(((AppConfig) getActivity().getApplication()).getApplicationComponent())
                .wifiRegisteModule(new WifiRegisteModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(WifiRegisteContract.WifiRegisteContractPresenter presenter) {
        mPresenter = (WifiRegistePresenter) presenter;
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
    public void registAssetSuccess() {
        if(seizeQlc != null)
        {
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "seizeWifiSuccess");
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "seizeWifiSuccess");
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "seizeWifiSuccess");
            mFirebaseAnalytics.logEvent("seizeWifiSuccess", bundle);
        }
        Intent intent = new Intent(getActivity(), RegisteWifiSuccessActivity.class);
        intent.putExtra("ssid", mWifiEntity.getSsid());
        List<WifiEntity> wifiEntityList = AppConfig.getInstance().getDaoSession().getWifiEntityDao().loadAll();
        for (WifiEntity wifiEntity : wifiEntityList) {
            if (wifiEntity.getSsid().equals(mWifiEntity.getSsid())) {
                wifiEntity = tempWifiEntity;
                wifiEntity.setOnline(true);
                wifiEntity.setOwnerP2PId(SpUtil.getString(getActivity(), ConstantValue.P2PID, ""));
                wifiEntity.setWalletAddress(AppConfig.getInstance().getDaoSession().getWalletDao().loadAll().get(SpUtil.getInt(getActivity(), ConstantValue.currentWallet, 0)).getAddress());
                wifiEntity.setLatitude(ConstantValue.mLatitude);
                wifiEntity.setLongitude(ConstantValue.mLongitude);
                wifiEntity.setAvaterUpdateTime(Long.parseLong(SpUtil.getString(getActivity(), ConstantValue.myAvaterUpdateTime, "0")));
                if (SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false)) {//主网
                    wifiEntity.setIsInMainWallet(true);
                }
                int groupNum = qlinkcom.CreatedNewGroupChat();
                wifiEntity.setGroupNum(groupNum);
                AppConfig.getInstance().getDaoSession().getWifiEntityDao().update(wifiEntity);
                //更新sd卡资产数据begin
                MyAsset myAsset = new MyAsset();
                myAsset.setType(0);
                myAsset.setWifiEntity(newWifiEntity);
                LocalAssetsUtils.insertLocalAssets(myAsset);
                //更新sd卡资产数据end
            }
        }
        startActivity(intent);
        getActivity().finish();
    }
    @Override
    public void updateAssetSuccess() {
        ToastUtil.displayShortToast(getString(R.string.update_success));
        closeProgressDialog();
        getActivity().finish();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.button1, R.id.button2, R.id.bet_tip})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button1:
                getActivity().onBackPressed();
                break;
            case R.id.button2:
                KLog.i(button2.getText().toString());
                switch (button2.getText().toString().toLowerCase()) {
                    case "register":
                        if (checkLocalWIfiInfo()) {
                            checkIsCanRegiste();
                        }
                        break;
                    case "update":
                        if (mWifiEntity.getWalletAddress() == null) {
                            ToastUtil.displayShortToast(getString(R.string.asset_info_Lack));
                            return;
                        }
                        Wallet wallet = AppConfig.getInstance().getDaoSession().getWalletDao().loadAll().get(SpUtil.getInt(getActivity(), ConstantValue.currentWallet, 0));
                        if (!mWifiEntity.getWalletAddress().equals(wallet.getAddress())) {
                            ToastUtil.displayShortToast(getString(R.string.The_asset_is_registered_under_another_wallet_Please_change_the_wallet_before_update));
                            return;
                        }
                        if (checkLocalWIfiInfo()) {
                            checkPassword();
                        }
                        break;
                    default:
                        break;
                }
                break;
            case R.id.bet_tip:
                showBetTipDialog();
                break;
            default:
                break;
        }
    }

    /**
     * 检查该资产是否可以注册，
     * 如果已经被注册，检查押注的qlc是否比资产的qlc大。
     * 还要检查自己的qlc余额是否比押注册qlc大
     */
    private void checkIsCanRegiste() {

        if(seizeQlc != null)
        {
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "beginSeizeWifi");
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "beginSeizeWifi");
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "beginSeizeWifi");
            mFirebaseAnalytics.logEvent("beginSeizeWifi", bundle);
            String currentTime = StringUitl.getNowDateShort();
            String currentTimeFlag = SpUtil.getString(AppConfig.getInstance(), currentTime+"_seizeWifi","0");
            if(currentTimeFlag.equals("0"))
            {
                bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "seizeWifiTotal");
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "seizeWifiTotal");
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "seizeWifiTotal");
                mFirebaseAnalytics.logEvent("seizeWifiTotal", bundle);
                SpUtil.putString(AppConfig.getInstance(), currentTime+"_seizeWifi","1");
            }
        }
        if (etYourBet.getText().toString().equals("") || etYourBet.getText().toString().equals("0")) {
            //没有输入押注的qlc
            ToastUtil.displayShortToast(getString(R.string.Please_enter_your_bet));
            return;
        }
        if (wifiIsRegisted) {
            if (Float.parseFloat(etYourBet.getText().toString()) > Float.parseFloat(etAssetTranfer.getText().toString())) {
                showVpnAlreadyRegisterDialog();
            } else {
                //抢注册的qlc不够
                ToastUtil.displayShortToast(getString(R.string.Your_bet_should_be_greater_than_the_current_asset_value));
            }
        } else {
            if (mBalance == null) {
                ToastUtil.displayShortToast(getString(R.string.please_wait));
                Map<String, String> map = new HashMap<>();
                map.put("address", AppConfig.getInstance().getDaoSession().getWalletDao().loadAll().get(SpUtil.getInt(getActivity(), ConstantValue.currentWallet, 0)).getAddress());
                mPresenter.getBalance(map);
            } else {
                if (Float.parseFloat(etYourBet.getText().toString()) < Float.parseFloat(mBalance.getData().getQLC() + "") && mBalance.getData().getGAS() >0.0001) {
                    //可以正常注册
                    checkPassword();
                } else {
                    //qlc不足
                    ToastUtil.displayShortToast(AppConfig.getInstance().getResources().getString(R.string.Not_enough_QLC_Or_GAS));
                }
            }
        }
    }


    /**
     * 显示下注的解释dialog
     */
    private void showBetTipDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = View.inflate(getActivity(), R.layout.vpn_bettip_dialog_layout, null);
        builder.setView(view);
        builder.setCancelable(true);
        TextView tvContent = (TextView) view.findViewById(R.id.tv_content);//输入内容
        TextView tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText(R.string.THE_WiFi_ASSET_HAS_BEEN_REGISTERED);
        Button bt_left = view.findViewById(R.id.btn_left);
        Button btn_comfirm = (Button) view.findViewById(R.id.btn_right);//确定按钮
        tvContent.setText(R.string.If_someone_else_wants_to_grab_your_WiFi);
        //取消或确定按钮监听事件处l
        AlertDialog dialog = builder.create();
        btn_comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
//                checkVpnInfo();
            }
        });
        bt_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /**
     * 显示vpn注册时需要扣费的dialog
     */
    private void showWiFiRegisterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = View.inflate(getActivity(), R.layout.vpn_bettip_dialog_layout, null);
        builder.setView(view);
        builder.setCancelable(true);
        TextView tvContent = (TextView) view.findViewById(R.id.tv_content);//输入内容
        Button bt_left = view.findViewById(R.id.btn_left);
        Button btn_comfirm = (Button) view.findViewById(R.id.btn_right);//确定按钮
        tvContent.setText(R.string.WiFi_asset_registration_will_consume_1_QLC);
        //取消或确定按钮监听事件处l
        AlertDialog dialog = builder.create();
        btn_comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Map<String, String> map = new HashMap<>();
                map.put("address", AppConfig.getInstance().getDaoSession().getWalletDao().loadAll().get(SpUtil.getInt(getActivity(), ConstantValue.currentWallet, 0)).getAddress());
                mPresenter.getBalance(map);
            }
        });
        bt_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }

    private void checkPassword() {
        try {
            showProgressDialog();
            WifiManager wifiManager = (WifiManager) AppConfig.getInstance().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (newWifiEntity != null && wifiManager != null) {
                boolean existingConfigHad = false;
                List<WifiConfiguration> existingConfigs = wifiManager
                        .getConfiguredNetworks();

                for (WifiConfiguration existingConfig : existingConfigs) {//先删除所有的配置，android6.0删除不了
                    if (existingConfig.SSID.equals("\"" + newWifiEntity.getSsid() + "\"")) {
                        wifiManager.removeNetwork(existingConfig.networkId);
                        wifiManager.saveConfiguration();
                    }

                }
                existingConfigsLeft = new ArrayList<>();
                existingConfigs = wifiManager
                        .getConfiguredNetworks();
                for (WifiConfiguration existingConfig : existingConfigs) {
                    if (existingConfig.SSID.equals("\"" + newWifiEntity.getSsid() + "\"")) {
                        existingConfigHad = true;
                        existingConfigsLeft.add(existingConfig);//android6.0删除不掉的剩余
                    }
                }
                WifiConfiguration conf = null;
                if (!existingConfigHad) {
                    conf = new WifiConfiguration();
                    conf.SSID = "\"" + newWifiEntity.getSsid() + "\"";
                    existingConfigsLeft.add(conf);
                }
                flag = 0;
                doConnectWiFi(existingConfigsLeft, password.getText().toString().trim(), wifiManager, existingConfigHad, 0);
            }
        }catch (NullPointerException e)
        {

        }
        /*WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = "\"" + newWifiEntity.getSsid() + "\"";
        if (newWifiEntity.getCapabilities().contains("WEP")) {
            conf.wepKeys[0] = "\"" + password.getText().toString().trim() + "\"";
            conf.wepTxKeyIndex = 0;
            conf.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            conf.wepTxKeyIndex = 0;
        } else if (newWifiEntity.getCapabilities().contains("WPA")) {
            conf.preSharedKey = "\"" + password.getText().toString().trim() + "\"";
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
        wifiManager = (WifiManager) getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            if (!wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(true);
            }
            int networkid = wifiManager.addNetwork(conf);
            wifiManager.disconnect();
            wifiManager.enableNetwork(networkid, true);
            wifiManager.reconnect();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ConnectivityManager connManager = (ConnectivityManager) getContext().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                    if (connManager != null) {
                        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                        if (mWifi.isConnected()) {
                            KLog.i("wifi通过qlink连接成功~~~");
                            if (wifiManager.getConnectionInfo().getSSID().replace("\"", "").equals(newWifiEntity.getSsid())) {
                                //连接成功，密码验证成功
                                connectSuccess();
                            } else {
                                //连接成功，密码验证失败
                                connectOver();
                                KLog.i("验证失败");
                            }
                        } else {
                            //密码错误
                            connectOver();
                            KLog.i("验证失败");
                        }
                    } else {
                        //连接错误
                        connectOver();
                        KLog.i("验证失败");
                    }
                }
            }, 5000);
        }*/
    }
    private void doConnectWiFi(List<WifiConfiguration> existingConfigsLeft, String password, WifiManager wifiManager, boolean existingConfigHad, int index)
    {
        WifiConfiguration conf = existingConfigsLeft.get(index);
        if (newWifiEntity.getCapabilities().contains("WEP")) {
            conf.wepKeys[0] = "\"" + password + "\"";
            conf.wepTxKeyIndex = 0;
            conf.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            conf.wepTxKeyIndex = 0;
        } else if (newWifiEntity.getCapabilities().contains("WPA")) {
            conf.preSharedKey = "\"" + password + "\"";
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
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        networkid = -1;
        if(!existingConfigHad)
        {
            networkid = wifiManager.addNetwork(conf);
        }else{
            wifiManager.updateNetwork(conf);
            networkid = conf.networkId;
        }
        KLog.i("networkid"+networkid);
        if(networkid == -1)
        {
            connectOver();
            KLog.i("验证失败");
            return;
        }
        wifiManager.disconnect();
        new Thread(new Runnable() {
            @Override
            public void run() {
                isOverTime = true;
                boolean flagNet = wifiManager.enableNetwork(networkid, true);
            }
        }).start();
        //wifiManager.reconnect();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ConnectivityManager connManager = (ConnectivityManager) AppConfig.getInstance().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                if (connManager != null) {
                    NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                    if (mWifi.isConnected()) {
                        if(wifiManager.getConnectionInfo().getSSID().replace("\"", "").equals(newWifiEntity.getSsid()))
                        {
                            //连接成功，密码验证成功
                            connectSuccess();
                        }else{
                            //连接失败，密码验证失败
                            flag ++;
                            if(flag < existingConfigsLeft.size())
                            {
                                doConnectWiFi(existingConfigsLeft,password,wifiManager,existingConfigHad,flag);
                            }else{
                                connectOver();
                                KLog.i("验证失败");
                            }
                        }

                    } else {
                        flag ++;
                        if(flag < existingConfigsLeft.size())
                        {
                            doConnectWiFi(existingConfigsLeft,password,wifiManager,existingConfigHad,flag);
                        }else{
                            //连接错误
                            connectOver();
                            KLog.i("验证失败");

                        }

                    }
                } else {
                    //连接错误
                    connectOver();
                    KLog.i("验证失败");
                }
            }
        }, 6000);
    }
    public void stopConnectWiFi()
    {
        if(existingConfigsLeft != null)
            flag = existingConfigsLeft.size();
    }
    private void connectOver() {
        activityRoot.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtil.displayShortToast(getString(R.string.password_error));
                closeProgressDialog();
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        activityRoot = activity;
        super.onAttach(activity);
    }

    private void connectSuccess() {
        mWifiEntityDao.update(newWifiEntity);
        if (button2.getText().toString().toLowerCase(Locale.ENGLISH).equals("update")) {
            //更新sd卡资产数据begin
            MyAsset myAsset = new MyAsset();
            myAsset.setType(0);
            myAsset.setWifiEntity(newWifiEntity);
            LocalAssetsUtils.updateLocalAssets(myAsset);
            //更新sd卡资产数据end
            if(activityRoot == null)
            {
                return;
            }
            activityRoot.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    closeProgressDialog();
                    Double value = (qlcSeekbar.getProgress() / (double) qlcSeekbar.getDefaultMax() * 10);
                    BigDecimal bd = new BigDecimal(value);
                    String expectedCost = bd.setScale(1, BigDecimal.ROUND_HALF_UP)+"";
                    String connectNum = connectSeekbar.getProgress() +"";

                    Map<String, String> recordMap = new HashMap<String, String>();
                    recordMap.put("wifiName", mWifiEntity.getSsid().replace("\"", ""));
                    recordMap.put("p2pId", SpUtil.getString(getActivity(), ConstantValue.P2PID, ""));
                    recordMap.put("expectedCost",expectedCost+"");
                    recordMap.put("connectNum",connectNum+"");
                    mPresenter.updateWiFiInfo(recordMap, mWifiEntity.getSsid().replace("\"", ""));
                }
            });
            return;
        }
        activityRoot.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                closeProgressDialog();
                showProgressDialog();
                Double value = (qlcSeekbar.getProgress() / (double) qlcSeekbar.getDefaultMax() * 10);
                BigDecimal bd = new BigDecimal(value);
                String expectedCost = bd.setScale(1, BigDecimal.ROUND_HALF_UP)+"";
                String connectNum = connectSeekbar.getProgress() +"";
                String walletAddress = AppConfig.getInstance().getDaoSession().getWalletDao().loadAll().get(SpUtil.getInt(getActivity(), ConstantValue.currentWallet, 0)).getAddress();
                Map<String, String> recordMap = new HashMap<String, String>();
                recordMap.put("ssId", mWifiEntity.getSsid().replace("\"", ""));
                recordMap.put("mac", mWifiEntity.getMacAdrees());
                recordMap.put("p2pId", SpUtil.getString(getActivity(), ConstantValue.P2PID, ""));
                recordMap.put("address", walletAddress);
//                recordMap.put("wif", AppConfig.getInstance().getDaoSession().getWalletDao().loadAll().get(SpUtil.getInt(getActivity(), ConstantValue.currentWallet, 0)).getWif());
                recordMap.put("qlc", etYourBet.getText().toString());
                recordMap.put("expectedCost",expectedCost+"");
                recordMap.put("connectNum",connectNum+"");
                TransactionApi.getInstance().registerWiFi(recordMap, walletAddress, ConstantValue.mainAddress, etYourBet.getText().toString(), new SendBackWithTxId() {
                    @Override
                    public void onSuccess(String txid) {
                        closeProgressDialog();
                        registAssetSuccess();
                    }

                    @Override
                    public void onFailure() {

                    }
                });
//                mPresenter.registeWifi(recordMap, mWifiEntity.getSsid().replace("\"", ""));
            }
        });
    }

    private Balance mBalance;

    @Override
    public void onGetBalancelSuccess(Balance balance) {
        mBalance = balance;
    }

    @Override
    public void vertifyWiFiBack(VertifyVpn vertifyVpn) {
        if (!vertifyVpn.getData().isIsExist()) {
            wifiIsRegisted = false;
            etAssetTranfer.setText("");
            assetTranfer.setVisibility(View.GONE);

        } else {
            wifiIsRegisted = true;
            assetTranfer.setVisibility(View.VISIBLE);
            if (button2.getText().toString().toLowerCase(Locale.ENGLISH).equals("update")) {

            } else {
                etAssetTranfer.setText(vertifyVpn.getData().getQlc() + "");
            }
        }
    }
}