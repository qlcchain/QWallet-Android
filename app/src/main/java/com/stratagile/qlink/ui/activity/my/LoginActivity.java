package com.stratagile.qlink.ui.activity.my;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.db.UserAccount;
import com.stratagile.qlink.entity.VcodeLogin;
import com.stratagile.qlink.entity.newwinq.Register;
import com.stratagile.qlink.ui.activity.my.component.DaggerLoginComponent;
import com.stratagile.qlink.ui.activity.my.contract.LoginContract;
import com.stratagile.qlink.ui.activity.my.module.LoginModule;
import com.stratagile.qlink.ui.activity.my.presenter.LoginPresenter;
import com.stratagile.qlink.utils.AccountUtil;
import com.stratagile.qlink.utils.MD5Util;
import com.stratagile.qlink.utils.RSAEncrypt;
import com.stratagile.qlink.utils.ToastUtil;
import com.stratagile.qlink.utils.UIUtils;

import java.util.Calendar;
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
 * @date 2019/04/23 10:05:31
 */

public class LoginActivity extends BaseActivity implements LoginContract.View {

    @Inject
    LoginPresenter mPresenter;
    @BindView(R.id.statusBar)
    View statusBar;
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.rlTitle)
    RelativeLayout rlTitle;
    @BindView(R.id.etAccount)
    EditText etAccount;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.tvLogin)
    TextView tvLogin;
    @BindView(R.id.tvForgetPassword)
    TextView tvForgetPassword;
    @BindView(R.id.tvSignUp)
    TextView tvSignUp;
    @BindView(R.id.tvVerificationCode)
    TextView tvVerificationCode;
    @BindView(R.id.etVcode)
    EditText etVcode;
    @BindView(R.id.llVcode)
    LinearLayout llVcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        needFront = true;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR); //设置状态栏黑色字体
        }

        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(UIUtils.getDisplayWidth(this), UIUtils.getStatusBarHeight(this));
        statusBar.setLayoutParams(llp);
    }

    @Override
    protected void initData() {
        etAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (AccountUtil.isEmail(s.toString())) {
                    regexAccount(s.toString());
                } else {
                    llVcode.setVisibility(View.GONE);
                }
            }
        });
    }

    boolean isVCodeLogin = false;

    //返回true，代表验证码登录
    private boolean regexAccount(String account) {
        List<UserAccount> userAccounts = AppConfig.getInstance().getDaoSession().getUserAccountDao().loadAll();
        if (userAccounts.size() > 0) {
            for (UserAccount userAccount : userAccounts) {
                if (userAccount.getAccount().equals(account) && userAccount.getPubKey() != null && !"".equals(userAccount.getPubKey())) {
                    //账号密码登录
                    isVCodeLogin = false;
                    llVcode.setVisibility(View.GONE);
                    return false;
                } else {
                    //验证码登录
                    isVCodeLogin = true;
                    llVcode.setVisibility(View.VISIBLE);
                }
            }
        }
        //验证码登录
        isVCodeLogin = true;
        llVcode.setVisibility(View.VISIBLE);
        return true;
    }

    @Override
    protected void setupActivityComponent() {
        DaggerLoginComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .loginModule(new LoginModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(LoginContract.LoginContractPresenter presenter) {
        mPresenter = (LoginPresenter) presenter;
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
    public void loginSuccess(Register register) {
        closeProgressDialog();
        ToastUtil.displayShortToast(getString(R.string.Login_success));
        List<UserAccount> userAccounts = AppConfig.getInstance().getDaoSession().getUserAccountDao().loadAll();
        if (userAccounts.size() > 0) {
            for (UserAccount userAccount : userAccounts) {
                if (userAccount.getAccount().equals(account) && userAccount.getPubKey() != null && !"".equals(userAccount.getPubKey())) {
                    //账号密码登录
                    userAccount.setIsLogin(true);
                    userAccount.setPassword(MD5Util.getStringMD5(password));
                    ConstantValue.currentUser = userAccount;
                    AppConfig.getInstance().getDaoSession().getUserAccountDao().update(userAccount);
                } else {
                    //验证码登录
                    userAccount.setIsLogin(false);
                    AppConfig.getInstance().getDaoSession().getUserAccountDao().update(userAccount);
                }
            }
        }
        finish();
    }

    @Override
    public void vCodeLoginSuccess(VcodeLogin register) {
        UserAccount userAccount = new UserAccount();
        userAccount.setAccount(account);
        userAccount.setPubKey(register.getData());
        userAccount.setAccount(register.getAccount());
        userAccount.setAvatar(register.getHead());
        userAccount.setInviteCode(register.getNumber());
        userAccount.setEmail(register.getEmail());
        userAccount.setPhone(register.getPhone());
        userAccount.setUserName(register.getNickname());
        userAccount.setIsLogin(true);
        ConstantValue.currentUser = userAccount;
        AppConfig.getInstance().getDaoSession().getUserAccountDao().insert(userAccount);
        login();
    }

    private String account;
    private String password;

    @OnClick({R.id.tvLogin, R.id.tvForgetPassword, R.id.tvVerificationCode, R.id.tvSignUp})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvLogin:
                account = etAccount.getText().toString().trim();
                password = etPassword.getText().toString().trim();
                if ("".equals(account)) {
                    ToastUtil.displayShortToast(getString(R.string.account_error));
                    return;
                }
                if (!AccountUtil.isTelephone(account) && !AccountUtil.isEmail(account)) {
                    ToastUtil.displayShortToast(getString(R.string.account_error));
                    return;
                }
                if ("".equals(password) || password.length() < 6) {
                    ToastUtil.displayShortToast(getString(R.string.password_error));
                    return;
                }
                if (regexAccount(account)) {
                    vCodeLogin();
                } else {
                    login();
                }
                break;
            case R.id.tvForgetPassword:
                startActivity(new Intent(this, RetrievePasswordActivity.class));
                break;
            case R.id.tvVerificationCode:
                getLoginVcode();
                break;

            case R.id.tvSignUp:
                startActivityForResult(new Intent(this, RegisgerActivity.class), 0);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == -1) {
            finish();
        }
    }

    /**
     * 获取登录验证码
     */
    private void getLoginVcode() {
        Map map = new HashMap<String, String>();
        map.put("account", etAccount.getText().toString().trim());
        mPresenter.getSignInVcode(map);
    }

    private void vCodeLogin() {
        showProgressDialog();
        Map map = new HashMap<String, String>();
        map.put("account", etAccount.getText().toString().trim());
        map.put("code", etVcode.getText().toString().trim());
        mPresenter.vCodeLogin(map);
    }

    private void login() {
        List<UserAccount> userAccounts = AppConfig.getInstance().getDaoSession().getUserAccountDao().loadAll();
        for (UserAccount userAccount : userAccounts) {
            if (userAccount.getAccount().equals(etAccount.getText().toString().trim())) {
                showProgressDialog();
                Map map = new HashMap<String, String>();
                map.put("account", account);
                String orgin = Calendar.getInstance().getTimeInMillis() + "," + MD5Util.getStringMD5(password);
                KLog.i("加密前的原始：" + orgin);
                String token = RSAEncrypt.encrypt(orgin, userAccount.getPubKey());
                KLog.i("加密后：" + token);
                map.put("token", token);
                mPresenter.login(map);
            }
        }
    }

}