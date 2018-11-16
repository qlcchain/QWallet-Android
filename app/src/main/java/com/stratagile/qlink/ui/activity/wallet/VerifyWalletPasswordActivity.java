package com.stratagile.qlink.ui.activity.wallet;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.fingerprint.CryptoObjectHelper;
import com.stratagile.qlink.fingerprint.MyAuthCallback;
import com.stratagile.qlink.guideview.Guide;
import com.stratagile.qlink.guideview.GuideBuilder;
import com.stratagile.qlink.guideview.GuideConstantValue;
import com.stratagile.qlink.guideview.GuideSpUtil;
import com.stratagile.qlink.guideview.compnonet.UnLockComponent;
import com.stratagile.qlink.qlinkcom;
import com.stratagile.qlink.ui.activity.main.MainActivity;
import com.stratagile.qlink.ui.activity.wallet.component.DaggerVerifyWalletPasswordComponent;
import com.stratagile.qlink.ui.activity.wallet.contract.VerifyWalletPasswordContract;
import com.stratagile.qlink.ui.activity.wallet.module.VerifyWalletPasswordModule;
import com.stratagile.qlink.ui.activity.wallet.presenter.VerifyWalletPasswordPresenter;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.ToastUtil;
import com.stratagile.qlink.utils.eth.ETHWalletUtils;

import java.util.Calendar;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: $description
 * @date 2018/01/24 18:19:18
 */

public class VerifyWalletPasswordActivity extends BaseActivity implements VerifyWalletPasswordContract.View {

