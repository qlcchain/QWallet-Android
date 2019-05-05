package com.stratagile.qlink.ui.activity.finance.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.finance.InviteNowActivity;
import com.stratagile.qlink.ui.activity.finance.module.InviteNowModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.finance
 * @Description: The component for InviteNowActivity
 * @date 2019/04/28 16:32:00
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = InviteNowModule.class)
public interface InviteNowComponent {
    InviteNowActivity inject(InviteNowActivity Activity);
}