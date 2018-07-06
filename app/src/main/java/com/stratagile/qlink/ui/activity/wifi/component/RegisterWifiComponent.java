package com.stratagile.qlink.ui.activity.wifi.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wifi.RegisterWifiActivity;
import com.stratagile.qlink.ui.activity.wifi.module.RegisterWifiModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wifi
 * @Description: The component for RegisterWifiActivity
 * @date 2018/01/09 17:28:09
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = RegisterWifiModule.class)
public interface RegisterWifiComponent {
    RegisterWifiActivity inject(RegisterWifiActivity Activity);
}