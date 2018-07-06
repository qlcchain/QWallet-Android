package com.stratagile.qlink.ui.activity.setting.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.setting.SettingsActivity;
import com.stratagile.qlink.ui.activity.setting.module.SettingsModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.setting
 * @Description: The component for SettingsActivity
 * @date 2018/05/29 09:30:35
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = SettingsModule.class)
public interface SettingsComponent {
    SettingsActivity inject(SettingsActivity Activity);
}