package com.stratagile.qlink.ui.activity.vpn;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.Uri;
import android.net.VpnService;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.VpnProfile;
import com.stratagile.qlink.activities.DisconnectVPN;
import com.stratagile.qlink.api.ExternalAppDatabase;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.constant.BroadCastAction;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.core.ConfigParser;
import com.stratagile.qlink.core.ConnectionStatus;
import com.stratagile.qlink.core.IServiceStatus;
import com.stratagile.qlink.core.OpenVPNStatusService;
import com.stratagile.qlink.core.PasswordCache;
import com.stratagile.qlink.core.Preferences;
import com.stratagile.qlink.core.ProfileManager;
import com.stratagile.qlink.core.VPNLaunchHelper;
import com.stratagile.qlink.core.VpnStatus;
import com.stratagile.qlink.data.api.API;
import com.stratagile.qlink.data.api.MainAPI;
import com.stratagile.qlink.db.VpnEntity;
import com.stratagile.qlink.entity.Balance;
import com.stratagile.qlink.entity.eventbus.CheckConnectRsp;
import com.stratagile.qlink.entity.eventbus.DisconnectVpn;
import com.stratagile.qlink.entity.eventbus.ShowGuide;
import com.stratagile.qlink.entity.eventbus.VpnSendEnd;
import com.stratagile.qlink.entity.vpn.VpnPrivateKeyRsp;
import com.stratagile.qlink.entity.vpn.VpnUserAndPasswordRsp;
import com.stratagile.qlink.fragments.Utils;
import com.stratagile.qlink.guideview.Guide;
import com.stratagile.qlink.guideview.GuideBuilder;
import com.stratagile.qlink.guideview.GuideConstantValue;
import com.stratagile.qlink.guideview.GuideSpUtil;
import com.stratagile.qlink.guideview.compnonet.ChooseCountryComponent;
import com.stratagile.qlink.guideview.compnonet.ConnectVpnComponent;
import com.stratagile.qlink.qlink.Qsdk;
import com.stratagile.qlink.qlinkcom;
import com.stratagile.qlink.service.ClientVpnService;
import com.stratagile.qlink.ui.activity.vpn.component.DaggerConnectVpnComponent;
import com.stratagile.qlink.ui.activity.vpn.contract.ConnectVpnContract;
import com.stratagile.qlink.ui.activity.vpn.module.ConnectVpnModule;
import com.stratagile.qlink.ui.activity.vpn.presenter.ConnectVpnPresenter;
/*import com.stratagile.qlink.utils.CountDownTimerUtils;*/
import com.stratagile.qlink.utils.LogUtil;
import com.stratagile.qlink.utils.QlinkUtil;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.StringUitl;
import com.stratagile.qlink.utils.ToastUtil;
import com.stratagile.qlink.views.FileSelectLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.stratagile.qlink.LaunchVPN.CLEARLOG;
import static com.stratagile.qlink.LaunchVPN.START_VPN_PROFILE;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.vpn
 * @Description: $description
 * @date 2018/02/08 16:38:02
 */

public class ConnectVpnActivity extends BaseActivity implements ConnectVpnContract.View {

