package com.stratagile.qlink.ui.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.socks.library.KLog;
import com.stratagile.qlink.Account;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseFragment;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.entity.newwinq.Register;
import com.stratagile.qlink.ui.activity.my.component.DaggerLoginComponent;
import com.stratagile.qlink.ui.activity.my.contract.LoginContract;
import com.stratagile.qlink.ui.activity.my.module.LoginModule;
import com.stratagile.qlink.ui.activity.my.presenter.LoginPresenter;
import com.stratagile.qlink.utils.AccountUtil;
import com.stratagile.qlink.utils.MD5Util;
import com.stratagile.qlink.utils.RSAEncrypt;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.ToastUtil;
import com.subgraph.orchid.crypto.RSAKeyEncoder;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.my
 * @Description: $description
 * @date 2019/04/09 11:45:22
 */

public class LoginFragment extends BaseFragment implements LoginContract.View {

    @Inject
    LoginPresenter mPresenter;
    @BindView(R.id.tvLogin)
    TextView tvLogin;
    @BindView(R.id.tvForgetPassword)
    TextView tvForgetPassword;
    @BindView(R.id.etAccount)
    EditText etAccount;
    @BindView(R.id.etPassword)
    EditText etPassword;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, null);
        ButterKnife.bind(this, view);
        Bundle mBundle = getArguments();
        return view;
    }


    @Override
    protected void setupFragmentComponent() {
        DaggerLoginComponent
                .builder()
                .appComponent(((AppConfig) getActivity().getApplication()).getApplicationComponent())
                .loginModule(new LoginModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(LoginContract.LoginContractPresenter presenter) {
        mPresenter = (LoginPresenter) presenter;
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
    public void loginSuccess(Register register) {
        closeProgressDialog();
        SpUtil.putBoolean(getActivity(), ConstantValue.isUserLogin, true);
        if (AccountUtil.isTelephone(etAccount.getText().toString().trim())) {
            KLog.i("手机号码登录成功");
            SpUtil.putString(getActivity(), ConstantValue.userPhone, etAccount.getText().toString().trim());
        } else if (AccountUtil.isEmail(etAccount.getText().toString().trim())) {
            KLog.i("邮箱登录成功");
            SpUtil.putString(getActivity(), ConstantValue.userEmail, etAccount.getText().toString().trim());
        }
//        SpUtil.putString(getActivity(), ConstantValue.userRsaPubKey, register.getData());
        SpUtil.putString(getActivity(), ConstantValue.userPassword, MD5Util.getStringMD5(etPassword.getText().toString().trim()));
        ToastUtil.displayShortToast("login Success");
        getActivity().finish();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private String account;
    private String password;

    @OnClick({R.id.tvLogin, R.id.tvForgetPassword})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvLogin:
                account = etAccount.getText().toString().trim();
                password = etPassword.getText().toString().trim();
                if ("".equals(account)) {
                    ToastUtil.displayShortToast("account error");
                    return;
                }
                if (!AccountUtil.isTelephone(account) && !AccountUtil.isEmail(account)) {
                    ToastUtil.displayShortToast("account error");
                    return;
                }
                if ("".equals(password) || password.length() < 6) {
                    ToastUtil.displayShortToast("password error");
                    return;
                }
                login();
                break;
            case R.id.tvForgetPassword:
                startActivity(new Intent(getActivity(), RetrievePasswordActivity.class));
                break;
            default:
                break;
        }
    }

    private void login() {
        SpUtil.putString(getActivity(), ConstantValue.userRsaPubKey, "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCF4Udd/6e3wjiLrJQG/81NwfUlSLWygnALqoqLtAmCRoewfDZ+/a3B1o4xv9QFW61MIMa0SrmOUsLZXsUWDjWYVzuq1Joo9O4OwKJe6Tz+5kEzubendrttocuvLm/hrsqJ4iDgM37Wb7JhuNZfVWeNtk6GWgj18bAFu3FTthLCcwIDAQAB");
        showProgressDialog();
        Map map = new HashMap<String, String>();
        map.put("account", account);
        String orgin = Calendar.getInstance().getTimeInMillis() + "," + MD5Util.getStringMD5(password);
        KLog.i("加密前的原始：" + orgin);
        KLog.i("RSA 公钥为：" + SpUtil.getString(getActivity(), ConstantValue.userRsaPubKey, ""));
        String token = RSAEncrypt.encrypt(orgin, SpUtil.getString(getActivity(), ConstantValue.userRsaPubKey, ""));
        KLog.i("加密后：" + token);
        KLog.i("解密后：" + RSAEncrypt.decrypt(token, "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIXhR13/p7fCOIuslAb/zU3B9SVItbKCcAuqiou0CYJGh7B8Nn79rcHWjjG/1AVbrUwgxrRKuY5SwtlexRYONZhXO6rUmij07g7Aol7pPP7mQTO5t6d2u22hy68ub+GuyoniIOAzftZvsmG41l9VZ422ToZaCPXxsAW7cVO2EsJzAgMBAAECgYA9rTsjotOxZFiIgEjxsIb0e5ZkRsruIglcVoTdN2PqEHQSaibw+g1Cb4WyhZ03mrSLjc384S/60UXSvkYtkv49M7AdyftUWwNitK6ascjmYY4R01GWC7D3ZAaQxtM5K8GKTOTitQPXBl0FEkDNJGiVmP3rcO5Wxac2PKcDrNqLAQJBANk4WJZipzFh07mUhUcmcM+igSrDUAMcLCuyeMnmQARc+vItgd4LJuMMFPO9gZhJKzDOqmZ36alNbqXqlJabjEkCQQCdyAXp5Dj+XFwydLCF+ZbRZJqrakpzbU4JLUAMDNHV4ri8ROgV57aEebRsl/1N9d8vL8d5ysanGLADgZapc8DbAkAtTH3U5r/dIXyIz/s3SkHuWwI6y75M17wyZKah3B1vi4BdrrXNe1/hq2xXJCb5fhC+vep1Mf6NavNvMEtKWSfZAkEAh8WW6SS9sowxvi1RtYgIMxmaSwVFGbymWRk6MuRZMO9PPpshB7CEC81a59OGYq7AJi+8PF60wRdqZyn9RsXX3wJAEkIC/p1foWi6EtT79pQo9cBBovD6ggWsP0dnMqZkPkNKy1nhtiz99hnQ2jB97WvDvnXoJDUy2dCx81z8SEBKaQ=="));
        map.put("token", token);

        mPresenter.login(map);
    }
}