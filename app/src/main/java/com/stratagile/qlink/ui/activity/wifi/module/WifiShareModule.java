package com.stratagile.qlink.ui.activity.wifi.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wifi.WifiShareFragment;
import com.stratagile.qlink.ui.activity.wifi.contract.WifiShareContract;
import com.stratagile.qlink.ui.activity.wifi.presenter.WifiSharePresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wifi
 * @Description: The moduele of WifiShareFragment, provide field for WifiShareFragment
 * @date 2018/01/15 11:52:51
 */
@Module
public class WifiShareModule {
    private final WifiShareContract.View mView;


    public WifiShareModule(WifiShareContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public WifiSharePresenter provideWifiSharePresenter(HttpAPIWrapper httpAPIWrapper, WifiShareFragment mFragment) {
        return new WifiSharePresenter(httpAPIWrapper, mView, mFragment);
    }

    @Provides
    @ActivityScope
    public WifiShareFragment provideWifiShareFragment() {
        return (WifiShareFragment) mView;
    }
}