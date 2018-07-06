package com.stratagile.qlink.ui.activity.vpn.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.vpn.ConnectVpnSuccessActivity;
import com.stratagile.qlink.ui.activity.vpn.contract.ConnectVpnSuccessContract;
import com.stratagile.qlink.ui.activity.vpn.presenter.ConnectVpnSuccessPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.vpn
 * @Description: The moduele of ConnectVpnSuccessActivity, provide field for ConnectVpnSuccessActivity
 * @date 2018/02/09 10:28:44
 */
@Module
public class ConnectVpnSuccessModule {
    private final ConnectVpnSuccessContract.View mView;


    public ConnectVpnSuccessModule(ConnectVpnSuccessContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public ConnectVpnSuccessPresenter provideConnectVpnSuccessPresenter(HttpAPIWrapper httpAPIWrapper) {
        return new ConnectVpnSuccessPresenter(httpAPIWrapper, mView);
    }
}