package com.stratagile.qlink.ui.activity.wifi.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wifi.ConnectWifiSuccessActivity;
import com.stratagile.qlink.ui.activity.wifi.module.ConnectWifiSuccessModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wifi
 * @Description: The component for ConnectWifiSuccessActivity
 * @date 2018/01/19 19:46:50
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = ConnectWifiSuccessModule.class)
public interface ConnectWifiSuccessComponent {
    ConnectWifiSuccessActivity inject(ConnectWifiSuccessActivity Activity);
}