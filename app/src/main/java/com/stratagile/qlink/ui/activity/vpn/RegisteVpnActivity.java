package com.stratagile.qlink.ui.activity.vpn;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.net.VpnService;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.VpnProfile;
import com.stratagile.qlink.activities.ConfigConverter;
import com.stratagile.qlink.activities.DisconnectVPN;
import com.stratagile.qlink.activities.FileSelect;
import com.stratagile.qlink.activities.VPNPreferences;
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
import com.stratagile.qlink.db.VpnServerRecord;
import com.stratagile.qlink.db.Wallet;
import com.stratagile.qlink.entity.Balance;
import com.stratagile.qlink.entity.ContinentAndCountry;
import com.stratagile.qlink.entity.MyAsset;
import com.stratagile.qlink.entity.VertifyVpn;
import com.stratagile.qlink.entity.eventbus.CheckConnectRsp;
import com.stratagile.qlink.entity.eventbus.ServerVpnSendComplete;
import com.stratagile.qlink.entity.eventbus.VpnRegisterSuccess;
import com.stratagile.qlink.entity.eventbus.VpnSendEnd;
import com.stratagile.qlink.entity.vpn.VpnServerFileRsp;
import com.stratagile.qlink.entity.vpn.WindowConfig;
import com.stratagile.qlink.fragments.Utils;
import com.stratagile.qlink.qlink.Qsdk;
import com.stratagile.qlink.qlinkcom;
import com.stratagile.qlink.ui.activity.file.FileChooseActivity;
import com.stratagile.qlink.ui.activity.vpn.component.DaggerRegisteVpnComponent;
import com.stratagile.qlink.ui.activity.vpn.contract.RegisteVpnContract;
import com.stratagile.qlink.ui.activity.vpn.module.RegisteVpnModule;
import com.stratagile.qlink.ui.activity.vpn.presenter.RegisteVpnPresenter;
import com.stratagile.qlink.ui.activity.wallet.ScanQrCodeActivity;
import com.stratagile.qlink.ui.adapter.vpn.WindowConfigAdapter;
import com.stratagile.qlink.utils.FileUtil;
import com.stratagile.qlink.utils.LocalAssetsUtils;
import com.stratagile.qlink.utils.LogUtil;
import com.stratagile.qlink.api.transaction.SendBackWithTxId;
import com.stratagile.qlink.utils.MD5Util;
import com.stratagile.qlink.utils.QlinkUtil;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.ToastUtil;
import com.stratagile.qlink.api.transaction.TransactionApi;
import com.stratagile.qlink.view.CustomPopWindow;
import com.stratagile.qlink.view.QlinkSeekBar;
import com.stratagile.qlink.view.TestConnectDialog;
import com.stratagile.qlink.views.FileSelectLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.stratagile.qlink.LaunchVPN.CLEARLOG;
import static com.stratagile.qlink.LaunchVPN.START_VPN_PROFILE;
import static com.stratagile.qlink.utils.VpnUtil.isInSameNet;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.vpn
 * @Description: $description
 * @date 2018/02/06 15:41:02
 */

public class RegisteVpnActivity extends BaseActivity implements RegisteVpnContract.View, EditText.OnEditorActionListener, WindowConfigAdapter.OnItemChangeListener {

    @Inject
    RegisteVpnPresenter mPresenter;
    @Inject
    WindowConfigAdapter walletListAdapter;
    @BindView(R.id.info_userid)
    EditText infoUserid;
    @BindView(R.id.tv_configuration)
    TextView tvConfiguration;
    @BindView(R.id.et_configuration)
    TextView etConfiguration;
    @BindView(R.id.et_private_key_password)
    EditText etPrivateKeyPassword;
    @BindView(R.id.cb_save_password)
    CheckBox cbSavePassword;
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_bandwidth)
    EditText etBandwidth;
    //    @BindView(R.id.connection_move)
//    TextMoveLayout connectionMove;
    //    @BindView(R.id.price)
