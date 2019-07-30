package com.stratagile.qlink.ui.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseFragment;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.db.UserAccount;
import com.stratagile.qlink.entity.VcodeLogin;
import com.stratagile.qlink.entity.eventbus.LoginSuccess;
import com.stratagile.qlink.ui.activity.main.WebViewActivity;
import com.stratagile.qlink.ui.activity.my.component.DaggerRegiesterComponent;
import com.stratagile.qlink.ui.activity.my.contract.RegiesterContract;
import com.stratagile.qlink.ui.activity.my.module.RegiesterModule;
import com.stratagile.qlink.ui.activity.my.presenter.RegiesterPresenter;
import com.stratagile.qlink.utils.AccountUtil;
import com.stratagile.qlink.utils.MD5Util;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.ToastUtil;
import com.stratagile.qlink.view.SmoothCheckBox;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
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
 * @date 2019/04/09 11:45:07
 */

public class RegiesterFragment extends BaseFragment implements RegiesterContract.View {

    @Inject
    RegiesterPresenter mPresenter;
    @BindView(R.id.llEmail)
    LinearLayout llEmail;
    @BindView(R.id.tvVerificationCode)
    TextView tvVerificationCode;
    @BindView(R.id.tvRegister)
    TextView tvRegister;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etVCode)
    EditText etVCode;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.etRepeatPassword)
    EditText etRepeatPassword;
    @BindView(R.id.etInviteCode)
    EditText etInviteCode;
    @BindView(R.id.checkBox)
    SmoothCheckBox checkBox;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_regiester, null);
        ButterKnife.bind(this, view);
        Bundle mBundle = getArguments();
        return view;
    }


    @Override
    protected void setupFragmentComponent() {
        DaggerRegiesterComponent
                .builder()
                .appComponent(((AppConfig) getActivity().getApplication()).getApplicationComponent())
                .regiesterModule(new RegiesterModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(RegiesterContract.RegiesterContractPresenter presenter) {
        mPresenter = (RegiesterPresenter) presenter;
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
    public void registerSuccess(VcodeLogin register) {
        closeProgressDialog();
        if (register.getCode().equals("0")) {
            closeProgressDialog();
            ToastUtil.displayShortToast(getString(R.string.register_success));
            UserAccount userAccount = new UserAccount();
            userAccount.setAccount(etEmail.getText().toString().trim());
            userAccount.setEmail(register.getEmail());
            userAccount.setPubKey(register.getData());
            userAccount.setUserName(register.getNickname());
            userAccount.setPhone(register.getPhone());
            userAccount.setAvatar(register.getHead());
            userAccount.setInviteCode(register.getId());
            userAccount.setPassword(MD5Util.getStringMD5(password));
            userAccount.setIsLogin(true);
            AppConfig.getInstance().getDaoSession().getUserAccountDao().insert(userAccount);
            ConstantValue.currentUser = userAccount;
            etVCode.setText("");
            etPassword.setText("");
            etEmail.setText("");
            etInviteCode.setText("");
            etRepeatPassword.setText("");
            EventBus.getDefault().post(new LoginSuccess());
            getActivity().finish();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    String password;

    @OnClick({R.id.tvVerificationCode, R.id.tvRegister, R.id.servicePrivacyPolicy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvVerificationCode:
                if ("".equals(etEmail.getText().toString().trim())) {
                    ToastUtil.displayShortToast(getString(R.string.wrong_account));
                    return;
                }
                if (AccountUtil.isEmail(etEmail.getText().toString().trim())) {
                    startVCodeCountDown();
                    Map map = new HashMap<String, String>();
                    map.put("account", etEmail.getText().toString().trim());
                    mPresenter.getSignUpVcode(map);
                } else {
                    ToastUtil.displayShortToast(getString(R.string.wrong_account));
                }
                break;
            case R.id.tvRegister:
                password = etPassword.getText().toString().trim();
                register();
                break;
            case R.id.servicePrivacyPolicy:
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("url", "https://winq.net/disclaimer.html");
                intent.putExtra("title", "Service agreement");
                startActivity(intent);
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
     * 手机号码注册
     */
    private void register() {
        if ("".equals(etEmail.getText().toString().trim())) {
            ToastUtil.displayShortToast(getString(R.string.wrong_account));
            return;
        }
        Map map = new HashMap<String, String>();
        map.put("account", etEmail.getText().toString().trim());
        if ("".equals(etVCode.getText().toString().trim())) {
            ToastUtil.displayShortToast(getString(R.string.wrong_code));
            return;
        }
        if ("".equals(password) || password.length() < 6) {
            ToastUtil.displayShortToast(getString(R.string.wrong_password));
            return;
        }
        if (!etPassword.getText().toString().trim().equals(etRepeatPassword.getText().toString().trim())) {
            ToastUtil.displayShortToast(getString(R.string.not_match));
            return;
        }
        if (!checkBox.isChecked()) {
            ToastUtil.displayShortToast(getString(R.string.please_agree_to_the_service_agreement));
            return;
        }
        showProgressDialog();
        map.put("password", MD5Util.getStringMD5(password));
        map.put("p2pId", SpUtil.getString(getActivity(), ConstantValue.P2PID, ""));
        map.put("code", etVCode.getText().toString().trim());
        if (!"".equals(etInviteCode.getText().toString().trim())) {
            map.put("number", etInviteCode.getText().toString().trim());
        }
        mPresenter.register(map);
    }

}