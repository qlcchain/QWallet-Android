package com.stratagile.qlink.ui.activity.my.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.my.VerificationActivity;
import com.stratagile.qlink.ui.activity.my.module.VerificationModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.my
 * @Description: The component for VerificationActivity
 * @date 2019/06/14 15:10:49
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = VerificationModule.class)
public interface VerificationComponent {
    VerificationActivity inject(VerificationActivity Activity);
}