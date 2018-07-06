package com.stratagile.qlink.ui.activity.wifi.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wifi.ConnectWifiSuccessActivity;
import com.stratagile.qlink.ui.activity.wifi.contract.ConnectWifiSuccessContract;
import com.stratagile.qlink.ui.activity.wifi.presenter.ConnectWifiSuccessPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wifi
 * @Description: The moduele of ConnectWifiSuccessActivity, provide field for ConnectWifiSuccessActivity
 * @date 2018/01/19 19:46:50
 */
@Module
public class ConnectWifiSuccessModule {
    private final ConnectWifiSuccessContract.View mView;


    public ConnectWifiSuccessModule(ConnectWifiSuccessContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public ConnectWifiSuccessPresenter provideConnectWifiSuccessPresenter(HttpAPIWrapper httpAPIWrapper, ConnectWifiSuccessActivity mActivity) {
        return new ConnectWifiSuccessPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public ConnectWifiSuccessActivity provideConnectWifiSuccessActivity() {
        return (ConnectWifiSuccessActivity) mView;
    }
}