    @Inject
    ConnectVpnPresenter mPresenter;
    @BindView(R.id.iv_user_avater)
    ImageView ivUserAvater;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_avater)
    ImageView ivAvater;
    @BindView(R.id.tv_ssid)
    TextView tvSsid;
    @BindView(R.id.tv_connect_count)
    TextView tvConnectCount;
    @BindView(R.id.bt_cancal)
    Button btCancal;
    @BindView(R.id.bt_connect)
    Button btConnect;
    @BindView(R.id.tv_sure)
    TextView tvSure;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_country)
    TextView tvCountry;
    @BindView(R.id.remove_all_profile)
    TextView removeAllProfile;
    @BindView(R.id.tv_continent)
    TextView tvContinent;
    @BindView(R.id.tv_ip)
    TextView tvIp;
    @BindView(R.id.tv_band_width)
    TextView tvBandWidth;

    private VpnEntity vpnEntity;

    private MyBroadcastReceiver myBroadcastReceiver = new MyBroadcastReceiver();

    private Timer timer;

    /*private static CountDownTimerUtils countDownTimerUtils;*/

    private FirebaseAnalytics mFirebaseAnalytics;

    int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        needFront = true;
        flag = 0;
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.stratagile.qlink.VPN_STATUS");
        registerReceiver(myBroadcastReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        mFirebaseAnalytics = null;
        EventBus.getDefault().unregister(this);
        unregisterReceiver(myBroadcastReceiver);
        super.onDestroy();
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_connect_vpn);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        tvTitle.setText(getString(R.string.vpn).toUpperCase());
        vpnEntity = getIntent().getParcelableExtra("vpnentity");
        checkSharerConnect();
        tvSure.setText(getString(R.string.ARE_YOU_SURE_YOU_WANT_TO_SPEND_QLC_TO_CONNECT, vpnEntity.getQlc() + ""));
        tvSsid.setText(vpnEntity.getVpnName());
        tvCountry.setText(vpnEntity.getCountry());
        tvIp.setText(vpnEntity.getIpV4Address());
        tvPrice.setText(vpnEntity.getQlc() + " QLC");
        tvConnectCount.setText(vpnEntity.getCurrentConnect() + "/" + vpnEntity.getConnectMaxnumber());
        int status = qlinkcom.GetFriendConnectionStatus(vpnEntity.getFriendNum());
