package com.stratagile.qlink.ui.activity.wifi.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wifi.ConnectWifiConfirmActivity;
import com.stratagile.qlink.ui.activity.wifi.contract.ConnectWifiConfirmContract;
import com.stratagile.qlink.ui.activity.wifi.presenter.ConnectWifiConfirmPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wifi
 * @Description: The moduele of ConnectWifiConfirmActivity, provide field for ConnectWifiConfirmActivity
 * @date 2018/01/19 19:46:26
 */
@Module
public class ConnectWifiConfirmModule {
    private final ConnectWifiConfirmContract.View mView;


    public ConnectWifiConfirmModule(ConnectWifiConfirmContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public ConnectWifiConfirmPresenter provideConnectWifiConfirmPresenter(HttpAPIWrapper httpAPIWrapper, ConnectWifiConfirmActivity mActivity) {
        return new ConnectWifiConfirmPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public ConnectWifiConfirmActivity provideConnectWifiConfirmActivity() {
        return (ConnectWifiConfirmActivity) mView;
    }
}