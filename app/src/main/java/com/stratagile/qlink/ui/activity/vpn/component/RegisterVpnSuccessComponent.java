package com.stratagile.qlink.ui.activity.vpn.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.vpn.RegisterVpnSuccessActivity;
import com.stratagile.qlink.ui.activity.vpn.module.RegisterVpnSuccessModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.vpn
 * @Description: The component for RegisterVpnSuccessActivity
 * @date 2018/02/11 16:34:20
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = RegisterVpnSuccessModule.class)
public interface RegisterVpnSuccessComponent {
    RegisterVpnSuccessActivity inject(RegisterVpnSuccessActivity Activity);
}