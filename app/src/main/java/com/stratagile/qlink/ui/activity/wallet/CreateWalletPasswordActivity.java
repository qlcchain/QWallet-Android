package com.stratagile.qlink.ui.activity.wallet;

import android.content.Context;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.fingerprint.CryptoObjectHelper;
import com.stratagile.qlink.fingerprint.MyAuthCallback;
import com.stratagile.qlink.ui.activity.wallet.component.DaggerCreateWalletPasswordComponent;
import com.stratagile.qlink.ui.activity.wallet.contract.CreateWalletPasswordContract;
import com.stratagile.qlink.ui.activity.wallet.module.CreateWalletPasswordModule;
import com.stratagile.qlink.ui.activity.wallet.presenter.CreateWalletPasswordPresenter;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.ToastUtil;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: $description
 * @date 2018/01/24 17:48:46
 */

public class CreateWalletPasswordActivity extends BaseActivity implements CreateWalletPasswordContract.View {

    @Inject
    CreateWalletPasswordPresenter mPresenter;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_password_repeat)
    EditText etPasswordRepeat;
    @BindView(R.id.bt_back)
    Button btBack;
    @BindView(R.id.bt_continue)
    Button btContinue;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    ImageView finger;

    /*private FingerprintManagerCompat fingerprintManager = null;*/
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
        needFront = true;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_create_wallet_password);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                switch (msg.what) {
                    case MSG_AUTH_SUCCESS:
                        setResultInfo(R.string.fingerprint_success);
                        /*  mCancelBtn.setEnabled(false);
                        mStartBtn.setEnabled(true);*/
                        cancellationSignal = null;
                        break;
                    case MSG_AUTH_FAILED:
                        setResultInfo(R.string.fingerprint_not_recognized);
                        /*mCancelBtn.setEnabled(false);
                        mStartBtn.setEnabled(true);*/
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
    }

    @Override
    protected void initData() {
        tvTitle.setText(getString(R.string.SET_A_PASSWORD).toUpperCase());
        // init fingerprint.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && SpUtil.getBoolean(this, ConstantValue.fingerprintUnLock, true)) {
        try {
            FingerprintManager fingerprintManager =(FingerprintManager)AppConfig.getInstance().getSystemService(Context.FINGERPRINT_SERVICE);
            if (fingerprintManager!= null && fingerprintManager.isHardwareDetected() && fingerprintManager.hasEnrolledFingerprints())
            {
                if(SpUtil.getString(this, ConstantValue.fingerPassWord, "").equals(""))
                {
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
                    finger = (ImageView) view.findViewById(R.id.finger);
                    tvContent.setText(R.string.choose_finger_dialog_title);
                    title.setText(R.string.unlock_wallet);
                    Context currentContext = this;
                    builderTips = builder.create();
                    builderTips.show();
                    btn_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            builderTips.dismiss();
                            if (cancellationSignal != null) {
                                cancellationSignal.cancel();
                                cancellationSignal = null;
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
                            }
                           }
                    });
                    try {
                        myAuthCallback = new MyAuthCallback(handler);
                        CryptoObjectHelper cryptoObjectHelper = new CryptoObjectHelper();
                        if (cancellationSignal == null) {
                            cancellationSignal = new CancellationSignal();
                        }
                        fingerprintManager.authenticate(cryptoObjectHelper.buildCryptoObject(),cancellationSignal, 0,
                                myAuthCallback, null);
                    } catch (Exception e) {
                        try {
                            myAuthCallback = new MyAuthCallback(handler);
                            CryptoObjectHelper cryptoObjectHelper = new CryptoObjectHelper();
                            if (cancellationSignal == null) {
                                cancellationSignal = new CancellationSignal();
                            }
                            fingerprintManager.authenticate(cryptoObjectHelper.buildCryptoObject(), cancellationSignal,0,
                                    myAuthCallback, null);
                        } catch (Exception er) {
                            er.printStackTrace();
                            builderTips.dismiss();
                            //Toast.makeText(CreateWalletPasswordActivity.this, "Fingerprint init failed! Try again!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

            }else{
                SpUtil.putString(this, ConstantValue.fingerPassWord, "");
            }
        }catch (NoClassDefFoundError e)
        {
            SpUtil.putString(this, ConstantValue.fingerPassWord, "");
        }
        }else{
            SpUtil.putString(this, ConstantValue.fingerPassWord, "");
        }

    }

    @Override
    protected void setupActivityComponent() {
        DaggerCreateWalletPasswordComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .createWalletPasswordModule(new CreateWalletPasswordModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(CreateWalletPasswordContract.CreateWalletPasswordContractPresenter presenter) {
        mPresenter = (CreateWalletPasswordPresenter) presenter;
    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void closeProgressDialog() {
        progressDialog.hide();
    }

    @OnClick({R.id.bt_back, R.id.bt_continue})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_back:
                finish();
                break;
            case R.id.bt_continue:
                if (etPassword.getText().toString().trim().equals(etPasswordRepeat.getText().toString().trim())) {
                    if (etPassword.getText().toString().trim().length() < 6) {
                        ToastUtil.displayShortToast(getString(R.string.The_password_must_contain_at_least_6_characters));
                        return;
                    }
                    SpUtil.putString(this, ConstantValue.walletPassWord, etPassword.getText().toString().trim());
                    ConstantValue.isShouldShowVertifyPassword = false;
                    ToastUtil.displayShortToast(getString(R.string.Passwords_match));
                    Intent intent = new Intent();
                    try {
                        intent.putExtra("position", getIntent().getStringExtra("position"));
                    } catch (Exception e) {

                    }
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    ToastUtil.displayShortToast(getString(R.string.Passwords_donot_match_Try_again));
                }
                break;
            default:
                break;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(builderTips != null)
            builderTips.dismiss();
        if (cancellationSignal != null) {
            cancellationSignal.cancel();
            cancellationSignal = null;
        }
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
                if(builderTips != null)
                    builderTips.dismiss();
                setResultInfo(R.string.ErrorHwUnavailable_warning);
                break;
        }
    }

    private void setResultInfo(int stringId) {
        if (stringId == R.string.fingerprint_success) {
            if(finger != null){
                finger.setImageDrawable(getResources().getDrawable(R.mipmap.icon_fingerprint_complete));
            }
            ConstantValue.isShouldShowVertifyPassword = false;
            ToastUtil.displayShortToast(getString(R.string.Passwords_match));
            Intent intent = new Intent();
            try {
                intent.putExtra("position", getIntent().getStringExtra("position"));
            } catch (Exception e) {

            }
            SpUtil.putString(this, ConstantValue.fingerPassWord, "888888");
            setResult(RESULT_OK, intent);
            finish();
        }else{
            Toast.makeText(CreateWalletPasswordActivity.this, stringId, Toast.LENGTH_SHORT).show();
        }
    }
}