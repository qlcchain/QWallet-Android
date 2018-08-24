package com.stratagile.qlink.ui.activity.vpn.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.vpn.RegisteVpnActivity;
import com.stratagile.qlink.ui.activity.vpn.module.RegisteVpnModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.vpn
 * @Description: The component for RegisteVpnActivity
 * @date 2018/02/06 15:41:02
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = RegisteVpnModule.class)
public interface RegisteVpnComponent {
    RegisteVpnActivity inject(RegisteVpnActivity Activity);
}