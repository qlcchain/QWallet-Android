package com.stratagile.qlink.ui.activity.vpn.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.vpn.RankActivity;
import com.stratagile.qlink.ui.activity.vpn.contract.RankContract;
import com.stratagile.qlink.ui.activity.vpn.presenter.RankPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.vpn
 * @Description: The moduele of RankActivity, provide field for RankActivity
 * @date 2018/07/31 17:14:45
 */
@Module
public class RankModule {
    private final RankContract.View mView;


    public RankModule(RankContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public RankPresenter provideRankPresenter(HttpAPIWrapper httpAPIWrapper, RankActivity mActivity) {
        return new RankPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public RankActivity provideRankActivity() {
        return (RankActivity) mView;
    }
}