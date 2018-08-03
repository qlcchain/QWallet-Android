package com.stratagile.qlink.ui.activity.rank.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.rank.RuleActivity;
import com.stratagile.qlink.ui.activity.rank.module.RuleModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.rank
 * @Description: The component for RuleActivity
 * @date 2018/08/03 09:44:05
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = RuleModule.class)
public interface RuleComponent {
    RuleActivity inject(RuleActivity Activity);
}