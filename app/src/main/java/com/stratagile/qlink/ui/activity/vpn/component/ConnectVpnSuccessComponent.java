package com.stratagile.qlink.ui.activity.vpn.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.vpn.ConnectVpnSuccessActivity;
import com.stratagile.qlink.ui.activity.vpn.module.ConnectVpnSuccessModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.vpn
 * @Description: The component for ConnectVpnSuccessActivity
 * @date 2018/02/09 10:28:44
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = ConnectVpnSuccessModule.class)
public interface ConnectVpnSuccessComponent {
    ConnectVpnSuccessActivity inject(ConnectVpnSuccessActivity Activity);
}