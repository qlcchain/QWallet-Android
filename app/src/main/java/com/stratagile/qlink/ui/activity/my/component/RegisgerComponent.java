package com.stratagile.qlink.ui.activity.my.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.my.RegisgerActivity;
import com.stratagile.qlink.ui.activity.my.module.RegisgerModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.my
 * @Description: The component for RegisgerActivity
 * @date 2019/04/23 12:02:02
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = RegisgerModule.class)
public interface RegisgerComponent {
    RegisgerActivity inject(RegisgerActivity Activity);
}