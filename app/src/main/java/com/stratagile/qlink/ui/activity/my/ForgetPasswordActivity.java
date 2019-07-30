package com.stratagile.qlink.ui.activity.my;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.ui.activity.my.component.DaggerForgetPasswordComponent;
import com.stratagile.qlink.ui.activity.my.contract.ForgetPasswordContract;
import com.stratagile.qlink.ui.activity.my.module.ForgetPasswordModule;
import com.stratagile.qlink.ui.activity.my.presenter.ForgetPasswordPresenter;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.my
 * @Description: $description 修改已经登录的用户的密码
 * @date 2019/04/25 10:28:27
 */

public class ForgetPasswordActivity extends BaseActivity implements ForgetPasswordContract.View {

    @Inject
    ForgetPasswordPresenter mPresenter;
    @BindView(R.id.etAccount)
    EditText etAccount;
    @BindView(R.id.etVcode)
    EditText etVcode;
    @BindView(R.id.tvVerificationCode)
    TextView tvVerificationCode;
    @BindView(R.id.tvNext)
    TextView tvNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_forget_password);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setupActivityComponent() {
        DaggerForgetPasswordComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .forgetPasswordModule(new ForgetPasswordModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(ForgetPasswordContract.ForgetPasswordContractPresenter presenter) {
        mPresenter = (ForgetPasswordPresenter) presenter;
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
     * 获取登录验证码
     */
    private void getForgetPasswordVcode() {
        Map map = new HashMap<String, String>();
        map.put("account", etAccount.getText().toString().trim());
        mPresenter.getForgetPasswordVcode(map);
    }

    @OnClick({R.id.tvVerificationCode, R.id.tvNext})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvVerificationCode:
                getForgetPasswordVcode();
                break;
            case R.id.tvNext:

                break;
            default:
                break;
        }
    }
}