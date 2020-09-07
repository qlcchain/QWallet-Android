package com.stratagile.qlink.ui.activity.my;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.constant.MainConstant;
import com.stratagile.qlink.db.UserAccount;
import com.stratagile.qlink.entity.VcodeLogin;
import com.stratagile.qlink.ui.activity.my.component.DaggerRetrievePasswordComponent;
import com.stratagile.qlink.ui.activity.my.contract.RetrievePasswordContract;
import com.stratagile.qlink.ui.activity.my.module.RetrievePasswordModule;
import com.stratagile.qlink.ui.activity.my.presenter.RetrievePasswordPresenter;
import com.stratagile.qlink.utils.AccountUtil;
import com.stratagile.qlink.utils.MD5Util;
import com.stratagile.qlink.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

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
 * @Description: $description 找回密码页面
 * @date 2019/04/09 14:21:19
 */

public class RetrievePasswordActivity extends BaseActivity implements RetrievePasswordContract.View {

    @Inject
    RetrievePasswordPresenter mPresenter;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.llEmail)
    LinearLayout llEmail;
    @BindView(R.id.etVCode)
    EditText etVCode;
    @BindView(R.id.tvVerificationCode)
    TextView tvVerificationCode;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.etRepeatPassword)
    EditText etRepeatPassword;
    @BindView(R.id.tvNext)
    TextView tvNext;
    @BindView(R.id.webview)
    WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainColor = R.color.white;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_retrieve_password);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {
        setTitle(getString(R.string.forget_passwrod));
        if (getIntent().hasExtra("flag")) {
            setTitle(getString(R.string.reset_password));
            etEmail.setText(ConstantValue.currentUser.getAccount());
            etEmail.setEnabled(false);
        }
        webview.setBackgroundColor(Color.WHITE);
        webview.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
        webview.getSettings().setUseWideViewPort(true);
        webview.getSettings().setLoadWithOverviewMode(true);
        // 禁止缓存加载，以确保可获取最新的验证图片。
        webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        // 设置不使用默认浏览器，而直接使用WebView组件加载页面。
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                KLog.i(url);
                view.loadUrl(url);
                return true;
            }
        });
        // 设置WebView组件支持加载JavaScript。
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setVerticalScrollBarEnabled(false);
        webview.setHorizontalScrollBarEnabled(false);
        // 建立JavaScript调用Java接口的桥梁。
        webview.addJavascriptInterface(new WebAppInterface(), "successCallback");
    }

    public class WebAppInterface {

        @JavascriptInterface
        public void postMessage(String message) {
            try {
                JSONObject jsonObject = new JSONObject(message);
                String token = jsonObject.getString("token");
                String sid = jsonObject.getString("sid");
                String sig = jsonObject.getString("sig");
                KLog.i(token);
                KLog.i(sid);
                KLog.i(sig);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        @JavascriptInterface
        public void sendToken(String token, String sid, String sig) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    KLog.i(token);
                    KLog.i(sid);
                    KLog.i(sig);
                    webview.setVisibility(View.GONE);
                    Map map = new HashMap<String, String>();
                    map.put("account", etEmail.getText().toString().trim());
                    map.put("sessionId", sid);
                    map.put("sig", sig);
                    map.put("afsToken", token);
                    map.put("appKey", MainConstant.afsFFFF0N00000000009290AppKey);
                    map.put("scene", MainConstant.ncOther);
                    mPresenter.getForgetPasswordVcode(map);
                }
            });
        }
    }

    @Override
    public void getResetPwCodeSuucess() {
        startVCodeCountDown();
    }

    @Override
    protected void setupActivityComponent() {
        DaggerRetrievePasswordComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .retrievePasswordModule(new RetrievePasswordModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(RetrievePasswordContract.RetrievePasswordContractPresenter presenter) {
        mPresenter = (RetrievePasswordPresenter) presenter;
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
    public void resetPasswordBack(VcodeLogin register) {
        closeProgressDialog();
        if (getIntent().hasExtra("flag")) {
            ToastUtil.displayShortToast(getString(R.string.reset_password_success));
            List<UserAccount> userAccounts = AppConfig.getInstance().getDaoSession().getUserAccountDao().loadAll();
            if (userAccounts.size() > 0) {
                for (UserAccount userAccount : userAccounts) {
                    if (userAccount.getAccount().toLowerCase().equals(etEmail.getText().toString().toLowerCase())) {
                        //账号密码登录
                        userAccount.setPassword(MD5Util.getStringMD5(etPassword.getText().toString().trim()));
                        userAccount.setPubKey(register.getData());
                        userAccount.setIsLogin(true);
                        ConstantValue.currentUser = userAccount;
                        AppConfig.getInstance().getDaoSession().getUserAccountDao().update(userAccount);
                        return;
                    }
                }
            }
            setResult(-1);
            finish();
        } else {
            ToastUtil.displayShortToast(getString(R.string.Password_retrieved_successfully_please_login_with_new_password));
            List<UserAccount> userAccounts = AppConfig.getInstance().getDaoSession().getUserAccountDao().loadAll();
            if (userAccounts.size() > 0) {
                for (UserAccount userAccount : userAccounts) {
                    if (userAccount.getAccount().equals(etEmail)) {
                        //账号密码登录
                        AppConfig.getInstance().getDaoSession().getUserAccountDao().delete(userAccount);
                    }
                }
            }
            UserAccount userAccount = new UserAccount();
            userAccount.setPubKey(register.getData());
            userAccount.setAccount(register.getAccount());
            userAccount.setAvatar(register.getHead());
            userAccount.setInviteCode(register.getNumber());
            userAccount.setPassword(MD5Util.getStringMD5(etPassword.getText().toString().trim()));
            userAccount.setEmail(register.getEmail());
            userAccount.setPhone(register.getPhone());
            userAccount.setUserName(register.getNickname());
            userAccount.setIsLogin(false);
            AppConfig.getInstance().getDaoSession().getUserAccountDao().insert(userAccount);
            finish();
        }
    }

    /**
     * 获取登录验证码
     */
    private void getForgetPasswordVcode() {
        webview.setVisibility(View.VISIBLE);
        webview.loadUrl("file:///android_asset/slideResetPassword.html");
    }

    private void resetPassword() {
        if ("".equals(etVCode.getText().toString().trim())) {
            ToastUtil.displayShortToast(getString(R.string.wrong_code));
            return;
        }
        if ("".equals(etPassword.getText().toString().trim()) || etPassword.getText().toString().trim().length() < 6) {
            ToastUtil.displayShortToast(getString(R.string.wrong_password));
            return;
        }
        if (!etPassword.getText().toString().trim().equals(etRepeatPassword.getText().toString().trim())) {
            ToastUtil.displayShortToast(getString(R.string.not_match));
            return;
        }
        Map map = new HashMap<String, String>();
        map.put("account", etEmail.getText().toString().trim());
        map.put("password", MD5Util.getStringMD5(etPassword.getText().toString().trim()));
        map.put("code", etVCode.getText().toString().trim());
        mPresenter.resetPassword(map);
    }

    @OnClick({R.id.tvVerificationCode, R.id.tvNext})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvVerificationCode:
                if (AccountUtil.isEmail(etEmail.getText().toString().trim())) {
                    getForgetPasswordVcode();
                } else {
                    ToastUtil.displayShortToast(getString(R.string.wrong_account));
                }
                break;
            case R.id.tvNext:
                resetPassword();
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

}