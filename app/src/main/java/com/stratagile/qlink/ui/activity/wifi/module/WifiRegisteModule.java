package com.stratagile.qlink.ui.activity.wifi.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wifi.WifiRegisteFragment;
import com.stratagile.qlink.ui.activity.wifi.contract.WifiRegisteContract;
import com.stratagile.qlink.ui.activity.wifi.presenter.WifiRegistePresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wifi
 * @Description: The moduele of WifiRegisteFragment, provide field for WifiRegisteFragment
 * @date 2018/01/15 11:52:32
 */
@Module
public class WifiRegisteModule {
    private final WifiRegisteContract.View mView;


    public WifiRegisteModule(WifiRegisteContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public WifiRegistePresenter provideWifiRegistePresenter(HttpAPIWrapper httpAPIWrapper, WifiRegisteFragment mFragment) {
        return new WifiRegistePresenter(httpAPIWrapper, mView, mFragment);
    }

    @Provides
    @ActivityScope
    public WifiRegisteFragment provideWifiRegisteFragment() {
        return (WifiRegisteFragment) mView;
    }
}