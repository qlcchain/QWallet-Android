package com.stratagile.qlink.ui.activity.wifi.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wifi.WifiFragment;
import com.stratagile.qlink.ui.activity.wifi.contract.WifiContract;
import com.stratagile.qlink.ui.activity.wifi.presenter.WifiPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wifi
 * @Description: The moduele of WifiFragment, provide field for WifiFragment
 * @date 2018/01/09 13:46:43
 */
@Module
public class WifiModule {
    private final WifiContract.View mView;


    public WifiModule(WifiContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public WifiPresenter provideWifiPresenter(HttpAPIWrapper httpAPIWrapper, WifiFragment mFragment) {
        return new WifiPresenter(httpAPIWrapper, mView, mFragment);
    }

    @Provides
    @ActivityScope
    public WifiFragment provideWifiFragment() {
        return (WifiFragment) mView;
    }
}