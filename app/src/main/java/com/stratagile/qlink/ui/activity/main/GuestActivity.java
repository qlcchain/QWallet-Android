package com.stratagile.qlink.ui.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nightonke.wowoviewpager.Animation.ViewAnimation;
import com.nightonke.wowoviewpager.Animation.WoWoPositionAnimation;
import com.nightonke.wowoviewpager.Animation.WoWoTranslationAnimation;
import com.nightonke.wowoviewpager.Enum.Ease;
import com.nightonke.wowoviewpager.WoWoViewPager;
import com.nightonke.wowoviewpager.WoWoViewPagerAdapter;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.db.Wallet;
import com.stratagile.qlink.ui.activity.main.component.DaggerGuestComponent;
import com.stratagile.qlink.ui.activity.main.contract.GuestContract;
import com.stratagile.qlink.ui.activity.main.module.GuestModule;
import com.stratagile.qlink.ui.activity.main.presenter.GuestPresenter;
import com.stratagile.qlink.ui.activity.wallet.VerifyWalletPasswordActivity;
import com.stratagile.qlink.utils.FileUtil;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.utils.UIUtils;
import com.stratagile.qlink.utils.VersionUtil;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.main
 * @Description: $description
 * @date 2018/06/21 15:39:34
 */

public class GuestActivity extends BaseActivity implements GuestContract.View {
    @Inject
    GuestPresenter mPresenter;
//    @BindView(R.id.wowo_viewpager)
//    WoWoViewPager wowo;
//    //    @BindView(R.id.circle)
////    View circle;
//    @BindView(R.id.iv_1)
//    ImageView iv1;
//
//    protected int screenW;
//    protected int screenH;
//    @BindView(R.id.tv_page0)
//    TextView tvPage0;
//    @BindView(R.id.tv_page0_test_net)
//    TextView tvPage0TestNet;
//    @BindView(R.id.iv_0)
//    ImageView iv0;
//    @BindView(R.id.tv_page1)
//    TextView tvPage1;
//    @BindView(R.id.iv_2)
//    ImageView iv2;
//    @BindView(R.id.tv_page2)
//    TextView tvPage2;
//    @BindView(R.id.got_it)
//    TextView gotIt;
//    @BindView(R.id.dot0)
//    ImageView dot0;
//    @BindView(R.id.dot1)
//    ImageView dot1;
//    @BindView(R.id.dot2)
//    ImageView dot2;
//    @BindView(R.id.dot)
//    ImageView dot;
//    @BindView(R.id.rl_dot)
//    RelativeLayout rlDot;

    private boolean animationAdded = false;

    private int r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        needFront = true;
//        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);//设置状态栏黑色字体
        super.onCreate(savedInstanceState);
    }

    protected int fragmentNumber() {
        return 3;
    }

    protected Integer[] fragmentColorsRes() {
        return new Integer[]{
                R.color.white,
                R.color.white,
                R.color.white
        };
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_guest);
        ButterKnife.bind(this);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        toolbar.setTitle("");
        toolbar.setVisibility(View.GONE);
//        wowo.setAdapter(WoWoViewPagerAdapter.builder()
//                .fragmentManager(getSupportFragmentManager())
//                .count(fragmentNumber())                       // Fragment Count
//                .colorsRes(fragmentColorsRes())                // Colors of fragments
//                .build());
//        screenW = UIUtils.getDisplayWidth(this);
//        screenH = UIUtils.getDisplayHeigh(this);

//        r = (int) Math.sqrt(screenW * screenW + screenH * screenH) + 10;

//        wowo.addTemporarilyInvisibleViews(1, gotIt, iv2, tvPage2);
    }

    @Override
    protected void initData() {
        File file = new File(Environment.getExternalStorageDirectory() + "/Qlink/neoWallet");
        if (!file.exists()) {
            String addressNames = FileUtil.getAllAddressNames();
            Map<String, String> map = new HashMap<>();
            map.put("key", addressNames);
            if (!("".equals(addressNames))) {
                List<Wallet> walletList = AppConfig.getInstance().getDaoSession().getWalletDao().loadAll();
                if (walletList.size() == 0) {
                    ConstantValue.canClickWallet = false;
                    mPresenter.importWallet(map);
                }
            }
        }
        SpUtil.putInt(this, ConstantValue.LOCALVERSIONCODE, VersionUtil.getAppVersionCode(this));
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
//        addAnimations();
    }

