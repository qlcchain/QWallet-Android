package com.stratagile.qlink.ui.activity.finance.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.finance.InviteActivity;
import com.stratagile.qlink.ui.activity.finance.module.InviteModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.finance
 * @Description: The component for InviteActivity
 * @date 2019/04/23 15:34:34
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = InviteModule.class)
public interface InviteComponent {
    InviteActivity inject(InviteActivity Activity);
}