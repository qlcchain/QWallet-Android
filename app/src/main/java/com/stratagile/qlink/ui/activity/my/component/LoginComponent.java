package com.stratagile.qlink.ui.activity.my.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.my.LoginFragment;
import com.stratagile.qlink.ui.activity.my.module.LoginModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.my
 * @Description: The component for LoginFragment
 * @date 2019/04/09 11:45:22
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = LoginModule.class)
public interface LoginComponent {
    LoginFragment inject(LoginFragment Fragment);
}