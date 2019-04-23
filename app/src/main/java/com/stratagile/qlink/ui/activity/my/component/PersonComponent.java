package com.stratagile.qlink.ui.activity.my.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.my.PersonActivity;
import com.stratagile.qlink.ui.activity.my.module.PersonModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.my
 * @Description: The component for PersonActivity
 * @date 2019/04/22 14:28:46
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = PersonModule.class)
public interface PersonComponent {
    PersonActivity inject(PersonActivity Activity);
}