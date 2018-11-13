package com.stratagile.qlink.ui.activity.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.stratagile.qlink.ColdWallet;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.ui.activity.wallet.component.DaggerChangePasswordComponent;
import com.stratagile.qlink.ui.activity.wallet.contract.ChangePasswordContract;
import com.stratagile.qlink.ui.activity.wallet.module.ChangePasswordModule;
import com.stratagile.qlink.ui.activity.wallet.presenter.ChangePasswordPresenter;
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
 * @date 2018/11/12 13:44:10
 */

public class ChangePasswordActivity extends BaseActivity implements ChangePasswordContract.View {

    @Inject
    ChangePasswordPresenter mPresenter;
    @BindView(R.id.etCurrentPassword)
    EditText etCurrentPassword;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.etRepeatPassword)
    EditText etRepeatPassword;
    @BindView(R.id.tvJoin)
    TextView tvJoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainColor = R.color.white;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);
        setTitle("Change Password");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {
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
    protected void setupActivityComponent() {
        DaggerChangePasswordComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .changePasswordModule(new ChangePasswordModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(ChangePasswordContract.ChangePasswordContractPresenter presenter) {
        mPresenter = (ChangePasswordPresenter) presenter;
    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void closeProgressDialog() {
        progressDialog.hide();
    }

    @OnClick(R.id.tvJoin)
    public void onViewClicked() {
        showProgressDialog();
        if ("".equals(etCurrentPassword.getText().toString().trim())) {
            closeProgressDialog();
            return;
        }
        String currentPassword = ETHWalletUtils.enCodePassword(etCurrentPassword.getText().toString().trim());
        if (!currentPassword.equals(SpUtil.getString(this, ConstantValue.walletPassWord, ""))) {
            ToastUtil.displayShortToast("old password error");
            closeProgressDialog();
            return;
        }
        if (etPassword.getText().toString().trim().equals(etRepeatPassword.getText().toString().trim())) {
            if (etPassword.getText().toString().trim().length() < 9) {
                ToastUtil.displayShortToast(getString(R.string.The_password_must_contain_at_least_6_characters));
                closeProgressDialog();
                return;
            }
            String password = ETHWalletUtils.enCodePassword(etPassword.getText().toString().trim());
            SpUtil.putString(ChangePasswordActivity.this, ConstantValue.walletPassWord, password);
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
        closeProgressDialog();
    }
}