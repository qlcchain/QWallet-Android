package com.stratagile.qlink.ui.activity.seize.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.seize.SeizeActivity;
import com.stratagile.qlink.ui.activity.seize.contract.SeizeContract;
import com.stratagile.qlink.ui.activity.seize.presenter.SeizePresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.seize
 * @Description: The moduele of SeizeActivity, provide field for SeizeActivity
 * @date 2018/04/13 10:58:53
 */
@Module
public class SeizeModule {
    private final SeizeContract.View mView;


    public SeizeModule(SeizeContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public SeizePresenter provideSeizePresenter(HttpAPIWrapper httpAPIWrapper, SeizeActivity mActivity) {
        return new SeizePresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public SeizeActivity provideSeizeActivity() {
        return (SeizeActivity) mView;
    }
}