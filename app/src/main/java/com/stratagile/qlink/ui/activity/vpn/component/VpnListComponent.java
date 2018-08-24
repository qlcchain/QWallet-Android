package com.stratagile.qlink.ui.activity.vpn.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.vpn.VpnListFragment;
import com.stratagile.qlink.ui.activity.vpn.module.VpnListModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.vpn
 * @Description: The component for VpnListFragment
 * @date 2018/02/06 15:16:44
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = VpnListModule.class)
public interface VpnListComponent {
    VpnListFragment inject(VpnListFragment Fragment);
}