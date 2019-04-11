package com.stratagile.qlink.ui.activity.my.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.my.AccountActivity;
import com.stratagile.qlink.ui.activity.my.module.AccountModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.my
 * @Description: The component for AccountActivity
 * @date 2019/04/09 11:31:42
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = AccountModule.class)
public interface AccountComponent {
    AccountActivity inject(AccountActivity Activity);
}