package com.stratagile.qlink.ui.activity.seize;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.net.VpnService;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.VpnProfile;
import com.stratagile.qlink.activities.ConfigConverter;
import com.stratagile.qlink.activities.DisconnectVPN;
import com.stratagile.qlink.activities.FileSelect;
import com.stratagile.qlink.api.ExternalAppDatabase;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.constant.BroadCastAction;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.core.ConfigParser;
import com.stratagile.qlink.core.ConnectionStatus;
import com.stratagile.qlink.core.Preferences;
import com.stratagile.qlink.core.ProfileManager;
import com.stratagile.qlink.core.VPNLaunchHelper;
import com.stratagile.qlink.core.VpnStatus;
import com.stratagile.qlink.db.VpnEntity;
import com.stratagile.qlink.entity.MyAsset;
import com.stratagile.qlink.entity.eventbus.VpnRegisterSuccess;
import com.stratagile.qlink.fragments.Utils;
import com.stratagile.qlink.qlinkcom;
import com.stratagile.qlink.ui.activity.file.FileChooseActivity;
import com.stratagile.qlink.ui.activity.seize.component.DaggerSeizeVpnComponent;
import com.stratagile.qlink.ui.activity.seize.contract.SeizeVpnContract;
import com.stratagile.qlink.ui.activity.seize.module.SeizeVpnModule;
import com.stratagile.qlink.ui.activity.seize.presenter.SeizeVpnPresenter;
import com.stratagile.qlink.ui.activity.vpn.RegisterVpnSuccessActivity;
import com.stratagile.qlink.ui.activity.vpn.SelectContinentActivity;
import com.stratagile.qlink.utils.LocalAssetsUtils;
import com.stratagile.qlink.utils.LogUtil;
import com.stratagile.qlink.api.transaction.SendBackWithTxId;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.StringUitl;
import com.stratagile.qlink.utils.ToastUtil;
import com.stratagile.qlink.api.transaction.TransactionApi;
import com.stratagile.qlink.view.CustomPopWindow;
import com.stratagile.qlink.view.QlinkSeekBar;
import com.stratagile.qlink.view.TestConnectDialog;
import com.stratagile.qlink.views.FileSelectLayout;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.stratagile.qlink.LaunchVPN.CLEARLOG;
import static com.stratagile.qlink.LaunchVPN.START_VPN_PROFILE;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.seize
 * @Description: $description
 * @date 2018/04/13 12:08:31
 */

public class SeizeVpnActivity extends BaseActivity implements SeizeVpnContract.View {

