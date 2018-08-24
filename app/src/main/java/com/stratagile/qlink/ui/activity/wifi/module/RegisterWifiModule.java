package com.stratagile.qlink.ui.activity.wifi.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wifi.RegisterWifiActivity;
import com.stratagile.qlink.ui.activity.wifi.contract.RegisterWifiContract;
import com.stratagile.qlink.ui.activity.wifi.presenter.RegisterWifiPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wifi
 * @Description: The moduele of RegisterWifiActivity, provide field for RegisterWifiActivity
 * @date 2018/01/09 17:28:09
 */
@Module
public class RegisterWifiModule {
    private final RegisterWifiContract.View mView;


    public RegisterWifiModule(RegisterWifiContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public RegisterWifiPresenter provideRegisterWifiPresenter(HttpAPIWrapper httpAPIWrapper, RegisterWifiActivity mActivity) {
        return new RegisterWifiPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public RegisterWifiActivity provideRegisterWifiActivity() {
        return (RegisterWifiActivity) mView;
    }
}