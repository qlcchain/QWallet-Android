package com.stratagile.qlink.ui.activity.seize.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.seize.SeizeActivity;
import com.stratagile.qlink.ui.activity.seize.module.SeizeModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.seize
 * @Description: The component for SeizeActivity
 * @date 2018/04/13 10:58:53
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = SeizeModule.class)
public interface SeizeComponent {
    SeizeActivity inject(SeizeActivity Activity);
}