    @Inject
    SeizeVpnPresenter mPresenter;
    @BindView(R.id.iv_step_one)
    ImageView ivStepOne;
    @BindView(R.id.iv_step_two)
    ImageView ivStepTwo;
    @BindView(R.id.iv_step_three)
    ImageView ivStepThree;
    @BindView(R.id.cardView_step_one)
    CardView cardViewStepOne;
    @BindView(R.id.cardView_step_two)
    CardView cardViewStepTwo;
    @BindView(R.id.cardView_step_three)
    CardView cardViewStepThree;
    @BindView(R.id.button1)
    Button button1;
    @BindView(R.id.button2)
    Button button2;
    @BindView(R.id.info_userid)
    EditText infoUserid;
    @BindView(R.id.et_country)
    TextView etCountry;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tv_configuration)
    TextView tvConfiguration;
    @BindView(R.id.et_configuration)
    TextView etConfiguration;
    @BindView(R.id.tv_privatekey_flag)
    TextView tvPrivatekeyFlag;
    @BindView(R.id.et_private_key_password)
    EditText etPrivateKeyPassword;
    @BindView(R.id.cb_save_password)
    CheckBox cbSavePassword;
    @BindView(R.id.tv_username_flag)
    TextView tvUsernameFlag;
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.qlc_seekbar)
    QlinkSeekBar qlcSeekbar;
    @BindView(R.id.connect_seekbar)
    QlinkSeekBar connectSeekbar;

    /**
     * 当前选中的是哪个item
     */
    private int currentItem = 0;

    /**
     * 原始的vpnentity。代表需要被抢注的vpn资产
     */
    private VpnEntity originalVpnEntity;

    /**
     * 新生成的vpnentity，代表自己抢注过来的vpnentity
     */
    private VpnEntity newVpnEntity;

    private static final int SELECT_COUNTRY = 0;

    private static final int START_VPN_CONFIG = 92;
    private static final int SELECT_PROFILE = 43;
    private static final int IMPORT_PROFILE = 231;
    private static final int FILE_PICKER_RESULT_KITKAT = 392;

    public final static int RESULT_VPN_DELETED = Activity.RESULT_FIRST_USER;
    public final static int RESULT_VPN_DUPLICATE = Activity.RESULT_FIRST_USER + 1;

    private MyBroadcastReceiver myBroadcastReceiver = new MyBroadcastReceiver();

    private String continent;

    private VpnProfile profile;
    private String vpnFilePath;
    private String profileUUID;

    /**
     * 密码
     */
    private String mTransientAuthPW;
    /**
     * 私钥
     */
    private String mTransientCertOrPCKS12PW;

    private TestConnectDialog testConnectDialog;

    private FirebaseAnalytics mFirebaseAnalytics;

    public class MyBroadcastReceiver extends BroadcastReceiver {
        public static final String TAG = "MyBroadcastReceiver";

        @Override
        public void onReceive(Context context, Intent intent) {
            KLog.i(intent.getStringExtra("detailstatus"));
            KLog.i("收到广播了。。。。");
            LogUtil.addLog("连接vpn的log：" + intent.getStringExtra("detailstatus"), getClass().getSimpleName());
            if (intent.getStringExtra("detailstatus").equals("CONNECTED")) {
                verifyCorrect(2);
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            Thread.sleep(5000);
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//
//
//                                }
//                            });
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }).start();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(AppConfig.getInstance());
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_seizevpn);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.stratagile.qlink.VPN_STATUS");
        registerReceiver(myBroadcastReceiver, filter);

        setTitle(getString(R.string.REGISTER_YOUR_VPN).toUpperCase());
        set2StepOne();
        originalVpnEntity = getIntent().getParcelableExtra("vpnEntity");
        newVpnEntity = originalVpnEntity;
        newVpnEntity.setAssetTranfer(Double.parseDouble(getIntent().getStringExtra("seizeQlc")));
        infoUserid.setText(originalVpnEntity.getVpnName());
    }

    @Override
    protected void setupActivityComponent() {
        DaggerSeizeVpnComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .seizeVpnModule(new SeizeVpnModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(SeizeVpnContract.SeizeVpnContractPresenter presenter) {
        mPresenter = (SeizeVpnPresenter) presenter;
    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void closeProgressDialog() {
        progressDialog.hide();
    }

    @OnClick({R.id.iv_step_one, R.id.iv_step_two, R.id.iv_step_three, R.id.button1, R.id.button2, R.id.et_country, R.id.et_configuration})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_step_one:
//                set2StepOne();
                break;
            case R.id.iv_step_two:
//                set2StepTwo();
                break;
            case R.id.iv_step_three:
//                set2StepThree();
                break;
            case R.id.button1:
                onBackPressed();
                break;
            case R.id.button2:
                if (currentItem == 0) {
                    set2StepTwo();
                } else if (currentItem == 1) {
                    set2StepThree();
                } else {
                    setFinish();
                }
                break;
            case R.id.et_country:
                Intent intent = new Intent(this, SelectContinentActivity.class);
                intent.putExtra("country", ConstantValue.longcountry);
                startActivityForResult(intent, SELECT_COUNTRY);
                break;
            case R.id.et_configuration:
                startActivityForResult(new Intent(this, FileChooseActivity.class), SELECT_PROFILE);
//                startImportConfigFilePicker();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myBroadcastReceiver);
    }


    private void set2StepOne() {
        cardViewStepOne.setVisibility(View.VISIBLE);
        cardViewStepTwo.setVisibility(View.GONE);
        cardViewStepThree.setVisibility(View.GONE);
        button2.setText(R.string.next);
        ivStepOne.setImageDrawable(getResources().getDrawable(R.mipmap.icon_step_one));
        ivStepTwo.setImageDrawable(getResources().getDrawable(R.mipmap.icon_step_two));
        ivStepThree.setImageDrawable(getResources().getDrawable(R.mipmap.icon_step_three));
        showAnimation(cardViewStepOne, cardViewStepTwo, Gravity.LEFT);
        currentItem = 0;
    }

    private void set2StepTwo() {
        if (etCountry.getText() == null || etCountry.getText().toString().equals("")) {
            ToastUtil.displayShortToast(getString(R.string.Please_choose_a_country));
            return;
        }
        cardViewStepOne.setVisibility(View.GONE);
        cardViewStepTwo.setVisibility(View.VISIBLE);
        cardViewStepThree.setVisibility(View.GONE);
        button2.setText(R.string.next);
        ivStepOne.setImageDrawable(getResources().getDrawable(R.mipmap.icon_step_one_completed));
        ivStepTwo.setImageDrawable(getResources().getDrawable(R.mipmap.icon_step_two_being));
        ivStepThree.setImageDrawable(getResources().getDrawable(R.mipmap.icon_step_three));
        if (currentItem == 0) {
            showAnimation(cardViewStepTwo, cardViewStepOne, Gravity.RIGHT);
        } else if (currentItem == 2) {
            showAnimation(cardViewStepTwo, cardViewStepThree, Gravity.LEFT);
        }
        currentItem = 1;
    }

    private void set2StepThree() {
        if (etConfiguration.getText().toString().trim().equals("")) {
            ToastUtil.displayShortToast(getString(R.string.Please_select_the_configuration_file));
            return;
        }
        cardViewStepOne.setVisibility(View.GONE);
        cardViewStepTwo.setVisibility(View.GONE);
        cardViewStepThree.setVisibility(View.VISIBLE);
        button2.setText(R.string.finish);
        ivStepOne.setImageDrawable(getResources().getDrawable(R.mipmap.icon_step_one_completed));
        ivStepTwo.setImageDrawable(getResources().getDrawable(R.mipmap.icon_step_two_complete));
        ivStepThree.setImageDrawable(getResources().getDrawable(R.mipmap.icon_step_three_being));
        showAnimation(cardViewStepThree, cardViewStepTwo, Gravity.RIGHT);
        currentItem = 2;
    }

    private void setFinish() {

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "beginSeizeVpn");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "beginSeizeVpn");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "beginSeizeVpn");
        mFirebaseAnalytics.logEvent("beginSeizeVpn", bundle);
        String currentTime = StringUitl.getNowDateShort();
        String currentTimeFlag = SpUtil.getString(AppConfig.getInstance(), currentTime+"_seizeVpn","0");
        if(currentTimeFlag.equals("0"))
        {
            bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "seizeVpnTotal");
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "seizeVpnTotal");
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "seizeVpnTotal");
            mFirebaseAnalytics.logEvent("seizeVpnTotal", bundle);
            SpUtil.putString(AppConfig.getInstance(), currentTime+"_seizeVpn","1");
        }
        showTestConnectDialog();
    }

    private void showAnimation(View intoView, View outView, int gravity) {
        if (gravity == Gravity.LEFT) {
            //从左边进入， 到右边
            intoView.setAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_left_to_center));
            outView.setAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_center_to_right));
        } else {
            //从右边进入， 到左边
            intoView.setAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_right_to_center));
            outView.setAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_center_to_left));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_COUNTRY && resultCode == RESULT_OK) {
            KLog.i(data.getStringExtra("country"));
            etCountry.setText(data.getStringExtra("country"));
            continent = data.getStringExtra("continent");
            newVpnEntity.setContinent(continent);
            return;
        }
