package com.stratagile.qlink.ui.activity.main;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.ui.activity.main.component.DaggerSplashComponent;
import com.stratagile.qlink.ui.activity.main.contract.SplashContract;
import com.stratagile.qlink.ui.activity.main.module.SplashModule;
import com.stratagile.qlink.ui.activity.main.presenter.SplashPresenter;
import com.stratagile.qlink.utils.LocalAssetsUtils;
import com.stratagile.qlink.utils.SpUtil;

import java.util.Calendar;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.main
 * @Description: $description
 * @date 2018/01/09 11:24:32
 */

public class SplashActivity extends BaseActivity implements SplashContract.View {

    @Inject
    SplashPresenter mPresenter;
    @BindView(R.id.activity_splash_ImageViewLogo)
    ImageView activitySplashImageViewLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        needFront = true;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        Glide.with(this)
                .load(R.mipmap.splash_img)
                .into(activitySplashImageViewLogo);
    }

    @Override
    protected void initData() {
        SpUtil.putLong(AppConfig.getInstance(),ConstantValue.lastRestart, Calendar.getInstance().getTimeInMillis());
        mPresenter.getLastVersion();
        mPresenter.getPermission();
        mPresenter.observeJump();
    }

    @Override
    protected void setupActivityComponent() {
        DaggerSplashComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .splashModule(new SplashModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(SplashContract.SplashContractPresenter presenter) {
        mPresenter = (SplashPresenter) presenter;
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
    public void loginSuccees() {
        startActivity(MainActivity.class);
        overridePendingTransition(R.anim.main_activity_in, R.anim.splash_activity_out);
        finish();
    }

    @Override
    public void jumpToLogin() {
        startActivity(MainActivity.class);
        overridePendingTransition(R.anim.main_activity_in, R.anim.splash_activity_out);
        finish();
    }

    @Override
    public void jumpToGuest() {
        if (ConstantValue.thisVersionShouldShowGuest) {
            startActivity(GuestActivity.class);
        } else {
            startActivity(MainActivity.class);
        }
        finish();
    }

}