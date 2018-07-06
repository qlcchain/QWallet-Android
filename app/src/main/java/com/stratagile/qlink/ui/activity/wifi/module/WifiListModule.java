package com.stratagile.qlink.ui.activity.wifi.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wifi.WifiListFragment;
import com.stratagile.qlink.ui.activity.wifi.contract.WifiListContract;
import com.stratagile.qlink.ui.activity.wifi.presenter.WifiListPresenter;
import com.stratagile.qlink.ui.adapter.wifi.WifiListAdapter;

import dagger.Module;
import dagger.Provides;
import java.util.ArrayList;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wifi
 * @Description: The moduele of WifiListFragment, provide field for WifiListFragment
 * @date 2018/01/09 14:02:09
 */
@Module
public class WifiListModule {
    private final WifiListContract.View mView;


    public WifiListModule(WifiListContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public WifiListPresenter provideWifiListPresenter(HttpAPIWrapper httpAPIWrapper, WifiListFragment fragment) {
        return new WifiListPresenter(httpAPIWrapper, mView, fragment);
    }

    @Provides
    @ActivityScope
    public WifiListFragment provideWifiListFragment() {
        return (WifiListFragment) mView;
    }

    @Provides
    @ActivityScope
    public WifiListAdapter provideWifiListAdapter() {
        return new WifiListAdapter(new ArrayList<>());
    }
}