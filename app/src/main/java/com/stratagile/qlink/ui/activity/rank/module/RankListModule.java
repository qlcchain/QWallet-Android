package com.stratagile.qlink.ui.activity.rank.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.rank.RankListFragment;
import com.stratagile.qlink.ui.activity.rank.contract.RankListContract;
import com.stratagile.qlink.ui.activity.rank.presenter.RankListPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.rank
 * @Description: The moduele of RankListFragment, provide field for RankListFragment
 * @date 2018/07/31 18:09:12
 */
@Module
public class RankListModule {
    private final RankListContract.View mView;


    public RankListModule(RankListContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public RankListPresenter provideRankListPresenter(HttpAPIWrapper httpAPIWrapper, RankListFragment mFragment) {
        return new RankListPresenter(httpAPIWrapper, mView, mFragment);
    }

    @Provides
    @ActivityScope
    public RankListFragment provideRankListFragment() {
        return (RankListFragment) mView;
    }
}