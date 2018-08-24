package com.stratagile.qlink.ui.activity.vpn.module;

import com.stratagile.qlink.data.api.HttpAPIWrapper;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.vpn.VpnListFragment;
import com.stratagile.qlink.ui.activity.vpn.contract.VpnListContract;
import com.stratagile.qlink.ui.activity.vpn.presenter.VpnListPresenter;
import com.stratagile.qlink.ui.adapter.vpn.VpnListAdapter;

import java.util.ArrayList;

import dagger.Module;
import dagger.Provides;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.vpn
 * @Description: The moduele of VpnListFragment, provide field for VpnListFragment
 * @date 2018/02/06 15:16:44
 */
@Module
public class VpnListModule {
    private final VpnListContract.View mView;


    public VpnListModule(VpnListContract.View view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public VpnListPresenter provideVpnListPresenter(HttpAPIWrapper httpAPIWrapper, VpnListFragment mFragment) {
        return new VpnListPresenter(httpAPIWrapper, mView, mFragment);
    }

    @Provides
    @ActivityScope
    public VpnListFragment provideVpnListFragment() {
        return (VpnListFragment) mView;
    }
    @Provides
    @ActivityScope
    public VpnListAdapter provideVpnListAdapter() {
        return new VpnListAdapter(new ArrayList<>());
    }
}