package com.stratagile.qlink.ui.activity.finance.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.finance.EarnRankActivity;
import com.stratagile.qlink.ui.activity.finance.contract.EarnRankContract;
import com.stratagile.qlink.ui.activity.finance.presenter.EarnRankPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.finance
 * @Description: The moduele of EarnRankActivity, provide field for EarnRankActivity
 * @date 2019/04/24 11:27:01
 */
@Module
public class EarnRankModule {
    private final EarnRankContract.View mView;


    public EarnRankModule(EarnRankContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public EarnRankPresenter provideEarnRankPresenter(HttpAPIWrapper httpAPIWrapper, EarnRankActivity mActivity) {
        return new EarnRankPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public EarnRankActivity provideEarnRankActivity() {
        return (EarnRankActivity) mView;
    }
}