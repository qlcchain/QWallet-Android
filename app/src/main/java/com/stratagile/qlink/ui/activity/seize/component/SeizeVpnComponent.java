package com.stratagile.qlink.ui.activity.seize.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.seize.SeizeVpnActivity;
import com.stratagile.qlink.ui.activity.seize.module.SeizeVpnModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.seize
 * @Description: The component for SeizeVpnActivity
 * @date 2018/04/13 12:08:31
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = SeizeVpnModule.class)
public interface SeizeVpnComponent {
    SeizeVpnActivity inject(SeizeVpnActivity Activity);
}