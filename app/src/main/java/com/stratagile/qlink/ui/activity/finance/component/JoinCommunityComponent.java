package com.stratagile.qlink.ui.activity.finance.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.finance.JoinCommunityActivity;
import com.stratagile.qlink.ui.activity.finance.module.JoinCommunityModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.finance
 * @Description: The component for JoinCommunityActivity
 * @date 2019/04/24 17:15:42
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = JoinCommunityModule.class)
public interface JoinCommunityComponent {
    JoinCommunityActivity inject(JoinCommunityActivity Activity);
}