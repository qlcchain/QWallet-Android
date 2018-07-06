package com.stratagile.qlink.ui.activity.wifi.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wifi.RegisteWifiSuccessActivity;
import com.stratagile.qlink.ui.activity.wifi.module.RegisteWifiSuccessModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wifi
 * @Description: The component for RegisteWifiSuccessActivity
 * @date 2018/01/19 19:45:43
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = RegisteWifiSuccessModule.class)
public interface RegisteWifiSuccessComponent {
    RegisteWifiSuccessActivity inject(RegisteWifiSuccessActivity Activity);
}