//        if (resultCode == RESULT_VPN_DELETED) {
//        } else if (resultCode == RESULT_VPN_DUPLICATE && data != null) {
//            String profileUUID = data.getStringExtra(VpnProfile.EXTRA_PROFILEUUID);
//            if (profile != null) {
//                onAddOrDuplicateProfile(profile);
//            }
//        }


        if (resultCode != Activity.RESULT_OK) {
            return;
        }


        if (requestCode == START_VPN_CONFIG) {
            String configuredVPN = data.getStringExtra(VpnProfile.EXTRA_PROFILEUUID);

            VpnProfile profile = ProfileManager.get(this, configuredVPN);
            getPM().saveProfile(this, profile);
            // Name could be modified, reset List adapter
        } else if (requestCode == SELECT_PROFILE) {
            KLog.i("返回。。。。。。。SELECT_PROFILE");
//            String fileData = data.getStringExtra(FileSelect.RESULT_DATA);
            String fileData = data.getStringExtra("path");
            if (!fileData.contains(".ovpn")) {
                ToastUtil.displayShortToast(getString(R.string.configuration_profile_error));
                return;
            }
            KLog.i(fileData);
            Uri uri = new Uri.Builder().path(fileData).scheme("file").build();
            startConfigImport(uri);
        } else if (requestCode == IMPORT_PROFILE) {
            profileUUID = data.getStringExtra(VpnProfile.EXTRA_PROFILEUUID);
            profile = ProfileManager.get(this, profileUUID);
            etConfiguration.setText(ProfileManager.get(this, profileUUID).getName());
        } else if (requestCode == FILE_PICKER_RESULT_KITKAT) {
            KLog.i("返回。。。。FILE_PICKER_RESULT_KITKAT");
            if (data != null) {
                Uri uri = data.getData();
                startConfigImport(uri);
            }
        }

        if (requestCode == START_VPN_PROFILE) {
            KLog.i("onActivityResult的requestCode   为  START_VPN_PROFILE");
            mTransientCertOrPCKS12PW = etPrivateKeyPassword.getText().toString().trim();
            KLog.i("私钥为:" + mTransientCertOrPCKS12PW);
            mTransientAuthPW = etPassword.getText().toString().trim();
            profile.mUsername = etUsername.getText().toString().trim();
            profile.mPassword = etPassword.getText().toString().trim();
            profile.mKeyPassword = etPrivateKeyPassword.getText().toString().trim();
            if (resultCode == Activity.RESULT_OK) {
                KLog.i("开始检查配置文件的类型");
                int needpw = profile.needUserPWInput(mTransientCertOrPCKS12PW, mTransientAuthPW);
                if (needpw != 0) {
                    VpnStatus.updateStateString("USER_VPN_PASSWORD", "", R.string.state_user_vpn_password,
                            ConnectionStatus.LEVEL_WAITING_FOR_USER_INPUT);
                    getPasswordFromRemote(needpw);
                } else {
                    KLog.i("不需要密码，直接连接，，。");
                    SharedPreferences prefs = Preferences.getDefaultSharedPreferences(this);
                    boolean showLogWindow = prefs.getBoolean("showlogwindow", true);

                    ProfileManager.updateLRU(this, profile);
                    VPNLaunchHelper.startOpenVpn(profile, getBaseContext());
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // User does not want us to start, so we just vanish
                VpnStatus.updateStateString("USER_VPN_PERMISSION_CANCELLED", "", R.string.state_user_vpn_permission_cancelled,
                        ConnectionStatus.LEVEL_NOTCONNECTED);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    VpnStatus.logError(R.string.nought_alwayson_warning);
                }

                finish();
            }
        }
    }

    /**
     * vpn登录失败，缺少需要的东西
     *
     * @param type
     */
    private void getPasswordFromRemote(int type) {
        if (type == R.string.password) {
            KLog.i("需要用户名和密码， vpn登录失败");
            verifyVpnError(1);
        } else {
            KLog.i("需要私钥， vpn登录失败");
            verifyVpnError(0);
        }
    }

    private void startConfigImport(Uri uri) {
        if(uri == null)
        {
            return;
        }
        KLog.i(uri.getPath());
        File file = new File(uri.getPath());
        if (file.exists()) {
            KLog.i("file 存在");
            vpnFilePath = uri.getPath();
            Intent startImport = new Intent(this, ConfigConverter.class);
            startImport.setAction(ConfigConverter.IMPORT_PROFILE);
            startImport.setData(uri);
            mPathsegments = uri.getPathSegments();
            startActivityForResult(startImport, IMPORT_PROFILE);
            return;
        }
        KLog.i(uri.toString());
        KLog.i(uri.getScheme().toString());
        LogUtil.addLog("uri的path为：" + uri.getPath(), getClass().getSimpleName());
        String filename = "";
        if (uri.getScheme().toString().compareTo("content") == 0) {  //MediaStore.Images.Media.DATA
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                KLog.i(cursor.getCount());
                LogUtil.addLog("路径放在cursor的第" + cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA) + "列", getClass().getSimpleName());
                KLog.i("路径放在cursor的第" + cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA) + "列");
                if (cursor.moveToFirst()) {
                    int xiabiao = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);
                    if (xiabiao >= 0) {
                        try {
                            KLog.i(cursor.getString(cursor.getColumnIndex("_data")));
                            LogUtil.addLog("cursor的data为:" + cursor.getString(cursor.getColumnIndex("_data")), getClass().getSimpleName());
                            filename = cursor.getString(cursor.getColumnIndex("_data"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if ("".equals(filename)) {
                            try {
                                filename = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if ("".equals(filename)) {
                            try {
                                filename = cursor.getString(0);
                                KLog.i("cursor.getString(0):" + cursor.getString(0));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (filename.length() < 25) {
                            LogUtil.addLog("通过getpath获取路径", getClass().getSimpleName());
                            filename = uri.getPath();
                            if (filename.contains("/root")) {
                                filename = filename.replace("/root", "");
                            }
                        }
                        KLog.i(filename);
                    } else {
                    }
                    filename = uri.getPath();
                    if (filename.contains("/root")) {
                        filename = filename.replace("/root", "");
                    }
                }
            }
        } else if (uri.getScheme().toString().compareTo("file") == 0) {
            filename = uri.toString();
            filename = uri.toString().replace("file://", "");
        }
        if (filename == null || filename.equals("")) {
            KLog.i("文件未找到，重新生成路径.");
            filename = Environment.getExternalStorageDirectory() + "/" + uri.getPath().substring(uri.getPath().lastIndexOf(":") + 1, uri.getPath().length());
        }
        if (filename.contains("primary:")) {
            filename = Environment.getExternalStorageDirectory() + "/" + filename.substring(filename.lastIndexOf(":") + 1, filename.length());
        }
        LogUtil.addLog("注册vpn的配置文件解析出来的路径为：" + filename, getClass().getSimpleName());
        KLog.i(filename);
        vpnFilePath = filename;
        if (!filename.contains(".ovpn")) {
            ToastUtil.displayShortToast(getString(R.string.configuration_profile_error));
            return;
        }
        Intent startImport = new Intent(this, ConfigConverter.class);
        startImport.setAction(ConfigConverter.IMPORT_PROFILE);
        startImport.setData(uri);
        startActivityForResult(startImport, IMPORT_PROFILE);
    }

    private ProfileManager getPM() {
        return ProfileManager.getInstance(this);
    }

    private boolean startImportConfigFilePicker() {
        boolean startOldFileDialog = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            startOldFileDialog = !startFilePicker();
        }

        if (startOldFileDialog) {
            startImportConfig();
        }
        return true;
    }

    private void startImportConfig() {
        Intent intent = new Intent(this, FileSelect.class);
        intent.putExtra(FileSelect.NO_INLINE_SELECTION, true);
        intent.putExtra(FileSelect.WINDOW_TITLE, R.string.import_configuration_file);
        startActivityForResult(intent, SELECT_PROFILE);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private boolean startFilePicker() {
        Intent i = Utils.getFilePickerIntent(this, Utils.FileType.OVPN_CONFIG);
        if (i != null) {
            startActivityForResult(i, FILE_PICKER_RESULT_KITKAT);
            return true;
        } else {
            return false;
        }
    }

    private String mEmbeddedPwFile;
    private String mAliasName = null;
    private transient List<String> mPathsegments;
    private Map<Utils.FileType, FileSelectLayout> fileSelectMap = new HashMap<>();

    private void readFile(File configFile) {
        KLog.i("读取文件,开启子线程");
        ConfigParser cp = new ConfigParser();
        FileInputStream fim = null;
        try {
            fim = new FileInputStream(configFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            InputStreamReader isr = new InputStreamReader(fim);

            cp.parseConfig(isr);
            profile = cp.convertProfile();
            embedFiles(cp);
            return;

        } catch (IOException | ConfigParser.ConfigParseError e) {
            e.printStackTrace();
        }
        KLog.i("读取文件到流中成功");
    }

    void embedFiles(ConfigParser cp) {
        KLog.i("embedfiles1111");
        if (profile.mPKCS12Filename != null) {
            File pkcs12file = findFileRaw(profile.mPKCS12Filename);
            if (pkcs12file != null) {
                mAliasName = pkcs12file.getName().replace(".p12", "");
            } else {
                mAliasName = "Imported PKCS12";
            }
        }

        KLog.i("embedfiles2222");


        profile.mCaFilename = embedFile(profile.mCaFilename, Utils.FileType.CA_CERTIFICATE, false);
        profile.mClientCertFilename = embedFile(profile.mClientCertFilename, Utils.FileType.CLIENT_CERTIFICATE, false);
        profile.mClientKeyFilename = embedFile(profile.mClientKeyFilename, Utils.FileType.KEYFILE, false);
        profile.mTLSAuthFilename = embedFile(profile.mTLSAuthFilename, Utils.FileType.TLS_AUTH_FILE, false);
        profile.mPKCS12Filename = embedFile(profile.mPKCS12Filename, Utils.FileType.PKCS12, false);
        profile.mCrlFilename = embedFile(profile.mCrlFilename, Utils.FileType.CRL_FILE, true);
        KLog.i("embedfiles3333");
        if (cp != null) {
            mEmbeddedPwFile = cp.getAuthUserPassFile();
            mEmbeddedPwFile = embedFile(cp.getAuthUserPassFile(), Utils.FileType.USERPW_FILE, false);
        }
        KLog.i("embedfiles4444");
        saveProfile();
    }

    private File findFile(String filename, Utils.FileType fileType) {
        File foundfile = findFileRaw(filename);

        if (foundfile == null && filename != null && !filename.equals("")) {
        }
        fileSelectMap.put(fileType, null);

        return foundfile;
    }


    private File findFileRaw(String filename) {
        if (filename == null || filename.equals("")) {
            return null;
        }

        // Try diffent path relative to /mnt/sdcard
        File sdcard = Environment.getExternalStorageDirectory();
        File root = new File("/");

        HashSet<File> dirlist = new HashSet<>();

        for (int i = mPathsegments.size() - 1; i >= 0; i--) {
            String path = "";
            for (int j = 0; j <= i; j++) {
                path += "/" + mPathsegments.get(j);
            }
            // Do a little hackish dance for the Android File Importer
            // /document/primary:ovpn/qlink-imt.conf


            if (path.indexOf(':') != -1 && path.lastIndexOf('/') > path.indexOf(':')) {
                String possibleDir = path.substring(path.indexOf(':') + 1, path.length());
                // Unquote chars in the  path
                try {
                    possibleDir = URLDecoder.decode(possibleDir, "UTF-8");
                } catch (UnsupportedEncodingException ignored) {
                }

                possibleDir = possibleDir.substring(0, possibleDir.lastIndexOf('/'));


                dirlist.add(new File(sdcard, possibleDir));

            }
            dirlist.add(new File(path));


        }
        dirlist.add(sdcard);
        dirlist.add(root);


        String[] fileparts = filename.split("/");
        for (File rootdir : dirlist) {
            String suffix = "";
            for (int i = fileparts.length - 1; i >= 0; i--) {
                if (i == fileparts.length - 1) {
                    suffix = fileparts[i];
                } else {
                    suffix = fileparts[i] + "/" + suffix;
                }

                File possibleFile = new File(rootdir, suffix);
                if (possibleFile.canRead()) {
                    return possibleFile;
                }

            }
        }
        return null;
    }

    private void saveProfile() {
        KLog.i("保存配置文件");
        Intent result = new Intent();
        ProfileManager vpl = ProfileManager.getInstance(this);

        if (!TextUtils.isEmpty(mEmbeddedPwFile)) {
            ConfigParser.useEmbbedUserAuth(profile, mEmbeddedPwFile);
        }

        vpl.addProfile(profile);
        vpl.saveProfile(this, profile);
        vpl.saveProfileList(this);
        newVpnEntity.setProfileUUid(profile.getUUIDString());
        KLog.i(profile.getUUIDString());
        AppConfig.getInstance().getDaoSession().getVpnEntityDao().insert(newVpnEntity);
        //更新sd卡资产数据begin
        MyAsset myAsset = new MyAsset();
        myAsset.setType(1);
        myAsset.setVpnEntity(newVpnEntity);
        LocalAssetsUtils.updateLocalAssets(myAsset);
        result.putExtra(VpnProfile.EXTRA_PROFILEUUID, profile.getUUID().toString());
        startOrStopVPN(profile);
    }

    private void startOrStopVPN(VpnProfile profile) {
        KLog.i("开始连接vpn了");

        if (VpnStatus.isVPNActive() && profile.getUUIDString().equals(VpnStatus.getLastConnectedVPNProfile())) {
            Intent disconnectVPN = new Intent(this, DisconnectVPN.class);
            startActivity(disconnectVPN);
        } else {
            if (Preferences.getDefaultSharedPreferences(this).getBoolean(CLEARLOG, true)) {
                VpnStatus.clearLog();
            }

            // we got called to be the starting point, most likely a shortcut
            String shortcutUUID = profile.getUUID().toString();
            String shortcutName = profile.getName().toString();

            VpnProfile profileToConnect = ProfileManager.get(this, shortcutUUID);
            if (shortcutName != null && profileToConnect == null) {
                profileToConnect = ProfileManager.getInstance(this).getProfileByName(shortcutName);
                if (!(new ExternalAppDatabase(this).checkRemoteActionPermission(this))) {
                    finish();
                    return;
                }
            }
            KLog.i("startOrStopVPN  2222");

            if (profileToConnect == null) {
                VpnStatus.logError(R.string.shortcut_profile_notfound);
                KLog.i("startOrStopVPN  3333");
                // show Log window to display error
//                finish();
            } else {
//                mSelectedProfile = profileToConnect;
                launchVPN();
            }
        }
    }

    /**
     * 验证或者登录vpn出错了，错误类型
     * 0， 私钥错误
     * 1， 用户名或者密码错误
     * 2， 配置文件出错
     * 3,  连接出错，比如，远程服务器未响应
     *
     * @param type
     */
    private void verifyVpnError(int type) {
        verifyError(2);
        switch (type) {
            case 0:
                tvPrivatekeyFlag.setText(R.string.invalid_Private_Key_Password_Try_gain);
                tvPrivatekeyFlag.setTextColor(Color.parseColor("#bc0000"));
                break;
            case 1:
                tvUsernameFlag.setText(R.string.Username_username_or_password_error);
                tvUsernameFlag.setTextColor(Color.parseColor("#bc0000"));
                break;
            case 2:
                break;
            case 3:
                break;
            default:
                break;
        }
    }

    void launchVPN() {
        KLog.i("开启vpn");
        int vpnok = profile.checkProfile(this);
        if (vpnok != R.string.no_error_found) {
            verifyVpnError(2);
            KLog.i("连接vpn出现错误");
            return;
        }

        KLog.i("校验配置文件没有出现错误");

        Intent intent = VpnService.prepare(this);
        // Check if we want to fix /dev/tun
        SharedPreferences prefs = Preferences.getDefaultSharedPreferences(this);
        boolean usecm9fix = prefs.getBoolean("useCM9Fix", false);
        boolean loadTunModule = prefs.getBoolean("loadTunModule", false);

        if (intent != null) {
            VpnStatus.updateStateString("USER_VPN_PERMISSION", "", R.string.state_user_vpn_permission,
                    ConnectionStatus.LEVEL_WAITING_FOR_USER_INPUT);
            // Start the query
            try {
                KLog.i("开启vpn的服务");
                startActivityForResult(intent, START_VPN_PROFILE);
            } catch (ActivityNotFoundException ane) {
                // Shame on you Sony! At least one user reported that
                // an official Sony Xperia Arc S image triggers this exception
                VpnStatus.logError(R.string.no_vpn_support_image);
//                showLogWindow();
            }
        } else {
            KLog.i("开启vpn的intent为空");
            onActivityResult(START_VPN_PROFILE, Activity.RESULT_OK, null);
        }
    }

    /**
     * 验证错误，position为第几个项验证不通过
     *
     * @param position
     */
    private void verifyError(int position) {
        if (testConnectDialog == null) {
            return;
        }
        switch (position) {
            case 0:
                testConnectDialog.stopAnimation(0, false);
                break;
            case 1:
                testConnectDialog.stopAnimation(1, false);
                break;
            case 2:
                testConnectDialog.stopAnimation(2, false);
                break;
            case 3:
                testConnectDialog.stopAnimation(3, false);
                break;
            default:
                break;
        }
    }

    /**
     * 处理正确
     *
     * @param position
     */
    private void verifyCorrect(int position) {
        if (testConnectDialog == null) {
            return;
        }
        switch (position) {
            case 0:
                testConnectDialog.stopAnimation(0, true);
                break;
            case 1:
                testConnectDialog.stopAnimation(1, true);
                break;
            case 2:
                testConnectDialog.stopAnimation(2, true);
                testBandWidth();
                break;
            case 3:
                testConnectDialog.stopAnimation(3, true);
                startRegisterVpn();
                break;
            default:
                break;
        }
    }

    /**
     * 验证通过，开始注册vpn
     */
    private void startRegisterVpn() {
        newVpnEntity.setUserId(infoUserid.getText().toString());
        newVpnEntity.setPassword(etPassword.getText().toString());
        newVpnEntity.setUsername(etUsername.getText().toString());
        newVpnEntity.setIpV4Address(profile.mIPv4Address);
        newVpnEntity.setOnline(true);
        newVpnEntity.setProfileUUid(profileUUID);
        newVpnEntity.setConfiguration(etConfiguration.getText().toString());
        newVpnEntity.setPrivateKeyPassword(etPrivateKeyPassword.getText().toString().trim());
        newVpnEntity.setP2pId(SpUtil.getString(this, ConstantValue.P2PID, ""));
        newVpnEntity.setAvaterUpdateTime(Long.parseLong(SpUtil.getString(this, ConstantValue.myAvaterUpdateTime, "0")));
        newVpnEntity.setConnectMaxnumber(connectSeekbar.getProgress());
        newVpnEntity.setPrice(Float.parseFloat((qlcSeekbar.getProgress() / 10.0) + ""));
        newVpnEntity.setProfileLocalPath(vpnFilePath);
        newVpnEntity.setFriendNum("");
        newVpnEntity.setQlc(Float.parseFloat((qlcSeekbar.getProgress() / 10.0) + ""));
//        AppConfig.getInstance().getDaoSession().getVpnEntityDao().update(vpnEntity);
        //更新sd卡资产数据end
        String walletAddress = AppConfig.getInstance().getDaoSession().getWalletDao().loadAll().get(SpUtil.getInt(this, ConstantValue.currentWallet, 0)).getAddress();

        Map<String, String> map = new HashMap<>();
        map.put("vpnName", infoUserid.getText().toString());
        map.put("country", etCountry.getText().toString());
        map.put("p2pId", SpUtil.getString(this, ConstantValue.P2PID, ""));
        map.put("qlc", newVpnEntity.getAssetTranfer() + "");
        map.put("address", AppConfig.getInstance().getDaoSession().getWalletDao().loadAll().get(SpUtil.getInt(this, ConstantValue.currentWallet, 0)).getAddress());
//        map.put("wif", AppConfig.getInstance().getDaoSession().getWalletDao().loadAll().get(SpUtil.getInt(this, ConstantValue.currentWallet, 0)).getWif());
        map.put("connectCost", newVpnEntity.getQlc() + "");
        map.put("connectNum", newVpnEntity.getConnectMaxnumber() + "");
        map.put("ipV4Address", newVpnEntity.getIpV4Address() == null ? "" : newVpnEntity.getIpV4Address());
        map.put("bandWidth", newVpnEntity.getBandwidth() == null? "" : newVpnEntity.getBandwidth());
        map.put("profileLocalPath", newVpnEntity.getProfileLocalPath() == null? "" : newVpnEntity.getProfileLocalPath());
        KLog.i(map);
        showProgressDialog();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                    TransactionApi.getInstance().registerVPN(map, walletAddress, ConstantValue.mainAddress, newVpnEntity.getAssetTranfer() + "", new SendBackWithTxId() {
                        @Override
                        public void onSuccess(String txid) {
                            registVpnSuccess();
                        }

                        @Override
                        public void onFailure() {

                        }
                    }, newVpnEntity);
//                    mPresenter.registerVpn(map, infoUserid.getText().toString());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void registVpnSuccess() {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "seizeVpnSuccess");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "seizeVpnSuccess");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "seizeVpnSuccess");
        mFirebaseAnalytics.logEvent("seizeVpnSuccess", bundle);
        CustomPopWindow.onBackPressed();
        getMenuInflater().inflate(R.menu.connect_vpn, toolbar.getMenu());
        button2.setText(R.string.update);
        EventBus.getDefault().post(new VpnRegisterSuccess());
        ToastUtil.displayShortToast(getString(R.string.Register_Vpn_Asset_Success));
        tvPrivatekeyFlag.setText(getResources().getString(R.string.private_key_password));
        tvPrivatekeyFlag.setTextColor(Color.parseColor("#a8a6ae"));
        tvUsernameFlag.setText(R.string.username);
        tvUsernameFlag.setTextColor(Color.parseColor("#a8a6ae"));
        int groupNum = qlinkcom.CreatedNewGroupChat();
        newVpnEntity.setGroupNum(groupNum);
        AppConfig.getInstance().getDaoSession().getVpnEntityDao().update(newVpnEntity);

        //更新sd卡资产数据begin
        MyAsset myAsset = new MyAsset();
        myAsset.setType(1);
        myAsset.setVpnEntity(newVpnEntity);
        LocalAssetsUtils.insertLocalAssets(myAsset);

        Intent intent = new Intent(this, RegisterVpnSuccessActivity.class);
        intent.putExtra("vpnentity", newVpnEntity);
        startActivity(intent);
        //断开vpn
        Intent intent2 = new Intent();
        intent2.setAction(BroadCastAction.disconnectVpn);
        KLog.i("断开连接");
        sendBroadcast(intent2);
        finish();
    }

    private void testBandWidth() {
        verifyCorrect(3);
    }

    private String embedFile(String filename, Utils.FileType type, boolean onlyFindFileAndNullonNotFound) {
        if (filename == null) {
            return null;
        }

        // Already embedded, nothing to do
        if (VpnProfile.isEmbedded(filename)) {
            return filename;
        }

        File possibleFile = findFile(filename, type);
        if (possibleFile == null)
            if (onlyFindFileAndNullonNotFound)
                return null;
            else
                return filename;
        else if (onlyFindFileAndNullonNotFound)
            return possibleFile.getAbsolutePath();
        else
            return readFileContent(possibleFile, type == Utils.FileType.PKCS12);

    }

    String readFileContent(File possibleFile, boolean base64encode) {
        byte[] filedata;
        try {
            filedata = readBytesFromFile(possibleFile);
        } catch (IOException e) {
            return null;
        }

        String data;
        if (base64encode) {
            data = Base64.encodeToString(filedata, Base64.DEFAULT);
        } else {
            data = new String(filedata);

        }

        return VpnProfile.DISPLAYNAME_TAG + possibleFile.getName() + VpnProfile.INLINE_TAG + data;

    }

    private byte[] readBytesFromFile(File file) throws IOException {
        InputStream input = new FileInputStream(file);

        long len = file.length();
        if (len > VpnProfile.MAX_EMBED_FILE_SIZE)
            throw new IOException("File size of file to import too large.");

        // Create the byte array to hold the data
        byte[] bytes = new byte[(int) len];

        // Read in the bytes
        int offset = 0;
        int bytesRead;
        while (offset < bytes.length
                && (bytesRead = input.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += bytesRead;
        }

        input.close();
        return bytes;
    }

    /**
     * 显示测试连接vpn的dialog
     */
    private void showTestConnectDialog() {
        testConnectDialog = new TestConnectDialog(this, button1);
        testConnectDialog.show();
        verifyConfigurationProfile();
    }

    private void verifyConfigurationProfile() {
        KLog.i(vpnFilePath);
        File configurationFile = new File(vpnFilePath);
        if (configurationFile.exists()) {
            KLog.i(configurationFile.getParent());
            KLog.i(configurationFile.getName());
            copyFile(configurationFile.getPath(), Environment.getExternalStorageDirectory() + "/Qlink/" + Calendar.getInstance().getTimeInMillis() + "." + configurationFile.getName().substring(configurationFile.getName().lastIndexOf(".") + 1));
        } else {
            verifyError(1);
        }
    }

    /**
     * 复制单个文件
     *
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    public void copyFile(String oldPath, String newPath) {
        KLog.i("复制文件。。。。。。");
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
            testConnectDialog.stopAnimation(1, true);
            vpnFilePath = newPath;
            LogUtil.addLog("配置文件复制到 " + newPath + " 成功", getClass().getSimpleName());
            verifyConnectVpn();
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            LogUtil.addLog("配置文件复制到 " + newPath + " 失败", getClass().getSimpleName());
            e.printStackTrace();
            verifyError(1);
        }

    }

    private void verifyConnectVpn() {
        startOrStopVPN(profile);
    }
}