package com.stratagile.qlink.ui.activity.main.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.main.GuestActivity;
import com.stratagile.qlink.ui.activity.main.module.GuestModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.main
 * @Description: The component for GuestActivity
 * @date 2018/06/21 15:39:34
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = GuestModule.class)
public interface GuestComponent {
    GuestActivity inject(GuestActivity Activity);
}