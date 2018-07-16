package com.stratagile.qlink.ui.activity.setting;

import android.content.Context;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.db.VpnEntity;
import com.stratagile.qlink.db.Wallet;
import com.stratagile.qlink.db.WifiEntity;
import com.stratagile.qlink.entity.MyAsset;
import com.stratagile.qlink.entity.SettingBean;
import com.stratagile.qlink.entity.eventbus.ChangeWalletNeedRefesh;
import com.stratagile.qlink.entity.eventbus.NeoRefrash;
import com.stratagile.qlink.entity.eventbus.Set2Asset;
import com.stratagile.qlink.entity.eventbus.ShowGuide;
import com.stratagile.qlink.guideview.Guide;
import com.stratagile.qlink.guideview.GuideBuilder;
import com.stratagile.qlink.guideview.GuideConstantValue;
import com.stratagile.qlink.guideview.GuideSpUtil;
import com.stratagile.qlink.guideview.compnonet.WalletDetailComponent;
import com.stratagile.qlink.ui.activity.setting.component.DaggerSettingsComponent;
import com.stratagile.qlink.ui.activity.setting.contract.SettingsContract;
import com.stratagile.qlink.ui.activity.setting.module.SettingsModule;
import com.stratagile.qlink.ui.activity.setting.presenter.SettingsPresenter;
import com.stratagile.qlink.ui.activity.wallet.ChangeWalletActivity;
import com.stratagile.qlink.ui.activity.wallet.TransactionRecordActivity;
import com.stratagile.qlink.ui.activity.wallet.WalletDetailActivity;
import com.stratagile.qlink.ui.adapter.settings.SettingsAdapter;
import com.stratagile.qlink.utils.LocalAssetsUtils;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.VersionUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_join_telegram)
    TextView tvJoinTelegram;
    @BindView(R.id.tv_share_facebook)
    TextView tvShareFacebook;
    private Guide guide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {
        setTitle(R.string.settings);
        settingsAdapter = new SettingsAdapter(new ArrayList<>());
        recyclerView.setAdapter(settingsAdapter);
        ArrayList<SettingBean> settingBeanArrayList = new ArrayList<>();
        Wallet wallet = AppConfig.getInstance().getDaoSession().getWalletDao().loadAll().get(SpUtil.getInt(this, ConstantValue.currentWallet, 0));
        settingBeanArrayList.add(new SettingBean("icon_wallet", getString(R.string.Wallet_Details), wallet.getAddress(), 0));
        settingBeanArrayList.add(new SettingBean("icon_switch", getString(R.string.Switch_Wallet), AppConfig.getInstance().getDaoSession().getWalletDao().loadAll().size() + getString(R.string.Wallets_available2), 0));
//        settingBeanArrayList.add(new SettingBean("icon_history", "Wallet history Records", "Registrations, Sharing, Transactions", 0));
        settingBeanArrayList.add(new SettingBean("icon_registered", getString(R.string.My_Registered_Assets), getString(R.string.WiFi_Assets) + getWiFiCount(getasseList()) + "  " + getString(R.string.VPN_Assets) + getVPNCount(getasseList()), 0));
        if (isSupportFingerPrint()) {
            SettingBean fingerPrintUnLock = new SettingBean("icon_fingerprint", getString(R.string.Fingerprint_to_unlock), "", 1);
            fingerPrintUnLock.setFingerprintUnLock(SpUtil.getBoolean(this, ConstantValue.fingerprintUnLock, true));
            if (SpUtil.getBoolean(this, ConstantValue.fingerprintUnLock, true)) {
                fingerPrintUnLock.setDesc("");
            } else {
                fingerPrintUnLock.setDesc("");
            }
            fingerPrintUnLock.setOnStr("");
            fingerPrintUnLock.setOffStr("");
            settingBeanArrayList.add(fingerPrintUnLock);
        }
        if (SpUtil.getBoolean(this, ConstantValue.isMainNet, false)) {
            SettingBean switchNet = new SettingBean("icon_cut", getString(R.string.Switch_Net), "", 1);
            switchNet.setFingerprintUnLock(true);
            switchNet.setOnStr(getString(R.string.MainNet));
            switchNet.setOffStr(getString(R.string.TestNet));
            settingBeanArrayList.add(switchNet);

        } else {
            SettingBean switchNet = new SettingBean("icon_cut", getString(R.string.Switch_Net), "", 1);
            switchNet.setFingerprintUnLock(false);
            switchNet.setOnStr(getString(R.string.MainNet));
            switchNet.setOffStr(getString(R.string.TestNet));
            settingBeanArrayList.add(switchNet);
        }
        String selectLanguage = SpUtil.getString(AppConfig.getInstance(), ConstantValue.selectLanguage, "");
        String defaultLanguage = Locale.getDefault().getLanguage();
        if ("".equals(selectLanguage)) {
            switch (defaultLanguage) {
                case "tr"://土耳其语
                    selectLanguage = "Türk";
                    break;
                default:
                    selectLanguage = "English";
                    break;
            }
        }else{
            switch (selectLanguage) {
                case "Turkish"://土耳其语
                    selectLanguage = "Türk";
                    break;
                default:
                    selectLanguage = "English";
                    break;
            }
        }
        settingBeanArrayList.add(new SettingBean("icon_language", getString(R.string.Language), selectLanguage, 0));
        settingBeanArrayList.add(new SettingBean("icon_version", getString(R.string.Version), getString(R.string.Version) + " " + VersionUtil.getAppVersionName(this), 2));
//        settingBeanArrayList.add(new SettingBean("icon_disclaimer", "Disclaimer", "My government...", 2));
        settingsAdapter.setNewData(settingBeanArrayList);
        settingsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                switch (((SettingsAdapter) adapter).getItem(position).getIcon()) {
                    case "icon_wallet":
                        Intent intent = new Intent(SettingsActivity.this, WalletDetailActivity.class);
                        intent.putExtra("fromType", "test");
                        startActivityForResult(intent, 0);
                        break;
                    case "icon_switch":
                        Intent in = new Intent(SettingsActivity.this, ChangeWalletActivity.class);
                        in.putExtra("fromType", "test");
                        startActivityForResult(in, 0);
                        break;
                    case "icon_history":
                        startActivity(new Intent(SettingsActivity.this, TransactionRecordActivity.class));
                        break;
                    case "icon_registered":
                        EventBus.getDefault().post(new Set2Asset());
                        finish();
                        break;
                    case "icon_fingerprint":
                        break;
                    case "icon_language":
                        startActivity(new Intent(SettingsActivity.this, SelectLanguageActivityActivity.class));
                        break;
                    default:
                        break;
                }
            }
        });
        settingsAdapter.setOnCheckChangeListener(new SettingsAdapter.OnCheckChangeListener() {
            @Override
            public void onCheck(boolean isCheck, int position, String icon) {
                if ("icon_fingerprint".equals(icon)) {
                    SpUtil.putBoolean(AppConfig.getInstance(), ConstantValue.fingerprintUnLock, isCheck);
                } else if ("icon_cut".equals(icon)) {
                    SpUtil.putBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, isCheck);
                    EventBus.getDefault().post(new ChangeWalletNeedRefesh());
                    if (SpUtil.getBoolean(SettingsActivity.this, ConstantValue.showTestFlag, true)) {
                        if (isCheck) {
                            view.setText("");
                            view.setBackground(getResources().getDrawable(R.drawable.navigation_shape));
                        } else {
                            view.setText(getString(R.string.testnet));
                            view.setBackgroundColor(getResources().getColor(R.color.color_f51818));
                        }
                    }
                }
            }
        });

        tvJoinTelegram.post(new Runnable() {
            @Override
            public void run() {
                showGuideView();
            }
        });
    }

    private void showGuideView() {
        if (!GuideSpUtil.getBoolean(this, GuideConstantValue.isShowSettingGuide, false)) {
            EventBus.getDefault().post(new ShowGuide(3));
            GuideSpUtil.putBoolean(this, GuideConstantValue.isShowSettingGuide, true);
            GuideBuilder builder = new GuideBuilder();
            builder.setTargetView(recyclerView.getChildAt(0))
                    .setAlpha(150)
                    .setHighTargetCorner(15)
                    .setHighTargetPadding(0)
                    .setOverlayTarget(false)
                    .setOutsideTouchable(false);
            builder.setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
                @Override
                public void onShown() {
                }

                @Override
                public void onDismiss() {

                }
            });

            builder.addComponent(new WalletDetailComponent());
            guide = builder.createGuide();
            guide.setShouldCheckLocInWindow(false);
            guide.show(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == RESULT_OK) {
            EventBus.getDefault().post(new NeoRefrash());
            initData();
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
        List<WifiEntity> wifiEntityList = AppConfig.getInstance().getDaoSession().getWifiEntityDao().queryBuilder().list();
        for (WifiEntity wifiEntity : wifiEntityList) {
            if (wifiEntity.getOwnerP2PId() != null && wifiEntity.getOwnerP2PId().equals(SpUtil.getString(this, ConstantValue.P2PID, ""))) {
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
            if (vpnEntity.getP2pId() != null && vpnEntity.getP2pId().equals(SpUtil.getString(this, ConstantValue.P2PID, ""))) {
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

    @OnClick({R.id.tv_join_telegram, R.id.tv_share_facebook})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_join_telegram:
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("https://t.me/winqdapp");
                intent.setData(content_url);
                startActivity(intent);
//                Intent telegram = new Intent(this, WebViewActivity.class);
//                telegram.putExtra("url", "https://t.me/winqdapp");
//                telegram.putExtra("title", "telegram");
//                startActivity(telegram);
                break;
            case R.id.tv_share_facebook:
                Intent intent1 = new Intent();
                intent1.setAction("android.intent.action.VIEW");
                Uri content_url1 = Uri.parse("https://www.facebook.com/dAppWINQ");
                intent1.setData(content_url1);
                startActivity(intent1);
//                Intent facebook = new Intent(this, WebViewActivity.class);
//                facebook.putExtra("url", "https://www.facebook.com/dAppWINQ");
//                facebook.putExtra("title", "facebook");
//                startActivity(facebook);
                break;
            default:
                break;
        }
    }
}