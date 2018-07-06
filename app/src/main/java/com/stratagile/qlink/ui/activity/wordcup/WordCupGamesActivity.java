package com.stratagile.qlink.ui.activity.wordcup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import com.stratagile.qlink.Account;
import com.stratagile.qlink.R;
import com.stratagile.qlink.application.AppConfig;
import com.stratagile.qlink.base.BaseActivity;
import com.stratagile.qlink.constant.ConstantValue;
import com.stratagile.qlink.db.Wallet;
import com.stratagile.qlink.entity.eventbus.ChangeToTestWallet;
import com.stratagile.qlink.entity.eventbus.SetToMatchs;
import com.stratagile.qlink.ui.activity.mainwallet.MainWalletActivity;
import com.stratagile.qlink.ui.activity.wallet.NoWalletActivity;
import com.stratagile.qlink.ui.activity.wordcup.component.DaggerWordCupGamesComponent;
import com.stratagile.qlink.ui.activity.wordcup.contract.WordCupGamesContract;
import com.stratagile.qlink.ui.activity.wordcup.module.WordCupGamesModule;
import com.stratagile.qlink.ui.activity.wordcup.presenter.WordCupGamesPresenter;
import com.stratagile.qlink.utils.SpUtil;
import com.stratagile.qlink.view.ParentNoDispatchNoScrollViewpager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wordcup
 * @Description: $description
 * @date 2018/06/11 14:38:56
 */

public class WordCupGamesActivity extends BaseActivity implements WordCupGamesContract.View {

    @Inject
    WordCupGamesPresenter mPresenter;

    ArrayList<String> titles = new ArrayList<>();
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ParentNoDispatchNoScrollViewpager viewPager;
    private Activity this_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this_ = this;
        EventBus.getDefault().register(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_word_cup_games);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        SpUtil.putInt(this, ConstantValue.currentMainWallet, 4);
    }

    int index = 0;
    @Override
    protected void initData() {
        List<Wallet> walletList = AppConfig.getInstance().getDaoSession().getWalletDao().loadAll();
        boolean hasMainWallet = false;
        for (int i = 0; i < walletList.size(); i++) {
            if (walletList.get(i).getIsMain()) {
                hasMainWallet = true;
                index = i;
                break;
            }
        }
        if (!hasMainWallet) {
            Intent intent = new Intent(this, NoWalletActivity.class);
            intent.putExtra("fromType", "worldCup");
            intent.putExtra("flag", "nowallet");
            startActivityForResult(intent, 0);
        } else {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Account.INSTANCE.fromWIF(walletList.get(SpUtil.getInt(WordCupGamesActivity.this, ConstantValue.currentMainWallet, index)).getWif());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                }
            }).start();
        }
        relativeLayout_root.setBackground(getResources().getDrawable(R.mipmap.bg_header));
        setTitle("worldcup game");
        titles.add("MATCHS");
        titles.add("MATCH RESULTS");
        titles.add("MAINNET WALLET");
        viewPager.setScroll(false);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if (position == 0) {
                    return GameListFragment.newInstance("0");
                } else if (position == 1) {
                    return GameListFragment.newInstance("1");
                } else if (position == 2) {
                   // viewPager.setCurrentItem(2);
//                    return OpenMainWalletFragment.newInstance("2");
                    return new Fragment();
                } else {
                    return new Fragment();
                }
            }

            @Override
            public int getCount() {
                return titles.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titles.get(position);
            }
        });
        viewPager.setOffscreenPageLimit(2);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(0);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText().toString().equals("MAINNET WALLET")) {
                    startActivity(new Intent(WordCupGamesActivity.this, MainWalletActivity.class));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    protected void setupActivityComponent() {
        DaggerWordCupGamesComponent
                .builder()
                .appComponent(((AppConfig) getApplication()).getApplicationComponent())
                .wordCupGamesModule(new WordCupGamesModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(WordCupGamesContract.WordCupGamesContractPresenter presenter) {
        mPresenter = (WordCupGamesPresenter) presenter;
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode != RESULT_OK) {
                finish();
            } else {

            }
        }
    }

    @Subscribe (threadMode = ThreadMode.MAIN)
    public void setToMatchs(SetToMatchs setToMatchs) {
        viewPager.setCurrentItem(0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().post(new ChangeToTestWallet());
        EventBus.getDefault().unregister(this);
    }
}