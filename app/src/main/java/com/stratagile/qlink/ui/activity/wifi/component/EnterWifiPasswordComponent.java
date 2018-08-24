package com.stratagile.qlink.ui.activity.wifi.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wifi.EnterWifiPasswordActivity;
import com.stratagile.qlink.ui.activity.wifi.module.EnterWifiPasswordModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wifi
 * @Description: The component for EnterWifiPasswordActivity
 * @date 2018/02/01 13:26:40
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = EnterWifiPasswordModule.class)
public interface EnterWifiPasswordComponent {
    EnterWifiPasswordActivity inject(EnterWifiPasswordActivity Activity);
}