//    TextMoveLayout price;
//    @BindView(R.id.price_seekbar)
//    SeekBar priceSeekbar;
    @BindView(R.id.button1)
    Button button1;
    @BindView(R.id.button2)
    Button button2;

    @BindView(R.id.vpninfo)
    TextView vpninfo;

    private static final int SELECT_COUNTRY = 0;
    @BindView(R.id.et_country)
    TextView etCountry;
    public final static int RESULT_VPN_DELETED = Activity.RESULT_FIRST_USER;
    public final static int RESULT_VPN_DUPLICATE = Activity.RESULT_FIRST_USER + 1;

    private static final int MENU_ADD_PROFILE = Menu.FIRST;
    private static final int START_VPN_CONFIG = 92;
    private static final int SELECT_PROFILE = 43;
    private static final int IMPORT_PROFILE = 231;
    private static final int FILE_PICKER_RESULT_KITKAT = 392;

    private static final int MENU_IMPORT_PROFILE = Menu.FIRST + 1;
    private static final int MENU_CHANGE_SORTING = Menu.FIRST + 2;
    private static final String PREF_SORT_BY_LRU = "sortProfilesByLRU";
    @BindView(R.id.tv_privatekey_flag)
    TextView tvPrivatekeyFlag;
    @BindView(R.id.tv_username_flag)
    TextView tvUsernameFlag;
    @BindView(R.id.et_your_bet)
    EditText etYourBet;
    @BindView(R.id.et_asset_tranfer)
    EditText etAssetTranfer;
    @BindView(R.id.asset_tranfer)
    LinearLayout assetTranfer;
    @BindView(R.id.ll_your_bet)
    LinearLayout llYourBet;
    @BindView(R.id.bet_tip)
    ImageView betTip;
    @BindView(R.id.qlc_seekbar)
    QlinkSeekBar qlcSeekbar;
    @BindView(R.id.connect_seekbar)
    QlinkSeekBar connectSeekbar;
    @BindView(R.id.titlevpn)
    TextView titlevpn;

    @BindView(R.id.toxidParent)
    LinearLayout toxidParent;
    @BindView(R.id.fileParent)
    RelativeLayout fileParent;
    @BindView(R.id.fileParentLine)
    View fileParentLine;
    @BindView(R.id.toxidBtn)
    LinearLayout toxidBtn;
    @BindView(R.id.tragainBtnParent)
    LinearLayout tragainBtnParent;
    @BindView(R.id.tragainBtnLing)
    View tragainBtnLing;
    @BindView(R.id.tragainBtn)
    TextView tragainBtn;

    @BindView(R.id.p2pid)
    TextView p2pid;
    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.registelocation)
    TextView registelocation;
    @BindView(R.id.toxid)
    TextView toxid;
    @BindView(R.id.paste)
    LinearLayout paste;
    @BindView(R.id.scan)
    LinearLayout scan;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private String vpnFilePath = "";

    private VpnEntity vpnEntity;

    private boolean isUpdate = false;

    private VpnEntity localVpnEntity;
    private TestConnectDialog testConnectDialog;
    private VpnProfile profile;
    private int selectFlag = -1;

    /**
     * vpn的名字是否是自己曾经注册过的
     */
    private boolean vpnNameIsSelf = false;

    /**
     * vpn名字是否被注册
     */
    private boolean vpnIsRegisted = false;

    /**
     * 测试配置文件改动
     */
    private int CONNECT_CONFGIFILE_CHANGE = 2;
    /**
     * 注册时的测试连接
     */
    private static final int CONNECT_TEST = 0;
    /**
     * 我自己注册的，我自己连接用
     */
    private static final int CONNECT_ME = 1;
    /**
     * 当前是什么连接类型
     */
    private int currentConnectType = CONNECT_TEST;

    private MyBroadcastReceiver myBroadcastReceiver = new MyBroadcastReceiver();

    private String profileUUID;

    /**
     * 为更新时，是否更改了配置文件的标志
     */
    private boolean isUpdateConfigFile = false;
    private String continent;

    private VpnProfile mResult;
    private AsyncTask<Void, Void, Integer> mImportTask;

    VpnEntity addVpnEntity;
    String selectProfileLocalPath = "";

    String tempDataString = "";

    List<VpnServerFileRsp> VpnServerFileRspList = new ArrayList<>();

    List<WindowConfig> WindowConfigList = new ArrayList<>();

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        switch (v.getId()) {
            case R.id.et_private_key_password:
                etUsername.requestFocus();
                break;
            case R.id.et_username:
                etPassword.requestFocus();
                break;
            case R.id.et_password:
                break;
            default:
                break;
        }
        return true;
    }

    public class MyBroadcastReceiver extends BroadcastReceiver {
        public static final String TAG = "MyBroadcastReceiver";

        @Override
        public void onReceive(Context context, Intent intent) {
            KLog.i(intent.getStringExtra("detailstatus"));
            KLog.i("收到广播了。。。。");
            LogUtil.addLog("连接vpn的log：" + intent.getStringExtra("detailstatus"), getClass().getSimpleName());
            if (intent.getStringExtra("detailstatus").equals("CONNECTED")) {
                if (currentConnectType != CONNECT_ME) {
                    if (currentConnectType == CONNECT_TEST) {
                        verifyCorrect(2);
                    } else {
                        Intent intent2 = new Intent();
                        intent2.setAction(BroadCastAction.disconnectVpn);
                        KLog.i("断开连接");
                        sendBroadcast(intent2);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(5000);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            startUpdateVpnInfo();
                                        }
                                    });
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                } else {
                    closeProgressDialog();
                    ToastUtil.displayShortToast(getString(R.string.Connect_vpn_success));
                    vpnEntity.setIsConnected(true);
                    AppConfig.getInstance().getDaoSession().getVpnEntityDao().update(vpnEntity);
                    EventBus.getDefault().post(new ArrayList<VpnEntity>());
                }
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        if (getIntent().getStringExtra("flag").equals("update")) {
//            getMenuInflater().inflate(R.menu.connect_vpn, menu);
//        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.connect:
                currentConnectType = CONNECT_ME;
                KLog.i(vpnEntity.getConfiguration());
                KLog.i(vpnEntity.getProfileUUid());
                profile = ProfileManager.get(this, vpnEntity.getProfileUUid());
                if (profile != null) {
                    if (VpnStatus.isVPNActive()) {
                        showVpnDisconnectDialog();
                    } else {
                        startOrStopVPN(profile);
                        showProgressDialog();
                    }
                } else {
                    KLog.i("profile为空。。。");
                    profile = ProfileManager.getInstance(this).getProFile(vpnEntity.getConfiguration());
                    if (profile != null) {
                        vpnEntity.setProfileUUid(profile.getUUIDString());
                        AppConfig.getInstance().getDaoSession().getVpnEntityDao().update(vpnEntity);
                        startOrStopVPN(profile);
                        showProgressDialog();
                    } else {
//                        ToastUtil.displayShortToast("vpn profile is null, please import vpn configuration file agin");
                        KLog.i("profile为空。。。");
                        if (vpnEntity != null && vpnEntity.getProfileLocalPath() != null) {
                            File configFile = new File(vpnEntity.getProfileLocalPath());
                            if (configFile.exists()) {
                                showProgressDialog();
                                readFile(configFile);
                            } else {
                                ToastUtil.displayShortToast(getString(R.string.error2));
                                closeProgressDialog();
                            }
                        }
                    }
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    //************************************当app为重新安装，或者app中没有改ovpn对应的profile文件时，通过ovpn来生成对应的profile文件*********//

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
            embedFiles(cp, true, true);
            return;

        } catch (IOException | ConfigParser.ConfigParseError e) {
            e.printStackTrace();
        }
        KLog.i("读取文件到流中成功");
    }

    void embedFiles(ConfigParser cp, boolean saveProfile, boolean flag) {
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
        if (saveProfile)
            saveProfile(flag);
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

    private void saveProfile(boolean flag) {
        KLog.i("保存配置文件");
        Intent result = new Intent();
        ProfileManager vpl = ProfileManager.getInstance(this);

        if (!TextUtils.isEmpty(mEmbeddedPwFile))
            ConfigParser.useEmbbedUserAuth(profile, mEmbeddedPwFile);

        vpl.addProfile(profile);
        vpl.saveProfile(this, profile);
        vpl.saveProfileList(this);
        vpnEntity.setProfileUUid(profile.getUUIDString());
        KLog.i(profile.getUUIDString());
        if (vpnEntity.getId() != null) {
            AppConfig.getInstance().getDaoSession().getVpnEntityDao().update(vpnEntity);
            //更新sd卡资产数据begin
            MyAsset myAsset = new MyAsset();
            myAsset.setType(1);
            myAsset.setVpnEntity(vpnEntity);
            LocalAssetsUtils.updateLocalAssets(myAsset);
        }
        result.putExtra(VpnProfile.EXTRA_PROFILEUUID, profile.getUUID().toString());
        if (flag)
            startOrStopVPN(profile);
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

    //************************************当app为重新安装，或者app中没有改ovpn对应的profile文件时，通过ovpn来生成对应的profile文件*********//


    /**
     * 显示vpn注册时需要扣费的dialog
     */
    private void showVpnDisconnectDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(this, R.layout.dialog_layout, null);
        builder.setView(view);
        builder.setCancelable(true);
        TextView title = (TextView) view.findViewById(R.id.title);//设置标题
        TextView tvContent = (TextView) view.findViewById(R.id.tv_content);//输入内容
        Button btn_cancel = (Button) view.findViewById(R.id.btn_left);//取消按钮
        Button btn_comfirm = (Button) view.findViewById(R.id.btn_right);//确定按钮
        title.setText(R.string.Disconnect_VPN);
        tvContent.setText(R.string.Are_you_sure_to_disconnect);
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
                Intent intent = new Intent();
                intent.setAction(BroadCastAction.disconnectVpn);
                sendBroadcast(intent);
            }
        });
        dialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        toxid.setText("");
        EventBus.getDefault().unregister(this);
        unregisterReceiver(myBroadcastReceiver);
    }

    @Override
    protected void initView() {
        isUpdate = false;
        setContentView(R.layout.activity_registe_vpn);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        etPrivateKeyPassword.setOnEditorActionListener(this);
        etUsername.setOnEditorActionListener(this);
        tragainBtnParent.setVisibility(View.GONE);
        tragainBtnLing.setVisibility(View.GONE);
        tvConfiguration.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        String str = getString(R.string.VPN_Name2);//"默认颜色<font color='#FF0000'>*</font>"
        str = "<font color='#333333'>" + str.substring(0, str.length() - 1) + "</font>" + "<font color='#FF0000'>*</font>";
        titlevpn.setText(Html.fromHtml(str));
        str = getString(R.string.VPN_Server_Location);
        str = "<font color='#333333'>" + str.substring(0, str.length() - 1) + "</font>" + "<font color='#FF0000'>*</font>";
        registelocation.setText(Html.fromHtml(str));
        str = getString(R.string.P2pId);
        str = str + "<font color='#FF0000'>*</font>";
        p2pid.setText(Html.fromHtml(str));
        str = getString(R.string.Choose_One_Configuration_File);
        str = "<font color='#333333'>" + str.substring(0, str.length()) + "</font>" + "<font color='#FF0000'>*</font>";
        tvConfiguration.setText(Html.fromHtml(str));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner.setSelection(0, true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if (getIntent().getStringExtra("flag").equals("update")) {
            vpnEntity = getIntent().getParcelableExtra("vpnentity");
            if (vpnEntity.getP2pIdPc() != null) {
                toxidParent.setVisibility(View.VISIBLE);
                toxidBtn.setVisibility(View.GONE);
                fileParent.setVisibility(View.GONE);
                fileParentLine.setVisibility(View.GONE);
                toxid.setText(vpnEntity.getP2pId());
                setToxID();
            } else {
                toxidParent.setVisibility(View.GONE);
                fileParent.setVisibility(View.VISIBLE);
                fileParentLine.setVisibility(View.VISIBLE);
                toxid.setText("");
            }
            setTitle(getString(R.string.VPN_DETAIL).toUpperCase());
            llYourBet.setVisibility(View.GONE);
            isUpdate = true;
            etAssetTranfer.setText(vpnEntity.getAssetTranfer() + "");
            KLog.i(vpnEntity.toString());
            etCountry.setText(vpnEntity.getCountry());
            infoUserid.setText(vpnEntity.getVpnName());
            infoUserid.setEnabled(false);
            etCountry.setEnabled(false);
            etConfiguration.setText(vpnEntity.getConfiguration());
            etPrivateKeyPassword.setText(vpnEntity.getPrivateKeyPassword());
            etUsername.setText(vpnEntity.getUsername());
            etPassword.setText(vpnEntity.getPassword());
            etBandwidth.setText(vpnEntity.getBandwidth());
            qlcSeekbar.setProgress((int) (vpnEntity.getQlc() * 10));
            connectSeekbar.setProgress(vpnEntity.getConnectMaxnumber());
//            priceSeekbar.setProgress((int) (vpnEntity.getQlc() * 10));
            vpnFilePath = vpnEntity.getProfileLocalPath();
//            price.setText(priceSeekbar.getProgress());
            button2.setText(getString(R.string.update).toLowerCase());
            if (vpnEntity.getCountry() == null) {
                return;
            }
            Gson gson = new Gson();
            ContinentAndCountry continentAndCountry = gson.fromJson(FileUtil.getJson(this, "ContinentAndCountryBean.json"), ContinentAndCountry.class);
            for (int i = 0; i < continentAndCountry.getContinent().size(); i++) {
                for (int j = 0; j < continentAndCountry.getContinent().get(i).getCountry().size(); j++) {
                    if (continentAndCountry.getContinent().get(i).getCountry().get(j).getName().toLowerCase(Locale.ENGLISH).equals(vpnEntity.getCountry().toLowerCase())) {
                        continent = continentAndCountry.getContinent().get(i).getContinent();
                        return;
                    }
                }
            }
        } else {
            toxidParent.setVisibility(View.VISIBLE);
            toxidBtn.setVisibility(View.VISIBLE);
            fileParent.setVisibility(View.GONE);
            fileParentLine.setVisibility(View.GONE);
            vpnEntity = getIntent().getParcelableExtra("vpnentity");
            if (vpnEntity == null) {
                vpnEntity = new VpnEntity();
            } else {
                String path = vpnEntity.getProfileLocalPath();
                String vpnFileName = path.substring(path.lastIndexOf("/") + 1, path.indexOf(".ovpn"));
                etConfiguration.setText(vpnFileName);
                vpnFilePath = vpnEntity.getProfileLocalPath();
                vpnEntity.setConfiguration(vpnFileName);
                profile = ProfileManager.getInstance(this).getProFile(vpnEntity.getConfiguration());
                if (profile == null) {
                    ConfigConverter configConverter = new ConfigConverter();
                    Uri uri = new Uri.Builder().path(vpnEntity.getProfileLocalPath()).scheme("file").build();
                    startImportTask(uri, vpnFileName, true);
                }
            }
            llYourBet.setVisibility(View.VISIBLE);
            setTitle(getString(R.string.REGISTER_YOUR_VPN).toUpperCase());
//            infoUserid.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                @Override
//                public void onFocusChange(View v, boolean hasFocus) {
//                    KLog.i(hasFocus);
//                    if (!hasFocus) {
//                        verifyVpnName();
//                    }
//                }
//            });
        }
    }


    @Override
    protected void initData() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        walletListAdapter.setOnItemChangeListener(this);
        recyclerView.setAdapter(walletListAdapter);
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.stratagile.qlink.VPN_STATUS");
        registerReceiver(myBroadcastReceiver, filter);
        AppConfig.currentVpnUseType = 0;


        //获取钱包数据
        Map<String, String> map = new HashMap<>();
        map.put("address", AppConfig.getInstance().getDaoSession().getWalletDao().loadAll().get(SpUtil.getInt(RegisteVpnActivity.this, ConstantValue.currentWallet, 0)).getAddress());
        mPresenter.getBalance(map);
        toxid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.i("toxid:", charSequence.toString());
                setToxID();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
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
        infoUserid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    return;
                }
                if (s.toString().trim().equals("")) {
                    infoUserid.setText("");
                    return;
                }
                verifyVpnName();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setToxID() {
        WindowConfigList = new ArrayList<>();
        tempDataString = "";
        VpnServerFileRspList = new ArrayList<>();
        ConstantValue.getWindowsVpnPath = "";
        String toxID = toxid.getText().toString();
        if (toxID.equals("")) {
            return;
        }
        if (qlinkcom.GetP2PConnectionStatus() <= 0) {
            ToastUtil.displayShortToast(getString(R.string.you_offline));
            tragainBtnParent.setVisibility(View.VISIBLE);
            return;
        }
        showProgressDialog();
        int friendNum = qlinkcom.GetFriendNumInFriendlist(toxID);
        if (friendNum < 0 && !toxID.equals(SpUtil.getString(AppConfig.getInstance(), ConstantValue.P2PID, ""))) {
            KLog.i(friendNum + "需要添加window好友");
            friendNum = qlinkcom.AddFriend(toxID);
            if (friendNum >= 0) {
                KLog.i("添加后好友索引" + friendNum);
            }
        } else {
            KLog.i(friendNum + "已经是好友");
        }
        String toxidStr = new String(toxID).trim();
        if (qlinkcom.GetFriendConnectionStatus(toxidStr + "") > 0) {
            KLog.i(friendNum + "在线");
            addVpnEntity = new VpnEntity();
            addVpnEntity.setP2pId(toxID);
            addVpnEntity.setP2pIdPc(SpUtil.getString(RegisteVpnActivity.this, ConstantValue.P2PID, ""));
            addVpnEntity.setFriendNum(toxidStr);
            ConstantValue.isWindows = true;
            tragainBtnParent.setVisibility(View.GONE);
            tragainBtnLing.setVisibility(View.GONE);
            Qsdk.getInstance().sendVpnFileRequest(addVpnEntity.getFriendNum(), "", "");
        } else {
            KLog.i(friendNum + "离线");
            ToastUtil.displayShortToast(getString(R.string.The_friend_is_not_online));
            tragainBtnParent.setVisibility(View.VISIBLE);
            tragainBtnLing.setVisibility(View.VISIBLE);
          /*  ToastUtil.displayShortToast(getString(R.string.The_friend_is_not_online));
            AlertDialog.Builder builder = new AlertDialog.Builder(RegisteVpnActivity.this);
            View view = View.inflate(RegisteVpnActivity.this, R.layout.dialog_need_qlc_layout, null);
            builder.setView(view);
            builder.setCancelable(true);
            TextView tvContent = (TextView) view.findViewById(R.id.tv_content);//输入内容
            Button btn_cancel = (Button) view.findViewById(R.id.btn_left);//取消按钮
            Button btn_comfirm = (Button) view.findViewById(R.id.btn_right);//确定按钮
            tvContent.setText(getString(R.string.The_friend_is_not_online) +"," + getString(R.string.AcquiredToSlow_warning) +"?");*/
            //取消或确定按钮监听事件处l
           /* AlertDialog dialog = builder.create();
            Window window = dialog.getWindow();
            window.setBackgroundDrawableResource(android.R.color.transparent);
            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    finish();
                }
            });
            btn_comfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String toxID = toxid.getText().toString();
                    toxid.setText(toxID);
                    dialog.dismiss();
                }
            });
            dialog.show();*/
            closeProgressDialog();
        }
    }

    @Override
    protected void setupActivityComponent() {
        DaggerRegisteVpnComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .registeVpnModule(new RegisteVpnModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(RegisteVpnContract.RegisteVpnContractPresenter presenter) {
        mPresenter = (RegisteVpnPresenter) presenter;
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
    public void registVpnSuccess() {
        //更新sd卡资产数据begin
        MyAsset myAsset = new MyAsset();
        myAsset.setType(1);
        if (localVpnEntity != null) {
            if (localVpnEntity.getP2pIdPc() != null) {
                localVpnEntity.setProfileLocalPath(ConstantValue.getWindowsVpnPath);
            }
            myAsset.setVpnEntity(localVpnEntity);
            LocalAssetsUtils.insertLocalAssets(myAsset);
        }
        LocalAssetsUtils.updateGreanDaoFromLocal();
        VpnServerRecord VpnServerRecord = new VpnServerRecord();
        String path = vpnEntity.getProfileLocalPath();
        String vpnFileName = path.substring(path.lastIndexOf("/") + 1, path.length());
        VpnServerRecord.setP2pId(vpnEntity.getP2pId());
        VpnServerRecord.setVpnName(vpnEntity.getVpnName());
        VpnServerRecord.setVpnfileName(vpnFileName);
        VpnServerRecord.setUserName(vpnEntity.getUsername() == null ? "" : vpnEntity.getUsername());
        VpnServerRecord.setPassword(vpnEntity.getPassword() == null ? "" : vpnEntity.getPassword());
        VpnServerRecord.setPrivateKey(vpnEntity.getPrivateKeyPassword() == null ? "" : vpnEntity.getPrivateKeyPassword());
        VpnServerRecord.setMainNet(SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false));
        AppConfig.getInstance().getDaoSession().getVpnServerRecordDao().insert(VpnServerRecord);
        mPresenter.upLoadImg(vpnEntity.getP2pId());
        QlinkUtil.parseMap2StringAndSend(vpnEntity.getP2pId(), ConstantValue.checkConnectReq, new HashMap());
        CustomPopWindow.onBackPressed();
        getMenuInflater().inflate(R.menu.connect_vpn, toolbar.getMenu());
        button2.setText(getString(R.string.update).toLowerCase());
        EventBus.getDefault().post(new VpnRegisterSuccess());
        ToastUtil.displayShortToast(getString(R.string.Register_Vpn_Asset_Success));
        tvPrivatekeyFlag.setText(getResources().getString(R.string.private_key_password));
        tvPrivatekeyFlag.setTextColor(Color.parseColor("#a8a6ae"));
        tvUsernameFlag.setText(R.string.auth_username);
        tvUsernameFlag.setTextColor(Color.parseColor("#a8a6ae"));
        int groupNum = qlinkcom.CreatedNewGroupChat();
        vpnEntity.setGroupNum(groupNum);
        //断开vpn
        Intent intent2 = new Intent();
        intent2.setAction(BroadCastAction.disconnectVpn);
        KLog.i("断开连接");
        sendBroadcast(intent2);
        //AppConfig.getInstance().getDaoSession().getVpnEntityDao().update(vpnEntity);
        Intent intent = new Intent(RegisteVpnActivity.this, RegisterVpnSuccessActivity.class);
        intent.putExtra("vpnentity", vpnEntity);
        startActivity(intent);
        closeProgressDialog();
        finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void checkSharerConnectRsp(CheckConnectRsp checkConnectRsp) {
        if (vpnEntity == null) {
            return;
        }
        Map<String, Object> infoMap = new HashMap<>();
        infoMap.put("vpnName", vpnEntity.getVpnName());
        String path = vpnEntity.getProfileLocalPath();
        String vpnFileName = path.substring(path.lastIndexOf("/") + 1, path.length());
        infoMap.put("vpnfileName", vpnFileName);
        infoMap.put("userName", vpnEntity.getUsername() == null ? "" : vpnEntity.getUsername());
        infoMap.put("password", vpnEntity.getPassword() == null ? "" : vpnEntity.getPassword());
        infoMap.put("privateKey", vpnEntity.getPrivateKeyPassword() == null ? "" : vpnEntity.getPrivateKeyPassword());
        QlinkUtil.parseMap2StringAndSend(vpnEntity.getP2pId(), ConstantValue.vpnUserPassAndPrivateKeyRsp, infoMap);
        List<VpnServerRecord> vpnServerRecordList = AppConfig.getInstance().getDaoSession().getVpnServerRecordDao().loadAll();
        for (VpnServerRecord vpnServerRecord : vpnServerRecordList) {
            if (vpnServerRecord.getVpnName().equals(vpnEntity.getVpnName())) {
                AppConfig.getInstance().getDaoSession().getVpnServerRecordDao().delete(vpnServerRecord);
                break;
            }
           /* if(vpnServerRecord.getVpnName().equals(vpnEntity.getVpnName()) && (SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false) == vpnServerRecord.isMainNet()))
            {
                AppConfig.getInstance().getDaoSession().getVpnServerRecordDao().delete(vpnServerRecord);
                break;
            }*/
        }

    }

    @Override
    public void onBackPressed() {
        if (CustomPopWindow.onBackPressed()) {
            return;
        }
        super.onBackPressed();
        overridePendingTransition(0, R.anim.activity_translate_out_1);
    }

    @OnClick({R.id.et_country, R.id.et_configuration, R.id.button1, R.id.button2, R.id.bet_tip, R.id.vpninfo, R.id.paste, R.id.scan, R.id.tragainBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.et_country:
                SelectCountryDialog.getInstance().createDialog(this, selectFlag).setSelectDelegate(new SelectCountryDialog.SelectDelegate() {
                    @Override
                    public void onSelected(String country, int selectIndex) {
                        etCountry.setText(country);
                        continent = "";
                        selectFlag = selectIndex;
                        SelectCountryDialog.getInstance().hideDialog();
                        KLog.i("选择了：" + country);
                    }
                });
               /* Intent intent = new Intent(this, SelectContinentActivity.class);
                intent.putExtra("country", ConstantValue.longcountry);
                startActivityForResult(intent, SELECT_COUNTRY);*/
                break;
            case R.id.et_configuration:
                if (vpnEntity.getP2pIdPc() != null && !"".equals(vpnEntity.getP2pIdPc())) {
                    return;
                }
                startActivityForResult(new Intent(this, FileChooseActivity.class), SELECT_PROFILE);
//                startImportConfigFilePicker();
                break;
            case R.id.button1:
                onBackPressed();
                break;
            case R.id.button2:
                currentConnectType = CONNECT_TEST;
                if (button2.getText().toString().toLowerCase(Locale.ENGLISH).equals("update")) {
                    Wallet wallet = AppConfig.getInstance().getDaoSession().getWalletDao().loadAll().get(SpUtil.getInt(this, ConstantValue.currentWallet, 0));
                    if (vpnEntity == null || vpnEntity.getAddress() == null || wallet == null || !vpnEntity.getAddress().equals(wallet.getAddress())) {
                        ToastUtil.displayShortToast(getString(R.string.The_asset_is_registered_under_another_wallet_Please_change_the_wallet_before_update));
                        return;
                    }
                    if (etConfiguration.getText() == null || etConfiguration.getText().equals("")) {
                        ToastUtil.displayShortToast(getString(R.string.You_must_update_your_asset_config_profile_before_update));
                        return;
                    }
                    if (isUpdateConfigFile) {
                        verifyConfigurationProfileFromUpdate();
                    } else {
                        vpnEntity.setPassword(etPassword.getText().toString());
                        vpnEntity.setUsername(etUsername.getText().toString());
                        if (profile == null) {
                            profile = ProfileManager.get(this, profileUUID);
                            if (profile != null) {
                                vpnEntity.setIpV4Address(profile.mIPv4Address);
                            }
                        } else {
                            vpnEntity.setIpV4Address(profile.mIPv4Address);
                        }
                        vpnEntity.setConfiguration(etConfiguration.getText().toString());
                        KLog.i(vpnFilePath);
                        if (vpnFilePath != null) {
                            vpnEntity.setProfileLocalPath(vpnFilePath);
                        }
                        if (profileUUID != null) {
                            vpnEntity.setProfileUUid(profileUUID);
                        }
                        vpnEntity.setAvaterUpdateTime(Long.parseLong(SpUtil.getString(this, ConstantValue.myAvaterUpdateTime, "0")));
                        vpnEntity.setConnectMaxnumber(connectSeekbar.getProgress());
                        vpnEntity.setPrice(Float.parseFloat((qlcSeekbar.getProgress() / 10.0) + ""));
                        vpnEntity.setPrivateKeyPassword(etPrivateKeyPassword.getText().toString().trim());
                        vpnEntity.setUsername(etUsername.getText().toString());
                        vpnEntity.setPassword(etPassword.getText().toString());
                        vpnEntity.setQlc(Float.parseFloat((qlcSeekbar.getProgress() / 10.0) + ""));
                        vpnEntity.setCountry(etCountry.getText().toString());
                        vpnEntity.setContinent(continent);
                        startUpdateVpnInfo();
                    }
                } else {
                    if (mbalance == null) {
                        ToastUtil.displayShortToast(getString(R.string.please_wait));
                        Map<String, String> map = new HashMap<>();
                        map.put("address", AppConfig.getInstance().getDaoSession().getWalletDao().loadAll().get(SpUtil.getInt(RegisteVpnActivity.this, ConstantValue.currentWallet, 0)).getAddress());
                        mPresenter.getBalance(map);
                        return;
                    }
                    if (infoUserid.getText().toString().equals("")) {
                        ToastUtil.displayShortToast(getString(R.string.Please_enter_VPN_name));
                        return;
                    }
                    if (vpnNameIsSelf) {
                        ToastUtil.displayShortToast(getString(R.string.This_asset_has_been_registered_by_you_please_enter_another_VPN_name));
                        return;
                    }
                    if (etYourBet.getText().toString().equals("") || etYourBet.getText().toString().equals("0")) {
                        ToastUtil.displayShortToast(getString(R.string.Please_enter_your_bet));
                        return;
                    }
                    if (vpnIsRegisted) {
                        ToastUtil.displayShortToast(getString(R.string.THIS_VPN_NAME_HAS_ALREADY_BEEN_REGISTERED).toLowerCase());
                        return;
                       /* if (Float.parseFloat(etYourBet.getText().toString()) > Float.parseFloat(etAssetTranfer.getText().toString())) {
                            showVpnAlreadyRegisterDialog();
                        } else {
                            //抢注册的qlc不够
                            ToastUtil.displayShortToast(getString(R.string.Your_bet_should_be_greater_than_the_current_asset_value));
                        }*/
                    } else {
                        if ((Float.parseFloat(etYourBet.getText().toString()) < Float.parseFloat(mbalance.getData().getQLC() + "")) && mbalance.getData().getGAS() > 0.0001) {
                            //可以正常注册
                            checkVpnInfo();
                        } else {
                            //qlc不足
                            ToastUtil.displayShortToast(getString(R.string.Not_enough_QLC_Or_GAS));
                        }
                    }
                }
                break;
            case R.id.bet_tip:
                showBetTipDialog();
                break;
            case R.id.vpninfo:
               /* Intent intent = new Intent(this, RegisteWindowVpnActivityActivity.class);
                intent.putExtra("flag", "");
                startActivityForResult(intent, 0);
                this.overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
                finish();*/
                break;
            case R.id.paste:
                try {
                    //获取剪贴板管理器：
                    ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    // 将ClipData内容放到系统剪贴板里。
                    toxid.setText(cm.getPrimaryClip().getItemAt(0).getText());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.scan:
                mPresenter.getScanPermission();
                break;
            case R.id.tragainBtn:
                tragainBtnParent.setVisibility(View.GONE);
                tragainBtnLing.setVisibility(View.GONE);
                String toxID = toxid.getText().toString();
                toxid.setText(toxID);
                break;
            default:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleConfigFile(VpnSendEnd vpnSendEnd) {
        KLog.i("c层的vpn配置文件传输完毕了，");
        if (vpnSendEnd.getProfileLocalPath() == null || "".equals(vpnSendEnd.getProfileLocalPath())) {
            ToastUtil.displayShortToast(AppConfig.getInstance().getResources().getString(R.string.configuration_profile_error));
            return;
        }

        /*Intent intent = new Intent(this, RegisteVpnActivity.class);
        intent.putExtra("flag", "");
        addVpnEntity.setProfileLocalPath(vpnSendEnd.getProfileLocalPath());
        intent.putExtra("vpnentity", addVpnEntity);
        startActivityForResult(intent, 0);
        this.overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);*/

        /*WindowConfig windowConfig = new WindowConfig();
        windowConfig.setVpnfileName(vpnSendEnd.getProfileLocalPath());
        String path = vpnSendEnd.getProfileLocalPath();
        String vpnFileName = path.substring(path.lastIndexOf("/") + 1,path.indexOf(".ovpn"));
        windowConfig.setVpnName(vpnFileName + ".ovpn");
        WindowConfigList.add(windowConfig);

        configfileParent.setVisibility(View.VISIBLE);
        coinfgfileBtn.setVisibility(View.VISIBLE);
        ConstantValue.isWindows = false;
        walletListAdapter.setSelectItem(0);
        selectProfileLocalPath = WindowConfigList.get(0).getVpnfileName();
        walletListAdapter.setNewData(WindowConfigList);*/
        //mPresenter.vpnProfileSendComplete();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleConfigFileComplete(ServerVpnSendComplete serverVpnSendComplete) {
        if (serverVpnSendComplete.getData() != null) {
            tempDataString += serverVpnSendComplete.getData().getFileData();
            if (serverVpnSendComplete.getData().getStatus() == 1) {
                serverVpnSendComplete.getData().setFileData(tempDataString);
                serverVpnSendComplete.getData().setStatus(1);
                VpnServerFileRspList.add(serverVpnSendComplete.getData());
                tempDataString = "";
            }
        } else {
            closeProgressDialog();
            Iterator it1 = VpnServerFileRspList.iterator();
            int index = 0;
            int i = 0;
            while (it1.hasNext()) {
                VpnServerFileRsp VpnServerFileRsp = (VpnServerFileRsp) it1.next();
                String path = VpnServerFileRsp.getVpnfileName();
                String vpnFileName = path.substring(path.lastIndexOf("/") + 1, path.indexOf(".ovpn"));
                FileUtil.saveVpnServerData(vpnFileName, VpnServerFileRsp.getFileData());
                WindowConfig windowConfig = new WindowConfig();
                String fileName = Environment.getExternalStorageDirectory() + "/Qlink/vpn/" + vpnFileName + ".ovpn";
                windowConfig.setVpnfileName(fileName);
                windowConfig.setServerVpnfileName(path);
                windowConfig.setVpnName(vpnFileName + ".ovpn");
                WindowConfigList.add(windowConfig);
                if (isUpdate && vpnEntity.getProfileLocalPath().contains(vpnFileName)) {
                    index = i;
                }
                i++;
            }
            ConstantValue.isWindows = false;


            walletListAdapter.setSelectItem(index);
            selectProfileLocalPath = WindowConfigList.get(index).getVpnfileName();
            ConstantValue.getWindowsVpnPath = WindowConfigList.get(index).getServerVpnfileName();

            List<WindowConfig> windowConfigListTemp = new ArrayList<>();
            String windowConfigStr = "";
            Iterator windowConfigListIt = WindowConfigList.iterator();
            while (windowConfigListIt.hasNext()) {
                WindowConfig indowConfig = (WindowConfig) windowConfigListIt.next();
                if (!windowConfigStr.contains(indowConfig.getServerVpnfileName())) {
                    windowConfigListTemp.add(indowConfig);
                }
                windowConfigStr += indowConfig.getServerVpnfileName() + ",";
            }
            tvConfiguration.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            walletListAdapter.setNewData(windowConfigListTemp);
            addVpnEntity = new VpnEntity();
            addVpnEntity.setP2pId(toxid.getText().toString());
            addVpnEntity.setP2pIdPc(SpUtil.getString(this, ConstantValue.P2PID, ""));
            addVpnEntity.setFriendNum(toxid.getText().toString());
            addVpnEntity.setProfileLocalPath(selectProfileLocalPath);
            if (!isUpdate) {
                vpnEntity = addVpnEntity;
            }
            String path = vpnEntity.getProfileLocalPath();
            String vpnFileName = path.substring(path.lastIndexOf("/") + 1, path.indexOf(".ovpn"));
            etConfiguration.setText(vpnFileName);
            vpnFilePath = vpnEntity.getProfileLocalPath();
            if (!isUpdate) {
                vpnEntity.setConfiguration(vpnFileName);
            }
            profile = ProfileManager.getInstance(this).getProFile(vpnEntity.getConfiguration());
            if (profile == null) {
                ConfigConverter configConverter = new ConfigConverter();
                Uri uri = new Uri.Builder().path(vpnEntity.getProfileLocalPath()).scheme("file").build();
                startImportTask(uri, vpnFileName, true);
            }
        }
    }

    @Override
    public void getScanPermissionSuccess() {
        Intent intent1 = new Intent(this, ScanQrCodeActivity.class);
        startActivityForResult(intent1, 1);
    }

    @Override
    public void onItemChange(int position) {
        KLog.i("item改变了");
        selectProfileLocalPath = WindowConfigList.get(position).getVpnfileName();
        ConstantValue.getWindowsVpnPath = WindowConfigList.get(position).getServerVpnfileName();
        isUpdateConfigFile = true;
        String path = selectProfileLocalPath;
        String vpnFileName = path.substring(path.lastIndexOf("/") + 1, path.indexOf(".ovpn"));
        etConfiguration.setText(vpnFileName);
        vpnFilePath = selectProfileLocalPath;
        vpnEntity.setProfileLocalPath(path);
        vpnEntity.setConfiguration(vpnFileName);
        profile = ProfileManager.getInstance(this).getProFile(vpnEntity.getConfiguration());
        if (profile == null) {
            ConfigConverter configConverter = new ConfigConverter();
            Uri uri = new Uri.Builder().path(vpnEntity.getProfileLocalPath()).scheme("file").build();
            startImportTask(uri, vpnFileName, false);
        }
        Log.i("selectProfileLocalPath", selectProfileLocalPath);
        tvPrivatekeyFlag.setText(R.string.private_Key_Password2);
        tvPrivatekeyFlag.setTextColor(getResources().getColor(R.color.color_333));

        tvUsernameFlag.setText(R.string.Username2);
        tvUsernameFlag.setTextColor(getResources().getColor(R.color.color_333));
    }

    private void startUpdateVpnInfo() {
        Map<String, String> infoMap = new HashMap<>();
        infoMap.put("vpnName", vpnEntity.getVpnName());
        if (vpnEntity.getP2pIdPc() != null && vpnEntity.getP2pId() != null) {
            infoMap.put("p2pId", vpnEntity.getP2pId());
        } else {
            infoMap.put("p2pId", SpUtil.getString(this, ConstantValue.P2PID, ""));
        }
        infoMap.put("connectCost", vpnEntity.getQlc() + "");
        infoMap.put("connectNum", vpnEntity.getConnectMaxnumber() + "");
        infoMap.put("ipV4Address", vpnEntity.getIpV4Address() == null ? "" : vpnEntity.getIpV4Address());
        infoMap.put("bandWidth", vpnEntity.getBandwidth() == null ? "" : vpnEntity.getBandwidth());
        if (vpnEntity.getP2pIdPc() != null) {
            infoMap.put("profileLocalPath", ConstantValue.getWindowsVpnPath);
        } else {
            infoMap.put("profileLocalPath", vpnEntity.getProfileLocalPath() == null ? "" : vpnEntity.getProfileLocalPath());
        }
        if (isUpdateConfigFile) {
            infoMap.put("hash", vpnEntity.getHash());
        } else {
            infoMap.put("hash", "");
        }
        mPresenter.updateVpnInfo(infoMap);
    }

    private void checkVpnInfo() {
        if (etCountry.getText().toString().trim().equals("")) {
            ToastUtil.displayShortToast(getString(R.string.Please_choose_a_country));
            return;
        }
        if (infoUserid.getText().toString().trim().equals("")) {
            ToastUtil.displayShortToast(getString(R.string.Please_fill_in_the_VPN_name));
            return;
        }
        if (etConfiguration.getText().toString().trim().equals("")) {
            ToastUtil.displayShortToast(getString(R.string.Please_select_the_configuration_file));
            return;
        }
        showTestConnectDialog();
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

    /**
     * 密码
     */
    private String mTransientAuthPW;
    /**
     * 私钥
     */
    private String mTransientCertOrPCKS12PW;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (toxid != null) {
                toxid.setText(data.getStringExtra("result"));
            }
            return;
        }
        if (requestCode == SELECT_COUNTRY && resultCode == RESULT_OK) {
            KLog.i(data.getStringExtra("country"));
            etCountry.setText(data.getStringExtra("country"));
            continent = data.getStringExtra("continent");
            return;
        }
        if (resultCode == RESULT_VPN_DELETED) {
        } else if (resultCode == RESULT_VPN_DUPLICATE && data != null) {
            String profileUUID = data.getStringExtra(VpnProfile.EXTRA_PROFILEUUID);
//            profile = ProfileManager.get(this, profileUUID);
            if (profile != null) {
                onAddOrDuplicateProfile(profile);
            }
        }


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
            if (!fileData.equals(vpnEntity.getProfileLocalPath())) {
                isUpdateConfigFile = true;
            }
            Uri uri = new Uri.Builder().path(fileData).scheme("file").build();
            startConfigImport(uri);
        } else if (requestCode == IMPORT_PROFILE) {
            profileUUID = data.getStringExtra(VpnProfile.EXTRA_PROFILEUUID);
            profile = ProfileManager.get(this, profileUUID);
            etConfiguration.setText(ProfileManager.get(this, profileUUID).getName());
        } else if (requestCode == FILE_PICKER_RESULT_KITKAT) {
            KLog.i("返回。。。。FILE_PICKER_RESULT_KITKAT");
            if (data != null) {
                if (button2.getText().toString().toLowerCase(Locale.ENGLISH).equals("update")) {
                    isUpdateConfigFile = true;
                }
                Uri uri = data.getData();
                startConfigImport(uri);
            }
        }

        if (requestCode == START_VPN_PROFILE) {
            KLog.i("onActivityResult的requestCode   为  START_VPN_PROFILE");
            if (!etPrivateKeyPassword.getText().toString().trim().equals("")) {
                mTransientCertOrPCKS12PW = etPrivateKeyPassword.getText().toString().trim();
                KLog.i("私钥为:" + mTransientCertOrPCKS12PW);
                profile.mKeyPassword = etPrivateKeyPassword.getText().toString().trim();
            }
            mTransientAuthPW = etPassword.getText().toString().trim();
            profile.mUsername = etUsername.getText().toString().trim();
            profile.mPassword = etPassword.getText().toString().trim();
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

    private void editVPN(VpnProfile profile) {
        Intent vprefintent = new Intent(this, VPNPreferences.class)
                .putExtra(getPackageName() + ".profileUUID", profile.getUUID().toString());

        startActivityForResult(vprefintent, START_VPN_CONFIG);
    }

    private void startConfigImport(Uri uri) {
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
        LogUtil.addLog("uri的Scheme为：" + uri.getScheme().toString(), getClass().getSimpleName());
        LogUtil.addLog("uri为：" + uri.toString(), getClass().getSimpleName());
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
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {

                    }
                } else {
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

    private void onAddOrDuplicateProfile(final VpnProfile mCopyProfile) {
        Context context = this;
        if (context != null) {
            final EditText entry = new EditText(context);
            entry.setSingleLine();

            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            if (mCopyProfile == null) {
                dialog.setTitle(R.string.menu_add_profile);
            } else {
                dialog.setTitle(context.getString(R.string.duplicate_profile_title, mCopyProfile.mName));
                entry.setText(getString(R.string.copy_of_profile, mCopyProfile.mName));
            }

            dialog.setMessage(R.string.add_profile_name_prompt);
            dialog.setView(entry);

            dialog.setNeutralButton(R.string.menu_import_short,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startImportConfigFilePicker();
                        }
                    });
            dialog.setPositiveButton(android.R.string.ok,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String name = entry.getText().toString();
                            if (getPM().getProfileByName(name) == null) {
                                VpnProfile profile;
                                if (mCopyProfile != null) {
                                    profile = mCopyProfile.copy(name);
                                } else {
                                    profile = new VpnProfile(name);
                                }

                                addProfile(profile);
                                editVPN(profile);
                            } else {
                                Toast.makeText(context, R.string.duplicate_profile_name, Toast.LENGTH_LONG).show();
                            }
                        }


                    });
            dialog.setNegativeButton(android.R.string.cancel, null);
            dialog.create().show();
        }

    }

    private void addProfile(VpnProfile profile) {
        getPM().addProfile(profile);
        getPM().saveProfileList(this);
        getPM().saveProfile(this, profile);
    }

    private ProfileManager getPM() {
        return ProfileManager.getInstance(this);
    }

    /**
     * 显示测试连接vpn的dialog
     */
    private void showTestConnectDialog() {
        testConnectDialog = new TestConnectDialog(this, button1);
        testConnectDialog.show();
        verifyConfigurationProfile();
    }

    private void verifyVpnName() {
        if (infoUserid.getText().toString().trim().equals("")) {
            ToastUtil.displayShortToast(getString(R.string.cannot_register_empty_vpn_asset));
            return;
        }
        List<VpnEntity> vpnEntityList = AppConfig.getInstance().getDaoSession().getVpnEntityDao().loadAll();
        for (VpnEntity vpnEntity : vpnEntityList) {
            if (isInSameNet(vpnEntity) && vpnEntity.getVpnName().equals(infoUserid.getText().toString()) && vpnEntity.getP2pId().equals(SpUtil.getString(this, ConstantValue.P2PID, ""))) {
                vpnNameIsSelf = true;
                ToastUtil.displayShortToast(getString(R.string.This_asset_has_been_registered_by_you_please_enter_another_VPN_name));
                return;
            }
        }
        vpnNameIsSelf = false;
        Map<String, String> map = new HashMap<>();
        map.put("vpnName", infoUserid.getText().toString().trim());
        map.put("type", "3");
        mPresenter.vertifyVpnName(map);
    }


    @Override
    public void vertifyVpnBack(VertifyVpn verifyVpn) {
        if (!verifyVpn.getData().isIsExist()) {
            etAssetTranfer.setText("");
            vpnIsRegisted = false;

        } else {
            ToastUtil.displayShortToast(getString(R.string.THIS_VPN_NAME_HAS_ALREADY_BEEN_REGISTERED).toLowerCase());
            vpnIsRegisted = true;
           /* if (getIntent().getStringExtra("flag").equals("update")) {

            } else {
                etAssetTranfer.setText(verifyVpn.getData().getQlc() + "");
            }*/
        }
    }

    private void verifyConfigurationProfile() {
        KLog.i(vpnFilePath);
        File configurationFile = new File(vpnFilePath);
        if (configurationFile.exists()) {
            vpnEntity.setHash(MD5Util.getFileMD5(configurationFile));
            KLog.i(configurationFile.getParent());
            KLog.i(configurationFile.getName());
            copyFile(configurationFile.getPath(), Environment.getExternalStorageDirectory() + "/Qlink/" + Calendar.getInstance().getTimeInMillis() + "." + configurationFile.getName().substring(configurationFile.getName().lastIndexOf(".") + 1));
        } else {
            verifyError(1);
        }
    }

    /**
     * 更新vpn资产时，
     */
    private void verifyConfigurationProfileFromUpdate() {
        KLog.i(vpnFilePath);
        File configurationFile = new File(vpnFilePath);
        if (configurationFile.exists()) {
            KLog.i(configurationFile.getParent());
            KLog.i(configurationFile.getName());
            copyFileFromUpdate(configurationFile.getPath(), Environment.getExternalStorageDirectory() + "/Qlink/" + Calendar.getInstance().getTimeInMillis() + "." + configurationFile.getName().substring(configurationFile.getName().lastIndexOf(".") + 1));
        } else {

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
            VpnProfile profileTemp = ProfileManager.getInstance(this).getProFile(vpnEntity.getConfiguration());
            if (profileTemp == null) {
                saveProfile(true);
            } else {
                verifyConnectVpn();
            }
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            LogUtil.addLog("配置文件复制到 " + newPath + " 失败", getClass().getSimpleName());
            e.printStackTrace();
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
    public void copyFileFromUpdate(String oldPath, String newPath) {
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
            vpnFilePath = newPath;
            vpnEntity.setHash(MD5Util.getFileMD5(oldfile));
            LogUtil.addLog("配置文件复制到 " + newPath + " 成功", getClass().getSimpleName());
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            LogUtil.addLog("配置文件复制到 " + newPath + " 失败", getClass().getSimpleName());
            e.printStackTrace();
        }
        vpnEntity.setPassword(etPassword.getText().toString());
        vpnEntity.setUsername(etUsername.getText().toString());
        if (profile == null) {
            profile = ProfileManager.get(this, profileUUID);
            if (profile != null) {
                vpnEntity.setIpV4Address(profile.mIPv4Address);
            }
        } else {
            vpnEntity.setIpV4Address(profile.mIPv4Address);
        }
        vpnEntity.setConfiguration(etConfiguration.getText().toString());
        KLog.i(vpnFilePath);
        if (vpnFilePath != null) {
            vpnEntity.setProfileLocalPath(vpnFilePath);
        }
        if (profileUUID != null) {
            vpnEntity.setProfileUUid(profileUUID);
        }
        vpnEntity.setAvaterUpdateTime(Long.parseLong(SpUtil.getString(this, ConstantValue.myAvaterUpdateTime, "0")));
        vpnEntity.setConnectMaxnumber(connectSeekbar.getProgress());
        vpnEntity.setPrice(Float.parseFloat((qlcSeekbar.getProgress() / 10.0) + ""));
        vpnEntity.setPrivateKeyPassword(etPrivateKeyPassword.getText().toString().trim());
        vpnEntity.setPassword(etPassword.getText().toString());
        vpnEntity.setUsername(etUsername.getText().toString());
        vpnEntity.setQlc(Float.parseFloat((qlcSeekbar.getProgress() / 10.0) + ""));
        vpnEntity.setCountry(etCountry.getText().toString());
        vpnEntity.setContinent(continent);
        vpnEntity.setIsMainNet(SpUtil.getBoolean(this, ConstantValue.isMainNet, false));
        KLog.i(vpnEntity.toString());
        verifyConnectVpn();
        currentConnectType = CONNECT_CONFGIFILE_CHANGE;
        showProgressDialog();
    }

    private void verifyConnectVpn() {
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
     * 验证或者登录vpn出错了，错误类型
     * 0， 私钥错误
     * 1， 用户名或者密码错误
     * 2， 配置文件出错
     * 3,  连接出错，比如，远程服务器未响应
     *
     * @param type
     */
    private void verifyVpnError(int type) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                verifyError(2);
                switch (type) {
                    case 0:
                        //私钥错误
                        tvPrivatekeyFlag.setText(R.string.invalid_Private_Key_Password_Try_gain);
                        tvPrivatekeyFlag.setTextColor(Color.parseColor("#bc0000"));

                        tvUsernameFlag.setText(R.string.Username2);
                        tvUsernameFlag.setTextColor(getResources().getColor(R.color.color_333));
                        break;
                    case 1:
                        //用户名或密码错误
                        tvUsernameFlag.setText(R.string.Username_username_or_password_error);
                        tvUsernameFlag.setTextColor(Color.parseColor("#bc0000"));

                        tvPrivatekeyFlag.setText(R.string.private_Key_Password2);
                        tvPrivatekeyFlag.setTextColor(getResources().getColor(R.color.color_333));
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    default:
                        break;
                }
            }
        });
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

    private void testBandWidth() {
        verifyCorrect(3);
    }

    /**
     * 显示下注的解释dialog
     */
    private void showBetTipDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(this, R.layout.vpn_bettip_dialog_layout, null);
        builder.setView(view);
        builder.setCancelable(true);
        TextView tvContent = (TextView) view.findViewById(R.id.tv_content);//输入内容
        Button bt_left = view.findViewById(R.id.btn_left);
        Button btn_comfirm = (Button) view.findViewById(R.id.btn_right);//确定按钮
        tvContent.setText(getString(R.string.if_some, "VPN", "VPN", "VPN"));
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
     * 显示下注的解释dialog
     */
    private void showVpnAlreadyRegisterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(this, R.layout.vpn_bettip_dialog_layout, null);
        builder.setView(view);
        builder.setCancelable(true);
        TextView tvContent = (TextView) view.findViewById(R.id.tv_content);//输入内容
        TextView tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText(getString(R.string.THIS_s_NAME_HAS_ALREADY_BEEN_REGISTERED, "VPN"));
        Button bt_left = view.findViewById(R.id.btn_left);
        Button btn_comfirm = (Button) view.findViewById(R.id.btn_right);//确定按钮
        tvContent.setText(getString(R.string.If_you_want_to_claim, "VPN"));
        //取消或确定按钮监听事件处l
        AlertDialog dialog = builder.create();
        btn_comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (Float.parseFloat(etYourBet.getText().toString()) > Float.parseFloat(etAssetTranfer.getText().toString())) {
                    if (Float.parseFloat(etYourBet.getText().toString()) < Float.parseFloat(mbalance.getData().getQLC() + "")) {
                        //可以正常抢注册
                        checkVpnInfo();
                    } else {
                        //qlc不足
                        ToastUtil.displayShortToast(getString(R.string.Not_enough_QLC));
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

    Balance mbalance;

    @Override
    public void onGetBalancelSuccess(Balance balance) {
        mbalance = balance;
//        if (Float.parseFloat(balance.getData().getQLC()) >= 1) {
//
//        } else {
//            ToastUtil.displayShortToast("Not enough QLC");
//            closeProgressDialog();
//        }
    }

    @Override
    public void updateVpnInfoSuccess(String data) {
        closeProgressDialog();
        KLog.i(vpnEntity.toString());
        if (vpnEntity.getP2pIdPc() != null) {
            vpnEntity.setProfileLocalPath(ConstantValue.getWindowsVpnPath);
        }
        vpnEntity.setCountry(data);
        etCountry.setText(vpnEntity.getCountry());
        vpnEntity.setIsMainNet(SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false));
        AppConfig.getInstance().getDaoSession().getVpnEntityDao().update(vpnEntity);
        //更新sd卡资产数据begin
        MyAsset myAsset = new MyAsset();
        myAsset.setType(1);
        myAsset.setVpnEntity(vpnEntity);
        LocalAssetsUtils.updateLocalAssets(myAsset);
        //更新sd卡资产数据end
        ToastUtil.displayShortToast(getString(R.string.update_success));
        VpnServerRecord VpnServerRecord = new VpnServerRecord();
        String path = vpnEntity.getProfileLocalPath();
        String vpnFileName = path.substring(path.lastIndexOf("/") + 1, path.length());
        VpnServerRecord.setP2pId(vpnEntity.getP2pId());
        VpnServerRecord.setVpnName(vpnEntity.getVpnName());
        VpnServerRecord.setVpnfileName(vpnFileName);
        VpnServerRecord.setUserName(vpnEntity.getUsername() == null ? "" : vpnEntity.getUsername());
        VpnServerRecord.setPassword(vpnEntity.getPassword() == null ? "" : vpnEntity.getPassword());
        VpnServerRecord.setPrivateKey(vpnEntity.getPrivateKeyPassword() == null ? "" : vpnEntity.getPrivateKeyPassword());
        VpnServerRecord.setMainNet(SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false));
        AppConfig.getInstance().getDaoSession().getVpnServerRecordDao().insert(VpnServerRecord);
        QlinkUtil.parseMap2StringAndSend(vpnEntity.getP2pId(), ConstantValue.checkConnectReq, new HashMap());
        //断开vpn
        Intent intent2 = new Intent();
        intent2.setAction(BroadCastAction.disconnectVpn);
        KLog.i("断开连接");
        sendBroadcast(intent2);
    }

    /**
     * 验证通过，开始注册vpn
     */
    private void startRegisterVpn() {
        VpnEntity vpnEntity = new VpnEntity();
        vpnEntity.setCountry(etCountry.getText().toString());
        vpnEntity.setContinent(continent);
        vpnEntity.setUserId(infoUserid.getText().toString());
        vpnEntity.setVpnName(infoUserid.getText().toString());
        vpnEntity.setPassword(etPassword.getText().toString());
        vpnEntity.setUsername(etUsername.getText().toString());
        if (profile == null) {
            vpnEntity.setIpV4Address(this.vpnEntity.getIpV4Address());
        } else {
            vpnEntity.setIpV4Address(profile.mIPv4Address);
        }
        if (this.vpnEntity.getP2pId() != null && !"".equals(this.vpnEntity.getP2pId())) {
            vpnEntity.setP2pId(this.vpnEntity.getP2pId());
            vpnEntity.setP2pIdPc(SpUtil.getString(this, ConstantValue.P2PID, ""));
        } else {
            vpnEntity.setP2pId(SpUtil.getString(this, ConstantValue.P2PID, ""));
        }
        vpnEntity.setOnline(true);
        vpnEntity.setProfileUUid(profileUUID);
        vpnEntity.setConfiguration(etConfiguration.getText().toString());
        vpnEntity.setPrivateKeyPassword(etPrivateKeyPassword.getText().toString().trim());
        vpnEntity.setAvaterUpdateTime(Long.parseLong(SpUtil.getString(this, ConstantValue.myAvaterUpdateTime, "0")));
        vpnEntity.setConnectMaxnumber(connectSeekbar.getProgress());
        vpnEntity.setPrice(Float.parseFloat((qlcSeekbar.getProgress() / 10.0) + ""));
        vpnEntity.setProfileLocalPath(vpnFilePath);
        vpnEntity.setFriendNum("");
        vpnEntity.setHash(this.vpnEntity.getHash());
        vpnEntity.setIsMainNet(SpUtil.getBoolean(this, ConstantValue.isMainNet, false));
        vpnEntity.setQlc(Float.parseFloat((qlcSeekbar.getProgress() / 10.0) + ""));
        if (SpUtil.getBoolean(AppConfig.getInstance(), ConstantValue.isMainNet, false)) {//主网
            vpnEntity.setIsInMainWallet(true);
        }
        this.vpnEntity = vpnEntity;
        localVpnEntity = vpnEntity;
//        AppConfig.getInstance().getDaoSession().getVpnEntityDao().insert(vpnEntity);
        String walletAddress = AppConfig.getInstance().getDaoSession().getWalletDao().loadAll().get(SpUtil.getInt(this, ConstantValue.currentWallet, 0)).getAddress();
        //更新sd卡资产数据end
        Map<String, String> map = new HashMap<>();
        map.put("vpnName", infoUserid.getText().toString());
        map.put("country", etCountry.getText().toString());
        String p2pId = localVpnEntity.getP2pId();
        map.put("p2pId", p2pId);
        map.put("qlc", etYourBet.getText().toString());
        map.put("address", walletAddress);
//        map.put("wif", AppConfig.getInstance().getDaoSession().getWalletDao().loadAll().get(SpUtil.getInt(this, ConstantValue.currentWallet, 0)).getWif());
        map.put("connectCost", vpnEntity.getQlc() + "");
        map.put("connectNum", vpnEntity.getConnectMaxnumber() + "");
        map.put("ipV4Address", vpnEntity.getIpV4Address() == null ? "" : vpnEntity.getIpV4Address());
        map.put("bandWidth", vpnEntity.getBandwidth() == null ? "" : vpnEntity.getBandwidth());
        map.put("hash", vpnEntity.getHash());
        if (localVpnEntity.getP2pIdPc() != null) {
            vpnEntity.setProfileLocalPath(ConstantValue.getWindowsVpnPath);
        }
        map.put("profileLocalPath", vpnEntity.getProfileLocalPath() == null ? "" : vpnEntity.getProfileLocalPath());
        KLog.i(map);
        showProgressDialog();
        if (!p2pId.equals(SpUtil.getString(this, ConstantValue.P2PID, ""))) {
            ConstantValue.windowsVpnName = infoUserid.getText().toString();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                    TransactionApi.getInstance().registerVPN(map, walletAddress, ConstantValue.mainAddress, etYourBet.getText().toString(), new SendBackWithTxId() {
                        @Override
                        public void onSuccess(String txid) {
                            registVpnSuccess();
                        }

                        @Override
                        public void onFailure() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    KLog.i("注册失败");
                                    closeProgressDialog();
                                    testConnectDialog.dismiss();
                                    Intent intent2 = new Intent();
                                    intent2.setAction(BroadCastAction.disconnectVpn);
                                    KLog.i("断开连接");
                                    sendBroadcast(intent2);
                                }
                            });
                        }
                    }, vpnEntity);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void startImportTask(final Uri data, final String possibleName, boolean flag) {
        mImportTask = new AsyncTask<Void, Void, Integer>() {
            private ProgressBar mProgress;

            @Override
            protected void onPreExecute() {

            }

            @Override
            protected Integer doInBackground(Void... params) {
                try {
                    InputStream is = getContentResolver().openInputStream(data);

                    doImport(is, possibleName, flag);
                    if (mResult == null)
                        return -3;
                } catch (FileNotFoundException |
                        SecurityException se)

                {
                    return -2;
                }

                return 0;
            }

            @Override
            protected void onPostExecute(Integer errorCode) {
                if (errorCode == 0) {
                    mResult.mName = getUniqueProfileName(possibleName);
                }
            }
        }.execute();
    }

    private String getUniqueProfileName(String possibleName) {

        int i = 0;

        ProfileManager vpl = ProfileManager.getInstance(this);

        String newname = possibleName;

        // 	Default to
        if (mResult.mName != null && !ConfigParser.CONVERTED_PROFILE.equals(mResult.mName))
            newname = mResult.mName;

        while (newname == null || vpl.getProfileByName(newname) != null) {
            i++;
            if (i == 1)
                newname = getString(R.string.converted_profile);
            else
                newname = getString(R.string.converted_profile_i, i);
        }

        return newname;
    }

    private void doImport(InputStream is, String possibleName, boolean flag) {
        ConfigParser cp = new ConfigParser();
        try {
            InputStreamReader isr = new InputStreamReader(is);

            cp.parseConfig(isr);
            mResult = cp.convertProfile();
            mResult.mName = possibleName;
            profile = mResult;

            embedFiles(cp, true, flag);
            return;

        } catch (IOException | ConfigParser.ConfigParseError e) {
        }
        mResult = null;

    }
}