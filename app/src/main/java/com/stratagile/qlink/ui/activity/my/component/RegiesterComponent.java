package com.stratagile.qlink.ui.activity.my.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.my.RegiesterFragment;
import com.stratagile.qlink.ui.activity.my.module.RegiesterModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.my
 * @Description: The component for RegiesterFragment
 * @date 2019/04/09 11:45:07
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = RegiesterModule.class)
public interface RegiesterComponent {
    RegiesterFragment inject(RegiesterFragment Fragment);
}