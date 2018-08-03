package com.stratagile.qlink.ui.activity.vpn.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.vpn.RegisteWindowVpnActivityActivity;
import com.stratagile.qlink.ui.activity.vpn.contract.RegisteWindowVpnActivityContract;
import com.stratagile.qlink.ui.activity.vpn.presenter.RegisteWindowVpnActivityPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author zl
 * @Package com.stratagile.qlink.ui.activity.vpn
 * @Description: The moduele of RegisteWindowVpnActivityActivity, provide field for RegisteWindowVpnActivityActivity
 * @date 2018/08/03 11:56:07
 */
@Module
public class RegisteWindowVpnActivityModule {
    private final RegisteWindowVpnActivityContract.View mView;


    public RegisteWindowVpnActivityModule(RegisteWindowVpnActivityContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public RegisteWindowVpnActivityPresenter provideRegisteWindowVpnActivityPresenter(HttpAPIWrapper httpAPIWrapper, RegisteWindowVpnActivityActivity mActivity) {
        return new RegisteWindowVpnActivityPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public RegisteWindowVpnActivityActivity provideRegisteWindowVpnActivityActivity() {
        return (RegisteWindowVpnActivityActivity) mView;
    }
}