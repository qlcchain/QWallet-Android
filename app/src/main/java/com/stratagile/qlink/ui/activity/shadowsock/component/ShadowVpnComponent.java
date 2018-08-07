package com.stratagile.qlink.ui.activity.shadowsock.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.shadowsock.ShadowVpnActivity;
import com.stratagile.qlink.ui.activity.shadowsock.module.ShadowVpnModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.shadowsock
 * @Description: The component for ShadowVpnActivity
 * @date 2018/08/07 11:54:13
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = ShadowVpnModule.class)
public interface ShadowVpnComponent {
    ShadowVpnActivity inject(ShadowVpnActivity Activity);
}