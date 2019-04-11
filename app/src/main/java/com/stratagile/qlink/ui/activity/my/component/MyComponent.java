package com.stratagile.qlink.ui.activity.my.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.my.MyFragment;
import com.stratagile.qlink.ui.activity.my.module.MyModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.my
 * @Description: The component for MyFragment
 * @date 2019/04/09 10:02:03
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = MyModule.class)
public interface MyComponent {
    MyFragment inject(MyFragment Fragment);
}