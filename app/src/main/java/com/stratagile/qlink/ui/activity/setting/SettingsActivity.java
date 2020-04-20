package com.stratagile.qlink.ui.activity.setting;

import android.content.Context;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.socks.library.KLog;
import com.stratagile.qlink.BuildConfig;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.db.UserAccount;
import com.stratagile.qlink.db.VpnEntity;
import com.stratagile.qlink.db.Wallet;
import com.stratagile.qlink.entity.MyAsset;
import com.stratagile.qlink.entity.eventbus.Logout;
import com.stratagile.qlink.entity.eventbus.NeoRefrash;
import com.stratagile.qlink.guideview.Guide;
import com.stratagile.qlink.ui.activity.main.SplashActivity;
import com.stratagile.qlink.ui.activity.my.RetrievePasswordActivity;
import com.stratagile.qlink.ui.activity.setting.component.DaggerSettingsComponent;
import com.stratagile.qlink.ui.activity.setting.contract.SettingsContract;
import com.stratagile.qlink.ui.activity.setting.module.SettingsModule;
import com.stratagile.qlink.ui.activity.setting.presenter.SettingsPresenter;
import com.stratagile.qlink.ui.adapter.settings.SettingsAdapter;
import com.stratagile.qlink.utils.AccountUtil;
import com.stratagile.qlink.utils.FileUtil;
import com.stratagile.qlink.utils.FireBaseUtils;
import com.stratagile.qlink.utils.LocalAssetsUtils;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.ToastUtil;
import com.stratagile.qlink.utils.VpnUtil;
import com.today.step.net.StepSpUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.setting
 * @Description: $description
 * @date 2018/05/29 09:30:35
 */

public class SettingsActivity extends BaseActivity implements SettingsContract.View {

    @Inject
    SettingsPresenter mPresenter;

