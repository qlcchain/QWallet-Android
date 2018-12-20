package com.stratagile.qlink.ui.activity.wallet;

import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.fingerprint.MyAuthCallback;
import com.stratagile.qlink.qlinkcom;
import com.stratagile.qlink.ui.activity.wallet.component.DaggerCreateWalletPasswordComponent;
import com.stratagile.qlink.ui.activity.wallet.contract.CreateWalletPasswordContract;
import com.stratagile.qlink.ui.activity.wallet.module.CreateWalletPasswordModule;
import com.stratagile.qlink.ui.activity.wallet.presenter.CreateWalletPasswordPresenter;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.ToastUtil;
import com.stratagile.qlink.utils.eth.ETHWalletUtils;

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
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.etRepeatPassword)
    EditText etRepeatPassword;
    @BindView(R.id.tvJoin)
    TextView tvJoin;

    private AlertDialog builderTips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainColor = R.color.white;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_create_wallet_password);
        ButterKnife.bind(this);
        etRepeatPassword.addTextChangedListener(new TextWatcher() {
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
    }

    @Override
    protected void initData() {
        setTitle("Set Password");
        tvJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etPassword.getText().toString().trim().equals(etRepeatPassword.getText().toString().trim())) {
                    if (etPassword.getText().toString().trim().length() < 9) {
                        ToastUtil.displayShortToast(getString(R.string.The_password_must_contain_at_least_6_characters));
                        return;
                    }
                    String password = ETHWalletUtils.enCodePassword(etPassword.getText().toString().trim());
                    SpUtil.putString(CreateWalletPasswordActivity.this, ConstantValue.walletPassWord, password);
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
            }
        });

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

//    @OnClick({R.id.tvJoin})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
////            case R.id.bt_back:
////                finish();
////                break;
//            case R.id.tvJoin:
//                if (etPassword.getText().toString().trim().equals(etRepeatPassword.getText().toString().trim())) {
//                    if (etPassword.getText().toString().trim().length() < 9) {
//                        ToastUtil.displayShortToast(getString(R.string.The_password_must_contain_at_least_6_characters));
//                        return;
//                    }
//                    String password = ETHWalletUtils.enCodePassword(etPassword.getText().toString().trim());
//                    SpUtil.putString(this, ConstantValue.walletPassWord, password);
//                    ConstantValue.isShouldShowVertifyPassword = false;
//                    ToastUtil.displayShortToast(getString(R.string.Passwords_match));
//                    Intent intent = new Intent();
//                    try {
//                        intent.putExtra("position", getIntent().getStringExtra("position"));
//                    } catch (Exception e) {
//
//                    }
//                    setResult(RESULT_OK, intent);
//                    finish();
//                } else {
//                    ToastUtil.displayShortToast(getString(R.string.Passwords_donot_match_Try_again));
//                }
//                break;
//            default:
//                break;
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if(builderTips != null)
//            builderTips.dismiss();
//        if (cancellationSignal != null) {
//            cancellationSignal.cancel();
//            cancellationSignal = null;
//        }
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
//        if (stringId == R.string.fingerprint_success) {
//            if(finger != null){
//                finger.setImageDrawable(getResources().getDrawable(R.mipmap.icon_fingerprint_complete));
//            }
//            ConstantValue.isShouldShowVertifyPassword = false;
//            ToastUtil.displayShortToast(getString(R.string.Passwords_match));
//            Intent intent = new Intent();
//            try {
//                intent.putExtra("position", getIntent().getStringExtra("position"));
//            } catch (Exception e) {
//
//            }
//            SpUtil.putString(this, ConstantValue.fingerPassWord, "888888");
//            setResult(RESULT_OK, intent);
//            finish();
//        }else{
//            Toast.makeText(CreateWalletPasswordActivity.this, stringId, Toast.LENGTH_SHORT).show();
//        }
    }
}