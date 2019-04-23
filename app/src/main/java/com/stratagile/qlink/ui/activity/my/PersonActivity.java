package com.stratagile.qlink.ui.activity.my;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.protobuf.Api;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.data.api.API;
import com.stratagile.qlink.data.api.MainAPI;
import com.stratagile.qlink.ui.activity.my.component.DaggerPersonComponent;
import com.stratagile.qlink.ui.activity.my.contract.PersonContract;
import com.stratagile.qlink.ui.activity.my.module.PersonModule;
import com.stratagile.qlink.ui.activity.my.presenter.PersonPresenter;
import com.stratagile.qlink.utils.SpUtil;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.my
 * @Description: $description
 * @date 2019/04/22 14:28:46
 */

public class PersonActivity extends BaseActivity implements PersonContract.View {

    @Inject
    PersonPresenter mPresenter;
    @BindView(R.id.profile)
    LinearLayout profile;
    @BindView(R.id.username)
    LinearLayout username;
    @BindView(R.id.inviteCode)
    LinearLayout inviteCode;
    @BindView(R.id.email)
    LinearLayout email;
    @BindView(R.id.mobile)
    LinearLayout mobile;
    @BindView(R.id.ivAvatar)
    ImageView ivAvatar;
    @BindView(R.id.tvUserName)
    TextView tvUserName;
    @BindView(R.id.tvInviteCode)
    TextView tvInviteCode;
    @BindView(R.id.tvEmail)
    TextView tvEmail;
    @BindView(R.id.tvMobile)
    TextView tvMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainColor = R.color.white;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_person);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void initData() {
        setTitle("Personal Info");
        if (!ConstantValue.currentUser.getEmail().equals("")) {
            tvEmail.setText(ConstantValue.currentUser.getEmail());
        } else {
            tvEmail.setText("unverified");
        }
        if (!ConstantValue.currentUser.getPhone().equals("")) {
            tvMobile.setText(ConstantValue.currentUser.getPhone());
        } else {
            tvMobile.setText("unverified");
        }
        tvInviteCode.setText(ConstantValue.currentUser.getInviteCode());
        tvUserName.setText(ConstantValue.currentUser.getUserName());
        if (!"".equals(ConstantValue.currentUser.getAvatar())) {
            Glide.with(this)
                    .load(API.BASE_URL + ConstantValue.currentUser.getAvatar())
                    .apply(AppConfig.getInstance().options)
                    .into(ivAvatar);
        }
    }

    @Override
    protected void setupActivityComponent() {
        DaggerPersonComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .personModule(new PersonModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(PersonContract.PersonContractPresenter presenter) {
        mPresenter = (PersonPresenter) presenter;
    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void closeProgressDialog() {
        progressDialog.hide();
    }

    @OnClick({R.id.profile, R.id.username, R.id.inviteCode, R.id.email, R.id.mobile})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.profile:
                break;
            case R.id.username:
                break;
            case R.id.inviteCode:
                break;
            case R.id.email:
                break;
            case R.id.mobile:
                break;
            default:
                break;
        }
    }
}