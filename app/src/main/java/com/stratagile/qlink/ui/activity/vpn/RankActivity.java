package com.stratagile.qlink.ui.activity.vpn;

import android.app.FragmentManager;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;

import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.card.CardFragmentPagerAdapter;
import com.stratagile.qlink.card.ShadowTransformer;
import com.stratagile.qlink.ui.activity.rank.RankListFragment;
import com.stratagile.qlink.ui.activity.rank.RankViewModel;
import com.stratagile.qlink.ui.activity.vpn.component.DaggerRankComponent;
import com.stratagile.qlink.ui.activity.vpn.contract.RankContract;
import com.stratagile.qlink.ui.activity.vpn.module.RankModule;
import com.stratagile.qlink.ui.activity.vpn.presenter.RankPresenter;
import com.stratagile.qlink.utils.UIUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.vpn
 * @Description: $description
 * @date 2018/07/31 17:14:45
 */

public class RankActivity extends BaseActivity implements RankContract.View {

    @Inject
    RankPresenter mPresenter;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    CardFragmentPagerAdapter cardFragmentPagerAdapter;
    ShadowTransformer mFragmentCardShadowTransformer;

    private RankViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_rank);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FragmentManager fragmentManager = getFragmentManager();
        //beginTransaction()
        //Start a series of edit operations on the Fragments associated with this FragmentManager.
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        //步骤二：用add()方法加上Fragment的对象rightFragment
        RankListFragment rightFragment = new RankListFragment();
        transaction.add(R.id.fragment_content, rightFragment);

        //步骤三：调用commit()方法使得FragmentTransaction实例的改变生效
        transaction.commit();
        setTitle(getString(R.string.weekly_ranking));
    }

    @Override
    protected void initData() {
        cardFragmentPagerAdapter = new CardFragmentPagerAdapter(getSupportFragmentManager(), UIUtils.dip2px(1, this));
        viewPager.setAdapter(cardFragmentPagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setPageMargin(20);
        mFragmentCardShadowTransformer = new ShadowTransformer(viewPager, cardFragmentPagerAdapter);
        mFragmentCardShadowTransformer.enableScaling(true);
        viewPager.setPageTransformer(false, mFragmentCardShadowTransformer);
        viewModel = ViewModelProviders.of(this).get(RankViewModel.class);
        viewModel.getCurrentPage().setValue(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                viewModel.getCurrentPage().postValue(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void setupActivityComponent() {
        DaggerRankComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .rankModule(new RankModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(RankContract.RankContractPresenter presenter) {
        mPresenter = (RankPresenter) presenter;
    }

    @Override
    public void showProgressDialog() {
        progressDialog.show();
    }

    @Override
    public void closeProgressDialog() {
        progressDialog.hide();
    }

}