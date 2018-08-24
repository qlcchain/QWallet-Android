package com.stratagile.qlink.ui.activity.wifi.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wifi.RegisteWifiSuccessActivity;
import com.stratagile.qlink.ui.activity.wifi.contract.RegisteWifiSuccessContract;
import com.stratagile.qlink.ui.activity.wifi.presenter.RegisteWifiSuccessPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wifi
 * @Description: The moduele of RegisteWifiSuccessActivity, provide field for RegisteWifiSuccessActivity
 * @date 2018/01/19 19:45:43
 */
@Module
public class RegisteWifiSuccessModule {
    private final RegisteWifiSuccessContract.View mView;


    public RegisteWifiSuccessModule(RegisteWifiSuccessContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public RegisteWifiSuccessPresenter provideRegisteWifiSuccessPresenter(HttpAPIWrapper httpAPIWrapper, RegisteWifiSuccessActivity mActivity) {
        return new RegisteWifiSuccessPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public RegisteWifiSuccessActivity provideRegisteWifiSuccessActivity() {
        return (RegisteWifiSuccessActivity) mView;
    }
}