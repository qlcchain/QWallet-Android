package com.stratagile.qlink.ui.activity.my.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.my.Login1Fragment;
import com.stratagile.qlink.ui.activity.my.module.Login1Module;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.my
 * @Description: The component for Login1Fragment
 * @date 2019/04/24 18:02:10
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = Login1Module.class)
public interface Login1Component {
    Login1Fragment inject(Login1Fragment Fragment);
}