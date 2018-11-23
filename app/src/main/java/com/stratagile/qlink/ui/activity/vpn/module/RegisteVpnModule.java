package com.stratagile.qlink.ui.activity.vpn.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.entity.vpn.WindowConfig;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.vpn.RegisteVpnActivity;
import com.stratagile.qlink.ui.activity.vpn.contract.RegisteVpnContract;
import com.stratagile.qlink.ui.activity.vpn.presenter.RegisteVpnPresenter;
import com.stratagile.qlink.ui.adapter.vpn.WindowConfigAdapter;

import java.util.ArrayList;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.vpn
 * @Description: The moduele of RegisteVpnActivity, provide field for RegisteVpnActivity
 * @date 2018/02/06 15:41:02
 */
@Module
public class RegisteVpnModule {
    private final RegisteVpnContract.View mView;


    public RegisteVpnModule(RegisteVpnContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public RegisteVpnPresenter provideRegisteVpnPresenter(HttpAPIWrapper httpAPIWrapper, RegisteVpnActivity mActivity) {
        return new RegisteVpnPresenter(httpAPIWrapper, mView, mActivity);
    }

    @Provides
    @ActivityScope
    public RegisteVpnActivity provideRegisteVpnActivity() {
        return (RegisteVpnActivity) mView;
    }

    @Provides
    @ActivityScope
    public WindowConfigAdapter provideWindowConfigAdapter() {
        return new WindowConfigAdapter(new ArrayList<String>());
    }
}