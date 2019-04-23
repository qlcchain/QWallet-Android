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

import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseFragment;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.entity.BaseBack;
import com.stratagile.qlink.entity.newwinq.Register;
import com.stratagile.qlink.ui.activity.my.component.DaggerRegiesterComponent;
import com.stratagile.qlink.ui.activity.my.contract.RegiesterContract;
import com.stratagile.qlink.ui.activity.my.module.RegiesterModule;
import com.stratagile.qlink.ui.activity.my.presenter.RegiesterPresenter;
import com.stratagile.qlink.utils.AccountUtil;
import com.stratagile.qlink.utils.MD5Util;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.ToastUtil;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
    @BindView(R.id.llphone)
    LinearLayout llphone;
    @BindView(R.id.tvVerificationCode)
    TextView tvVerificationCode;
    @BindView(R.id.registMode)
    TextView registMode;
    @BindView(R.id.tvRegister)
    TextView tvRegister;
    @BindView(R.id.country)
    TextView country;
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
    @BindView(R.id.etPhone)
    EditText etPhone;

    private boolean isPhoneRegisterMode = true;

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
    public void registerSuccess(Register register) {
        closeProgressDialog();
        if (register.getCode().equals("0")) {
            SpUtil.putString(getActivity(), ConstantValue.userRsaPubKey, register.getData());
            if (isPhoneRegisterMode) {
                SpUtil.putString(getActivity(), ConstantValue.userPhone, etPhone.getText().toString().trim());
            } else {
                SpUtil.putString(getActivity(), ConstantValue.userEmail, etEmail.getText().toString().trim());
            }
            SpUtil.putString(getActivity(), ConstantValue.userPassword, MD5Util.getStringMD5(etPassword.getText().toString().trim()));
            ToastUtil.displayShortToast("register success");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    String password;
    String phone;
    String email;

    @OnClick({R.id.tvVerificationCode, R.id.registMode, R.id.tvRegister, R.id.country})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.country:
                startActivity(new Intent(getActivity(), SelectCountryActivity.class));
                break;
            case R.id.tvVerificationCode:
                Map map = new HashMap<String, String>();
                if (isPhoneRegisterMode) {
                    map.put("account", "18670819116");
                } else {
                    map.put("account", "18670819116");
                }
                mPresenter.getSignUpVcode(map);
                break;
            case R.id.registMode:
                if (isPhoneRegisterMode) {
                    isPhoneRegisterMode = false;
                    registMode.setText("手机注册");
                    llEmail.setVisibility(View.VISIBLE);
                    llphone.setVisibility(View.GONE);
                } else {
                    isPhoneRegisterMode = true;
                    registMode.setText("邮箱注册");
                    llEmail.setVisibility(View.GONE);
                    llphone.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.tvRegister:
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
        if (isPhoneRegisterMode) {
            phone = etPhone.getText().toString().trim();
            if ("".equals(phone) || !AccountUtil.isTelephone(phone)) {
                ToastUtil.displayShortToast("account error");
                return;
            }
            map.put("account", phone);
        } else {
            email = etEmail.getText().toString().trim();
            if ("".equals(email) || !AccountUtil.isTelephone(email)) {
                ToastUtil.displayShortToast("account error");
                return;
            }
            map.put("account", email);
        }
        if ("".equals(password) || password.length() < 6) {
            ToastUtil.displayShortToast("password error");
            return;
        }
        showProgressDialog();
        map.put("password", MD5Util.getStringMD5(password));
        map.put("p2pId", SpUtil.getString(getActivity(), ConstantValue.P2PID, ""));
        map.put("code", etVCode.getText().toString().trim());
        mPresenter.register(map);
    }

}