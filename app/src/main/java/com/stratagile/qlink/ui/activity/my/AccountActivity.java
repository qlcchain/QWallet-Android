package com.stratagile.qlink.ui.activity.my;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.ui.activity.my.component.DaggerAccountComponent;
import com.stratagile.qlink.ui.activity.my.contract.AccountContract;
import com.stratagile.qlink.ui.activity.my.module.AccountModule;
import com.stratagile.qlink.ui.activity.my.presenter.AccountPresenter;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.my
 * @Description: $description
 * @date 2019/04/09 11:31:42
 * 用户登录或者注册页面
 */

public class AccountActivity extends BaseActivity implements AccountContract.View {

    @Inject
    AccountPresenter mPresenter;
    @BindView(R.id.indicator)
    MagicIndicator indicator;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    public ChangePage changePage;

    public ChangePage getChangePage() {
        return changePage;
    }

    public void setChangePage(ChangePage changePage) {
        this.changePage = changePage;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainColor = R.color.white;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_account);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        ArrayList<String> titles = new ArrayList<>();
        titles.add(getString(R.string.login));
        titles.add(getString(R.string.sign_up));
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if (position == 0) {
                    return new Login1Fragment();
                } else {
                    return new RegiesterFragment();
                }
            }

            @Override
            public int getCount() {
                return titles.size();
            }
        });
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return titles.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, int i) {
                SimplePagerTitleView simplePagerTitleView = new SimplePagerTitleView(context);
                simplePagerTitleView.setText(titles.get(i));
                simplePagerTitleView.setNormalColor(getResources().getColor(R.color.color_333));
                simplePagerTitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19);
                simplePagerTitleView.setSelectedColor(getResources().getColor(R.color.mainColor));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(i);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                indicator.setLineHeight(getResources().getDimension(R.dimen.x3));
                indicator.setColors(getResources().getColor(R.color.mainColor));
                return indicator;
            }
        });
        indicator.setNavigator(commonNavigator);
        commonNavigator.getTitleContainer().setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        commonNavigator.getTitleContainer().setDividerDrawable(new ColorDrawable() {
            @Override
            public int getIntrinsicWidth() {
                return (int) getResources().getDimension(R.dimen.x140);
            }
        });
        ViewPagerHelper.bind(indicator, viewPager);
    }

    @Override
    protected void setupActivityComponent() {
        DaggerAccountComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .accountModule(new AccountModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(AccountContract.AccountContractPresenter presenter) {
        mPresenter = (AccountPresenter) presenter;
    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void closeProgressDialog() {
        progressDialog.hide();
    }

    public interface ChangePage {
        void change(int page);
    }

}