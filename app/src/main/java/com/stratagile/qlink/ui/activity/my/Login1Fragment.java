package com.stratagile.qlink.ui.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseFragment;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.db.UserAccount;
import com.stratagile.qlink.entity.VcodeLogin;
import com.stratagile.qlink.entity.eventbus.LoginSuccess;
import com.stratagile.qlink.entity.newwinq.Register;
import com.stratagile.qlink.ui.activity.my.component.DaggerLogin1Component;
import com.stratagile.qlink.ui.activity.my.contract.Login1Contract;
import com.stratagile.qlink.ui.activity.my.module.Login1Module;
import com.stratagile.qlink.ui.activity.my.presenter.Login1Presenter;
import com.stratagile.qlink.utils.AccountUtil;
import com.stratagile.qlink.utils.MD5Util;
import com.stratagile.qlink.utils.RSAEncrypt;
import com.stratagile.qlink.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.my
 * @Description: $description
 * @date 2019/04/24 18:02:10
 */

public class Login1Fragment extends BaseFragment implements Login1Contract.View {

    @Inject
    Login1Presenter mPresenter;
    @BindView(R.id.etAccount)
    EditText etAccount;
    @BindView(R.id.etVcode)
    EditText etVcode;
    @BindView(R.id.tvVerificationCode)
    TextView tvVerificationCode;
    @BindView(R.id.llVcode)
    LinearLayout llVcode;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.tvLogin)
    TextView tvLogin;
    @BindView(R.id.tvForgetPassword)
    TextView tvForgetPassword;

    private FirebaseAnalytics mFirebaseAnalytics;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, null);
        ButterKnife.bind(this, view);
        Bundle mBundle = getArguments();
        etAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (AccountUtil.isEmail(s.toString().trim()) || AccountUtil.isTelephone(s.toString().trim())) {
                    regexAccount(s.toString().trim());
                } else {
                    llVcode.setVisibility(View.GONE);
                }
            }
        });
        if (ConstantValue.lastLoginOut != null) {
            if (ConstantValue.lastLoginOut.getEmail() == null || "".equals(ConstantValue.lastLoginOut.getEmail())) {
                etAccount.setText(ConstantValue.lastLoginOut.getPhone());
            } else {
                etAccount.setText(ConstantValue.lastLoginOut.getEmail());
            }
            etAccount.setSelection(etAccount.getText().length());
        }
        return view;
    }

    boolean isVCodeLogin = false;

    //返回true，代表验证码登录
    private boolean regexAccount(String account) {
        List<UserAccount> userAccounts = AppConfig.getInstance().getDaoSession().getUserAccountDao().loadAll();
        if (userAccounts.size() > 0) {
            for (UserAccount userAccount : userAccounts) {
                if (userAccount.getEmail() == null) {
                    userAccount.setEmail("");
                }
                if (userAccount.getPhone() == null) {
                    userAccount.setPhone("");
                }
                if ((account.equals(userAccount.getEmail()) || account.equals(userAccount.getPhone())) && userAccount.getPubKey() != null && !"".equals(userAccount.getPubKey())) {
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
    protected void setupFragmentComponent() {
        DaggerLogin1Component
                .builder()
                .appComponent(((AppConfig) getActivity().getApplication()).getApplicationComponent())
                .login1Module(new Login1Module(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(Login1Contract.Login1ContractPresenter presenter) {
        mPresenter = (Login1Presenter) presenter;
    }

    @Override
    protected void initDataFromLocal() {

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
    public void onDestroyView() {
        super.onDestroyView();
    }

    private String account;
    private String password;

    @OnClick({R.id.tvLogin, R.id.tvForgetPassword, R.id.tvVerificationCode})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvLogin:
                account = etAccount.getText().toString().trim();
                password = etPassword.getText().toString().trim();
                if ("".equals(account)) {
                    ToastUtil.displayShortToast(getString(R.string.wrong_account));
                    return;
                }
                if (!AccountUtil.isTelephone(account) && !AccountUtil.isEmail(account)) {
                    ToastUtil.displayShortToast(getString(R.string.wrong_account));
                    return;
                }
                if ("".equals(password) || password.length() < 6) {
                    ToastUtil.displayShortToast(getString(R.string.wrong_password));
                    return;
                }
                if (regexAccount(account)) {
                    vCodeLogin();
                } else {
                    login();
                }
                break;
            case R.id.tvForgetPassword:
                startActivity(new Intent(getActivity(), RetrievePasswordActivity.class));
                break;
            case R.id.tvVerificationCode:
                getLoginVcode();
                break;
            default:
                break;
        }
    }
    private Disposable mdDisposable;

    private void startVCodeCountDown() {
        tvVerificationCode.setEnabled(false);
        tvVerificationCode.setBackground(getResources().getDrawable(R.drawable.vcode_count_down_bg));
        mdDisposable = Flowable.intervalRange(0, 60, 0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        tvVerificationCode.setText("" + (60 - aLong) + "");
                    }
                })
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        //倒计时完毕置为可点击状态
                        tvVerificationCode.setEnabled(true);
                        tvVerificationCode.setText(getString(R.string.get_the_code));
                        tvVerificationCode.setBackgroundColor(getResources().getColor(R.color.white));
                    }
                })
                .subscribe();
    }

    @Override
    public void onDestroy() {
        if (mdDisposable != null) {
            mdDisposable.dispose();
        }
        super.onDestroy();
    }

    /**
     * 获取登录验证码
     */
    private void getLoginVcode() {
        showProgressDialog();
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


    @Override
    public void loginSuccess(Register register) {
        closeProgressDialog();
        ToastUtil.displayShortToast(getString(R.string.Login_success));
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "login");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "login");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "login");
        mFirebaseAnalytics.logEvent("login", bundle);
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
        EventBus.getDefault().post(new LoginSuccess());
        getActivity().finish();
    }

    @Override
    public void vCodeLoginSuccess(VcodeLogin register) {
        UserAccount userAccount = new UserAccount();
        userAccount.setAccount(account);
        userAccount.setUserId(register.getId());
        userAccount.setVstatus(register.getVstatus());
        userAccount.setFacePhoto(register.getFacePhoto());
        userAccount.setHoldingPhoto(register.getHoldingPhoto());
        userAccount.setPubKey(register.getData());
        userAccount.setAccount(register.getAccount());
        userAccount.setAvatar(register.getHead());
        userAccount.setBindDate(register.getBindDate());
        userAccount.setInviteCode(register.getNumber());
        userAccount.setTotalInvite(register.getTotalInvite());
        userAccount.setEmail(register.getEmail());
        userAccount.setPhone(register.getPhone());
        userAccount.setUserName(register.getNickname());
        userAccount.setIsLogin(true);
        ConstantValue.currentUser = userAccount;
        AppConfig.getInstance().getDaoSession().getUserAccountDao().insert(userAccount);
        login();
    }

    @Override
    public void getLoginVCodeSuccess() {
        ToastUtil.displayShortToast("The verification code has been sent successfully.");
        startVCodeCountDown();
    }
}