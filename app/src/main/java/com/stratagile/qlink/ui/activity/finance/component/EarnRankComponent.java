package com.stratagile.qlink.ui.activity.finance.component;

import com.stratagile.qlink.application.AppComponent;
import com.stratagile.qlink.ui.activity.base.ActivityScope;
import com.stratagile.qlink.ui.activity.finance.EarnRankActivity;
import com.stratagile.qlink.ui.activity.finance.module.EarnRankModule;

import dagger.Component;

/**
 * @author hzp
 * @Package com.stratagile.qlink.ui.activity.finance
 * @Description: The component for EarnRankActivity
 * @date 2019/04/24 11:27:01
 */
@ActivityScope
@Component(dependencies = AppComponent.class, modules = EarnRankModule.class)
public interface EarnRankComponent {
    EarnRankActivity inject(EarnRankActivity Activity);
}