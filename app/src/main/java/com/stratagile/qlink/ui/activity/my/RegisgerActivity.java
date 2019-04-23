package com.stratagile.qlink.ui.activity.my;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.db.UserAccount;
import com.stratagile.qlink.entity.newwinq.Register;
import com.stratagile.qlink.ui.activity.my.component.DaggerRegisgerComponent;
import com.stratagile.qlink.ui.activity.my.contract.RegisgerContract;
import com.stratagile.qlink.ui.activity.my.module.RegisgerModule;
import com.stratagile.qlink.ui.activity.my.presenter.RegisgerPresenter;
import com.stratagile.qlink.utils.AccountUtil;
import com.stratagile.qlink.utils.MD5Util;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.ToastUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.my
 * @Description: $description
 * @date 2019/04/23 12:02:02
 */

public class RegisgerActivity extends BaseActivity implements RegisgerContract.View {

    @Inject
    RegisgerPresenter mPresenter;
    @BindView(R.id.etAccount)
    EditText etAccount;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.tvVerificationCode)
    TextView tvVerificationCode;
    @BindView(R.id.tvNext)
    TextView tvNext;
    @BindView(R.id.etVcode)
    EditText etVcode;
    @BindView(R.id.etRepeatPassword)
    EditText etRepeatPassword;
    @BindView(R.id.etInviteCode)
    EditText etInviteCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainColor = R.color.white;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_regisger);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setupActivityComponent() {
        DaggerRegisgerComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .regisgerModule(new RegisgerModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(RegisgerContract.RegisgerContractPresenter presenter) {
        mPresenter = (RegisgerPresenter) presenter;
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
    public void registerSuccess(Register register) {
        closeProgressDialog();
        SpUtil.putBoolean(this, ConstantValue.isUserLogin, true);
        if (AccountUtil.isTelephone(etAccount.getText().toString().trim())) {
            KLog.i("手机号码登录成功");
            SpUtil.putString(this, ConstantValue.userPhone, etAccount.getText().toString().trim());
        } else if (AccountUtil.isEmail(etAccount.getText().toString().trim())) {
            KLog.i("邮箱登录成功");
            SpUtil.putString(this, ConstantValue.userEmail, etAccount.getText().toString().trim());
        }
        SpUtil.putString(this, ConstantValue.userPassword, MD5Util.getStringMD5(etPassword.getText().toString().trim()));
        ToastUtil.displayShortToast("regisger success");

        UserAccount userAccount = new UserAccount();
        userAccount.setAccount(etAccount.getText().toString().trim());
        userAccount.setPubKey(register.getData());
        userAccount.setPassword(MD5Util.getStringMD5(password));
        userAccount.setIsLogin(true);
        AppConfig.getInstance().getDaoSession().getUserAccountDao().insert(userAccount);
        setResult(RESULT_OK);
        ConstantValue.currentUser = userAccount;
        finish();
    }

    String password;

    @OnClick({R.id.tvVerificationCode, R.id.tvNext})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvVerificationCode:
                Map map = new HashMap<String, String>();
                map.put("account", etAccount.getText().toString().trim());
                mPresenter.getSignUpVcode(map);
                break;
            case R.id.tvNext:
                password = etPassword.getText().toString().trim();
                register();
                break;
            default:
                break;
        }
    }

    /**
     * 手机号码注册
     */
    private void register() {
        Map map = new HashMap<String, String>();
        map.put("account", etAccount.getText().toString().trim());
        if ("".equals(password) || password.length() < 6) {
            ToastUtil.displayShortToast("password error");
            return;
        }
        showProgressDialog();
        map.put("password", MD5Util.getStringMD5(password));
        map.put("p2pId", SpUtil.getString(this, ConstantValue.P2PID, ""));
        map.put("code", etVcode.getText().toString().trim());
        mPresenter.register(map);
    }
}