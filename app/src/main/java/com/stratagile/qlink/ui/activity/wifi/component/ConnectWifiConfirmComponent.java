package com.stratagile.qlink.ui.activity.wifi.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wifi.ConnectWifiConfirmActivity;
import com.stratagile.qlink.ui.activity.wifi.module.ConnectWifiConfirmModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wifi
 * @Description: The component for ConnectWifiConfirmActivity
 * @date 2018/01/19 19:46:26
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = ConnectWifiConfirmModule.class)
public interface ConnectWifiConfirmComponent {
    ConnectWifiConfirmActivity inject(ConnectWifiConfirmActivity Activity);
}