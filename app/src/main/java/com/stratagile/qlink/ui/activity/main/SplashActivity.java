package com.stratagile.qlink.ui.activity.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.statusbar.StatusBarCompat;
import com.stratagile.qlink.ui.activity.main.component.DaggerSplashComponent;
import com.stratagile.qlink.ui.activity.main.contract.SplashContract;
import com.stratagile.qlink.ui.activity.main.module.SplashModule;
import com.stratagile.qlink.ui.activity.main.presenter.SplashPresenter;
import com.stratagile.qlink.ui.activity.wallet.VerifyWalletPasswordActivity;
import com.stratagile.qlink.utils.FireBaseUtils;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.UIUtils;
import com.stratagile.qlink.utils.VersionUtil;

import java.util.Calendar;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.NO_ID;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.main
 * @Description: $description
 * @date 2018/01/09 11:24:32
 */

public class SplashActivity extends BaseActivity implements SplashContract.View {

    @Inject
    SplashPresenter mPresenter;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        needFront = true;
        super.onCreate(savedInstanceState);
        StatusBarCompat.translucentStatusBar(this, false);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_splash);
        rootLayout.setVisibility(View.GONE);
        ButterKnife.bind(this);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
//        KLog.i(UIUtils.isNavigationBarExist(this));
//        if (UIUtils.isNavigationBarExist(this)) {
//            statusBar1.setLayoutParams(new LinearLayout.LayoutParams(UIUtils.getDisplayWidth(this), (int) (UIUtils.getStatusBarHeight(this))));
//        }
    }

    @Override
    protected void initData() {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
//        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "startApp");
//        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "startApp");
//        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "startApp");
//        mFirebaseAnalytics.logEvent("startApp", bundle);
        FireBaseUtils.logEvent(this, FireBaseUtils.eventStartApp);
        SpUtil.putLong(AppConfig.getInstance(), ConstantValue.lastRestart, Calendar.getInstance().getTimeInMillis());
        mPresenter.getLastVersion();
        mPresenter.getPermission();
        mPresenter.observeJump();
        KLog.i(VersionUtil.getAppVersionCode(this));
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
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("flag", "splash");
        if (getIntent().getBundleExtra(ConstantValue.EXTRA_BUNDLE) != null) {
            intent.putExtra(ConstantValue.EXTRA_BUNDLE, getIntent().getBundleExtra(ConstantValue.EXTRA_BUNDLE));
        }
        startActivity(intent);
        overridePendingTransition(R.anim.main_activity_in, R.anim.splash_activity_out);
        finish();
    }

    @Override
    public void jumpToLogin() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("flag", "splash");
        if (getIntent().getBundleExtra(ConstantValue.EXTRA_BUNDLE) != null) {
            intent.putExtra(ConstantValue.EXTRA_BUNDLE,
                    getIntent().getBundleExtra(ConstantValue.EXTRA_BUNDLE));
        }
        startActivity(intent);
        overridePendingTransition(R.anim.main_activity_in, R.anim.splash_activity_out);
        finish();
    }

    @Override
    public void jumpToGuest() {
        if (ConstantValue.thisVersionShouldShowGuest) {
            Intent intent = new Intent(this, GuestActivity.class);
            intent.putExtra("flag", "splash");
            if (getIntent().getBundleExtra(ConstantValue.EXTRA_BUNDLE) != null) {
                intent.putExtra(ConstantValue.EXTRA_BUNDLE, getIntent().getBundleExtra(ConstantValue.EXTRA_BUNDLE));
            }
            startActivity(intent);
        } else {
            startActivity(VerifyWalletPasswordActivity.class);
        }
        finish();
    }

}