package com.stratagile.qlink.ui.activity.wifi.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wifi.WifiRegisteFragment;
import com.stratagile.qlink.ui.activity.wifi.module.WifiRegisteModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wifi
 * @Description: The component for WifiRegisteFragment
 * @date 2018/01/15 11:52:32
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = WifiRegisteModule.class)
public interface WifiRegisteComponent {
    WifiRegisteFragment inject(WifiRegisteFragment Fragment);
}