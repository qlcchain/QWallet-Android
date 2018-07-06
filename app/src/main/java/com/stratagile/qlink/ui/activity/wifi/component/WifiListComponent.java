package com.stratagile.qlink.ui.activity.wifi.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wifi.WifiListFragment;
import com.stratagile.qlink.ui.activity.wifi.module.WifiListModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wifi
 * @Description: The component for WifiListFragment
 * @date 2018/01/09 14:02:09
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = WifiListModule.class)
public interface WifiListComponent {
    WifiListFragment inject(WifiListFragment Fragment);
}