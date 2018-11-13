package com.stratagile.qlink.ui.activity.wallet.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.wallet.ChangePasswordActivity;
import com.stratagile.qlink.ui.activity.wallet.module.ChangePasswordModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.wallet
 * @Description: The component for ChangePasswordActivity
 * @date 2018/11/12 13:44:10
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = ChangePasswordModule.class)
public interface ChangePasswordComponent {
    ChangePasswordActivity inject(ChangePasswordActivity Activity);
}