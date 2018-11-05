package com.stratagile.qlink.ui.activity.main.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.main.SettingsFragment;
import com.stratagile.qlink.ui.activity.main.module.SettingsModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.main
 * @Description: The component for SettingsFragment
 * @date 2018/10/29 10:38:15
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = SettingsModule.class)
public interface SettingsComponent {
    SettingsFragment inject(SettingsFragment Fragment);
}