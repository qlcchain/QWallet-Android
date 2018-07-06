package com.stratagile.qlink.ui.activity.wifi.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wifi.WifiFragment;
import com.stratagile.qlink.ui.activity.wifi.module.WifiModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wifi
 * @Description: The component for WifiFragment
 * @date 2018/01/09 13:46:43
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = WifiModule.class)
public interface WifiComponent {
    WifiFragment inject(WifiFragment Fragment);
}