    SettingsAdapter settingsAdapter;
    @BindView(R.id.llLoginOut)
    LinearLayout llLoginOut;
    @BindView(R.id.touchIdToUnlock)
    Switch touchIdToUnlock;
    @BindView(R.id.resetPassword)
    LinearLayout resetPassword;
    @BindView(R.id.selectUnit)
    LinearLayout selectUnit;
    @BindView(R.id.tvVersion)
    TextView tvVersion;
    @BindView(R.id.language)
    LinearLayout language;
    @BindView(R.id.debugMode)
    Switch debugMode;
    @BindView(R.id.llDebugMode)
    LinearLayout llDebugMode;
    private Guide guide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainColor = R.color.white;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tvVersion.setText(getString(R.string.version) + " " + BuildConfig.VERSION_NAME + " (" + getString(R.string.Build) + " " + BuildConfig.VERSION_CODE + ")");
        touchIdToUnlock.setChecked(SpUtil.getBoolean(this, ConstantValue.fingerprintUnLock, true));
        touchIdToUnlock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SpUtil.putBoolean(SettingsActivity.this, ConstantValue.fingerprintUnLock, isChecked);
            }
        });
        if (ConstantValue.currentUser == null) {
            llLoginOut.setVisibility(View.GONE);
            resetPassword.setVisibility(View.GONE);
        }
        debugMode.setChecked(!SpUtil.getBoolean(this, ConstantValue.isMainNet, true));
        debugMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                FileUtil.savaData("/Qwallet/indexInterfaceStr.txt", "");
                FileUtil.savaData("/Qwallet/productListStr.txt", "");
                SpUtil.putBoolean(SettingsActivity.this, ConstantValue.isMainNet, !b);
                StepSpUtil.putBoolean(SettingsActivity.this, "stepMainNet", !b);
                Intent intent = new Intent(SettingsActivity.this, SplashActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
            }
        });
        tvVersion.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                llDebugMode.setVisibility(View.VISIBLE);
                return true;
            }
        });
    }

    @Override
    protected void initData() {
        setTitle(R.string.settings);
        settingsAdapter = new SettingsAdapter(new ArrayList<>());

        List<UserAccount> userAccounts = AppConfig.getInstance().getDaoSession().getUserAccountDao().loadAll();
        for (UserAccount userAccount : userAccounts) {
            if (userAccount.getIsLogin()) {
                llLoginOut.setVisibility(View.VISIBLE);
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == RESULT_OK) {
            EventBus.getDefault().post(new NeoRefrash());
            initData();
        }
        if (requestCode == 1 && resultCode == RESULT_OK) {
            finish();
        }

    }

    /**
     * 判断是否支持指纹解锁
     *
     * @return
     */
    private boolean isSupportFingerPrint() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                FingerprintManager fingerprintManager = (FingerprintManager) AppConfig.getInstance().getSystemService(Context.FINGERPRINT_SERVICE);
                if (fingerprintManager != null && fingerprintManager.isHardwareDetected() && fingerprintManager.hasEnrolledFingerprints()) {
                    return true;
                }
            } catch (Exception e) {

            }
        }
        return false;
    }

    /**
     * 获取资产数量
     *
     * @return
     */
    private ArrayList<MyAsset> getasseList() {
        List<Wallet> walletList = AppConfig.getInstance().getDaoSession().getWalletDao().loadAll();
        Wallet wallet;
        if (walletList != null && walletList.size() != 0) {
            int currentWallet = SpUtil.getInt(this, ConstantValue.currentWallet, 0);
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
        List<VpnEntity> vpnEntityList = AppConfig.getInstance().getDaoSession().getVpnEntityDao().loadAll();
        for (VpnEntity vpnEntity : vpnEntityList) {
            if (SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, true) && !vpnEntity.getIsInMainWallet()) {//主网
                continue;
            }
            if (!SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, true) && vpnEntity.getIsInMainWallet()) {//测试网
                continue;
            }
            if (vpnEntity.getP2pId() != null && vpnEntity.getP2pId().equals(SpUtil.getString(this, ConstantValue.P2PID, ""))) {
                if (!VpnUtil.isInSameNet(vpnEntity)) {
                    continue;
                }
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

    private int getWiFiCount(ArrayList<MyAsset> assets) {
        int wifiCount = 0;
        for (MyAsset myAsset : assets) {
            if (myAsset.getType() == 0) {
                wifiCount++;
            }
        }
        return wifiCount;
    }

    private int getVPNCount(ArrayList<MyAsset> assets) {
        int vpnCount = 0;
        for (MyAsset myAsset : assets) {
            if (myAsset.getType() == 1) {
                vpnCount++;
            }
        }
        return vpnCount;
    }

    @Override
    protected void setupActivityComponent() {
        DaggerSettingsComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .settingsModule(new SettingsModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(SettingsContract.SettingsContractPresenter presenter) {
        mPresenter = (SettingsPresenter) presenter;
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
    public void logoutSuccess() {
        closeProgressDialog();
        ConstantValue.lastLoginOut = ConstantValue.currentUser;
        List<UserAccount> userAccounts = AppConfig.getInstance().getDaoSession().getUserAccountDao().loadAll();
        for (UserAccount userAccount : userAccounts) {
            if (userAccount.getIsLogin()) {
                userAccount.setIsLogin(false);
                AppConfig.getInstance().getDaoSession().getUserAccountDao().update(userAccount);
                ConstantValue.currentUser = null;
                EventBus.getDefault().post(new Logout());
                setResult(1);
                finish();
                ToastUtil.displayShortToast(getString(R.string.logout_success));
            }
        }
    }

    @OnClick({R.id.llLoginOut, R.id.resetPassword, R.id.selectUnit, R.id.language})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.llLoginOut:
                logout();
                break;
            case R.id.resetPassword:
                Intent intent = new Intent(this, RetrievePasswordActivity.class);
                intent.putExtra("flag", "");
                startActivityForResult(intent, 1);
                break;
            case R.id.selectUnit:
                startActivity(new Intent(this, CurrencyUnitActivity.class));
                break;
            case R.id.language:
                FireBaseUtils.logEvent(this, FireBaseUtils.Me_Settings_Languages);
                startActivity(new Intent(this, SelectLanguageActivityActivity.class));
                break;
            default:
                break;
        }
    }

    private void logout() {
        showProgressDialog();
        Map map = new HashMap<String, String>();
        map.put("account", ConstantValue.currentUser.getAccount());
        map.put("token", AccountUtil.getUserToken());
        mPresenter.logout(map);
        logoutSuccess();
    }
}