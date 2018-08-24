package com.stratagile.qlink.ui.activity.vpn.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.vpn.RegisteWindowVpnActivityActivity;
import com.stratagile.qlink.ui.activity.vpn.module.RegisteWindowVpnActivityModule;

import dagger.Component;

/**
 * @author zl
 * @Package com.stratagile.qlink.ui.activity.vpn
 * @Description: The component for RegisteWindowVpnActivityActivity
 * @date 2018/08/03 11:56:07
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = RegisteWindowVpnActivityModule.class)
public interface RegisteWindowVpnActivityComponent {
    RegisteWindowVpnActivityActivity inject(RegisteWindowVpnActivityActivity Activity);
}