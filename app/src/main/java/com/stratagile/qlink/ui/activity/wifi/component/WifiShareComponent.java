package com.stratagile.qlink.ui.activity.wifi.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wifi.WifiShareFragment;
import com.stratagile.qlink.ui.activity.wifi.module.WifiShareModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wifi
 * @Description: The component for WifiShareFragment
 * @date 2018/01/15 11:52:51
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = WifiShareModule.class)
public interface WifiShareComponent {
    WifiShareFragment inject(WifiShareFragment Fragment);
}