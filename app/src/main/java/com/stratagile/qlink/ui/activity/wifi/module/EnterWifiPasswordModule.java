package com.stratagile.qlink.ui.activity.wifi.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wifi.EnterWifiPasswordActivity;
import com.stratagile.qlink.ui.activity.wifi.contract.EnterWifiPasswordContract;
import com.stratagile.qlink.ui.activity.wifi.presenter.EnterWifiPasswordPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wifi
 * @Description: The moduele of EnterWifiPasswordActivity, provide field for EnterWifiPasswordActivity
 * @date 2018/02/01 13:26:40
 */
@Module
public class EnterWifiPasswordModule {
    private final EnterWifiPasswordContract.View mView;


    public EnterWifiPasswordModule(EnterWifiPasswordContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public EnterWifiPasswordPresenter provideEnterWifiPasswordPresenter(HttpAPIWrapper httpAPIWrapper, EnterWifiPasswordActivity mActivity) {
        return new EnterWifiPasswordPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public EnterWifiPasswordActivity provideEnterWifiPasswordActivity() {
        return (EnterWifiPasswordActivity) mView;
    }
}