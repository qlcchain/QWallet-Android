package com.stratagile.qlink.ui.activity.my.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.my.ForgetPasswordActivity;
import com.stratagile.qlink.ui.activity.my.module.ForgetPasswordModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.my
 * @Description: The component for ForgetPasswordActivity
 * @date 2019/04/25 10:28:27
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = ForgetPasswordModule.class)
public interface ForgetPasswordComponent {
    ForgetPasswordActivity inject(ForgetPasswordActivity Activity);
}