//    private void addAnimations() {
//        if (animationAdded) {
//            return;
//        }
//        animationAdded = true;
//
//        addIv0();
//        addTvPage0();
//        addTvPage0TestNet();
//
//        addIv1();
//        addTvPage1();
//
//        addIv2();
//        addTvPage2();
//        addGotIt();
//
//        addDot();
//
//        wowo.ready();
//    }
//
//    protected int color(int colorRes) {
//        return ContextCompat.getColor(this, colorRes);
//    }
//
//    private void addTvPage0TestNet() {
//        wowo.addAnimation(tvPage0TestNet)
//                .add(WoWoTranslationAnimation.builder().page(0)
//                        .fromX(0).toX(-screenW)
//                        .fromY(0).toY(-600).build());
//    }
//
//    private void addTvPage0() {
//        wowo.addAnimation(tvPage0)
//                .add(WoWoTranslationAnimation.builder().page(0)
//                        .fromX(0).toX(screenW)
//                        .fromY(0).toY(500).build());
//    }
//
//
//    private void addIv0() {
//        wowo.addAnimation(iv0)
//                .add(WoWoTranslationAnimation.builder().page(0)
//                        .fromX(0).toX(-screenW)
//                        .keepY(0).toY(-500).build());
//    }
//
//    private void addTvPage1() {
//        wowo.addAnimation(tvPage1)
//                .add(WoWoTranslationAnimation.builder().page(0)
//                        .fromX(-screenW).toX(0)
//                        .fromY(500).toY(0).build())
//                .add(WoWoTranslationAnimation.builder().page(1)
//                        .fromX(0).toX(screenW)
//                        .fromY(0).toY(500).build());
//    }
//
//
//    private void addIv1() {
//        wowo.addAnimation(iv1)
//                .add(WoWoTranslationAnimation.builder().page(0)
//                        .fromX(screenW).toX(0)
//                        .keepY(-500).toY(0).build())
//                .add(WoWoTranslationAnimation.builder().page(1)
//                        .fromX(0).toX(-screenW)
//                        .keepY(0).toY(-500).build());
//    }
//
//    private void addTvPage2() {
//        wowo.addAnimation(tvPage2)
//                .add(WoWoTranslationAnimation.builder().page(1)
//                        .fromX(-screenW).toX(0)
//                        .fromY(500).toY(0).build())
//                .add(WoWoTranslationAnimation.builder().page(1)
//                        .fromX(0).toX(screenW)
//                        .fromY(0).toY(500).build());
//    }
//
//
//    private void addIv2() {
//        wowo.addAnimation(iv2)
//                .add(WoWoTranslationAnimation.builder().page(1)
//                        .fromX(screenW).toX(0)
//                        .keepY(-500).toY(0).build())
//                .add(WoWoTranslationAnimation.builder().page(1)
//                        .fromX(0).toX(-screenW)
//                        .keepY(0).toY(-500).build());
//    }
//
//    private void addGotIt() {
//        wowo.addAnimation(gotIt)
//                .add(WoWoTranslationAnimation.builder().page(1)
//                        .keepX(gotIt.getTranslationX())
//                        .fromY(screenH).toY(0).ease(Ease.OutBack)
//                        .build())
//                .add(WoWoTranslationAnimation.builder().page(1)
//                        .keepX(0)
//                        .fromY(0).toY(screenH)
//                        .ease(Ease.InCubic).sameEaseBack(false).build());
//    }
//
//    private void addDot() {
//        ViewAnimation viewAnimation = new ViewAnimation(dot);
//        viewAnimation.add(WoWoPositionAnimation.builder().page(0)
//                .fromX(-getResources().getDimension(R.dimen.x18) + dot.getWidth()).toX(dot.getX())
//                .keepY(0)
//                .ease(Ease.Linear).build());
//        viewAnimation.add(WoWoPositionAnimation.builder().page(1)
//                .fromX(dot.getX()).toX(dot.getX() + getResources().getDimension(R.dimen.x20) + dot.getWidth())
//                .keepY(0)
//                .ease(Ease.Linear).build());
//        wowo.addAnimation(viewAnimation);
//    }

    @Override
    protected void setupActivityComponent() {
        DaggerGuestComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .guestModule(new GuestModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }


    @Override
    public void setPresenter(GuestContract.GuestContractPresenter presenter) {
        mPresenter = (GuestPresenter) presenter;
    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void closeProgressDialog() {
        progressDialog.hide();
    }

    @OnClick(R.id.start)
    public void onViewClicked() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("flag", "splash");
        startActivity(intent);
        overridePendingTransition(R.anim.main_activity_in, R.anim.splash_activity_out);
        finish();
    }
}