//        ToastUtil.displayShortToast(status + "");
        if (vpnEntity.getContinent() != null) {
            tvContinent.setText(vpnEntity.getContinent());
        }
        if (!SpUtil.getString(this, ConstantValue.myAvatarPath, "").equals("")) {
            if (SpUtil.getBoolean(this, ConstantValue.isMainNet, false)) {
                Glide.with(this)
                        .load(MainAPI.MainBASE_URL + SpUtil.getString(this, ConstantValue.myAvatarPath, "").replace("\\", "/"))
                        .apply(AppConfig.getInstance().options)
                        .into(ivUserAvater);
            } else {
                Glide.with(this)
                        .load(API.BASE_URL + SpUtil.getString(this, ConstantValue.myAvatarPath, "").replace("\\", "/"))
                        .apply(AppConfig.getInstance().options)
                        .into(ivUserAvater);
            }
        } else {
            Glide.with(this)
                    .load(R.mipmap.img_connected_head_portrait)
                    .apply(AppConfig.getInstance().options)
                    .into(ivUserAvater);
        }
        if (vpnEntity.getAvatar() != null && !vpnEntity.getAvatar().equals("")) {
            if (SpUtil.getBoolean(this, ConstantValue.isMainNet, false)) {
                Glide.with(this)
                        .load(MainAPI.MainBASE_URL + vpnEntity.getAvatar().replace("\\", "/"))
                        .apply(AppConfig.getInstance().options)
                        .into(ivAvater);
            } else {
                Glide.with(this)
                        .load(API.BASE_URL + vpnEntity.getAvatar().replace("\\", "/"))
                        .apply(AppConfig.getInstance().options)
                        .into(ivAvater);
            }
        } else {
            Glide.with(this)
                    .load(R.mipmap.img_connected_head_portrait)
                    .apply(AppConfig.getInstance().options)
                    .into(ivAvater);
        }
    }

    /**
     * 检查和分享者的连接情况
     */
    private void checkSharerConnect() {
        if (qlinkcom.GetP2PConnectionStatus() > 0) {
            if (vpnEntity.getFriendNum() == null || "".equals(vpnEntity.getFriendNum())) {
                byte[] p2pId = new byte[100];
                String friendNumStr = "";
                qlinkcom.GetFriendP2PPublicKey(vpnEntity.getP2pId(), p2pId);
                friendNumStr = new String(p2pId).trim();
                KLog.i(friendNumStr);
                if (friendNumStr == null) {
                    friendNumStr = "";
                }
                vpnEntity.setFriendNum(friendNumStr);
            }
            if (qlinkcom.GetFriendConnectionStatus(vpnEntity.getFriendNum()) > 0) {
                showProgressDialog();
                QlinkUtil.parseMap2StringAndSend(vpnEntity.getFriendNum(), ConstantValue.checkConnectReq, new HashMap());
                handler.sendEmptyMessage(1);
            } else {
                ToastUtil.displayShortToast(getString(R.string.The_friend_is_not_online));
            }
        } else {
            ToastUtil.displayShortToast(getString(R.string.you_offline));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void checkSharerConnectRsp(CheckConnectRsp checkConnectRsp) {
        closeProgressDialog();
        handler.sendEmptyMessage(0);
        if (vpnEntity.getProfileLocalPath() != null && !vpnEntity.getProfileLocalPath().equals("")) {
            ToastUtil.displayShortToast(getString(R.string.Connect_with_Sharer_Success_You_Can_Connect_this_VPN_Asset));
            showGuideViewConnectVpn();
        } else {
            ToastUtil.displayShortToast(getString(R.string.data_damage_of_this_asset));
        }
    }

    @Override
    protected void setupActivityComponent() {
        DaggerConnectVpnComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .connectVpnModule(new ConnectVpnModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(ConnectVpnContract.ConnectVpnContractPresenter presenter) {
        mPresenter = (ConnectVpnPresenter) presenter;
    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void closeProgressDialog() {
        progressDialog.hide();
    }

    /**
     * @see com.stratagile.qlink.ui.activity.vpn.VpnListFragment#updateVpnList
     */
    @Override
    public void onRecordSuccess(boolean upUI) {
        LogUtil.addLog("扣费成功，开始跳转页面", getClass().getSimpleName());
        if (ConstantValue.isConnectVpn) {
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "ConnectVpnSuccessActivity");
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "ConnectVpnSuccessActivity");
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "ConnectVpnSuccessActivity");
            if(mFirebaseAnalytics != null){
                mFirebaseAnalytics.logEvent("ConnectVpnSuccessActivity", bundle);
            }
            ConstantValue.isConnectVpn = false;
        }
        closeProgressDialog();
        Intent intent = new Intent(getBaseContext(), ConnectVpnSuccessActivity.class);
        intent.putExtra("vpnentity", vpnEntity);
        vpnEntity.setIsConnected(true);
        AppConfig.currentUseVpn.setIsConnected(true);
        KLog.i("当前：" + vpnEntity.getVpnName());
        KLog.i("当前：" + AppConfig.currentUseVpn.getVpnName());
        AppConfig.getInstance().getDaoSession().getVpnEntityDao().update(vpnEntity);
        if (upUI) {
            EventBus.getDefault().post(new ArrayList<VpnEntity>());
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onGetBalancelSuccess(Balance balance) {
        if (Float.parseFloat(balance.getData().getQLC() + "") >= vpnEntity.getQlc() && balance.getData().getGAS() > 0.0001) {
            preConnect();
        } else {
            ToastUtil.displayShortToast(getString(R.string.Not_enough_QLC_Or_GAS));
            closeProgressDialog();
        }
    }

    @OnClick({R.id.bt_cancal, R.id.bt_connect, R.id.remove_all_profile})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_cancal:
//                KLog.i("断开连接");
//                Intent intent = new Intent();
//                intent.setAction("com.stratagile.qlink.disconnectVpn");
//                sendBroadcast(intent);
                finish();
                break;
            case R.id.bt_connect:
                showProgressDialog();
                Map<String, String> map = new HashMap<>();
                map.put("address", AppConfig.getInstance().getDaoSession().getWalletDao().loadAll().get(SpUtil.getInt(this, ConstantValue.currentWallet, 0)).getAddress());
                mPresenter.getBalance(map);
                break;
            case R.id.remove_all_profile:
                getPasswordFromRemote(01);
                break;
            default:
                break;
        }
    }

    private VpnProfile mResult;
    private String mEmbeddedPwFile;
    private String mAliasName = null;
    private transient List<String> mPathsegments;
    private Map<Utils.FileType, FileSelectLayout> fileSelectMap = new HashMap<>();

    private void preConnect() {
        ConstantValue.isConnectVpn = true;
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "beginConnectVpn");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "beginConnectVpn");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "beginConnectVpn");
        mFirebaseAnalytics.logEvent("beginConnectVpn", bundle);
        String currentTime = StringUitl.getNowDateShort();
        String currentTimeFlag = SpUtil.getString(AppConfig.getInstance(), currentTime + "_vpn", "0");
        if (currentTimeFlag.equals("0")) {
            bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "useVpnTotal");
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "useVpnTotal");
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "useVpnTotal");
            mFirebaseAnalytics.logEvent("useVpnTotal", bundle);
            SpUtil.putString(AppConfig.getInstance(), currentTime + "_vpn", "1");
        }
        startService(new Intent(this, ClientVpnService.class));
        AppConfig.currentVpnUseType = 1;
        AppConfig.currentUseVpn = vpnEntity;
        KLog.i(AppConfig.currentVpnUseType);
        KLog.i(AppConfig.currentUseVpn.getVpnName());
        KLog.i(vpnEntity.getProfileLocalPath());
        if (vpnEntity.getProfileLocalPath() == null) {
            ToastUtil.displayShortToast(getResources().getString(R.string.import_content_resolve_error));
            closeProgressDialog();
            return;
        }
        File configFile = new File(vpnEntity.getProfileLocalPath());
        if (configFile.exists()) {
//            ToastUtil.displayShortToast("文件存在");
            configFile.delete();
//            readFile(configFile);
        } else {
        }
        Qsdk.getInstance().sendVpnFileRequest(vpnEntity.getFriendNum(), vpnEntity.getProfileLocalPath(), vpnEntity.getVpnName());
        handler.sendEmptyMessage(1);
        flag = 0;
    }

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
            mResult = cp.convertProfile();
            embedFiles(cp);
            return;

        } catch (IOException | ConfigParser.ConfigParseError e) {
            e.printStackTrace();
        }
        KLog.i("读取文件到流中成功");
    }

    void embedFiles(ConfigParser cp) {
        KLog.i("embedfiles1111");
        if (mResult.mPKCS12Filename != null) {
            File pkcs12file = findFileRaw(mResult.mPKCS12Filename);
            if (pkcs12file != null) {
                mAliasName = pkcs12file.getName().replace(".p12", "");
            } else {
                mAliasName = "Imported PKCS12";
            }
        }

        KLog.i("embedfiles2222");


        mResult.mCaFilename = embedFile(mResult.mCaFilename, Utils.FileType.CA_CERTIFICATE, false);
        mResult.mClientCertFilename = embedFile(mResult.mClientCertFilename, Utils.FileType.CLIENT_CERTIFICATE, false);
        mResult.mClientKeyFilename = embedFile(mResult.mClientKeyFilename, Utils.FileType.KEYFILE, false);
        mResult.mTLSAuthFilename = embedFile(mResult.mTLSAuthFilename, Utils.FileType.TLS_AUTH_FILE, false);
        mResult.mPKCS12Filename = embedFile(mResult.mPKCS12Filename, Utils.FileType.PKCS12, false);
        mResult.mCrlFilename = embedFile(mResult.mCrlFilename, Utils.FileType.CRL_FILE, true);
        KLog.i("embedfiles3333");
        if (cp != null) {
            mEmbeddedPwFile = cp.getAuthUserPassFile();
            mEmbeddedPwFile = embedFile(cp.getAuthUserPassFile(), Utils.FileType.USERPW_FILE, false);
        }
        KLog.i("embedfiles4444");
        saveProfile();
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

    private File findFile(String filename, Utils.FileType fileType) {
        File foundfile = findFileRaw(filename);

        if (foundfile == null && filename != null && !filename.equals("")) {
        }
        fileSelectMap.put(fileType, null);

        return foundfile;
    }


    private File findFileRaw(String filename) {
        if (filename == null || filename.equals(""))
            return null;

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
                if (i == fileparts.length - 1)
                    suffix = fileparts[i];
                else
                    suffix = fileparts[i] + "/" + suffix;

                File possibleFile = new File(rootdir, suffix);
                if (possibleFile.canRead())
                    return possibleFile;

            }
        }
        return null;
    }

    private void saveProfile() {
        KLog.i("保存配置文件");
        Intent result = new Intent();
        ProfileManager vpl = ProfileManager.getInstance(this);

        if (!TextUtils.isEmpty(mEmbeddedPwFile))
            ConfigParser.useEmbbedUserAuth(mResult, mEmbeddedPwFile);

        vpl.addProfile(mResult);
        vpl.saveProfile(this, mResult);
        vpl.saveProfileList(this);
        result.putExtra(VpnProfile.EXTRA_PROFILEUUID, mResult.getUUID().toString());
        startOrStopVPN(mResult);
//        setResult(RESULT_OK, result);
//        finish();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleConfigFile(VpnSendEnd vpnSendEnd) {
        KLog.i("c层的vpn配置文件传输完毕了，");
        String newPath = Environment.getExternalStorageDirectory() + "/Qlink/vpn";
        String fileName = "";
        if (vpnEntity.getProfileLocalPath().contains("/")) {
            fileName = vpnEntity.getProfileLocalPath().substring(vpnEntity.getProfileLocalPath().lastIndexOf("/"), vpnEntity.getProfileLocalPath().length());
        } else {
            fileName = "/" + vpnEntity.getProfileLocalPath();
        }
        File configFile = new File(newPath + fileName);
        Uri uri = new Uri.Builder().path(newPath + fileName).scheme("file").build();
        mPathsegments = uri.getPathSegments();
        handler.sendEmptyMessage(0);
        if (configFile.exists()) {
//            ToastUtil.displayShortToast("文件传送过来了");
            List<VpnEntity> vpnEntityList = AppConfig.getInstance().getDaoSession().getVpnEntityDao().loadAll();
            for (VpnEntity vpnEntity : vpnEntityList) {
                if (vpnEntity.getIsConnected()) {
                    vpnEntity.setIsConnected(false);
                    AppConfig.getInstance().getDaoSession().getVpnEntityDao().update(vpnEntity);
                }
            }
            EventBus.getDefault().post(new DisconnectVpn());
            readFile(configFile);
        } else {
            ToastUtil.displayShortToast(getString(R.string.Cannot_Read_Configuration_Profilefile));
            closeProgressDialog();
        }
    }

    private void startOrStopVPN(VpnProfile profile) {
        if (VpnStatus.isVPNActive() && profile.getUUIDString().equals(VpnStatus.getLastConnectedVPNProfile())) {
            Intent disconnectVPN = new Intent(this, DisconnectVPN.class);
            startActivity(disconnectVPN);
        } else {
            if (Preferences.getDefaultSharedPreferences(this).getBoolean(CLEARLOG, true))
                VpnStatus.clearLog();

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
     * 密码
     */
    private String mTransientAuthPW;
    /**
     * 私钥
     */
    private String mTransientCertOrPCKS12PW;
    private boolean mhideLog = false;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == START_VPN_PROFILE) {
            KLog.i("onActivityResult的requestCode   为  START_VPN_PROFILE");
            if (resultCode == Activity.RESULT_OK) {
                KLog.i("开始检查配置文件的类型");
                int needpw = mResult.needUserPWInput(mTransientCertOrPCKS12PW, mTransientAuthPW);
                if (needpw != 0) {
                    VpnStatus.updateStateString("USER_VPN_PASSWORD", "", R.string.state_user_vpn_password,
                            ConnectionStatus.LEVEL_WAITING_FOR_USER_INPUT);
//                    askForPW(needpw);
                    getPasswordFromRemote(needpw);
                } else {
                    KLog.i("不需要密码，直接连接，，。");
                    SharedPreferences prefs = Preferences.getDefaultSharedPreferences(this);
                    boolean showLogWindow = prefs.getBoolean("showlogwindow", true);

                    if (!mhideLog && showLogWindow)
//                        showLogWindow();
                        ProfileManager.updateLRU(this, mResult);
                    VPNLaunchHelper.startOpenVpn(mResult, getBaseContext());
                    startListener();
//                    finish();
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                // User does not want us to start, so we just vanish
                VpnStatus.updateStateString("USER_VPN_PERMISSION_CANCELLED", "", R.string.state_user_vpn_permission_cancelled,
                        ConnectionStatus.LEVEL_NOTCONNECTED);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    VpnStatus.logError(R.string.nought_alwayson_warning);

                finish();
            }
        }
    }

    private void startListener() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(20000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (vpnDetailstatus != null && vpnDetailstatus.contains("CONNECTED")) {
                    KLog.i("时间到了，已经连接了");
                } else if (vpnDetailstatus != null) {
                    closeProgressDialog();
                    if (ConstantValue.isConnectVpn) {
                        Bundle bundle = new Bundle();
                        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "ConnectVpnFailedActivity");
                        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "ConnectVpnFailedActivity");
                        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "ConnectVpnFailedActivity");
                        if(mFirebaseAnalytics != null){
                            mFirebaseAnalytics.logEvent("ConnectVpnFailedActivity", bundle);
                        }
                        ConstantValue.isConnectVpn = false;
                    }
                    KLog.i("error");
                    ToastUtil.displayShortToast(getString(R.string.Connect_to_VPN_error_last_VPN_status) + vpnDetailstatus);
                    Intent intent = new Intent();
                    intent.setAction(BroadCastAction.disconnectVpn);
                    sendBroadcast(intent);
                } else {
                    closeProgressDialog();
                    if (ConstantValue.isConnectVpn) {
                        Bundle bundle = new Bundle();
                        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "ConnectVpnFailedActivity");
                        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "ConnectVpnFailedActivity");
                        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "ConnectVpnFailedActivity");
                        if(mFirebaseAnalytics != null){
                            mFirebaseAnalytics.logEvent("ConnectVpnFailedActivity", bundle);
                        }
                        ConstantValue.isConnectVpn = false;
                    }
                    KLog.i("error");
                    ToastUtil.displayShortToast(getString(R.string.Connect_to_VPN_error));
                    Intent intent = new Intent();
                    intent.setAction(BroadCastAction.disconnectVpn);
                    sendBroadcast(intent);
                }
            }
        }).start();
    }

    private void getPasswordFromRemote(int type) {
        KLog.i("从远程拿数据");
        if (type == R.string.password) {
            KLog.i("需要用户名和密码");
            Qsdk.getInstance().sendVpnUserAndPasswordReq(vpnEntity.getFriendNum(), vpnEntity.getVpnName());
            handler.sendEmptyMessage(1);
        } else {
            KLog.i("需要私钥");
            Qsdk.getInstance().sendVpnPrivateKeyReq(vpnEntity.getFriendNum(), vpnEntity.getVpnName());
            handler.sendEmptyMessage(1);
        }
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    flag = 0;
                    removeMessages(1);
                    break;
                case 1:
                    flag += 1;
                    if (flag >= 8) {
                        closeProgressDialog();
                        KLog.i("error");
                        ToastUtil.displayShortToast(getString(R.string.Connect_to_Sharer_Timeout));
                    } else {
                        sendEmptyMessageDelayed(1, 2000);
                    }
                    break;
            }
        }
    };

    /**
     * @param vpnPrivateKeyRsp
     * @see Qsdk# handleVpnPrivateKeyRsp(int, VpnPrivateKeyRsp)
     * 拿到私钥的返回
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void privateKeyBack(VpnPrivateKeyRsp vpnPrivateKeyRsp) {
        if (vpnPrivateKeyRsp.getPrivateKey() == null || vpnPrivateKeyRsp.getPrivateKey().equals("")) {
            ToastUtil.displayShortToast(getString(R.string.Private_Key_Error));
            KLog.i("error");
            closeProgressDialog();
            return;
        }
        KLog.i("收到了私钥的返回了。。。。");
        handler.sendEmptyMessage(0);
        mTransientCertOrPCKS12PW = vpnPrivateKeyRsp.getPrivateKey();
        KLog.i("私钥验证完成，开始连接，打开OpenVPNStatusService服务。");
        Intent intent = new Intent(ConnectVpnActivity.this, OpenVPNStatusService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * @param vpnUserAndPasswordRsp
     * @see Qsdk# handleVpnUserAndPasswordRsp(int, VpnUserAndPasswordRsp)
     * 拿到账号和密码的返回
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void userNameAndPasswordBack(VpnUserAndPasswordRsp vpnUserAndPasswordRsp) {
        if (vpnUserAndPasswordRsp.getUserName() == null || vpnUserAndPasswordRsp.getUserName().equals("")) {
            ToastUtil.displayShortToast(getString(R.string.username_or_password_error));
            closeProgressDialog();
            return;
        }
        KLog.i(vpnUserAndPasswordRsp.toString());
        handler.sendEmptyMessage(0);
        mResult.mUsername = vpnUserAndPasswordRsp.getUserName();
        mTransientAuthPW = vpnUserAndPasswordRsp.getPassword();
        KLog.i("用户名和密码验证完成，开始连接，打开OpenVPNStatusService服务。");
        Intent intent = new Intent(ConnectVpnActivity.this, OpenVPNStatusService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    private void startConnectSuccess(boolean upUI) {
        mPresenter.connectToVpnRecord(AppConfig.currentUseVpn, upUI);
    }

    void showLogWindow() {

//        Intent startLW = new Intent(getBaseContext(), LogWindow.class);
//        startLW.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//        startActivity(startLW);

    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder binder) {
            KLog.i("服务连接了。。。");
            IServiceStatus service = IServiceStatus.Stub.asInterface(binder);
            try {
                if (mTransientAuthPW != null) {
                    service.setCachedPassword(mResult.getUUIDString(), PasswordCache.AUTHPASSWORD, mTransientAuthPW);
                }
                if (mTransientCertOrPCKS12PW != null) {
                    service.setCachedPassword(mResult.getUUIDString(), PasswordCache.PCKS12ORCERTPASSWORD, mTransientCertOrPCKS12PW);
                }

                onActivityResult(START_VPN_PROFILE, Activity.RESULT_OK, null);

            } catch (RemoteException e) {
                e.printStackTrace();
            }

            unbindService(this);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            KLog.i("服务断开连接了。。。。");
        }
    };

    private void askForPW(final int type) {
        KLog.i("需要密码。。。");
        final EditText entry = new EditText(this);

        entry.setSingleLine();
        entry.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        entry.setTransformationMethod(new PasswordTransformationMethod());

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(getString(R.string.pw_request_dialog_title, getString(type)));
        dialog.setMessage(getString(R.string.pw_request_dialog_prompt, mResult.mName));


        @SuppressLint("InflateParams") final View userpwlayout = getLayoutInflater().inflate(R.layout.userpass, null, false);

        if (type == R.string.password) {
            KLog.i("需要用户名和密码");
            ((EditText) userpwlayout.findViewById(R.id.username)).setText(mResult.mUsername);
            ((EditText) userpwlayout.findViewById(R.id.password)).setText(mResult.mPassword);
            ((CheckBox) userpwlayout.findViewById(R.id.save_password)).setChecked(!TextUtils.isEmpty(mResult.mPassword));
            ((CheckBox) userpwlayout.findViewById(R.id.show_password)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                        ((EditText) userpwlayout.findViewById(R.id.password)).setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    else
                        ((EditText) userpwlayout.findViewById(R.id.password)).setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            });

            dialog.setView(userpwlayout);
        } else {
            KLog.i("需要私钥密码");
            dialog.setView(entry);
        }

        AlertDialog.Builder builder = dialog.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (type == R.string.password) {
                            mResult.mUsername = ((EditText) userpwlayout.findViewById(R.id.username)).getText().toString();

                            String pw = ((EditText) userpwlayout.findViewById(R.id.password)).getText().toString();
                            if (((CheckBox) userpwlayout.findViewById(R.id.save_password)).isChecked()) {
                                mResult.mPassword = pw;
                            } else {
                                mResult.mPassword = null;
                                mTransientAuthPW = pw;
                            }
                        } else {
                            mTransientCertOrPCKS12PW = entry.getText().toString();
                        }
                        KLog.i("密码验证完成，开始连接，打开OpenVPNStatusService服务。");
                        Intent intent = new Intent(ConnectVpnActivity.this, OpenVPNStatusService.class);
                        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
                    }

                });
        dialog.setNegativeButton(android.R.string.cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        VpnStatus.updateStateString("USER_VPN_PASSWORD_CANCELLED", "", R.string.state_user_vpn_password_cancelled,
                                ConnectionStatus.LEVEL_NOTCONNECTED);
                        finish();
                    }
                });

        dialog.create().show();

    }

    void launchVPN() {
        KLog.i("开启vpn");
        int vpnok = mResult.checkProfile(ConnectVpnActivity.this);
        if (vpnok != R.string.no_error_found) {
//            showConfigErrorDialog(vpnok);
            KLog.i("连接vpn出现错误");
            return;
        }

        KLog.i("校验配置文件没有出现错误");

        Intent intent = VpnService.prepare(ConnectVpnActivity.this);
        // Check if we want to fix /dev/tun
        SharedPreferences prefs = Preferences.getDefaultSharedPreferences(ConnectVpnActivity.this);
        boolean usecm9fix = prefs.getBoolean("useCM9Fix", false);
        boolean loadTunModule = prefs.getBoolean("loadTunModule", false);

//        if (loadTunModule)
//            execeuteSUcmd("insmod /system/lib/modules/tun.ko");
//
//        if (usecm9fix && !mCmfixed) {
//            execeuteSUcmd("chown system /dev/tun");
//        }

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
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//
//            }
//        });
    }

    String vpnDetailstatus;

    public class MyBroadcastReceiver extends BroadcastReceiver {
        public static final String TAG = "MyBroadcastReceiver";

        @Override
        public void onReceive(Context context, Intent intent) {
            KLog.i(intent.getStringExtra("detailstatus"));
            vpnDetailstatus = intent.getStringExtra("detailstatus");
            //if (ConstantValue.showLog) {
            LogUtil.addLog("连接已经注册的vpn的log：" + intent.getStringExtra("detailstatus"), getClass().getSimpleName());
            KLog.i("收到广播了。。。。");
            if (intent.getStringExtra("detailstatus").contains("CONNECTED")) {
                LogUtil.addLog("vpn连接成功，开始进行扣费：" + vpnEntity.getVpnName(), getClass().getSimpleName());
                if (vpnEntity.getProfileLocalPath() != null) {
                    String newPath = Environment.getExternalStorageDirectory() + "/Qlink/vpn";
                    String fileName = "";
                    if (vpnEntity.getProfileLocalPath().contains("/")) {
                        fileName = vpnEntity.getProfileLocalPath().substring(vpnEntity.getProfileLocalPath().lastIndexOf("/"), vpnEntity.getProfileLocalPath().length());
                    } else {
                        fileName = "/" + vpnEntity.getProfileLocalPath();
                    }
                    File configFile = new File(newPath + fileName);
                    if (configFile.exists()) {
                        configFile.delete();
                    }
                }
                if (mResult != null) {
                    ProfileManager.getInstance(ConnectVpnActivity.this).removeProfile(ConnectVpnActivity.this, mResult);
                }
                onRecordSuccess(true);
            }
        }

        private void openNewThreed(boolean upUI) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            startConnectSuccess(upUI);
                        }
                    });
                }
            }).start();
        }

    }

    private void showGuideViewConnectVpn() {
        if (!GuideSpUtil.getBoolean(this, GuideConstantValue.isShowConnectVpnGuide, false)) {
            GuideSpUtil.putBoolean(this, GuideConstantValue.isShowConnectVpnGuide, true);
            GuideBuilder builder = new GuideBuilder();
            builder.setTargetView(btConnect)
                    .setAlpha(150)
                    .setHighTargetCorner(10)
                    .setOverlayTarget(false)
                    .setOutsideTouchable(true);
            builder.setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
                @Override
                public void onShown() {
                }

                @Override
                public void onDismiss() {

                }
            });

            builder.addComponent(new ConnectVpnComponent());
            Guide guide = builder.createGuide();
            guide.setShouldCheckLocInWindow(false);
            guide.show(this);
        }
    }
}