    @Inject
    VerifyWalletPasswordPresenter mPresenter;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.tvJoin)
    TextView tvJoin;
    ImageView finger;
    @BindView(R.id.tvFingerPrinte)
    TextView tvFingerPrinte;

    private MyAuthCallback myAuthCallback = null;
    private CancellationSignal cancellationSignal = null;

    private Handler handler = null;
    public static final int MSG_AUTH_SUCCESS = 100;
    public static final int MSG_AUTH_FAILED = 101;
    public static final int MSG_AUTH_ERROR = 102;
    public static final int MSG_AUTH_HELP = 103;

    private AlertDialog builderTips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainColor = R.color.white;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_verify_wallet_password);
        ButterKnife.bind(this);
        if (getIntent().hasExtra("flag") && getIntent().getStringExtra("flag").equals("")) {
        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    @Override
    protected void initData() {
        if (SpUtil.getString(AppConfig.getInstance(), ConstantValue.walletPassWord, "").equals("")) {
            Intent intent = new Intent(AppConfig.getInstance(), CreateWalletPasswordActivity.class);
            startActivityForResult(intent, 2);
        }
//        if (!GuideSpUtil.getBoolean(this, GuideConstantValue.isShowUnLockGuide, false)) {
//            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(etPassword.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//            llUnlock.post(new Runnable() {
//                @Override
//                public void run() {
//                    showGuideViewUnlock();
//                }
//            });
//            return;
//        }
        setTitle("Enter Password");
        etPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                etPassword.performClick();
                return true;
            }
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if ("".equals(s.toString())) {
                    tvJoin.setBackground(getResources().getDrawable(R.drawable.set_password_bt_bg_unenable));
                } else {
                    tvJoin.setBackground(getResources().getDrawable(R.drawable.main_color_bt_bg));
                }
            }
        });

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case MSG_AUTH_SUCCESS:
                        setResultInfo(R.string.fingerprint_success);
                        cancellationSignal = null;
                        break;
                    case MSG_AUTH_FAILED:
                        setResultInfo(R.string.fingerprint_not_recognized);
                        cancellationSignal = null;
                        break;
                    case MSG_AUTH_ERROR:
                        handleErrorCode(msg.arg1);
                        break;
                    case MSG_AUTH_HELP:
                        handleHelpCode(msg.arg1);
                        break;
                }
            }
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // init fingerprint.
            try {
                FingerprintManager fingerprintManager = (FingerprintManager) AppConfig.getInstance().getSystemService(Context.FINGERPRINT_SERVICE);
                /*if(!SpUtil.getString(this, ConstantValue.fingerPassWord, "").equals(""))
                {*/
                if (fingerprintManager != null && fingerprintManager.isHardwareDetected() && fingerprintManager.hasEnrolledFingerprints()) {
                    tvFingerPrinte.setVisibility(View.VISIBLE);
                    if (SpUtil.getBoolean(this, ConstantValue.fingerprintUnLock, true) && !SpUtil.getString(AppConfig.getInstance(), ConstantValue.walletPassWord, "").equals("")) {
                        tvFingerPrinte.performClick();
                    }
                } else {
                    etPassword.requestFocus();
                    tvFingerPrinte.setVisibility(View.GONE);
                    SpUtil.putString(this, ConstantValue.fingerPassWord, "");
                }
                /*}else{
                    etPassword.requestFocus();
                }*/
            } catch (NoClassDefFoundError e) {
                SpUtil.putString(this, ConstantValue.fingerPassWord, "");
            }

        } else {
            etPassword.requestFocus();
            SpUtil.putString(this, ConstantValue.fingerPassWord, "");
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                startActivity(MainActivity.class);
                overridePendingTransition(R.anim.main_activity_in, R.anim.splash_activity_out);
                ConstantValue.isShouldShowVertifyPassword = false;
                finish();
            } else {
                finish();
            }
        }

    }

    @Override
    protected void setupActivityComponent() {
        DaggerVerifyWalletPasswordComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .verifyWalletPasswordModule(new VerifyWalletPasswordModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(VerifyWalletPasswordContract.VerifyWalletPasswordContractPresenter presenter) {
        mPresenter = (VerifyWalletPasswordPresenter) presenter;
    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void closeProgressDialog() {
        progressDialog.hide();
    }

    @OnClick({R.id.tvJoin, R.id.tvFingerPrinte})
    public void onViewClicked(View view1) {
        switch (view1.getId()) {
            case R.id.tvFingerPrinte:
                try {
                    myAuthCallback = new MyAuthCallback(handler);
                    CryptoObjectHelper cryptoObjectHelper = new CryptoObjectHelper();
                    if (cancellationSignal == null) {
                        cancellationSignal = new CancellationSignal();
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        FingerprintManager fingerprintManager = (FingerprintManager) AppConfig.getInstance().getSystemService(Context.FINGERPRINT_SERVICE);
                        fingerprintManager.authenticate(cryptoObjectHelper.buildCryptoObject(), cancellationSignal, 0, myAuthCallback, null);
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        View view = View.inflate(this, R.layout.finger_dialog_layout, null);
                        builder.setView(view);
                        builder.setCancelable(false);
                        TextView title = (TextView) view.findViewById(R.id.title);//设置标题
                        TextView tvContent = (TextView) view.findViewById(R.id.tv_content);//输入内容
                        Button btn_cancel = (Button) view.findViewById(R.id.btn_left);//取消按钮
                        btn_cancel.setText(R.string.back_btn);

                        Button btn_comfirm = (Button) view.findViewById(R.id.btn_right);//确定按钮
                        btn_comfirm.setText(R.string.cancel_btn_dialog);

//                        if (!SpUtil.getString(this, ConstantValue.walletPassWord, "").equals("")) {
//                            btn_cancel.setVisibility(View.VISIBLE);
//                            btn_comfirm.setVisibility(View.VISIBLE);
//                            builder.setCancelable(true);
//                        } else {
                        btn_cancel.setVisibility(View.VISIBLE);
                        btn_comfirm.setVisibility(View.VISIBLE);
                        builder.setCancelable(false);
//                        btn_cancel.setVisibility(View.GONE);
//                        btn_comfirm.setVisibility(View.GONE);
//                        builder.setCancelable(false);
                        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                builderTips.dismiss();
                                if (cancellationSignal != null) {
                                    cancellationSignal.cancel();
                                    cancellationSignal = null;
                                    if (SpUtil.getString(AppConfig.getInstance(), ConstantValue.walletPassWord, "").equals("")) {
                                        Intent intent = new Intent(AppConfig.getInstance(), CreateWalletPasswordActivity.class);
                                        startActivityForResult(intent, 1);
                                        finish();
                                    } else {
                                        etPassword.requestFocus();
                                    }
                                }
                            }
                        });
//                        }
                        btn_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                builderTips.dismiss();
                                if (cancellationSignal != null) {
                                    cancellationSignal.cancel();
                                    cancellationSignal = null;
                                    if (SpUtil.getString(AppConfig.getInstance(), ConstantValue.walletPassWord, "").equals("")) {
                                        Intent intent = new Intent(AppConfig.getInstance(), CreateWalletPasswordActivity.class);
                                        startActivityForResult(intent, 1);
                                        finish();
                                    } else {
                                        etPassword.requestFocus();
                                    }
                                }
                            }
                        });
                        btn_comfirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                builderTips.dismiss();
                                if (cancellationSignal != null) {
                                    cancellationSignal.cancel();
                                    cancellationSignal = null;
                                    if (SpUtil.getString(AppConfig.getInstance(), ConstantValue.walletPassWord, "").equals("")) {
                                        Intent intent = new Intent(AppConfig.getInstance(), CreateWalletPasswordActivity.class);
                                        startActivityForResult(intent, 1);
                                        finish();
                                    } else {
                                        etPassword.requestFocus();
                                    }
                                }
                            }
                        });
                        finger = (ImageView) view.findViewById(R.id.finger);
                        tvContent.setText(R.string.choose_finger_dialog_title);
                        title.setText(R.string.unlock_wallet);
                        Context currentContext = this;
                        builderTips = builder.create();
                        builderTips.show();
                    }
                } catch (Exception e) {
//                    try {
//                        myAuthCallback = new MyAuthCallback(handler);
//                        CryptoObjectHelper cryptoObjectHelper = new CryptoObjectHelper();
//                        if (cancellationSignal == null) {
//                            cancellationSignal = new CancellationSignal();
//                        }
//
//                        fingerprintManager.authenticate(cryptoObjectHelper.buildCryptoObject(), cancellationSignal, 0,
//                                myAuthCallback, null);
//                       /* fingerprintManager.authenticate(cryptoObjectHelper.buildCryptoObject(), 0,
//                                cancellationSignal, myAuthCallback, null);*/
//                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                        View view = View.inflate(this, R.layout.finger_dialog_layout, null);
//                        builder.setView(view);
//                        builder.setCancelable(false);
//                        TextView title = (TextView) view.findViewById(R.id.title);//设置标题
//                        TextView tvContent = (TextView) view.findViewById(R.id.tv_content);//输入内容
//                        Button btn_cancel = (Button) view.findViewById(R.id.btn_left);//取消按钮
//                        btn_cancel.setText(R.string.back_btn);
//                        Button btn_comfirm = (Button) view.findViewById(R.id.btn_right);//确定按钮
//                        btn_comfirm.setText(R.string.cancel_btn_dialog);
//                        finger = (ImageView) view.findViewById(R.id.finger);
//                        tvContent.setText(R.string.choose_finger_dialog_title);
//                        title.setText(R.string.unlock_wallet);
//                        Context currentContext = this;
//                        builderTips = builder.create();
//                        builderTips.show();
//                    } catch (Exception er) {
                    e.printStackTrace();
                    if (builderTips != null) {
                        builderTips.dismiss();
                    }
                    Toast.makeText(VerifyWalletPasswordActivity.this, "Fingerprint init failed! Try again!", Toast.LENGTH_SHORT).show();
//                    }
                }
                break;
            case R.id.tvJoin:
                if ("".equals(etPassword.getText().toString().trim())) {
                    return;
                }
                String password = ETHWalletUtils.enCodePassword(etPassword.getText().toString().trim());
                if (password.equals(SpUtil.getString(this, ConstantValue.walletPassWord, ""))) {
                    ConstantValue.isShouldShowVertifyPassword = false;
                    if (getIntent().hasExtra("flag") && !getIntent().getStringExtra("flag").equals("")) {
                        startActivity(MainActivity.class);
                        overridePendingTransition(R.anim.main_activity_in, R.anim.splash_activity_out);
                        finish();
                    } else {
                        Intent intent = new Intent();
                        try {
                            intent.putExtra("position", getIntent().getStringExtra("position"));
                        } catch (Exception e) {

                        }
                        setResult(RESULT_OK, intent);
                        ConstantValue.isShouldShowVertifyPassword = false;
                        SpUtil.putLong(this, ConstantValue.unlockTime, Calendar.getInstance().getTimeInMillis());
                        finishActivity();
                    }
                } else {
                    ToastUtil.displayShortToast(getString(R.string.Incorrect_Password));
                }
                break;
            default:
                break;
        }
    }

    private void finishActivity() {
        finish();
        overridePendingTransition(0, R.anim.activity_translate_out_1);
    }

    @Override
    public void onBackPressed() {
        if (getIntent().hasExtra("flag") && getIntent().getStringExtra("flag").equals("")) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        mPresenter.unsubscribe();
        if (builderTips != null) {
            builderTips.dismiss();
            builderTips = null;
        }
        if (cancellationSignal != null) {
            cancellationSignal.cancel();
            cancellationSignal = null;
        }
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        if (myAuthCallback != null) {
            myAuthCallback.removeHandle();
            myAuthCallback = null;
        }
        if (handler != null) {
            handler = null;
        }
        super.onDestroy();
    }

    private void handleHelpCode(int code) {
        switch (code) {
            case FingerprintManager.FINGERPRINT_ACQUIRED_GOOD:
            case FingerprintManager.FINGERPRINT_ACQUIRED_IMAGER_DIRTY:
            case FingerprintManager.FINGERPRINT_ACQUIRED_INSUFFICIENT:
            case FingerprintManager.FINGERPRINT_ACQUIRED_PARTIAL:
            case FingerprintManager.FINGERPRINT_ACQUIRED_TOO_FAST:
            case FingerprintManager.FINGERPRINT_ACQUIRED_TOO_SLOW:
                setResultInfo(R.string.AcquiredToSlow_warning);
                break;
        }
    }

    private void handleErrorCode(int code) {
        switch (code) {
            //case FingerprintManager.FINGERPRINT_ERROR_CANCELED:
            case FingerprintManager.FINGERPRINT_ERROR_HW_UNAVAILABLE:
            case FingerprintManager.FINGERPRINT_ERROR_LOCKOUT:
            case FingerprintManager.FINGERPRINT_ERROR_NO_SPACE:
            case FingerprintManager.FINGERPRINT_ERROR_TIMEOUT:
            case FingerprintManager.FINGERPRINT_ERROR_UNABLE_TO_PROCESS:
                if (builderTips != null)
                    builderTips.dismiss();
                setResultInfo(R.string.ErrorHwUnavailable_warning);
                break;
        }
    }

    private void setResultInfo(int stringId) {
        if (stringId == R.string.fingerprint_success) {
            if (finger != null) {
                finger.setImageDrawable(getResources().getDrawable(R.mipmap.icon_fingerprint_correct));
            }
            if (getIntent().hasExtra("flag")) {
                startActivity(MainActivity.class);
                ConstantValue.isShouldShowVertifyPassword = false;
                overridePendingTransition(R.anim.main_activity_in, R.anim.splash_activity_out);
                finish();
            } else {
                Intent intent = new Intent();
                try {
                    intent.putExtra("position", getIntent().getStringExtra("position"));
                } catch (Exception e) {

                }
                setResult(RESULT_OK, intent);
                ConstantValue.isShouldShowVertifyPassword = false;
                SpUtil.putString(this, ConstantValue.fingerPassWord, "888888");
                SpUtil.putLong(this, ConstantValue.unlockTime, Calendar.getInstance().getTimeInMillis());
                finishActivity();
            }
        } else {
            Toast.makeText(VerifyWalletPasswordActivity.this, stringId, Toast.LENGTH_SHORT).show();
        }
    }

    private void showGuideViewUnlock() {
        if (!GuideSpUtil.getBoolean(this, GuideConstantValue.isShowUnLockGuide, false)) {
            GuideSpUtil.putBoolean(this, GuideConstantValue.isShowUnLockGuide, true);
            GuideBuilder builder = new GuideBuilder();
//            builder.setTargetView(llUnlock)
//                    .setAlpha(150)
//                    .setHighTargetCorner(20)
//                    .setOverlayTarget(false)
//                    .setOutsideTouchable(true);
            builder.setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
                @Override
                public void onShown() {
                }

                @Override
                public void onDismiss() {
                    initData();
                }
            });

            builder.addComponent(new UnLockComponent());
            Guide guide = builder.createGuide();
            guide.setShouldCheckLocInWindow(false);
            guide.show(this);
        }
    }
}