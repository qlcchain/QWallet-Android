package com.stratagile.qlink.ui.activity.vpn.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.vpn.ConnectVpnActivity;
import com.stratagile.qlink.ui.activity.vpn.module.ConnectVpnModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.vpn
 * @Description: The component for ConnectVpnActivity
 * @date 2018/02/08 16:38:02
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = ConnectVpnModule.class)
public interface ConnectVpnComponent {
    ConnectVpnActivity inject(ConnectVpnActivity Activity);
}