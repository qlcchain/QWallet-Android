package com.stratagile.qlink.ui.activity.vpn.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.vpn.ConnectVpnActivity;
import com.stratagile.qlink.ui.activity.vpn.contract.ConnectVpnContract;
import com.stratagile.qlink.ui.activity.vpn.presenter.ConnectVpnPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.vpn
 * @Description: The moduele of ConnectVpnActivity, provide field for ConnectVpnActivity
 * @date 2018/02/08 16:38:02
 */
@Module
public class ConnectVpnModule {
    private final ConnectVpnContract.View mView;


    public ConnectVpnModule(ConnectVpnContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public ConnectVpnPresenter provideConnectVpnPresenter(HttpAPIWrapper httpAPIWrapper) {
        return new ConnectVpnPresenter(httpAPIWrapper, mView);
    }
}