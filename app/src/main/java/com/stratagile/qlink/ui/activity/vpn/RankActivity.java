package com.stratagile.qlink.ui.activity.vpn;

import android.app.FragmentManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.socks.library.KLog;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.card.CardFragment;
import com.stratagile.qlink.card.CardFragmentPagerAdapter;
import com.stratagile.qlink.card.ShadowTransformer;
import com.stratagile.qlink.entity.Active;
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
        LayoutInflaterCompat.setFactory(LayoutInflater.from(this), new LayoutInflaterFactory() {
            @Override
            public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
                if (name.equalsIgnoreCase("com.android.internal.view.menu.IconMenuItemView") || name.equalsIgnoreCase("com.android.internal.view.menu.ActionMenuItemView") || name.equalsIgnoreCase("android.support.v7.view.menu.ActionMenuItemView")) {
                    try {
                        LayoutInflater f = getLayoutInflater();
                        final View view = f.createView(name, null, attrs);
                        if (view instanceof TextView) {
                            ((TextView) view).setTextColor(getResources().getColor(R.color.white));
                            ((TextView) view).setTypeface(Typeface.createFromAsset(getAssets(), "vagroundedbt.ttf"));
                        }
                        return view;
                    } catch (InflateException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
        });
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
        viewModel = ViewModelProviders.of(this).get(RankViewModel.class);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                viewModel.getCurrentAct().postValue(((CardFragment) cardFragmentPagerAdapter.getItem(position)).getActId());
//                KLog.i(((CardFragment)cardFragmentPagerAdapter.getItem(position)).getActId());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewModel.getAct();
        setListener();
    }

    private void setListener() {
        viewModel.getActive().observe(this, new Observer<Active>() {
            @Override
            public void onChanged(@Nullable Active active) {
                cardFragmentPagerAdapter = new CardFragmentPagerAdapter(getSupportFragmentManager(), UIUtils.dip2px(1, RankActivity.this), active);
                viewPager.setAdapter(cardFragmentPagerAdapter);
                viewPager.setOffscreenPageLimit(3);
                viewPager.setPageMargin(20);
                mFragmentCardShadowTransformer = new ShadowTransformer(viewPager, cardFragmentPagerAdapter);
                mFragmentCardShadowTransformer.enableScaling(true);
                viewPager.setPageTransformer(false, mFragmentCardShadowTransformer);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.rank_rule, menu);
        return super.onCreateOptionsMenu(menu);
    }


}