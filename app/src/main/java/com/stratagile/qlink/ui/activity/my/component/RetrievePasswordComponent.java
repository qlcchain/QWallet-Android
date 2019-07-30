package com.stratagile.qlink.ui.activity.my.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.my.RetrievePasswordActivity;
import com.stratagile.qlink.ui.activity.my.module.RetrievePasswordModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.my
 * @Description: The component for RetrievePasswordActivity
 * @date 2019/04/09 14:21:19
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = RetrievePasswordModule.class)
public interface RetrievePasswordComponent {
    RetrievePasswordActivity inject(RetrievePasswordActivity Activity);
}