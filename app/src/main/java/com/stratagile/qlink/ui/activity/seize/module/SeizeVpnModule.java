package com.stratagile.qlink.ui.activity.seize.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.seize.SeizeVpnActivity;
import com.stratagile.qlink.ui.activity.seize.contract.SeizeVpnContract;
import com.stratagile.qlink.ui.activity.seize.presenter.SeizeVpnPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.seize
 * @Description: The moduele of SeizeVpnActivity, provide field for SeizeVpnActivity
 * @date 2018/04/13 12:08:31
 */
@Module
public class SeizeVpnModule {
    private final SeizeVpnContract.View mView;


    public SeizeVpnModule(SeizeVpnContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public SeizeVpnPresenter provideSeizeVpnPresenter(HttpAPIWrapper httpAPIWrapper, SeizeVpnActivity mActivity) {
        return new SeizeVpnPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public SeizeVpnActivity provideSeizeVpnActivity() {
        return (SeizeVpnActivity) mView;
    }
}