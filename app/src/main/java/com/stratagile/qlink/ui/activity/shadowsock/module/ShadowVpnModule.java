package com.stratagile.qlink.ui.activity.shadowsock.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.shadowsock.ShadowVpnActivity;
import com.stratagile.qlink.ui.activity.shadowsock.contract.ShadowVpnContract;
import com.stratagile.qlink.ui.activity.shadowsock.presenter.ShadowVpnPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.shadowsock
 * @Description: The moduele of ShadowVpnActivity, provide field for ShadowVpnActivity
 * @date 2018/08/07 11:54:13
 */
@Module
public class ShadowVpnModule {
    private final ShadowVpnContract.View mView;


    public ShadowVpnModule(ShadowVpnContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public ShadowVpnPresenter provideShadowVpnPresenter(HttpAPIWrapper httpAPIWrapper, ShadowVpnActivity mActivity) {
        return new ShadowVpnPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public ShadowVpnActivity provideShadowVpnActivity() {
        return (ShadowVpnActivity) mView;
    }
}